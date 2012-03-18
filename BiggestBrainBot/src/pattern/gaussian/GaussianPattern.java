package pattern.gaussian;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import pattern.Patternable;

public class GaussianPattern implements Patternable {

    char id;

    Point media;
    double scartoQuadMedioX;
    double scartoQuadMedioY;

    private ArrayList<Point> punti = new ArrayList<Point>();
    private int sommaX = 0;
    private int sommaY = 0;

    private static int a = 5, b = 2, c = 4;

    public GaussianPattern(char id) {
	this.id = id;
    }

    private void add(BufferedImage img) {
	if (img != null)
	    for (int y = 0; y < img.getHeight(); y++)
		for (int x = 0; x < img.getWidth(); x++)
		    if (isScuro(img, x, y)) {
			punti.add(new Point(x, y));
			sommaX += x;
			sommaY += y;
		    }
    }

    private boolean isScuro(BufferedImage img, int x, int y) {
	boolean scuro = true;
	for (int b = 0; b < img.getRaster().getNumBands() && scuro; b++) {
	    if (img.getRaster().getSample(x, y, b) > 10) {
		scuro = false;
	    }
	}
	return scuro;
    }

    public void add(BufferedImage[] images) {
	for (int i = 0; i < images.length; i++) {
	    add(images[i]);
	}

	if (punti.size() != 0) {
	    this.media = calcolaMedia();
	    calcolaScartiQuadraticiMedi();
	} else {
	    this.media = new Point(0, 0);
	    this.scartoQuadMedioX = 0;
	    this.scartoQuadMedioY = 0;
	}
	punti = null;

    }

    private Point calcolaMedia() {
	int xm = sommaX / punti.size();
	int ym = sommaY / punti.size();
	Point pMedia = new Point(xm, ym);
	return pMedia;
    }

    private void calcolaScartiQuadraticiMedi() {
	double sommaDistanzeX = 0;
	double sommaDistanzeY = 0;
	for (int i = 0; i < punti.size(); i++) {
	    int scartox = media.x - punti.get(i).x;
	    sommaDistanzeX += scartox * scartox;
	    int scartoy = media.y - punti.get(i).y;
	    sommaDistanzeY += scartoy * scartoy;
	}
	this.scartoQuadMedioX = sommaDistanzeX / punti.size();
	this.scartoQuadMedioY = sommaDistanzeY / punti.size();
    }

    private double distance(Point p1, Point p2) {
	int sommaQuadrati = (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y)
		* (p1.y - p2.y);
	return Math.sqrt(sommaQuadrati);
    }

    public double compareTo(BufferedImage img) {

	GaussianPattern p = new GaussianPattern('E');
	BufferedImage[] a = { img };
	p.add(a);

	return this.compareTo(p);
    }

    public double compareTo(GaussianPattern p) {

	double dMedia = 0;
	double dScartoX = 0;
	double dScartoY = 0;

	dMedia = distance(media, p.media);
	dScartoX = Math.abs(this.scartoQuadMedioX - p.scartoQuadMedioX);
	dScartoY = Math.abs(this.scartoQuadMedioY - p.scartoQuadMedioY);

	return a * dMedia + b * dScartoX + c * dScartoY;
    }


    @Override
    public String toString() {
	String str = "";

	str = id + "(m=" + media.x + "," + media.y + ";sqX=" + scartoQuadMedioX
		+ ";sqY=" + scartoQuadMedioY + ")";

	return str;
    }

    public double compareTo(Patternable p) {
	if (p instanceof GaussianPattern)
	    return this.compareTo((GaussianPattern) p);
	return Double.MAX_VALUE;
    }

    public double distance(BufferedImage p) {
	return this.compareTo(p);
    }

    public char getId() {
	return this.id;
    }

    public void setA(int a) {
	this.a = a;
    }

    public void setB(int b) {
	this.b = b;
    }

    public void setC(int c) {
	this.c = c;
    }

}
