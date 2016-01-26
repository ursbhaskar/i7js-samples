package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.core.pdf.PdfArray;
import com.itextpdf.core.pdf.PdfDictionary;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfName;
import com.itextpdf.core.pdf.PdfReader;
import com.itextpdf.core.pdf.PdfStream;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.core.pdf.filespec.PdfFileSpec;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.core.xmp.impl.Utils;
import com.itextpdf.model.Document;
import com.itextpdf.model.element.List;
import com.itextpdf.model.element.ListItem;
import com.itextpdf.model.element.Paragraph;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Ignore
@Category(SampleTest.class)
public class Listing_16_06_KubrickDocumentary extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part4/chapter16/Listing_16_06_KubrickDocumentary.pdf";
    public static final String FILENAME = "Listing_16_06_KubrickDocumentary.pdf";
    public static final String PATH = "./target/test/resources/book/part4/chapter16/%s";
    public static final String RESULT = String.format(PATH, FILENAME);

    public static void main(String args[]) throws Exception {
        new Listing_16_06_KubrickDocumentary().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        FileOutputStream os = new FileOutputStream(RESULT);
        os.write(createPdf());
        os.flush();
        os.close();
        extractDocLevelAttachments(RESULT);
    }

    public byte[] createPdf() throws IOException, SQLException {
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        java.util.List<Movie> movies = PojoFactory.getMovies(connection, 1);
        connection.close();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph(
                "'Stanley Kubrick: A Life in Pictures' is a documentary about Stanley Kubrick and his films:"));

        ByteArrayOutputStream txt = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(txt);
        out.println("<movies>");
        List list = new List();
        list.setSymbolIndent(20);
        ListItem item;
        for (Movie movie : movies) {
            out.println("<movie>");
            out.println(String.format("<title>%s</title>", Utils.escapeXML(movie.getMovieTitle(), true,true)));
            out.println(String.format("<year>%s</year>", movie.getYear()));
            out.println(String.format("<duration>%s</duration>", movie.getDuration()));
            out.println("</movie>");
            item = new ListItem(movie.getMovieTitle());
            list.add(item);
        }
        doc.add(list);
        out.print("</movies>");
        out.flush();
        out.close();
        pdfDoc.addFileAttachment("kubrick", PdfFileSpec.createEmbeddedFileSpec(pdfDoc, txt.toByteArray(), null, "kubrick.xml", null, null, null, true));
        doc.close();
        return baos.toByteArray();
    }

    /**
     * Extracts document level attachments
     * @param src     a file from which document level attachments will be extracted
     * @throws IOException
     */
    public void extractDocLevelAttachments(String src) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        PdfDictionary root = pdfDoc.getCatalog().getPdfObject();
        PdfDictionary documentnames = root.getAsDictionary(PdfName.Names);
        PdfDictionary embeddedfiles = documentnames.getAsDictionary(PdfName.EmbeddedFiles);
        PdfArray filespecs = embeddedfiles.getAsArray(PdfName.Names);
        PdfDictionary filespec;
        PdfDictionary refs;
        FileOutputStream fos;
        PdfStream stream;
        for (int i = 0; i < filespecs.size(); ) {
            filespecs.getAsString(i++);
            filespec = filespecs.getAsDictionary(i++);
            refs = filespec.getAsDictionary(PdfName.EF);
            for (PdfName key : refs.keySet()) {
                fos = new FileOutputStream(String.format(PATH, filespec.getAsString(key).toString()));
                stream = refs.getAsStream(key);
                fos.write(stream.getBytes());
                fos.flush();
                fos.close();
            }
        }
        pdfDoc.close();
    }
}