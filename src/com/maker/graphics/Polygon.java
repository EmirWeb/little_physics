package com.maker.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

public class Polygon {

	private float[] vertices = { 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 1f, 1f, 0f };
	private short[] indices = { 0, 1, 2, 0, 2, 3 };
	float textureCoordinates[] = { 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f };

	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	private FloatBuffer textureBuffer;

	private float[] position = { 0f, 0f, 0f };
	private float[] rotation = { 0f, 0f, 0f };

	public Polygon() {

		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);

		ByteBuffer byteBuf = ByteBuffer.allocateDirect(textureCoordinates.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(textureCoordinates);
		textureBuffer.position(0);
	}

	public void draw(GL10 gl, int textureId) {
		gl.glFrontFace(GL10.GL_CCW);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		// New part...
		if (textureId != -1 && textureBuffer != null) {
			gl.glEnable(GL10.GL_TEXTURE_2D);
			// Enable the texture state
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

			// Point to our buffers
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
		}

		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

		// New part...
		if (textureId != -1 && textureBuffer != null) {
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		}

		gl.glDisable(GL10.GL_CULL_FACE);
	}

	public void setRotation(float[] rotation) {
		this.rotation = rotation;
	}

	public float[] getRotation() {
		return rotation;
	}

	public void setPosition(float[] position) {
		this.position = position;
	}

	public float[] getPosition() {
		return position;
	}

}