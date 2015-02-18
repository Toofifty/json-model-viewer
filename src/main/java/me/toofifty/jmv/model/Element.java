package me.toofifty.jmv.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jackson.JsonNodeReader;

/**
 * Holds all the information for one piece of a model.
 * Piece must be rendered through the CubeRenderer class.
 * 
 * @author Toofifty
 *
 */
public class Element {

	// Main bounds
	private Vector3f from;
	private Vector3f to;

	// Rotations
	private Vector3f origin = new Vector3f(8F, 8F, 8F);
	private Axis axis;
	private float angle = 0;
	private boolean rescale = false;
	
	// Faces
	private HashMap<Dir, Face> faces = new HashMap<Dir, Face>();
	
	// Object name
	private String name;
	private boolean shade = true;

	/**
	 * Create a new Element with float values.
	 * 
	 * @param from x
	 * @param from y
	 * @param from z
	 * @param to x
	 * @param to y
	 * @param to z
	 */
	public Element(float fx, float fy, float fz, float tx, float ty, float tz) {
		this.from = new Vector3f(fx, fy, fz);
		this.to = new Vector3f(tx, ty, tz);
	}

	/**
	 * Create a new Element with Vectors.
	 * 
	 * @param from
	 * @param to
	 */
	public Element(Vector3f from, Vector3f to) {
		this.from = from;
		this.to = to;
	}
	
	public Element(JsonNode from, JsonNode to) {
		this.from = new Vector3f(from.get(0).floatValue(), 
				from.get(1).floatValue(), 
				from.get(2).floatValue());
		this.from = new Vector3f(to.get(0).floatValue(), 
				to.get(1).floatValue(), 
				to.get(2).floatValue());
	}

	/**
	 * Create a Element from a JSON node.
	 * 
	 * @param rootNode
	 */
	public Element(Model model, JsonNode rootNode) {
		final JsonNode fromNode = rootNode.path("from");
		final JsonNode toNode = rootNode.path("to");
		if (fromNode == null || toNode == null || fromNode.size() != 3 || toNode.size() != 3) {
			return;
		}
		this.from = new Vector3f(
			fromNode.get(0).floatValue(), 
			fromNode.get(1).floatValue(), 
			fromNode.get(2).floatValue()
		);
		this.to = new Vector3f(
			toNode.get(0).floatValue(), 
			toNode.get(1).floatValue(), 
			toNode.get(2).floatValue()
		);
		
		final JsonNode rotationNode = rootNode.path("rotation");
		if (rotationNode != null) {
			final JsonNode originNode = rotationNode.path("origin");
			if (originNode != null && originNode.size() == 3) {
				this.origin = new Vector3f(
					originNode.get(0).floatValue(),
					originNode.get(1).floatValue(),
					originNode.get(2).floatValue()
				);
			}
			
			final JsonNode axisNode = rotationNode.path("axis");
			if (axisNode != null && axisNode.toString() != "") {
				this.axis = getAxis(axisNode.textValue());
			}
			
			final JsonNode angleNode = rotationNode.path("angle");
			if (angleNode != null && angleNode.asInt() != 0) {
				this.angle = angleNode.floatValue();
			}
		}
		
		final JsonNode facesNode = rootNode.path("faces");
		if (facesNode != null) {
			Iterator<Entry<String, JsonNode>> facesIter = facesNode.fields();
			while (facesIter.hasNext()) {
				final Entry faceInfo = facesIter.next();
				final Dir faceDir = getDir(faceInfo.getKey().toString());
				final Face face = new Face(model, (JsonNode)faceInfo.getValue());
				this.faces.put(faceDir, face);
			}
		}
		
		final JsonNode commentNode = rootNode.path("__comment");
		if (commentNode != null && commentNode.toString() != "") {
			setName(commentNode.toString());
		}
		
		final JsonNode nameNode = rootNode.path("__name");
		if (commentNode != null && commentNode.toString() != "") {
			setName(commentNode.toString());
		}
	}

	/**
	 * Rotate the piece with a specific origin.
	 * 
	 * @param origin
	 * @param axis
	 * @param angle
	 */
	public void rotate(Vector3f origin, Axis axis, float angle) {
		this.origin = origin;
		this.axis = axis;
		this.angle = angle;
	}

