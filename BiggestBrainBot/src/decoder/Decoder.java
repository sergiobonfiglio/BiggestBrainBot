package decoder;

import io.PatternImporter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import pattern.Patternable;
import training.TrainingSetOptimizer;

public class Decoder {

    private Patternable[] patterns;

    public Decoder(Patternable[] patterns) {
	this.patterns = patterns;
    }

    private static String format(String expr) {

	String formatted = "";

	for (int j = 0; j < expr.length(); j++) {
	    char c = expr.charAt(j);
	    if (!isDigit(c)) {
		formatted += c + " ";
	    } else {
		int i = j;
		do {
		    if (c != ' ')
			formatted += c;
		    i++;
		    if (i < expr.length())
			c = expr.charAt(i);
		    else {
			return formatted + " ";
		    }
		} while (isDigit(c) || c == ' ');

		j = i - 1;
		formatted += " ";
	    }

	}

	return formatted;
    }

    private static boolean isDigit(char c) {
	if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4'
		|| c == '5' || c == '6' || c == '7' || c == '8' || c == '9') {
	    return true;
	} else {
	    return false;
	}

    }

    private static int toDigit(String s) {
	try {
	    int d = Integer.valueOf(s);
	    if (d >= 0)
		return d;
	    else
		return -1;
	} catch (NumberFormatException e) {
	    return -1;
	}

    }

    public String decodeExpr(BufferedImage[] tokens) {
	String expr = "";
	boolean aperta = false;
	for (int i = 0; i < tokens.length; i++) {
	    char token = decodeToken(tokens[i]);
	    if (aperta && token == '(') {
		token = ')';
		aperta = false;
	    }
	    if (token == '(')
		aperta = true;

	    expr += token;
	}
	expr = format(expr);
	return expr;
    }

    private char decodeToken(BufferedImage token) {
	double min = Double.MAX_VALUE;
	int minIndex = -1;
	for (int i = 0; i < patterns.length; i++) {
	    double distanza = patterns[i].distance(token);
	    if (distanza < min) {
		min = distanza;
		minIndex = i;
	    }
	}

	return patterns[minIndex].getId();
    }

    public static void main(String[] args) throws IOException {
	String pathTokensDir = "/Users/sergio/Desktop/calculateSamples/samples/tokens";

	String pathDestinationDir = "/Users/sergio/Desktop/calculateSamples/tokensGuessSign";

	File patternsFile = new File("/Users/sergio/Desktop/patternsGuessSign");
	Patternable[] pattern = PatternImporter.readPatterns(patternsFile);
	Decoder decoder = new Decoder(pattern);

	classifyTokens(pathTokensDir, pathDestinationDir, decoder);
	TrainingSetOptimizer.optimize(pathDestinationDir);

    }

    public static void classifyTokens(String pathTokensDir,
	    String pathDestinationDir, Decoder decoder) throws IOException {
	File tokensDir = new File(pathTokensDir);
	String[] tokens = tokensDir.list();
	for (int j = 0; j < tokens.length; j++) {
	    File token = new File(pathTokensDir + "/" + tokens[j]);
	    if (!token.isHidden() && token.isFile()) {
		// per ogni token
		System.out.println(token.getAbsolutePath());

		BufferedImage imgToken = ImageIO.read(token);

		char decoded = decoder.decodeToken(imgToken);

		File dest = new File(pathDestinationDir + "/" + decoded + "/"
			+ token.getName());
		token.renameTo(dest);
	    }
	}
    }
}
