package com.itextpdf.samples.book.part2.chapter06;

import com.itextpdf.basics.font.FontConstants;
import com.itextpdf.canvas.PdfCanvas;
import com.itextpdf.core.font.PdfFont;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfReader;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.core.testutils.annotations.type.SampleTest;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.samples.book.part1.chapter01.Listing_01_05_HelloWorldLandscape1;
import com.itextpdf.samples.book.part1.chapter01.Listing_01_06_HelloWorldLandscape2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Ignore
@Category(SampleTest.class)
public class Listing_06_12_StampText extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part2/chapter06/Listing_06_12_StampText.pdf";

    public static final String DEST1 = "./target/test/resources/book/part2/chapter06/Listing_06_12_StampText_1.pdf";
    public static final String DEST2 = "./target/test/resources/book/part2/chapter06/Listing_06_12_StampText_2.pdf";
    public static final String DEST3 = "./target/test/resources/book/part2/chapter06/Listing_06_12_StampText_3.pdf";

    public String[] arguments;

    public static void main(String args[]) throws IOException {
        Listing_06_12_StampText stampText = new Listing_06_12_StampText();
        stampText.arguments = args;
        stampText.manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        Listing_01_05_HelloWorldLandscape1.main(arguments);
        Listing_01_06_HelloWorldLandscape2.main(arguments);
        stampPdf(Listing_01_05_HelloWorldLandscape1.DEST, DEST1);
        stampIgnoreRotation(Listing_01_05_HelloWorldLandscape1.DEST, DEST2);
        stampPdf(Listing_01_06_HelloWorldLandscape2.DEST, DEST3);
        // Create file to compare via CompareTool
        concatenateResults(DEST, new String[]{DEST1, DEST2, DEST3});
    }

    public void stampPdf(String source, String destination) throws IOException {
        //Initialize document
        FileInputStream fis = new FileInputStream(source);
        PdfReader reader = new PdfReader(fis);
        FileOutputStream fos = new FileOutputStream(destination);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(reader, writer);

        //Initialize canvas and write to it
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        canvas
                .saveState()
                .beginText()
                .setFontAndSize(PdfFont.createStandardFont(pdfDoc, FontConstants.HELVETICA), 12)
                .moveText(36, 540)
                .showText("Hello people!")
                .endText()
                .restoreState()
                .release();

        //Close document
        pdfDoc.close();
    }

    public void stampIgnoreRotation(String source, String destination) throws IOException {
        //Initialize document
        FileInputStream fis = new FileInputStream(source);
        PdfReader reader = new PdfReader(fis);
        FileOutputStream fos = new FileOutputStream(destination);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(reader, writer);

        //Initialize canvas and write to it
        // TODO No setRotateContents(boolean)
        PdfCanvas canvas = new PdfCanvas(pdfDoc.getFirstPage());
        canvas
                .saveState()
                .beginText()
                .setFontAndSize(PdfFont.createStandardFont(pdfDoc, FontConstants.HELVETICA), 12)
                .moveText(36, 540)
                .showText("Hello people!")
                .endText()
                .restoreState()
                .release();

        //Close document
        pdfDoc.close();
    }

    // Only for testing reasons
    protected void concatenateResults(String dest, String[] names) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PdfDocument tempDoc;
        for (String name : names) {
            tempDoc = new PdfDocument(new PdfReader(name));
            tempDoc.copyPages(1, tempDoc.getNumOfPages(), pdfDoc);
            tempDoc.close();
        }
        pdfDoc.close();
    }
}