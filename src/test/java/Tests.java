import ocrOnline.controllers.ScanController;
import org.junit.Test;

public class Tests {
    @Test
    public void test() {
        ScanController scanController = new ScanController();
        String res = scanController.recognize(ScanController.test).getContent();
        System.out.println("RES<" + res + ">");
        assert (res.trim().equalsIgnoreCase("tes"));
    }
}
