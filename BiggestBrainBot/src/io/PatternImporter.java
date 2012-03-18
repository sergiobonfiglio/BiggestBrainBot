package io;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import pattern.Patternable;

public class PatternImporter {
    
    public static Patternable[] readPatterns(File file) {
	Patternable[] patterns = null;
	FileInputStream fis = null;
	ObjectInputStream reader = null;

	try {
	    fis = new FileInputStream(file);
	    reader = new ObjectInputStream(fis);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	Object o = null;
	try {
	    o = reader.readObject();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	patterns = (Patternable[]) o;

	return patterns;
    }

}
