package com.maker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.maker.R;

public class FailureDialog extends Dialog{

	public FailureDialog(Context context) {
		super(context);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.failure_dialog);
		setTitle("Try again");
	}

}
