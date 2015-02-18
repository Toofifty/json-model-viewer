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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A collection of load methods
 * 
 * @author Toofifty
 *
 */
public class FileLoader {
	
	public static String assetsDir;
	
	public static void setAssetsDirFromFile(File file) {
		String fileLoc = file.getAbsolutePath();
		if (fileLoc.contains("\\assets\\")) {
			assetsDir = fileLoc.split("\\assets")[0] + "\\assets";
			System.out.println("New assetsDir: " + assetsDir);
		} else {
			assetsDir = "";
		}
	}
	
	public static boolean checkAssetsDir() {
		return assetsDir != null && assetsDir.equals("") && assetsDir.toLowerCase().endsWith("\\assets");
	}
	
	/**
	 * Load any texture
	 * 
	 * @param key File path (excluding .png)
	 * @return texture
	 */
	public static Texture loadTexture(String key) {
		try {
			Texture ret = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(key + ".png"));
			if (ret == null) {
				ret = loadTexture("null");
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loadTexture("null");
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
			return loadTexture(assetsDir + "/" + splitKey[0] + "/textures/" + splitKey[1]);
		} else {
			return loadTexture(assetsDir + "/minecraft/textures/" + key);
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
			BufferedImage ret = ImageIO.read(ResourceLoader.getResourceAsStream(key + ".png"));
			if (ret == null) {
				ret = loadImage("null");
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loadImage("null");
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
			return loadImage(assetsDir + "/" + splitKey[0] + "/textures/" + splitKey[1]);
		} else {
			return loadImage(assetsDir + "/minecraft/textures/" + key);
		}
	}
	
	public static boolean isValidJSON(String json) {
		try {
			new ObjectMapper().readTree(json);
			return true;
		} catch (JsonProcessingException e) {
			return false;
		} catch (IOException e) {
			return false;
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
	
	public static String readFileString(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}

}