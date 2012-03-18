
import io.PatternImporter;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import pattern.Patternable;
import tokenizer.ImageNumberTokenizer;
import calculator.ExpressionEvaluator;
import decoder.Decoder;

public class ScreenGrabber implements Runnable {

    private Robot robot;
    private Rectangle rect;

    public ScreenGrabber() {

	try {
	    this.robot = new Robot();

	} catch (AWTException e) {
	    e.printStackTrace();
	}
	this.initRect();
    }

    public void initRect() {
	JOptionPane
		.showMessageDialog(null,
			"Metti il mouse sull'angolo in alto a sinistra del rettangolo da catturare");
	Point tl = MouseInfo.getPointerInfo().getLocation();

	JOptionPane
		.showMessageDialog(null,
			"Metti il mouse sull'angolo in basso a destra del rettangolo da catturare");
	Point br = MouseInfo.getPointerInfo().getLocation();

	this.rect = new Rectangle(tl.x, tl.y, br.x - tl.x + 1, br.y - tl.y + 1);
    }

    public void run() {
	while (true) {
	    if (rect != null) {
		BufferedImage img = robot.createScreenCapture(rect);

		// try {
		// ImageIO.write(img, "png", new File("/Users/sergio/Desktop/"
		// + (int) (Math.random() * 10000) + "sample.png"));
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }

		ImageNumberTokenizer t = new ImageNumberTokenizer(img);

		ArrayList<BufferedImage> lista = new ArrayList<BufferedImage>();
		BufferedImage token = t.nextToken();
		while (token != null) {
		    lista.add(token);
		    token = t.nextToken();
		}

		BufferedImage[] tokens = new BufferedImage[lista.size()];
		lista.toArray(tokens);

		File patternsFile = new File("/Users/sergio/Desktop/patterns");
		Patternable[] pattern = PatternImporter
			.readPatterns(patternsFile);
		Decoder decoder = new Decoder(pattern);
		String expr = decoder.decodeExpr(tokens);

		expr = expr.replace('=', ' ');
		expr = expr.trim();

		System.out.println("expr=" + expr);

		int result = ExpressionEvaluator.evaluateInfix(expr);

		typeInt(robot, result);

		System.out.println(result);
		try {
		    Thread.sleep(600);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}

	    } else {
		try {
		    System.out.println("aspetto...");
		    this.wait();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    private static void typeInt(Robot robot, int number) {
	try {
	    String numberStr = Integer.toString(number);
	    for (int i = 0; i < numberStr.length(); i++) {
		int digitInt = Integer.parseInt("" + numberStr.charAt(i));
		int keycode = digit2keyCode(digitInt);
		robot.keyPress(keycode);
		robot.keyRelease(keycode);
	    }
	} catch (NumberFormatException e) {
	    System.out.println("errore!");
	    int keycode = digit2keyCode((int) (Math.random() * 10));
	    robot.keyPress(keycode);
	    robot.keyRelease(keycode);
	    robot.keyPress(keycode);
	    robot.keyRelease(keycode);
	}
    }

    private static int digit2keyCode(int c) {
	switch (c) {
	case 0:
	    return KeyEvent.VK_0;
	case 1:
	    return KeyEvent.VK_1;
	case 2:
	    return KeyEvent.VK_2;
	case 3:
	    return KeyEvent.VK_3;
	case 4:
	    return KeyEvent.VK_4;
	case 5:
	    return KeyEvent.VK_5;
	case 6:
	    return KeyEvent.VK_6;
	case 7:
	    return KeyEvent.VK_7;
	case 8:
	    return KeyEvent.VK_8;
	case 9:
	    return KeyEvent.VK_9;
	default:
	    return -1;

	}
    }
}
