package com.itextpdf.samples.book.part4.chapter14;

import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfReader;
import com.itextpdf.core.testutils.annotations.type.SampleTest;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.samples.book.part1.chapter01.Listing_01_01_HelloWorld;
import com.itextpdf.samples.book.part1.chapter05.Listing_05_15_Hero1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Category(SampleTest.class) @Ignore
public class Listing_14_01_GetContentStream extends GenericTest {
    /** The content stream of a first PDF. */
    public static final String RESULT1
            = "./target/test/resources/book/part4/chapter14/Listing_14_01_GetContentStream1.txt";
    /** The content stream of a second PDF. */
    public static final String RESULT2
            = "./target/test/resources/book/part4/chapter14/Listing_14_01_GetContentStream2.txt";
    public static final String CMP_RESULT1
            = "./src/test/resources/book/part4/chapter14/cmp_Listing_14_01_GetContentStream1.txt";
    public static final String CMP_RESULT2
            = "./src/test/resources/book/part4/chapter14/cmp_Listing_14_01_GetContentStream2.txt";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_14_01_GetContentStream().manipulatePdf(RESULT1);
    }

    @Override
    protected void manipulatePdf(String dest) throws IOException, SQLException {
        new Listing_01_01_HelloWorld().manipulatePdf(Listing_01_01_HelloWorld.DEST);
        new Listing_05_15_Hero1().manipulatePdf(Listing_05_15_Hero1.DEST);
        readContent(Listing_01_01_HelloWorld.DEST, RESULT1);
        readContent(Listing_05_15_Hero1.DEST, RESULT2);
    }


    /**
     * Reads the content stream of the first page of a PDF into a text file.
     * @param src    the path to a PDF file
     * @param result the path to the resulting text file
     * @throws IOException
     */
    public void readContent(String src, String result) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        FileOutputStream out = new FileOutputStream(result);
        out.write(pdfDoc.getFirstPage().getContentBytes());
        out.flush();
        out.close();
        pdfDoc.close();
    }

    @Override
    protected void comparePdf(String dest, String cmp) throws Exception {
        //super.comparePdf(dest, cmp);
        // For the first content stream
        BufferedReader destReader = new BufferedReader(new InputStreamReader(new FileInputStream(dest)));
        BufferedReader cmpReader = new BufferedReader(new InputStreamReader(new FileInputStream(cmp)));
        String curDestStr;
        String curCmpStr;
        int row = 1;
        while ((curDestStr = destReader.readLine()) != null) {
            if ((curCmpStr = cmpReader.readLine()) != null) {
                addError("The lengths of files are different.");
            }
            if (!curCmpStr.equals(curDestStr)) {
                addError("The files are different on the row " + row );
            }
            row++;
        }
        if ((curCmpStr = cmpReader.readLine()) != null) {
            addError("The lengths of files are different.");
        }

        // For the first content stream
        destReader = new BufferedReader(new InputStreamReader(new FileInputStream(RESULT2)));
        cmpReader = new BufferedReader(new InputStreamReader(new FileInputStream(CMP_RESULT2)));
        row = 1;
        while ((curDestStr = destReader.readLine()) != null) {
            if ((curCmpStr = cmpReader.readLine()) != null) {
                addError("The lengths of files are different.");
            }
            if (!curCmpStr.equals(curDestStr)) {
                addError("The files are different on the row " + row );
            }
            row++;
        }
        if ((curCmpStr = cmpReader.readLine()) != null) {
            addError("The lengths of files are different.");
        }

    }

    @Override
    protected String getDest() {
        // dummy
        return RESULT1;
    }

    @Override
    protected String getCmpPdf() {
        // dummy
        return CMP_RESULT2;
    }
}