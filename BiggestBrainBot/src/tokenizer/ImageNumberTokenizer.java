package tokenizer;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class ImageNumberTokenizer {

    private BufferedImage img;
    private int startX;

    public ImageNumberTokenizer(BufferedImage img) {
	this.img = img;
    }

    private Rectangle findBounds(int startX) {
	int minX = findMinX(startX);
	if (minX != -1)
	    return findRect(minX);
	else
	    return null;

    }

    public BufferedImage nextToken() {
	BufferedImage token = null;

	/* trovo i bound del primo token */
	Rectangle rect = findBounds(this.startX);
	if (rect != null) {
	    this.startX = rect.x + rect.width + 1;

	    /* creo immagine token */
	    token = new BufferedImage(rect.width, rect.height,
		    BufferedImage.TYPE_INT_RGB);

	    WritableRaster wraster = token.getRaster();
	    Raster source = img.getRaster();
	    int[] pixels = new int[rect.width * rect.height * 3];
	    source.getPixels(rect.x, rect.y, rect.width, rect.height, pixels);
	    wraster.setPixels(0, 0, rect.width, rect.height, pixels);
	}
	return token;
    }

    private Rectangle findRect(int minX) {
	Rectangle rect = null;
	Raster raster = img.getRaster();
	int maxX = -1;

	/*
	 * Se in ogni colonna trovo un pixel nero fa tutto parte dello stesso
	 * token
	 */
	int x = minX + 1;
	int minY = Integer.MAX_VALUE;
	int maxY = -1;
	while (maxX == -1 && x < raster.getWidth()) {
	    boolean foundBlack = false;
	    int y = 0;
	    while (y < raster.getHeight()) {
		if (isBlack(x, y)) {
		    foundBlack = true;
		    if (y < minY)
			minY = y;
		    if (y > maxY)
			maxY = y;
		}
		y++;
	    }
	    if (foundBlack == false || x == raster.getWidth() - 1) {
		if (x == raster.getWidth() - 1)
		    maxX = x;
		else
		    maxX = x - 1;
		rect = new Rectangle(minX, minY, maxX - minX + 1, maxY - minY
			+ 1);
		return rect;
	    }
	    x++;
	}

	return rect;
    }

    private int findMinX(int startX) {
	Raster raster = img.getRaster();
	int minX = -1;
	for (int x = startX; x < raster.getWidth(); x++) {
	    for (int y = 0; y < raster.getHeight(); y++) {
		if (isBlack(x, y)) {
		    return x;
		}
	    }
	}
	return minX;
    }

    private boolean isBlack(int x, int y) {
	Raster raster = img.getRaster();
	boolean black = true;
	for (int b = 0; black == true && b < raster.getNumBands(); b++) {
	    int band = raster.getSample(x, y, b);
	    if (band < 10) {
		black = true;
	    } else {
		black = false;

	    }

	}

	return black;
    }

   
}
