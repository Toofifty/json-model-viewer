package me.toofifty.jmv.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import me.toofifty.jmv.FileLoader;
import me.toofifty.jmv.model.Element.Dir;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Direct object form of a JSON model.
 * Handles loading of parents and child elements.
 * 
 * @author Toofifty
 *
 */
public class Model {
	
	private String parent;
	private Model parentModel;
	private Model childModel;
	private boolean ambientOcclusion = true;
	private HashMap<String, String> textureStringMap = new HashMap<>();
	private TextureAtlas texture;
	private List<Element> elements = new ArrayList<>();
	private ReferenceStrings refStrings = new ReferenceStrings();
	
	/**
	 * MUST BE CALLED WITHIN OPENGL CONTEXT
	 * 
	 * Loads a model from a JsonNode.
	 * 
	 * @param rootNode
	 */
	public Model(JsonNode rootNode) {
		if (rootNode == null) {
			return;
		}		
		final JsonNode parentNode = rootNode.path("parent");
		if (parentNode != null && parentNode.toString() != "") {
			parent = parentNode.toString().replace("\"", ""); // model-level parent, won't change
			parentModel = loadParent(parent);
		}
		loadModel(rootNode);
	}
	
	/**
	 * MUST BE CALLED WITHIN OPENGL CONTEXT
	 * 
	 * Used to make parent models.
	 * 
	 * @param child, rootNode
	 */
	public Model(Model child, JsonNode rootNode) {
		if (rootNode == null) {
			return;
		}
		childModel = child;
		final JsonNode parentNode = rootNode.path("parent");
		if (parentNode != null && parentNode.toString() != "") {
			parent = parentNode.toString().replace("\"", "");
			parentModel = loadParent(parent);
		}
		loadModel(rootNode);
	}
	
	/**
	 * MUST BE CALLED WITHIN OPENGL CONTEXT
	 * 
	 * Loads the default model (brick)
	 * Example JSON
	 * {
	 *   "parent" : "block/cube_all",
	 *   "textures" : {
	 *     "all" : "blocks/brick"
	 *   }
	 * }
	 */
	public Model() {
		parent = "block/cube_all";
		loadParent(parent);
		
		textureStringMap.put("all", "blocks/brick");
		texture = new TextureAtlas(textureStringMap);
	}
	
