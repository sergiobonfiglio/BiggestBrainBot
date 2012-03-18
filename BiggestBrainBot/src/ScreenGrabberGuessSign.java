
import io.PatternImporter;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import pattern.Patternable;
import tokenizer.ImageNumberTokenizer;
import calculator.ExpressionEvaluator;
import decoder.Decoder;

public class ScreenGrabberGuessSign implements Runnable {

    private Robot robot;
    private Rectangle rect;
    private HashMap<Character, Point> signPosition;

    public ScreenGrabberGuessSign() {

	try {
	    this.robot = new Robot();

	} catch (AWTException e) {
	    e.printStackTrace();
	}
	this.initRect();

	this.initSigns();
    }

    private void initSigns() {
	signPosition = new HashMap<Character, Point>();
	JOptionPane.showMessageDialog(null,
		"Metti il mouse al centro del segno -");
	Point pMinus = MouseInfo.getPointerInfo().getLocation();
	signPosition.put('-', pMinus);

	// JOptionPane.showMessageDialog(null,
	// "Metti il mouse al centro del segno +");
	// Point pPlus = MouseInfo.getPointerInfo().getLocation();
	Point pPlus = new Point(pMinus.x + 100, pMinus.y);
	signPosition.put('+', pPlus);

	// JOptionPane.showMessageDialog(null,
	// "Metti il mouse al centro del segno x");
	// Point pMult = MouseInfo.getPointerInfo().getLocation();
	Point pMult = new Point(pMinus.x + 200, pMinus.y);

	signPosition.put('x', pMult);

	// JOptionPane.showMessageDialog(null,
	// "Metti il mouse al centro del segno /");
	// Point pDiv = MouseInfo.getPointerInfo().getLocation();
	Point pDiv = new Point(pMinus.x + 300, pMinus.y);

	signPosition.put('/', pDiv);
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
	int totErrori = 0;
	int totali = 0;
	while (true) {
	    if (rect != null && signPosition != null) {
		BufferedImage img = robot.createScreenCapture(rect);

		try {
		    ImageIO.write(img, "png", new File(
			    "/Users/sergio/Desktop/sampleGS/"
				    + (int) (Math.random() * 10000)
				    + "sample.png"));
		} catch (IOException e1) {
		    e1.printStackTrace();
		}

		ImageNumberTokenizer t = new ImageNumberTokenizer(img);

		ArrayList<BufferedImage> lista = new ArrayList<BufferedImage>();
		BufferedImage token = t.nextToken();
		while (token != null) {
		    lista.add(token);
		    token = t.nextToken();
		}

		BufferedImage[] tokens = new BufferedImage[lista.size()];
		lista.toArray(tokens);

		File patternsFile = new File(
			"/Users/sergio/Desktop/patternsGuessSign");
		Patternable[] pattern = PatternImporter
			.readPatterns(patternsFile);
		Decoder decoder = new Decoder(pattern);
		String decodedExpr = decoder.decodeExpr(tokens);

		String secondoMembro = decodedExpr.substring(decodedExpr
			.lastIndexOf('=') + 1);
		secondoMembro = secondoMembro.trim();

		try {
		    int decodedResult = Integer.parseInt(secondoMembro);

		    String primoMembro = decodedExpr.substring(0, decodedExpr
			    .lastIndexOf('=') - 1);

		    String possibleSign = "+-x:";
		    char guessedSign = ' ';
		    for (int j = 0; j < possibleSign.length()
			    && guessedSign == ' '; j++) {
			String expr11 = primoMembro.replace('q', possibleSign
				.charAt(j));

			int currentResult = ExpressionEvaluator
				.evaluateInfix(expr11);

			if (currentResult == decodedResult) {
			    guessedSign = possibleSign.charAt(j);
			}
		    }
		    totali++;
		    clickSign(robot, guessedSign);

		    try {
			System.out.println(totErrori + " errori su " + totali
				+ " = " + (double) totErrori / totali + "%");
			Thread.sleep(500);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		} catch (NumberFormatException e) {
		    totErrori++;

		}

	    } else {
		// aspetto che venga definita l'area dello schermo da catturare
		try {
		    this.wait();
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    private void clickSign(Robot robot, char guessedSign) {

	Point p = signPosition.get(guessedSign);
	if (p == null) {
	    String str = "-+x/";
	    int rand = (int) (Math.random() * str.length());
	    p = signPosition.get(str.charAt(rand));
	}
	robot.mouseMove(p.x, p.y);
	robot.mousePress(InputEvent.BUTTON1_MASK);
	robot.mouseRelease(InputEvent.BUTTON1_MASK);

    }
}
