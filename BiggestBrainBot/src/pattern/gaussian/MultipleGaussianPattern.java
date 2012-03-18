package pattern.gaussian;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import pattern.Patternable;
import util.Rounder;

public class MultipleGaussianPattern implements Patternable {

    char id;
    GaussianPattern[] subPatterns;

    public MultipleGaussianPattern(char id, int numSubPattern) {
	this.id = id;
	this.subPatterns = new GaussianPattern[numSubPattern];
	for (int i = 0; i < subPatterns.length; i++) {
	    subPatterns[i] = new GaussianPattern(id);
	}
    }

    public void add(BufferedImage[] images) {
	addSubPatterns(images);
    }

    private BufferedImage[] extractSubImages(BufferedImage[] images, int i) {
	BufferedImage[] result = null;

	if (images != null) {
	    result = new BufferedImage[images.length];
	    for (int j = 0; j < images.length; j++) {
		if (images[j] != null) {
		    int pieceH = images[j].getHeight() / subPatterns.length;
		    Rectangle rect = new Rectangle(0, i * pieceH, images[j]
			    .getWidth(), pieceH);
		    result[j] = extractSubImage(images[j], rect);
		}
	    }
	}
	return result;

    }

    private void addSubPatterns(BufferedImage[] images) {
	if (images != null) {
	    for (int j = 0; j < subPatterns.length; j++) {
		BufferedImage[] subImages = extractSubImages(images, j);
		subPatterns[j].add(subImages);
	    }
	}
    }

    private static BufferedImage extractSubImage(BufferedImage img,
	    Rectangle rect) {
	try {
	    BufferedImage subImg = new BufferedImage(rect.width, rect.height,
		    BufferedImage.TYPE_INT_RGB);

	    WritableRaster wraster = subImg.getRaster();
	    Raster source = img.getRaster();
	    int[] pixels = new int[rect.width * rect.height * 3];

	    source.getPixels(rect.x, rect.y, rect.width, rect.height, pixels);
	    wraster.setPixels(0, 0, rect.width, rect.height, pixels);
	    return subImg;
	} catch (IllegalArgumentException e) {
	    return null;
	}
    }

    public double compareTo(BufferedImage img) {

	MultipleGaussianPattern p = new MultipleGaussianPattern('E',
		subPatterns.length);
	BufferedImage[] a = { img };
	p.add(a);

	return this.compareTo(p);
    }

    public double compareTo(Patternable p) {
	if (p instanceof MultipleGaussianPattern) {
	    MultipleGaussianPattern mp = (MultipleGaussianPattern) p;

	    if (mp.subPatterns.length == this.subPatterns.length) {
		double dist = 0;
		double maxD = 0;
		for (int i = 0; i < subPatterns.length; i++) {
		    double currentD = subPatterns[i]
			    .compareTo(mp.subPatterns[i]);
		    dist += currentD;
		    if (currentD > maxD)
			maxD = currentD;
		}

		return maxD + dist / subPatterns.length;
	    }

	}
	return Double.MAX_VALUE;
    }

    public double distance(BufferedImage p) {
	return this.compareTo(p);
    }

    public char getId() {
	return id;
    }

    @Override
    public String toString() {
	String str = "" + id + "-----------------------\n";
	for (int i = 0; i < subPatterns.length; i++) {
	    GaussianPattern p = subPatterns[i];
	    str += "(m=(" + p.media.x + "," + p.media.y + "); sqX="
		    + Rounder.truncate(p.scartoQuadMedioX, 3) + "; sqY="
		    + Rounder.truncate(p.scartoQuadMedioY, 3) + ")\n";
	}
	return str;
    }

    /*
     * Calcola la matrice di covarianza del subpattern i
     */
    public void calcolaMatCovarianza(int i) {

    }

    public void setA(int a) {
	for (int i = 0; i < subPatterns.length; i++) {
	    subPatterns[i].setA(a);
	}
    }

    public void setB(int b) {
	for (int i = 0; i < subPatterns.length; i++) {
	    subPatterns[i].setB(b);
	}
    }

    public void setC(int c) {
	for (int i = 0; i < subPatterns.length; i++) {
	    subPatterns[i].setC(c);
	}
    }

}
