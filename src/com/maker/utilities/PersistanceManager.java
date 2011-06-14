package com.maker.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.HashSet;

import android.content.Context;

public class PersistanceManager {
	private static HashSet<String> files;

	public static void save(Object object, String fileName, Context context) throws Exception {
		if (!fileName.equals(getFileManger()) && getFiles(context).contains(fileName))
			throw new Exception("FILE EXISTS");
		
		try {
			FileOutputStream fos = null;
			try {
				fos = context.openFileOutput(getPREFIX() + fileName + getPOSTFIX(), Context.MODE_PRIVATE);
				ObjectOutputStream out = new ObjectOutputStream(fos);
				out.writeObject(object);
				out.close();
				if (!fileName.equals(getFileManger())) 
					addFile(fileName, context);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (fos != null)
					fos.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static void addFile(String fileName, Context context) {
		try {
			getFiles(context).add(fileName);
			save(getFiles(context), getFileManger(), context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Object load(String fileName, Context context) {
		try {
			FileInputStream fin = context.openFileInput(getPREFIX() + fileName + getPOSTFIX());
			try {
				ObjectInputStream in = new ObjectInputStream(fin);
				try {
					return in.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fin.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getPREFIX() {
		return "UTMAKER_";
	}

	public static String getPOSTFIX() {
		return ".WRLD";
	}
	
	public static HashSet<String> getFiles(Context context){
		if (files == null)
			files = (HashSet<String>) load(getFileManger(), context);
		
		if (files == null)
			files = new HashSet<String>();
		
		return files;
	}
	
	public static String getFileManger() {
		return "UTMMAKER_FILEMANAGER.TLB";
	}
}
