package com.maker.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.TextView;

import com.maker.Global;
import com.maker.R;
import com.maker.Listeners.EditListener;
import com.maker.world.Custom;
import com.maker.world.Eraser;
import com.maker.world.World;
import com.maker.world.WorldObject;
import com.maker.world.mobile.SimpleReflection;
import com.maker.world.movable.Paddle;
import com.maker.world.terrain.Breakable;
import com.maker.world.terrain.Crate;
import com.maker.world.terrain.WinningTrigger;

public class CreateActivity extends GameActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setWorld(new World(null));
		super.onCreate(savedInstanceState, false);

		// setContentView(R.layout.create_activity);
		final View view = getLayoutInflater().inflate(R.layout.create_activity, null, false);
		ListView objectList = (ListView) view.findViewById(R.id.object_list);

		SlidingDrawer drawer = (SlidingDrawer) view.findViewById(R.id.drawer);
		drawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {

			@Override
			public void onDrawerClosed() {
				SlidingDrawer drawer = (SlidingDrawer) view.findViewById(R.id.drawer);
				drawer.close();

			}
		});

		ArrayList<WorldObject> worldObjects = new ArrayList<WorldObject>();
		worldObjects.add(new Crate());
		worldObjects.add(new SimpleReflection());
		worldObjects.add(new Breakable());
		worldObjects.add(new WinningTrigger());
		worldObjects.add(new Paddle());
		worldObjects.add(new Custom());

		objectList.setAdapter(new ArrayAdapter<WorldObject>(getApplicationContext(), 0, worldObjects) {
			@Override
			public View getView(int position, View v, ViewGroup parent) {
				if (v == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = vi.inflate(R.layout.object_list_item, null);
				}

				ImageView image = (ImageView) v.findViewById(R.id.icon);
				TextView description = (TextView) v.findViewById(R.id.description);

				final WorldObject object = getItem(position);
				int iconID = object.getIcon();
				Drawable icon = getResources().getDrawable(iconID);
				image.setBackgroundDrawable(icon);
				description.setText(object.getDescription());
				
				if (object instanceof Custom) {
					v.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							SlidingDrawer drawer = (SlidingDrawer) view.findViewById(R.id.drawer);
							drawer.close();
							startActivity(new Intent(CreateActivity.this, CustomObjectActivity.class));
						}
					});
				} else {
					v.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							SlidingDrawer drawer = (SlidingDrawer) view.findViewById(R.id.drawer);
							drawer.close();
							setChosen(object);
						}
					});
				}
				return v;
			}
		});

		addContentView(view, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		getMenuInflater().inflate(R.menu.create_menu, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.eraser:
			setChosen(new Eraser());
			break;
		case R.id.reset:
			reset();
			break;
		case R.id.save:
			save();
			break;
		case R.id.edit:
			edit(new EditListener() {
				@Override
				public void edit(WorldObject object) {
					Class editClass = object.getEditActivity();
					if (editClass != null) {
						Global.setEditingObject(object);
						startActivity(new Intent(CreateActivity.this, editClass));
					}
				}
			});
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

}
