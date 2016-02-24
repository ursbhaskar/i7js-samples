/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/**
 * This example was written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/33726908/writing-arabic-in-pdf-using-itext
 */
package com.itextpdf.samples.sandbox.fonts;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Property;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.SampleTest;

import java.io.File;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Ignore
@Category(SampleTest.class)
public class ArabicExample extends GenericTest {
    public static final String DEST
            = "./target/test/resources/sandbox/fonts/arabic_example.pdf";
    public static final String FONT
            = "./src/test/resources/sandbox/fonts/NotoNaskhArabic-Regular.ttf";
    public static final String ARABIC
            = "\u0627\u0644\u0633\u0639\u0631 \u0627\u0644\u0627\u062c\u0645\u0627\u0644\u064a";
    public static final String ARABIC1
            = " \u0627\u0644\u0633\u0639\u0631 ";
    public static final String ARABIC2
            = " \u0627\u0644\u0627\u062c\u0645\u0627\u0644\u064a ";


    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ArabicExample().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        PdfFont f = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H, true);
        Paragraph p = new Paragraph("This is incorrect: ");
        p.add(new Text(ARABIC).setFont(f));
        p.add(new Text(": 50.00 USD"));
        doc.add(p);

        // TODO No way to set direction for thwe whole paragraph, not only certain texts
        // TODO Delete the next lines after sample revising
        p = new Paragraph("This is correct: ").setBaseDirection(Property.BaseDirection.LEFT_TO_RIGHT).setFontScript(Character.UnicodeScript.ARABIC);
        p.add(new Text(ARABIC1).setFont(f));
        p.add(new Text(ARABIC2).setFont(f));
        p.add(new Text(": 50.00"));
        doc.add(p);

        p = new Paragraph("This is correct: ").setBaseDirection(Property.BaseDirection.LEFT_TO_RIGHT).setFontScript(Character.UnicodeScript.ARABIC);
        p.add(new Text(ARABIC).setFont(f));
        p.add(new Text(": 50.00"));
        doc.add(p);


        doc.close();
    }
}