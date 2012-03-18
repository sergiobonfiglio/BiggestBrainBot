
public class MainGuessSign {

    public static void main(String[] args) {

	// ScreenGrabber sg = new ScreenGrabber();
	ScreenGrabberGuessSign sg = new ScreenGrabberGuessSign();

	Thread t = new Thread(sg);
	t.start();

    }

}
