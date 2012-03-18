import io.PatternImporterTest;
import junit.framework.Test;
import junit.framework.TestSuite;
import tokenizer.ImageNumberTokenizerTest;

public class TestAll {

    public static Test suite() {
	TestSuite suite = new TestSuite();

	suite.addTestSuite(PatternImporterTest.class);
	suite.addTestSuite(ImageNumberTokenizerTest.class);

	return suite;
    }

}
