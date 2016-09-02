package JavaOCR;

import com.sun.media.jai.widget.DisplayJAI;

import javax.imageio.ImageIO;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;

/**
 * Created by swaggerton.
 */
public class EdgeDetector extends JFrame {

    private final int BASE_SIZE = 300;

    public EdgeDetector(File fileIn) throws IOException {
        super("Naive Similarity Finder");
        //create the detector
        CannyEdgeDetector detector = new CannyEdgeDetector();

//adjust its parameters as desired
        detector.setLowThreshold(0.5f);
        detector.setHighThreshold(1f);

        // turn image to buffered image
        Image img = ImageIO.read(fileIn);
        BufferedImage inputBufferedImage = toBufferedImage(img);
//apply it to an image
        detector.setSourceImage(inputBufferedImage);
        detector.process();
        BufferedImage edges = detector.getEdgesImage();


        File outputfile = new File("image.jpg");
        ImageIO.write(edges, "jpg", outputfile);



        // create GUI
        // Create the GUI

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());

        // Turn buffered image into reference

        // Put the reference, scaled, in the left part of the UI.
        RenderedImage input = rescale(ImageIO.read(fileIn));
        cp.add(new DisplayJAI(input), BorderLayout.WEST);
        // Calculate the signature vector for the reference.
        // Now we need a component to store X images in a stack, where X is the
        // number of images in the same directory as the original one.
        JPanel otherPanel = new JPanel(new GridLayout(15, 2));
        cp.add(new JScrollPane(otherPanel), BorderLayout.CENTER);
        // For each image, calculate its signature and its distance from the
        // reference signature.
        RenderedImage output = rescale(ImageIO.read(outputfile));
        otherPanel.add(new DisplayJAI(output));
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    public static void main(String[] args) throws IOException {
        JFileChooser fc = new JFileChooser("/home/swaggerton/Pictures/Image_library");
        fc.setFileFilter(new JPEGImageFileFilter());
        int res = fc.showOpenDialog(null);
        // We have an image!
        if (res == JFileChooser.APPROVE_OPTION)
        {
            File file = fc.getSelectedFile();
            new EdgeDetector(file);
        }
        // Oops!
        else
        {
            JOptionPane.showMessageDialog(null,
                    "You must select one image to be the reference.", "Aborting...",
                    JOptionPane.WARNING_MESSAGE);
        }
    }



    private RenderedImage rescale(RenderedImage i) {
        float scaleW = ((float) BASE_SIZE) / i.getWidth();
        float scaleH = ((float) BASE_SIZE) / i.getHeight();
        // Scales the original image
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(i);
        pb.add(scaleW);

        pb.add(scaleH);
        pb.add(0.0F);
        pb.add(0.0F);
        pb.add(new InterpolationNearest());
        // Creates a new, scaled image and uses it on the DisplayJAI component
        return JAI.create("scale", pb);
    }

}
