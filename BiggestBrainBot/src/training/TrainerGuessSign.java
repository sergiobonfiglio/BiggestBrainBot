package training;

import io.PatternExporter;
import io.PatternImporter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pattern.Patternable;
import pattern.gaussian.MultipleGaussianPattern;
import util.Rounder;
import decoder.Decoder;

public class TrainerGuessSign {

    public static void main(String[] args) throws IOException {

	String pathTokensDir = "/Users/sergio/Desktop/calculateSamples/samples/tokens";

	String pathDestinationDir = "/Users/sergio/Desktop/calculateSamples/tokensGuessSign";

	File patternsFile = new File("/Users/sergio/Desktop/patternsGuessSign");
	Patternable[] pattern = PatternImporter.readPatterns(patternsFile);
	Decoder decoder = new Decoder(pattern);
	
	Decoder.classifyTokens(pathTokensDir, pathDestinationDir, decoder);

	TrainingSetOptimizer.optimize(pathDestinationDir);

	String str = "q=()x+-:0123456789";

	char[] classi = str.toCharArray();

	pattern = train(classi);

	// Pattern[] pattern = PatternImporter.readPatterns(patternsFile);

	for (int i = 0; i < pattern.length; i++) {
	    for (int j = 0; j < pattern.length; j++) {
		double distanza = pattern[i].compareTo(pattern[j]);
		if (distanza != Double.MAX_VALUE && i != j) {
		    System.out.println(classi[i] + " -> " + classi[j] + ": "
			    + Rounder.truncate(distanza, 3));
		}
	    }
	    System.out.println("--------------------------------");
	}

	PatternExporter.writePatterns(pattern, patternsFile);

    }

    private static Patternable[] train(char[] classi) throws IOException {
	Patternable[] pattern = new Patternable[classi.length];
	for (int i = 0; i < classi.length; i++) {

	    pattern[i] = new MultipleGaussianPattern(classi[i], 3);

	    String pathTokenDir = "/Users/sergio/Desktop/calculateSamples/tokensGuessSign/"
		    + classi[i];
	    File tokenDir = new File(pathTokenDir);
	    String[] tokens = tokenDir.list();
	    BufferedImage[] imgs = new BufferedImage[tokens.length];

	    for (int j = 0; j < tokens.length; j++) {
		File token = new File(pathTokenDir + "/" + tokens[j]);
		if (!token.isHidden() && token.isFile()) {
		    BufferedImage imgToken = ImageIO.read(token);

		    imgs[j] = imgToken;
		    // pattern[i].add(imgToken);
		}
	    }

	    pattern[i].add(imgs);

	    System.out.println(pattern[i]);
	}
	return pattern;
    }
}
