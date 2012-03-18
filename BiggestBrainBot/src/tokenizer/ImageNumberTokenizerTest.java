package tokenizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

public class ImageNumberTokenizerTest extends TestCase {

    public final void testNextToken() throws IOException {
	String pathSamplesDir = "/Users/sergio/Documents/eclipseWorkspace/BiggestBrainBot/calculateSamples/samples";
	File sampleDir = new File(pathSamplesDir);
	String[] samples = sampleDir.list();

	int id = 0;

	for (int j = 0; j < samples.length; j++) {
	    File sample = new File(pathSamplesDir + "/" + samples[j]);
	    if (!sample.isHidden() && sample.isFile()) {
//		System.out.println(sample.getAbsolutePath());
		ImageNumberTokenizer t = new ImageNumberTokenizer(ImageIO
			.read(sample));

		BufferedImage token = t.nextToken();
		do {

		    // ImageIO.write(token, "png", new File(pathSamplesDir
		    // + "/tokens/token" + id + ".png"));
		    BufferedImage expectedToken = ImageIO.read(new File(
			    pathSamplesDir + "/tokens/token" + id + ".png"));

		    areImageEquals(expectedToken, token);
		    id++;
		    token = t.nextToken();
		} while (token != null);

	    }
	}
    }

    private boolean areImageEquals(BufferedImage img1, BufferedImage img2) {
	assertEquals(img1.getWidth(), img2.getWidth());
	assertEquals(img1.getHeight(), img2.getHeight());

	for (int y = 0; y < img1.getHeight(); y++) {
	    for (int x = 0; x < img1.getWidth(); x++) {
		assertEquals(img1.getRGB(x, y), img2.getRGB(x, y));
	    }
	}

	return true;
    }

}
