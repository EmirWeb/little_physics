package com.maker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.maker.R;

public class SuccessDialog extends Dialog {

	private View.OnClickListener listener;

	protected SuccessDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public SuccessDialog(Context context, int theme) {
		super(context, theme);
	}

	public SuccessDialog(Context context, View.OnClickListener listener) {
		super(context);
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (Math.random() < 0.5) {
			setContentView(R.layout.success_splash_dialog);
			setTitle("Ummm... now what?");
		} else {
			setContentView(R.layout.success_genius_dialog);
			setTitle("Genius!");
		}

		Button success = (Button) findViewById(R.id.next);
		success.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dismiss();
			}
		});
	}
}