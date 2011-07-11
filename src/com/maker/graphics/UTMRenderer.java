package com.maker.graphics;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import com.maker.Logger;
import com.maker.Listeners.EditListener;
import com.maker.graphics.wrapper.MatrixTrackingGL;
import com.maker.world.World;
import com.maker.world.WorldObject;

public class UTMRenderer implements Renderer {
	private Polygon polygon = new Polygon();
	private ArrayList<Integer> textures;
	private Bitmap[] bitmaps;
	private World world;
	private int width, height;
	private boolean hasTouch;
	private float[][] touch;
	private float depth = -20;
	private WorldObject chosenObject;
	private int frames;
	private long time = System.currentTimeMillis();
	private EditListener editListener;
	private boolean animate;
	private boolean moving;
	private long draw;
	private long build;
	private long full;
	private int backgroundTexture = -1;

	public UTMRenderer(Bitmap[] bitmaps, World world, boolean animate) {
		this.world = world;
		this.bitmaps = bitmaps;
		this.animate = animate;
	}

	@Override
	public synchronized void onDrawFrame(GL10 gl) {
		long fullTime = System.currentTimeMillis();
		frames++;

		if (System.currentTimeMillis() - time > 1000) {
			time = System.currentTimeMillis();

			int drawPercentage = (int) ((double) draw / (double) full * 100d);
			int buildPercentage = (int) ((double) build / (double) full * 100d);

			Logger.debug(getClass(), "PERCENTAGE: drawPercentage: " + drawPercentage + "% buildPercentage: " + buildPercentage + "% fullTime: " + fullTime + " FPS: " + frames);
			frames = 0;
			draw = 0;
			full = 0;
			build = 0;
		}

		long buildTime = System.currentTimeMillis();
		Iterable<WorldObject> frame = world.getCurrentFrame(animate);
		buildTime = System.currentTimeMillis() - buildTime;

		long drawTime = System.currentTimeMillis();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if (textures == null && bitmaps != null) {
			textures = loadTextures(gl, bitmaps);
			bitmaps = null;
		}

		if (backgroundTexture != -1) {
			gl.glPushMatrix();
			{
				gl.glScalef(9.0f, 9.0f, 0.4f);
				gl.glTranslatef(-.5f, -.5f, depth + 5);
				int textureId = textures.get(backgroundTexture);
				polygon.draw(gl, textureId);
			}
			gl.glPopMatrix();
		}

		gl.glLoadIdentity();
		for (WorldObject object : frame) {
			float[] position = object.getPosition();
			float x = position[0];
			float y = position[1];
			gl.glPushMatrix();
			{
				gl.glTranslatef(x, y, depth);
				int texture = (int) object.getTexture();
				int textureId = textures.get(texture);
				polygon.draw(gl, textureId);
			}
			gl.glPopMatrix();
		}

		if (hasTouch && touch != null) {
			for (int i = 0; i < touch.length; i++) {
				if (touch[i] != null && touch[i].length >= 2) {
					float[] screenTouch = getWorldCoords(touch[i], gl);
					float[] worldTouch = getWorldCoord(screenTouch);
					float x = (int) worldTouch[0];
					float y = (int) worldTouch[1];
					// Logger.debug(getClass(), "touch[" + i + "] x: " +
					// touch[i][0] + " y: " + touch[i][1]);
					// Logger.debug(getClass(), "worldTouch x: " + worldTouch[0]
					// + " y: " + worldTouch[1]);
					// Logger.debug(getClass(), "int x: " + x + " y: " + y);

					if (chosenObject != null) {
						WorldObject object = chosenObject.New(x, y);
						world.add(object);
					}

					if (moving)
						world.setTouch(i, worldTouch);

					if (editListener != null) {

						WorldObject object = world.getObjectInt(new float[] { x, y });

						if (object != null) {
							editListener.edit(object);
							editListener = null;
						}
					}

					gl.glPushMatrix();
					{
						gl.glTranslatef(x, y, depth);
						int textureId = textures.get(3);
						polygon.draw(gl, textureId);
					}
					gl.glPopMatrix();
				}
			}
		} else {
			world.setTouch(0, null);
			world.setTouch(1, null);
		}

		long current = System.currentTimeMillis();
		fullTime = current - fullTime;
		drawTime = current - drawTime;

		full += fullTime;
		draw += drawTime;
		build += buildTime;

	}

	private float[] getWorldCoord(float[] screenTouch) {
		float lambda = depth / screenTouch[2];
		return new float[] { screenTouch[0] * lambda, screenTouch[1] * lambda };
	}

