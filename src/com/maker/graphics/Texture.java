package com.maker.graphics;

import android.graphics.Bitmap;

public class Texture {

	private int textureId = -1;
	private Bitmap bitmap;

	public Texture (int textureId, Bitmap bitmap){
		this.textureId = textureId;
		this.bitmap = bitmap;
	}
	
	public int getTextureId(){
		return textureId;
	}
	
	public Bitmap getBitmap (){
		return bitmap;
	}
}
