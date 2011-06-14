package com.maker.graphics.wrapper;

import javax.microedition.khronos.opengles.GL;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class UTMakerGLSurfaceView extends GLSurfaceView {

	public UTMakerGLSurfaceView(Context context) {
		super(context);

		setFocusable(true);

		setGLWrapper(new GLSurfaceView.GLWrapper() {
			@Override
			public GL wrap(GL gl) {
				return new MatrixTrackingGL(gl);
			}
		});
	}
}