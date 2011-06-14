package com.maker.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.maker.Global;
import com.maker.R;
import com.maker.world.WorldObject;
import com.maker.world.mobile.SimpleReflection;

public class EditPenguinActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_penguin_activity);

		Button save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				WorldObject object = Global.getEditingObject();
				Global.setEditingObject(null);
				
				if (object == null)
					return;

				if (!(object instanceof SimpleReflection))
					return;

				SimpleReflection simpleReflection = (SimpleReflection) object;

				EditText xEdit = (EditText) findViewById(R.id.x);
				EditText yEdit = (EditText) findViewById(R.id.y);
				EditText magnitudeEdit = (EditText) findViewById(R.id.magnitude);

				float x = Float.parseFloat(xEdit.getText().toString());
				float y = Float.parseFloat(yEdit.getText().toString());
				float magnitude = Float.parseFloat(magnitudeEdit.getText().toString());

				simpleReflection.setVelocity(new float[] { x, y, magnitude });
				
				finish();
			}
		});

	}
}
