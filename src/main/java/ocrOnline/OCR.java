package ocrOnline;

import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

class OCR {
    File dir;
    StringBuffer sb;
    Tesseract t;
    TreeMap<String, String> names;

    public void loadList() throws Exception {
        System.out.print("Loading...");
        // System.out.println(sb);
        System.out.print("Building index...");
        names = new TreeMap<>();
        for (Map.Entry<String, String> e : Arrays.<Map.Entry<String, String>>asList()) {
            names.put(clean(e.getKey()), e.getValue());
        }
        System.out.println(names.size() + " entries.");

        t = new Tesseract();
        t.setLanguage("deu");
    }

    public void loadOCR() throws Exception {
        t = new Tesseract();
        t.setLanguage("deu");
    }

    String clean(String textContent) {
        return textContent
                .toLowerCase()
                .replaceAll("f", "t")
                .replaceAll("/", "t")
                .replaceAll("w", "v")
                .replaceAll("c", "e")
                .replaceAll("l", "t")
                .replaceAll("i", "t")
                .replaceAll("j", "t");
    }

   String process(BufferedImage img, String name, boolean withTres) throws Exception {

        // BufferedImage img = ImageIO.read(imgP.toFile());
        if (withTres) {
            img = treshold(img, name);
        }

        String res = "";
        try {
            res = clean(t.doOCR(img));
        } catch (Throwable e) {
            System.err.println("! " + name + "=>" + e.getMessage());
        }
        return res;

    }

    BufferedImage treshold(BufferedImage image, String name) throws Exception {
        int arrayWidth = image.getWidth(null);
        int arrayHeight = image.getHeight(null);
        int[] pixels = new int[arrayHeight * arrayWidth];
        PixelGrabber grabber = new PixelGrabber(image, 0, 0, arrayWidth, arrayHeight, pixels, 0, arrayWidth);

        grabber.grabPixels();

        int s = 10;
        for (int j = 0; j < pixels.length; j++) {
            int pixel = pixels[j];
            pixel = Math.abs(pixel) > s * 1000000 ? 0xFF000000 : 0xFFFFFFFF;
            pixels[j] = pixel;
        }

        image.setRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
        // ImageIO.write(image, "PNG", Paths.get(name + "out.png").toFile());
        return image;
    }
}
