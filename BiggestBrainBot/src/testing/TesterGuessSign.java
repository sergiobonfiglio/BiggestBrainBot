package testing;

import io.PatternImporter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import pattern.Patternable;
import pattern.gaussian.MultipleGaussianPattern;
import tokenizer.ImageNumberTokenizer;
import Jama.EigenvalueDecomposition;
import decoder.Decoder;

public class TesterGuessSign {
    static int maxA = -1, maxB = -1, maxC = -1;
    static double maxHit = -1;

    public static void main(String[] args) throws IOException {

	File patternsFile = new File("/Users/sergio/Desktop/patternsGuessSign");
	Patternable[] pattern = PatternImporter.readPatterns(patternsFile);

	File dirSample = new File(
		"/Users/sergio/Desktop/calculateSamples/samplesUsatiGuessSign");
	String[] samples = dirSample.list();
	int a = 6, b = 1, c = 2;
//	 for (a = 2; a < 7; a++)
//	 for (b = 1; b < 6; b++)
//	 for (c = 1; c < 6; c++)
	test(pattern, dirSample, samples, a, b, c);

	System.out.println("MAX(f(" + maxA + "," + maxB + "," + maxC + ")) = "
		+ maxHit);

    }

    private static void test(Patternable[] pattern, File dirSample,
	    String[] samples, int a, int b, int c) throws IOException {
	for (int i = 0; i < pattern.length; i++) {
	    ((MultipleGaussianPattern) pattern[i]).setA(a);
	    ((MultipleGaussianPattern) pattern[i]).setB(b);
	    ((MultipleGaussianPattern) pattern[i]).setC(c);
	}
	int esatte = 0;
	int tokenTotali = 0;
	int sampleTotali = 0;
	Decoder decoder = new Decoder(pattern);
	for (int i = 0; i < samples.length; i++) {
	    File f = new File(dirSample + "/" + samples[i]);
	    if (!f.isHidden() && !f.isDirectory()) {
		String fileName = f.getName();
		int strSampleIndex = fileName.lastIndexOf("sample");
		int lastIndexOfR = fileName.lastIndexOf('r');
		int rIndex;
		if (lastIndexOfR == -1)
		    rIndex = fileName.length() - 4;
		else
		    rIndex = lastIndexOfR;

		String realExpr = fileName
			.substring(strSampleIndex + 6, rIndex);

		// leggo il sample
		BufferedImage img = ImageIO.read(f);

		// Creo il tokenizer per il sample
		ImageNumberTokenizer t = new ImageNumberTokenizer(img);

		// creo la lista di token estratti dal sample
		ArrayList<BufferedImage> lista = new ArrayList<BufferedImage>();
		BufferedImage token = t.nextToken();
		while (token != null) {
		    lista.add(token);
		    token = t.nextToken();
		}

		BufferedImage[] tokens = new BufferedImage[lista.size()];
		lista.toArray(tokens);

		// decodifico l'espressione
		String decodedExpr = "";
		decodedExpr = decoder.decodeExpr(tokens);
		decodedExpr = decodedExpr.trim();
	

		sampleTotali++;
		if (realExpr.equals(decodedExpr)) {
		    esatte += tokens.length;

		} else {
//		     System.out.println("errore: " + realExpr
//		     + " decodificato in: " + decodedExpr);

		    int minLength = Math.min(decodedExpr.length(), realExpr
			    .length());
		    for (int j = 0; j < minLength; j++) {
			if (decodedExpr.charAt(j) != ' '
				&& realExpr.charAt(j) == decodedExpr.charAt(j)) {
			    esatte++;
			} else if (decodedExpr.charAt(j) != ' ') {
//			    System.out.println(decodedExpr.charAt(j)
//				    + " scambiato per " + realExpr.charAt(j));
			}
		    }

		}
		tokenTotali += tokens.length;

	    }
	}
	double hitRate = ((double) esatte / tokenTotali) * 100;

	// System.out.println(a + " " + b + " " + c);

	// System.out.println("esatte=" + esatte + "/" + (samples.length-1));

	// System.out.println("percentuale decodifiche esatte="
	// + hitRate + "%");

	System.out.println("f(" + a + "," + b + "," + c + ")=" + hitRate + "("
		+ esatte + "/" + tokenTotali + ")");
	if (hitRate > maxHit) {
	    maxHit = hitRate;
	    maxA = a;
	    maxB = b;
	    maxC = c;
	}
    }

    public static void test2() {

	// calcolo matrice di covarianza
	// Matrix c = new Matrix();

	// calcolo autovalori
	EigenvalueDecomposition egDecomposition;
	// eg= new EigenvalueDecomposition();

    }

}
