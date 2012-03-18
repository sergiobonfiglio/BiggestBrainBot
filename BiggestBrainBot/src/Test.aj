
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import decoder.Decoder;

public aspect Test {

    pointcut format(String s) : execution( * Decoder.format(String) ) && args(s);

    after(String s) throwing() : format(s) {
	System.out.println("format(" + s + ") threw an exception");

    }

    pointcut decode(BufferedImage img): call(char decodeToken*(BufferedImage)) && args(img);

    after(BufferedImage img) throwing() : decode(img) {
	System.out.println("can't decode");
	File file = new File("/Users/sergio/Desktop/unknown_token.png");
	try {
	    ImageIO.write(img, "png", file);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
