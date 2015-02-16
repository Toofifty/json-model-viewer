package me.toofifty.jmv;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import me.toofifty.jmv.ModelElement.Dir;
import me.toofifty.jmv.ModelElement.Face;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Direct object form of a JSON model.
 * Handles loading of parents and child elements.
 * 
 * @author Toofifty
 *
 */
public class Model {
	
	private String parent;	
	private HashMap<String, BufferedImage> imageMap = new HashMap<String, BufferedImage>();	
	private TextureAtlas texture;
	private List<ModelElement> elements = new ArrayList<>();
	
	/**
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
			parent = parentNode.toString(); // model-level parent, won't change
			loadParent(parent);
		}
		loadModel(rootNode);
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
	protected void loadParent(String parentPath) {
		System.out.println("Loading parent " + parentPath + "...");
		final String path = "assets/minecraft/models/" + parentPath.substring(1, parentPath.length() - 1) + ".json";
		final ObjectMapper m = new ObjectMapper();
		try {
			final JsonNode rootNode = m.readTree(new File(path));
			final JsonNode parentNode = rootNode.path("parent");
			if (parentNode != null && parentNode.toString() != "") {
				loadParent(parentNode.toString());
			}
			loadModel(rootNode);
		} catch (IOException e) {
			e.printStackTrace();
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
					ReferenceStrings.add(textureName, texturePath.substring(1));
					continue;
				}
				final BufferedImage textureImage = FileLoader.loadModelImage(texturePath);
				imageMap.put(textureName, textureImage);
			}
			if (imageMap.size() > 0) {
				texture = new TextureAtlas(imageMap);
			}
		}
		
		final JsonNode elementsNode = rootNode.path("elements");
		if (elementsNode != null && elementsNode.size() != 0) {
			Iterator<JsonNode> elementIter = elementsNode.iterator();
			while (elementIter.hasNext()) {
				final JsonNode elementNode = elementIter.next();
				final ModelElement modelElement = new ModelElement(this, elementNode);
				elements.add(modelElement);
			}
		}
	}
	
	public List<ModelElement> getElements() {
		return this.elements;
	}
	
	public TextureAtlas getAtlas() {
		return texture;
	}
	
	public Vector2f getTextureCoords(Face face) {
		return texture.getTextureCoords(face.getTexture());
	}
}