	/**
	 * Loads a parent model from the relative path,
	 * and uses attributes found in the model .json.
	 * 
	 * Currently only loads models from the 'minecraft'
	 * assets folder.
	 * 
	 * Is very recursive and will go to the highest level
	 * model.
	 * 
	 * @param parentPath
	 */
	protected Model loadParent(String parentPath) {
		parentPath = parentPath.replace("\"", "");
		System.out.println("Loading parent " + parentPath + "...");
		final String path = "assets/minecraft/models/" + parentPath + ".json";
		final ObjectMapper m = new ObjectMapper();
		try {
			final JsonNode rootNode = m.readTree(new File(path));
			return new Model(this, rootNode);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Converts JSON into java variables in
	 * the current model.
	 * 
	 * @param rootNode
	 */
	protected void loadModel(JsonNode rootNode) {
		System.out.println("Loading model...");
		System.out.println("Node: " + rootNode.toString());
		
		final JsonNode textureNode = rootNode.path("textures");
		if (textureNode != null && textureNode.size() != 0 && this.texture == null) {
			System.out.println("Loading textures...");
			Iterator<Entry<String, JsonNode>> textureIter = textureNode.fields();
			while (textureIter.hasNext()) {
				final Entry textureInfo = textureIter.next();
				final String textureName = textureInfo.getKey().toString();
				String texturePath = textureInfo.getValue().toString();
				texturePath = texturePath.substring(1, texturePath.length() - 1);
				if (texturePath.contains("#")) {
					System.out.println("Adding reference string: " + textureName + " -> " + texturePath);
					refStrings.add(textureName, texturePath.substring(1));
					continue;
				}
				final BufferedImage textureImage = FileLoader.loadModelImage(texturePath);
				textureStringMap.put(textureName, texturePath);
			}
			if (textureStringMap.size() > 0) {
				texture = new TextureAtlas(textureStringMap);
			}
		}
		
		final JsonNode elementsNode = rootNode.path("elements");
		if (elementsNode != null && elementsNode.size() != 0) {
			Iterator<JsonNode> elementIter = elementsNode.iterator();
			while (elementIter.hasNext()) {
				final JsonNode elementNode = elementIter.next();
				final Element element = new Element(this, elementNode);
				elements.add(element);
			}
		}
	}
	
	public String getJSON() throws IOException {
		JsonNodeFactory factory = new JsonNodeFactory(false);
		JsonFactory jsonFactory = new JsonFactory();
		JsonGenerator generator = jsonFactory.createGenerator(System.out);
		ObjectMapper mapper = new ObjectMapper();
		
		/* Build JSON */
		
		// root
		ObjectNode rootNode = factory.objectNode();
		rootNode.put("ambientocclusion", ambientOcclusion);
		if (parent != null && parent != "") {
			rootNode.put("parent", parent);
		}
		
		// textures
		ObjectNode texturesNode = factory.objectNode();
		Iterator textureMapIter = textureStringMap.entrySet().iterator();
		while (textureMapIter.hasNext()) {
			final Entry e = (Entry) textureMapIter.next();
			final String field = e.getKey().toString();
			final String value = e.getValue().toString();
			texturesNode.put(field, value);
		}
		Iterator refStringsIterator = refStrings.getReferenceMap().entrySet().iterator();
		while (refStringsIterator.hasNext()) {
			final Entry e = (Entry) refStringsIterator.next();
			final String field = e.getKey().toString();
			final String value = "#" + e.getValue().toString();
			texturesNode.put(field, value);
		}
		rootNode.set("textures", texturesNode);
		
		// elements
		ArrayNode elementsNode = factory.arrayNode();
		for (Element e : elements) {
			ObjectNode elementNode = factory.objectNode();
			
			// __name
			String elementName = e.getName();
			if (elementName != null && elementName != "") {
				elementNode.put("__name", elementName);
			}
			
			// from
			ArrayNode fromNode = factory.arrayNode();
			Vector3f fromVector = e.getFrom();
			fromNode.add(fromVector.x);
			fromNode.add(fromVector.y);
			fromNode.add(fromVector.z);
			elementNode.set("from", fromNode);
			
			// to
			ArrayNode toNode = factory.arrayNode();
			Vector3f toVector = e.getTo();
			toNode.add(toVector.x);
			toNode.add(toVector.y);
			toNode.add(toVector.z);
			elementNode.set("to", toNode);
			
			// shade
			elementNode.put("shade", e.getShade());
			
			// rotation
			ObjectNode rotationNode = factory.objectNode();
			if (e.getAxis() != null) {
				
				// origin
				ArrayNode originNode = factory.arrayNode();
				Vector3f originVector = e.getOrigin();
				originNode.add(originVector.x);
				originNode.add(originVector.y);
				originNode.add(originVector.z);
				rotationNode.set("origin", originNode);
				
				// axis
				rotationNode.put("axis", e.getAxisString());
				
				// angle
				rotationNode.put("angle", e.getAngle());
				
				// rescale
				rotationNode.put("rescale", e.getRescale());
				
				elementNode.set("rotation", rotationNode);
			}
			
			// faces
			ObjectNode facesNode = factory.objectNode();
			Iterator facesIter = e.getFaces().entrySet().iterator();
			while (facesIter.hasNext()) {
				final Entry en = (Entry) facesIter.next();
				
				// face
				final String dirString = e.getDirString((Dir) en.getKey());
				final Face f = (Face) en.getValue();
				ObjectNode faceNode = factory.objectNode();
				{
					// uv
					ArrayNode uvNode = factory.arrayNode();
					Vector2f uvFromVector = f.getUVFrom();
					Vector2f uvToVector = f.getUVTo();
					uvNode.add(uvFromVector.x);
					uvNode.add(uvFromVector.y);
					uvNode.add(uvToVector.x);
					uvNode.add(uvToVector.y);
					faceNode.set("uv", uvNode);
					
					// texture
					faceNode.put("texture", "#" + f.getTexture());
					
					// cullface
					if (f.getCullface() != null) {
						String dir = e.getDirString(f.getCullface());
						faceNode.put("cullface", dir);
					}
					
					// rotation
					if (f.getRotation() != 0) {
						faceNode.put("rotation", f.getRotation());
					}
				}
				facesNode.set(dirString, faceNode);
				
			}
			elementNode.set("faces", facesNode);
			
			elementsNode.add(elementNode);
		}
		
		rootNode.set("elements", elementsNode);
		
		String formattedJSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
		// indent hack
		formattedJSON.replace("  ", "----");
		formattedJSON.replace("----", "    ");
		
		System.out.println(formattedJSON);
		
		return formattedJSON;
	}
	
	public List<Element> getElements() {
		return elements;
	}
	
	public Model getParent() {
		return parentModel;
	}
	
	public Model getChild() {
		return childModel;
	}
	
	public TextureAtlas getAtlas() {
		return texture;
	}
	
	public String getReference(String var) {
		//System.out.println("Looking for reference " + var + ".");
		String ret = refStrings.getString(var);
		if (ret == null) {
			if (this.childModel == null) {
				String mapOut = textureStringMap.get(var);
				if (mapOut != null) {
					//System.out.println("Managed to find " + mapOut);
					//System.out.println("Returning input (" + var + ")");
					return var;
				}
				System.out.println("A texture is missing for a face that requires it! SHIT!");
				System.out.println("Missing texture/ref: " + var);
				return null;
			}
			return this.childModel.getReference(var);
		}
		//System.out.println("Found reference! " + ret);
		return ret;
	}
	
	public Vector2f getTextureCoords(Face face) {
		return getTextureCoords(face.getTexture());
	}
	
	public Vector2f getTextureCoords(String face) {
		//System.out.println("Looking for " + face + " coords...");
		//refStrings.printStrings();
		if (texture == null) {
			// If no texture is found, must be ref string to another!
			//System.out.println("Null texture, checking child");
			if (this.childModel == null) {
				return new Vector2f(0, 0);
			}
			return this.childModel.getTextureCoords(getReference(face));
		}
		return texture.getTextureCoords(this, face);
	}
	
	public ReferenceStrings getReferenceStrings() {
		return refStrings;
	}
}