	/**
	 * Rotate the piece around the default origin [8, 8, 8]
	 * 
	 * @param axis
	 * @param angle
	 */
	public void rotate(Axis axis, float angle) {
		this.axis = axis;
		this.angle = angle;
	}
	
	/**
	 * Set (or overwrite) a face in the faces map.
	 * 
	 * @param dir
	 * @param face
	 */
	public void face(Dir dir, Face face) {
		this.faces.put(dir, face);
	}
	
	/**
	 * Set object name for use in GUI
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name.replace("\"", "");
	}
	
	/**
	 * Set shade value
	 * 
	 * @param shade
	 */
	public void setShade(boolean shade) {
		this.shade = shade;
	}
	
	/**
	 * Set rotation rescale value
	 * 
	 * @param shade
	 */
	public void setRescale(boolean rescale) {
		this.rescale = rescale;
	}
	
	/**
	 * From getter
	 * 
	 * @return from
	 */
	public Vector3f getFrom() {
		return from;
	}
	
	/**
	 * To getter
	 * 
	 * @return to
	 */
	public Vector3f getTo() {
		return to;
	}
	
	/**
	 * Origin getter
	 * 
	 * @return origin
	 */
	public Vector3f getOrigin() {
		return origin;
	}
	
	/**
	 * Axis getter
	 * 
	 * @return axis
	 */
	public Axis getAxis() {
		return axis;
	}
	
	/**
	 * Axis getter
	 * 
	 * @return axis
	 */
	public String getAxisString() {
		switch (axis) {
		case X:
			return "x";
		case Y:
			return "y";
		case Z:
			return "z";
		default:
			return null;
		}
	}
	
	/**
	 * Angle getter
	 * 
	 * @return angle
	 */
	public float getAngle() {
		return angle;
	}
	
	/**
	 * Face getter
	 * 
	 * @param dir
	 * @return face
	 */
	public Face getFace(Dir dir) {
		return faces.get(dir);
	}
	
	/**
	 * Convert dir to string equivalent
	 * 
	 * @param dir
	 * @return string dir
	 */
	public String getDirString(Dir dir) {
		switch (dir) {
		case NORTH:
			return "north";
		case SOUTH:
			return "south";
		case EAST:
			return "east";
		case WEST:
			return "west";
		case UP:
			return "up";
		case DOWN:
			return "down";
		default:
			return null;
		}
	}
	
	/**
	 * Get all faces as HashMap
	 * 
	 * @return map<Dir, Face>
	 */
	public HashMap<Dir, Face> getFaces() {
		return faces;
	}
	
	/**
	 * Get object name for GUI
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get shade
	 * 
	 * @return name
	 */
	public boolean getShade() {
		return shade;
	}
	
	/**
	 * Get rescale
	 * 
	 * @return name
	 */
	public boolean getRescale() {
		return rescale;
	}

	/**
	 * Rotation axis enum
	 * 
	 * @author Toofifty
	 *
	 */
	public enum Axis {
		X, Y, Z;
	}

	/**
	 * Face direction enum
	 * 
	 * @author Toofifty
	 *
	 */
	public enum Dir {
		UP, DOWN, NORTH, SOUTH, EAST, WEST;
	}


	/**
	 * Convert to enum Axis from string.
	 * 
	 * @param axis
	 * @return enum axis
	 * @throws Exception
	 */
	public static Axis getAxis(String axis) {
		switch (axis.toLowerCase()) {
		case "x":
			return Axis.X;
		case "y":
			return Axis.Y;
		case "z":
			return Axis.Z;
		default:
			return null;
		}
	}
	
	/**
	 * Convert to enum Dir from string.
	 * 
	 * @param dir
	 * @return enum dir
	 * @throws Exception
	 */
	public static Dir getDir(String dir) {
		switch (dir.toLowerCase()) {
		case "up":
			return Dir.UP;
		case "down":
			return Dir.DOWN;
		case "north":
			return Dir.NORTH;
		case "south":
			return Dir.SOUTH;
		case "east":
			return Dir.EAST;
		case "west":
			return Dir.WEST;
		default:
			return null;
		}
	}

}
