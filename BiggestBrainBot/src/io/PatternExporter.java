package io;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import pattern.Patternable;

public class PatternExporter {

    public static void writePatterns(Patternable[] patterns, File file) {
	FileOutputStream fos;
	ObjectOutputStream writer;
	try {
	    fos = new FileOutputStream(file);
	    writer = new ObjectOutputStream(fos);

	    writer.writeObject(patterns);

	    writer.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

}
