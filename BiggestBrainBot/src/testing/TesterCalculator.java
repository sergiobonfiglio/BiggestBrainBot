package testing;
import io.PatternImporter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import decoder.Decoder;

import pattern.Patternable;
import pattern.gaussian.MultipleGaussianPattern;
import tokenizer.ImageNumberTokenizer;
import Jama.EigenvalueDecomposition;

public class TesterCalculator {

    static int maxA = -1, maxB = -1, maxC = -1;
    static double maxHit = -1;

    public static void main(String[] args) throws IOException {

	File patternsFile = new File("/Users/sergio/Desktop/patterns");
	Patternable[] pattern = PatternImporter.readPatterns(patternsFile);

	File dirSample = new File(
		"/Users/sergio/Desktop/calculateSamples/samplesUsati");
	String[] samples = dirSample.list();
	int a = 5, b = 2, c = 4;
//	 for (a = 2; a < 6; a++)
//	 for (b = 2; b < 6; b++)
//	 for (c = 2; c < 6; c++)
	test(pattern, dirSample, samples, a, b, c);

	System.out.println("(" + maxA + "," + maxB + "," + maxC + ")-maxHit="
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
	Decoder decoder = new Decoder(pattern);
	for (int i = 0; i < samples.length; i++) {
	    File f = new File(dirSample + "/" + samples[i]);
	    if (!f.isHidden() && !f.isDirectory()) {
		// System.out.println("file: " + samples[i]);
		String strExpr = f.getName();
		int r = strExpr.lastIndexOf("sample");
		strExpr = strExpr.substring(r + 6, strExpr.length() - 4);

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
		String expr = "";
		expr = decoder.decodeExpr(tokens);

		expr = expr.replace('=', ' ');
		expr = expr.trim();

		// System.out.println("strExpr=" + strExpr);
		// System.out.println("expr=" + expr);

		// int result = ExpressionEvaluator.evaluateInfix(expr);

		if (strExpr.equals(expr)) {
		    esatte += tokens.length;
		} else {
		    // System.out.println("errore: " + strExpr
		    // + " decodificato in: " + expr);

		    int minLength = Math.min(strExpr.length(), expr.length());
		    for (int j = 0; j < minLength; j++) {
			if (strExpr.charAt(j) != ' '
				&& strExpr.charAt(j) == expr.charAt(j)) {
			    esatte++;
			} else if (strExpr.charAt(j) != ' ') {
			    System.out.println(expr.charAt(j)
				    + " scambiato per " + strExpr.charAt(j));
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
