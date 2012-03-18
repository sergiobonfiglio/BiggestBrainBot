package io;

import java.io.File;

import junit.framework.TestCase;
import pattern.Patternable;
import pattern.gaussian.GaussianPattern;

public class PatternImporterTest extends TestCase {

    public void testReadPatterns() {

	Patternable[] patterns = { new GaussianPattern('0') };
	File file = new File("/Users/sergio/Desktop/prova.txt");
	test(patterns, file);

    }

    public void testNullPointerExceptionImporter() {
	try {
	    PatternImporter.readPatterns(null);
	    fail("NullPointerException expected");
	} catch (NullPointerException e) {
	}
    }

    public void testNullPointerExceptionExporter() {
	try {
	    PatternExporter.writePatterns(null, null);
	    fail("NullPointerException expected");
	} catch (NullPointerException e) {
	}
    }

    public void testNullPattern() {
	File file = new File("/Users/sergio/Desktop/prova.txt");
	PatternExporter.writePatterns(null, file);
	Patternable[] readed = PatternImporter.readPatterns(file);

	assertTrue(readed == null);

    }

    private void test(Patternable[] patterns, File file) {
	PatternExporter.writePatterns(patterns, file);
	Patternable[] patternsReaded = PatternImporter.readPatterns(file);

	assertTrue(patterns.length == patternsReaded.length);
    }

}
