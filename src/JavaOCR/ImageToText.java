package JavaOCR;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.io.File;

/**
 * Created by swaggerton.
 */
public class ImageToText {
    public static void main(String[] args) throws TesseractException {
        long startTime = System.currentTimeMillis();
        File imageFile = new File("C:\\Users\\corpi\\Pictures\\aaaaa.PNG");
        String result = getText(imageFile);

        System.out.println(result);
        long endTime   = System.currentTimeMillis();
        double totalTime = (endTime - startTime)/1000.0;
        System.out.println("Time elapsed: " + totalTime);

    }
    public static String getText(File imageFile) throws TesseractException {

        System.out.println("File exists: "+imageFile.exists());
        Tesseract tessInstance = Tesseract.getInstance();
        tessInstance.setDatapath(System.getenv("TESSDATA_PREFIX"));
        //ImageIO.scanForPlugins(); // make sure it knows about GhostScript, to work with PDFs
        String result = tessInstance.doOCR(imageFile);
        return result;
    }
}
