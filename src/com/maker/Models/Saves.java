package com.maker.Models;

import java.io.Serializable;
import java.util.HashSet;

public class Saves implements Serializable {
	private HashSet<String> files = new HashSet<String>();
	
	
	public void addFile(String fileName){
		files.add(fileName);
	}
	
	public void removeFile(String fileName){
		files.remove(fileName);
	}
}
