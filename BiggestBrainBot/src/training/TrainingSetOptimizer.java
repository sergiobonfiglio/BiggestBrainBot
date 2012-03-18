package training;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class TrainingSetOptimizer {
    public static void main(String[] args) throws IOException {
	String[] dirs = { "/Users/sergio/Desktop/calculateSamples/tokens",
		"/Users/sergio/Desktop/calculateSamples/tokensGuessSign" };
	for (int i = 0; i < dirs.length; i++) {
	    String TSDirPath = dirs[i];
	    optimize(TSDirPath);
	}
    }

    public static void optimize(String TSDirPath) throws IOException {
	File TSDir = new File(TSDirPath);

	int contatore = 0;
	String[] TSTokensDir = TSDir.list();
	for (int i = 0; i < TSTokensDir.length; i++) {

	    File sampleDir = new File(TSDirPath + "/" + TSTokensDir[i]);
	    if (sampleDir.isDirectory()) {
		String[] samples = sampleDir.list();
		ArrayList<BufferedImage> tokens = new ArrayList<BufferedImage>();
		// per tutti i file che rappresetano lo stesso token
		for (int j = 0; j < samples.length; j++) {
		    File sample = new File(TSDirPath + "/" + TSTokensDir[i]
			    + "/" + samples[j]);

		    if (!sample.isHidden()) {
			BufferedImage token = ImageIO.read(sample);

			if (tokens.size() == 0)
			    tokens.add(token);
			else {
			    boolean uguali = false;
			    // controllo che non ci siano immagini uguali
			    for (int k = 0; k < tokens.size() && !uguali; k++) {
				BufferedImage t = tokens.get(k);

				if (token.getWidth() == t.getWidth()
					&& token.getHeight() == t.getHeight()) {

				    uguali = areEquals(token, t);

				}

			    }
			    if (uguali == false) {
				tokens.add(token);
			    } else {
				contatore++;
				sample.renameTo(new File(
					"/Users/sergio/.Trash/"
						+ sample.getName()));
				// sample.delete();
			    }
			}

		    }
		}

	    }
	}
	System.out.println("cancellati " + contatore + " sample");
    }

    private static boolean isScuro(BufferedImage img, int x, int y) {
	boolean scuro = true;
	for (int b = 0; b < img.getRaster().getNumBands() && scuro; b++) {
	    if (img.getRaster().getSample(x, y, b) > 10) {
		scuro = false;
	    }
	}
	return scuro;
    }

    private static boolean areEquals(BufferedImage token, BufferedImage t) {
	boolean uguale = true;
	for (int x = 0; x < t.getWidth() && uguale; x++) {
	    for (int y = 0; y < t.getHeight() && uguale; y++) {
		boolean tScuro = isScuro(t, x, y);
		boolean tokenScuro = isScuro(token, x, y);
		if (tScuro && !tokenScuro || !tScuro && tokenScuro)
		    uguale = false;
	    }
	}
	return uguale;
    }
}
