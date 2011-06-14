package com.maker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.maker.Global;
import com.maker.Logger;
import com.maker.R;
import com.maker.Listeners.EditListener;
import com.maker.Listeners.WinningListener;
import com.maker.graphics.UTMRenderer;
import com.maker.graphics.wrapper.UTMakerGLSurfaceView;
import com.maker.world.World;
import com.maker.world.WorldObject;

public class GameActivity extends Activity {

	private final String FILE_NAME = "filename.obj";
	private World world = new World(null);
	private UTMRenderer renderer;
	private boolean[] isTouched = new boolean[2];

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize(false);
	}

	protected void onCreate(Bundle savedInstanceState, boolean animate) {
		super.onCreate(savedInstanceState);
		initialize(animate);
	}
	
	private void initialize(boolean animate) {
		UTMakerGLSurfaceView view = new UTMakerGLSurfaceView(getApplicationContext());
		renderer = new UTMRenderer(loadBitmaps(Global.ids), world, animate);
		renderer.setMoving(animate);

		view.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		view.setRenderer(renderer);
		view.getHolder().setFormat(PixelFormat.TRANSLUCENT);

		setContentView(view);
	}

	protected void setWorld(World world) {
		if (world == null)
			world = new World(null);

		this.world = world;
	}

	private Bitmap[] loadBitmaps(int[] ids) {
		Bitmap[] bitmaps = new Bitmap[ids.length];

		for (int i = 0; i < ids.length; i++)
			bitmaps[i] = BitmapFactory.decodeResource(getResources(), ids[i]);

		return bitmaps;
	}

	protected UTMRenderer getRenderer() {
		return renderer;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();

		if (action == MotionEvent.ACTION_DOWN)
			getRenderer().touchDown();

		if (action == MotionEvent.ACTION_UP)
			getRenderer().touchUp();

		final int historySize = ev.getHistorySize();
		final int pointerCount = ev.getPointerCount();

//		for (int h = 0; h < historySize; h++) {
//			Logger.debug(getClass(), "At time History: " + ev.getHistoricalEventTime(h));
//			for (int p = 0; p < pointerCount; p++)
//				Logger.debug(getClass(), "\tpointer : " + ev.getPointerId(p) + "(" + ev.getHistoricalX(p, h) + "," + ev.getHistoricalY(p, h) + ")");
//		}

//		Logger.debug(getClass(), "At time Present: " + ev.getEventTime());
		float[][] touch = new float[2][];
		for (int p = 0; p < pointerCount; p++) {
			touch[ev.getPointerId(p)] = new float[] { ev.getX(p), ev.getY(p) };
//			Logger.debug(getClass(), "\tpointer : " + ev.getPointerId(p) + "(" + ev.getX(p) + "," + ev.getY(p) + ")");
		}

		getRenderer().setTouch(touch);
		return super.onTouchEvent(ev);
	}

	public void setChosen(WorldObject object) {
		getRenderer().setChosen(object);
	}

	public void reset() {
		world.reset();
		setChosen(null);
	}

	public void edit(EditListener listener) {
		getRenderer().edit(listener);
	}

	public void save() {
		Global.setSavingWorld(world);
		startActivity(new Intent(this, SaveWorldActivity.class));
	}

}
