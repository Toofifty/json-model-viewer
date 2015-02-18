package me.toofifty.jmv.model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import me.toofifty.jmv.FileLoader;

import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.BufferedImageUtil;

/**
 * A stitching of currently used textures into
 * one that is bound to OpenGL at runtime.
 * 
 * @author Toofifty
 *
 */
public class TextureAtlas {
	
	public Texture texture;
	public HashMap<String, Vector2f> textureMap = new HashMap<>();
	
	public int width;
	public int height;
	
	/**
	 * Merge all textures given into a line to be bound to
	 * the OpenGL context
	 * 
	 * @param textures
	 */
	public TextureAtlas(HashMap<String, String> textures) {
		width = 0;
		height = 0;
		Iterator textureIter = textures.entrySet().iterator(); 
		while (textureIter.hasNext()) {
			final Entry e = (Entry) textureIter.next();
			final BufferedImage t = FileLoader.loadModelImage(e.getValue().toString());
			width += t.getWidth();
			height = (int) Math.max(t.getHeight(), height);
		}
		
		System.out.println("Width: " + width + " Height: " + height);
		
		if (height == 0 || width == 0) return;
		
		BufferedImage atlas = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = atlas.getGraphics();
		
		int currentX = 0;
		int currentY = 0;

		textureIter = textures.entrySet().iterator(); 
		while (textureIter.hasNext()) {
			final Entry e = (Entry) textureIter.next();
			final BufferedImage t = FileLoader.loadModelImage(e.getValue().toString());
			final String s = (String) e.getKey();
			
			graphics.drawImage(t, currentX, currentY, null);
			textureMap.put(s, new Vector2f(currentX / (float) width, currentY / (float) height));
			currentX += t.getWidth();
		}
		
		try {
			texture = BufferedImageUtil.getTexture("", atlas);
			//ImageIO.write(atlas, "PNG", new File("atlas.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get texture coordinates from reference string
	 * 
	 * @param textureName
	 * @return texture coordinates
	 */
	public Vector2f getTextureCoords(Model model, String textureName) {
		if (textureName == null) {
			System.out.println("Null texture!");
			model.getReferenceStrings().printStrings();
			System.exit(0);
		}
		final Vector2f vec = textureMap.get(textureName);
		if (vec == null) return getTextureCoords(model, model.getReferenceStrings().getString(textureName));
		return vec;
	}
	
	/**
	 * Bind the texture to the OpenGL context
	 */
	public void bind() {
		if (texture != null) {
			texture.bind();
		}
	}

}
