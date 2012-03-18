
public class MainCalculator {

    public static void main(String[] args) {

	ScreenGrabber sg = new ScreenGrabber();

	Thread t = new Thread(sg);
	t.start();

    }
}
