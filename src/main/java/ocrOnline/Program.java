package ocrOnline;

import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

class Program {
    File dir;
    StringBuffer sb;
    Tesseract t;
    TreeMap<String, String> names;

    void run(String dirStr) throws Exception {
        final OCR ocr = new OCR();
        dir = new File(dirStr);
        ocr.loadList();

        System.out.println("Reading files in " + dir + " ...");

        String[] list = dir.list();
        if (list == null) {
            System.err.println("Not a file");
            System.exit(-2);
        }

        for (final String name : list) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Path imgP = Paths.get(dir.getAbsolutePath(), name);

                        BufferedImage img = ImageIO.read(imgP.toFile());
                        String s = ocr.process(img, name, false);

                        if ("".equals(s)) {
                            s = ocr.process(img, name, true);

                            if (s != null) {
                                System.err.println("! " + name + "=>" + sb.toString().replaceAll("\n", " "));
                            }
                        }
                        if (!"".equals(s)) {
                            System.err.println(":)" + name + "=>" + s);
                        }else{
                            System.err.println(":(" + name + "=>" + s);
                        }
                    } catch (Exception e) {
                        System.err.println("! " + name + "=>" + e);
                    }
                }
            }).run(); // start();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Run with 1 argument that is a folder of images.");
            System.exit(-1);
        }
        new Program().run(args[0]);
    }
}
