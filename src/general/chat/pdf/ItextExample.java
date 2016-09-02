package general.chat.pdf;

/**
 * Created by corpi on 2016-08-22.
 */
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Bruno Lowagie (iText Software)
 */
public class ItextExample {


    public static void main(String[] args) throws IOException, DocumentException {

    }

    public static String getTextFromPDF(String filename)
    {
        try {
            return new ItextExample().parse(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return new String();
        }
    }
    public String parse(String filename) throws IOException {
        PdfReader reader = new PdfReader(filename);
        StringBuilder pdfText = new StringBuilder();
        for (int page = 1; page <= 1; page++) {
            pdfText.append(PdfTextExtractor.getTextFromPage(reader, page).getBytes("UTF-8"));
        }
        return pdfText.toString();
    }
}