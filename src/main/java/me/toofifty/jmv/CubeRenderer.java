package me.toofifty.jmv;

import me.toofifty.jmv.ModelElement.Dir;
import me.toofifty.jmv.ModelElement.Face;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

/**
 * Handles rendering of models
 * 
 * @author Toofifty
 *
 */
public class CubeRenderer {

	public static CubeRenderer instance = new CubeRenderer();
	private float rx;
	private float ry;
	private float rz;
	
	/**
	 * Apply all rotations here
	 */
	public void doRotate() {
		GL11.glRotatef(rx, 1, 0, 0);
		GL11.glRotatef(ry, 0, 1, 0);
		GL11.glRotatef(rz, 0, 0, 1);
	}
	
	/**
	 * Render the floorTexture as the floor
	 * 
	 * @param floorTexture
	 */
	public void renderFloor(Texture floorTexture) {
		GL11.glPushMatrix();
		{
			floorTexture.bind();
			GL11.glScalef(0.25F, 0.25F, 0.25F);
			GL11.glTranslatef(-8F, -8F, -8F);
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glTexCoord2f(0, 0);
				GL11.glVertex3f(-16, 0, -16);
				GL11.glTexCoord2f(0, 1);
				GL11.glVertex3f(-16, 0, 32);
				GL11.glTexCoord2f(1, 1);
				GL11.glVertex3f(32, 0, 32);
				GL11.glTexCoord2f(1, 0);
				GL11.glVertex3f(32, 0, -16);
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();
	}
	
	/**
	 * Render a model
	 * 
	 * @param model
	 */
	public void renderModel(Model model) {
		for (ModelElement modelElement : model.getElements()) {
			renderModelElement(model, modelElement);
		}
	}
	
	/**
	 * Render an element
	 * 
	 * @param model
	 * @param modelElement
	 */
	public void renderModelElement(Model model, ModelElement modelElement) {
		bindModelTexture(model);
		setTexParemeters();
		
		GL11.glPushMatrix();
		{
			// Scale to 1/4
			GL11.glScalef(0.25F, 0.25F, 0.25F);
			// Translate so model is centred
			GL11.glTranslatef(-8F, -8F, -8F);

			// Handle rotation
			if (modelElement.getAngle() != 0) {
				Vector3f origin = modelElement.getOrigin();
				// Translate to element's rotation "origin"
				GL11.glTranslatef(origin.x, origin.y, origin.z);
				// Rotate about said origin
				switch (modelElement.getAxis()) {
				case X:
					GL11.glRotatef(modelElement.getAngle(), 1, 0, 0);
					break;
				case Y:
					GL11.glRotatef(modelElement.getAngle(), 0, 1, 0);
					break;
				case Z:
					GL11.glRotatef(modelElement.getAngle(), 0, 0, 1);
					break;
				default:
					break;
				}
				// Return from origin
				GL11.glTranslatef(-origin.x, -origin.y, -origin.z);
			}

			// Get element's start and end points
			Vector3f from = modelElement.getFrom();
			Vector3f to = modelElement.getTo();

			// 'FROM' is NORTH, WEST, and DOWN
			// 'TO' is SOUTH, EAST, and UP

			Face north = modelElement.getFace(Dir.NORTH);
			Face south = modelElement.getFace(Dir.SOUTH);
			Face east = modelElement.getFace(Dir.EAST);
			Face west = modelElement.getFace(Dir.WEST);
			Face down = modelElement.getFace(Dir.DOWN);
			Face up = modelElement.getFace(Dir.UP);

			GL11.glBegin(GL11.GL_QUADS);

			// If face exists...
			if (north != null) {
				// Get texture coords from atlas
				Vector2f textureLocation = model.getTextureCoords(north);
				
				// Add custom uv to coords from atlas
				Vector2f uv_from = north.getUVFrom(textureLocation);
				Vector2f uv_to = north.getUVTo(textureLocation);

				// Render and texture.
				GL11.glTexCoord2f(uv_to.x, uv_to.y);
				GL11.glVertex3f(from.x, from.y, from.z);
				GL11.glTexCoord2f(uv_to.x, uv_from.y);
				GL11.glVertex3f(from.x, to.y, from.z);
				GL11.glTexCoord2f(uv_from.x, uv_from.y);
				GL11.glVertex3f(to.x, to.y, from.z);
				GL11.glTexCoord2f(uv_from.x, uv_to.y);
				GL11.glVertex3f(to.x, from.y, from.z);
			}

			if (south != null) {
				Vector2f textureLocation = model.getTextureCoords(south);
				
				Vector2f uv_from = south.getUVFrom(textureLocation);
				Vector2f uv_to = south.getUVTo(textureLocation);

				GL11.glTexCoord2f(uv_to.x, uv_to.y);
				GL11.glVertex3f(to.x, from.y, to.z);
				GL11.glTexCoord2f(uv_to.x, uv_from.y);
				GL11.glVertex3f(to.x, to.y, to.z);
				GL11.glTexCoord2f(uv_from.x, uv_from.y);
				GL11.glVertex3f(from.x, to.y, to.z);
				GL11.glTexCoord2f(uv_from.x, uv_to.y);
				GL11.glVertex3f(from.x, from.y, to.z);
			}

			if (east != null) {
				Vector2f textureLocation = model.getTextureCoords(east);
				
				Vector2f uv_from = east.getUVFrom(textureLocation);
				Vector2f uv_to = east.getUVTo(textureLocation);

				GL11.glTexCoord2f(uv_to.x, uv_to.y);
				GL11.glVertex3f(to.x, from.y, from.z);
				GL11.glTexCoord2f(uv_to.x, uv_from.y);
				GL11.glVertex3f(to.x, to.y, from.z);
				GL11.glTexCoord2f(uv_from.x, uv_from.y);
				GL11.glVertex3f(to.x, to.y, to.z);
				GL11.glTexCoord2f(uv_from.x, uv_to.y);
				GL11.glVertex3f(to.x, from.y, to.z);
			}

			if (west != null) {
				Vector2f textureLocation = model.getTextureCoords(west);
				
				Vector2f uv_from = west.getUVFrom(textureLocation);
				Vector2f uv_to = west.getUVTo(textureLocation);

				GL11.glTexCoord2f(uv_to.x, uv_to.y);
				GL11.glVertex3f(from.x, from.y, to.z);
				GL11.glTexCoord2f(uv_to.x, uv_from.y);
				GL11.glVertex3f(from.x, to.y, to.z);
				GL11.glTexCoord2f(uv_from.x, uv_from.y);
				GL11.glVertex3f(from.x, to.y, from.z);
				GL11.glTexCoord2f(uv_from.x, uv_to.y);
				GL11.glVertex3f(from.x, from.y, from.z);
			}

			if (down != null) {
				Vector2f textureLocation = model.getTextureCoords(down);
				
				Vector2f uv_from = down.getUVFrom(textureLocation);
				Vector2f uv_to = down.getUVTo(textureLocation);

				GL11.glTexCoord2f(uv_to.x, uv_to.y);
				GL11.glVertex3f(to.x, from.y, from.z);
				GL11.glTexCoord2f(uv_to.x, uv_from.y);
				GL11.glVertex3f(to.x, from.y, to.z);
				GL11.glTexCoord2f(uv_from.x, uv_from.y);
				GL11.glVertex3f(from.x, from.y, to.z);
				GL11.glTexCoord2f(uv_from.x, uv_to.y);
				GL11.glVertex3f(from.x, from.y, from.z);
			}
			
			if (up != null) {
				Vector2f textureLocation = model.getTextureCoords(up);
				
				Vector2f uv_from = up.getUVFrom(textureLocation);
				Vector2f uv_to = up.getUVTo(textureLocation);

				GL11.glTexCoord2f(uv_to.x, uv_to.y);
				GL11.glVertex3f(from.x, to.y, from.z);
				GL11.glTexCoord2f(uv_to.x, uv_from.y);
				GL11.glVertex3f(from.x, to.y, to.z);
				GL11.glTexCoord2f(uv_from.x, uv_from.y);
				GL11.glVertex3f(to.x, to.y, to.z);
				GL11.glTexCoord2f(uv_from.x, uv_to.y);
				GL11.glVertex3f(to.x, to.y, from.z);
			}

			GL11.glEnd();

		}
		GL11.glPopMatrix();
	}
	
	/**
	 * Bind the model's texture to OpenGL
	 * and apply flags.
	 * 
	 * @param model
	 */
	public void bindModelTexture(Model model) {
		model.getAtlas().bind();
		setTexParemeters();
	}
	
	/**
	 * Set filtering etc.
	 */
	public void setTexParemeters() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);		
	}
	
	/**
	 * Rotate the 'view' up
	 * 
	 * @param delta
	 */
	public void rotateUp(float delta) {
		rx += delta;
		if (rx > 90) rx = 90;
		if (rx < -90) rx = -90;
	}

	/**
	 * Rotate the 'view' left
	 * 
	 * @param delta
	 */
	public void rotateLeft(float delta) {
		ry += delta;
	}

	/* Getters and setters */
	public void setRX(float rx) { this.rx = rx; }
	public void setRY(float ry) { this.ry = ry; }
	public void setRZ(float rz) { this.rz = rz; }

	public float getRX() { return rx; }
	public float getRY() { return ry; }
	public float getRZ() { return rz; }

}