	private ArrayList<Integer> loadTextures(GL10 gl, Bitmap[] bitmaps) {
		ArrayList<Integer> textures = new ArrayList<Integer>();
		for (int i = 0; i < bitmaps.length; i++)
			textures.add(loadGLTexture(gl, bitmaps[i]));
		return textures;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		this.width = width;
		this.height = height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}

	private int loadGLTexture(GL10 gl, Bitmap bitmap) {
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);
		int textureId = textures[0];

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		return textureId;
	}

	/**
	 * Calculates the transform from screen coordinate system to world
	 * coordinate system coordinates for a specific point, given a camera
	 * position.
	 * 
	 * @param touch
	 *            Vec2 point of screen touch, the actual position on physical
	 *            screen (ej: 160, 240)
	 * @param cam
	 *            camera object with x,y,z of the camera and screenWidth and
	 *            screenHeight of the device.
	 * @return position in WCS.
	 */
	public float[] getWorldCoords(float[] touch, GL10 gl) {
		// Initialize auxiliary variables.
		float[] worldPos = new float[4];

		// Auxiliary matrix and vectors
		// to deal with ogl.
		float[] invertedMatrix, transformMatrix, normalizedInPoint, outPoint;
		invertedMatrix = new float[16];
		transformMatrix = new float[16];
		normalizedInPoint = new float[4];
		outPoint = new float[4];

		// Invert y coordinate, as android uses
		// top-left, and ogl bottom-left.
		int oglTouchY = (int) (height - touch[1]);

		/*
		 * Transform the screen point to clip space in ogl (-1,1)
		 */
		Logger.debug(getClass(), "width: " + width + " height: " + height);
		normalizedInPoint[0] = (float) ((touch[0]) * 2.0f / width - 1.0);
		normalizedInPoint[1] = (float) ((oglTouchY) * 2.0f / height - 1.0);
		normalizedInPoint[2] = -1.0f;
		normalizedInPoint[3] = 1.0f;

		/*
		 * Obtain the transform matrix and then the inverse.
		 */
		Matrix.multiplyMM(transformMatrix, 0, getCurrentProjection(gl), 0, getCurrentModelView(gl), 0);
		Matrix.invertM(invertedMatrix, 0, transformMatrix, 0);

		/*
		 * Apply the inverse to the point in clip space
		 */
		Matrix.multiplyMV(outPoint, 0, invertedMatrix, 0, normalizedInPoint, 0);

		if (outPoint[3] == 0.0) {
			// Avoid /0 error.
			Logger.debug(getClass(), "World coords: DIVISION BY ZERO ERROR!");
			return worldPos;
		}

		// Divide by the 3rd component to find
		// out the real position.
		worldPos[0] = outPoint[0] / outPoint[3];
		worldPos[1] = outPoint[1] / outPoint[3];
		worldPos[2] = outPoint[2] / outPoint[3];
		worldPos[3] = 1;

		return worldPos;
	}

	/**
	 * Record the current projection matrix state. Has the side effect of
	 * setting the current matrix state to GL_PROJECTION
	 * 
	 * @param gl
	 *            context
	 */
	public float[] getCurrentProjection(GL10 gl) {
		float[] mProjection = new float[16];
		getMatrix(gl, GL10.GL_PROJECTION, mProjection);
		return mProjection;
	}

	/**
	 * Fetches a specific matrix from opengl
	 * 
	 * @param gl
	 *            gl context
	 * @param mode
	 *            mode of the matrix
	 * @param mat
	 *            initialized float[16] array to fill with the matrix
	 */
	private void getMatrix(GL10 gl, int mode, float[] mat) {
		MatrixTrackingGL gl2 = (MatrixTrackingGL) gl;
		gl2.glMatrixMode(mode);
		gl2.getMatrix(mat, 0);
	}

	/**
	 * Record the current modelView matrix state. Has the side effect of setting
	 * the current matrix state to GL_MODELVIEW
	 * 
	 * @param gl
	 *            gl context
	 */
	public float[] getCurrentModelView(GL10 gl) {
		float[] mModelView = new float[16];
		getMatrix(gl, GL10.GL_MODELVIEW, mModelView);
		return mModelView;
	}

	public synchronized void touchDown() {
		hasTouch = true;
	}

	public synchronized void touchUp() {
		hasTouch = false;
	}

	public synchronized void setTouch(float[][] touch) {
		this.touch = touch;
	}

	public void setChosen(WorldObject object) {
		chosenObject = object;
	}

	public void edit(EditListener listener) {
		setChosen(null);
		editListener = listener;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setAnimate(boolean animate) {
		this.animate = animate;
	}

	public void setWorld(World world) {
		this.world = world;

	}

	public void setBackgroundTexture(int backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
	}
}
