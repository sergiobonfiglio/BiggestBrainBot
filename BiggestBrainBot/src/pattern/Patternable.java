package pattern;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public interface Patternable extends Serializable {

    public char getId();
    
    public void add(BufferedImage[] images);

    public double compareTo(BufferedImage img);

    public double compareTo(Patternable p);

    public double distance(BufferedImage p);

}
