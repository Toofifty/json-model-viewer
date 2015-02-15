package me.toofifty.jmv;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A collection of load methods
 * 
 * @author Toofifty
 *
 */
public class FileLoader {
	
	/**
	 * Load any texture
	 * 
	 * @param key File path (excluding .png)
	 * @return texture
	 */
	public static Texture loadTexture(String key) {
		try {
			return TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(key + ".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Deprecated: use loadModelImage or loadTexture
	 * 
	 * @param key File name (excluding .png)
	 * @return texture
	 */
	@Deprecated
	public static Texture loadModelTexture(String key) {
		if (key.contains(":")) {
			final String[] splitKey = key.split("\\:");
			return loadTexture("assets/" + splitKey[0] + "/textures/" + splitKey[1]);
		} else {
			return loadTexture("assets/minecraft/textures/" + key);
		}
	}
	
	/**
	 * Load any image
	 * 
	 * @param key File path (excluding .png)
	 * @return BufferedImage
	 */
	public static BufferedImage loadImage(String key) {
		try {
			return ImageIO.read(ResourceLoader.getResourceAsStream(key + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Load an image for a model's texture
	 * 
	 * @param key File name (excluding .png)
	 * @return BufferedImage
	 */
	public static BufferedImage loadModelImage(String key) {
		if (key.contains(":")) {
			final String[] splitKey = key.split("\\:");
			return loadImage("assets/" + splitKey[0] + "/textures/" + splitKey[1]);
		} else {
			return loadImage("assets/minecraft/textures/" + key);
		}
	}
	
	/**
	 * Load a JsonNode from a Json string
	 * 
	 * @param rawJson
	 * @return
	 */
	public static JsonNode loadJson(String rawJson) {
		final ObjectMapper m = new ObjectMapper();
		try {
			return m.readTree(rawJson);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
