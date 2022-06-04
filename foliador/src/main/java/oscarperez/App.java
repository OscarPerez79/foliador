package oscarperez;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        System.out.println("\n\t\t  FOLIADOR v1.0\n\t\t=================\n\n");
        if (args==null || args.length!=2)
        {
            System.out.println("\n\nError en la crida.\nHa de ser:\n\n");
            System.out.println("\tjava -jar foliador.jar ARXIU_PDF NUMERO_DE_FOLI_INICIAL");
            System.out.println("\n\n");
            System.exit(0);
        }
        File file = File.createTempFile("foliador",".pdf");
        file.getParentFile().mkdirs();

        new App().manipulatePdf(args[0],file.getAbsolutePath(), Integer.parseInt(args[1]));
        App.copia(file, args[0]);
        file.delete();
        System.out.println("\n\nAcabat\n\n");
    }
    
    private static void copia(File origen, String pathDesti) throws Exception
    {
        try(FileInputStream fis = new FileInputStream(origen))
        {
            try(FileOutputStream fos = new FileOutputStream(pathDesti))
            {
                int quants;
                byte[] buff=new byte[2048];
                while ((quants=fis.read(buff))>0)
                {
                    fos.write(buff,0,quants);
                }
            }
        }
    }

    protected void manipulatePdf(String origin, String dest, int primeraPagina) throws Exception {
        //Document document = new Document();
        PdfReader reader = new PdfReader(origin);
        int numberOfPages = reader.getNumberOfPages();
        System.out.println("Número de pàgines: "+numberOfPages);
        BaseFont bf = BaseFont.createFont();
  
        PdfStamper pdfStamper = new PdfStamper(reader, new FileOutputStream(dest));
        for(int i=1; i<= numberOfPages; i+=2){

//          System.out.println("Pàgina: "+i);
          Rectangle rect = reader.getPageSize(i);
          String text = "Foli "+(((i+1)/2+primeraPagina-1));
          PdfContentByte content = pdfStamper.getOverContent(i);
          int textHeightInGlyphSpace = bf.getAscent(text) - bf.getDescent(text);
          float fontSize = 10.0f;//1000f * rect.getHeight() / textHeightInGlyphSpace;
//          System.out.println("fontSize="+fontSize);
          Phrase phrase = new Phrase(text, new Font(bf, fontSize));
//          System.out.println("Bottom: "+rect.getTop()+"  right: "+rect.getRight());
          ColumnText.showTextAligned(content, Element.ALIGN_CENTER, phrase,
                    (rect.getRight()-30),
                    rect.getBottom() + 20,
                    0);
      }

      pdfStamper.close();
        
        reader.close();
/*        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        
        // the direct content
        PdfContentByte cb = writer.getDirectContent();
        // the rectangle and the text we want to fit in the rectangle
        Rectangle rect = new Rectangle(100, 150, 220, 200);
        String text = "test";
        // try to get max font size that fit in rectangle
        BaseFont bf = BaseFont.createFont();
        
        for (int i = 1; i <= numberOfPages; i++) {
            System.out.println("Pàgina: "+i);
if (true)            break;
            int textHeightInGlyphSpace = bf.getAscent(text) - bf.getDescent(text);
            float fontSize = 1000f * rect.getHeight() / textHeightInGlyphSpace;
            Phrase phrase = new Phrase("Foli "+i, new Font(bf, fontSize));
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER, phrase,
                    // center horizontally
                    (rect.getLeft() + rect.getRight()) / 2,
                    // shift baseline based on descent
                    rect.getBottom() - bf.getDescentPoint(text, fontSize),
                    i);
        }


        // draw the rect
        cb.saveState();
        cb.setColorStroke(BaseColor.BLUE);
        cb.rectangle(rect.getLeft(), rect.getBottom(), rect.getWidth(), rect.getHeight());
        cb.stroke();
        cb.restoreState();
        
        document.close();
        */
    }
}
