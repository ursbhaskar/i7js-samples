/*

    This file is part of the iText (R) project.
    Copyright (c) 1998-2016 iText Group NV

*/

/*
* This code sample was written in the context of the tutorial:
* ZUGFeRD: The future of Invoicing
*/
package com.itextpdf.samples.sandbox.zugferd.chapter02;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfVersion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.samples.GenericTest;
import com.itextpdf.test.annotations.type.IntegrationTest;
import org.junit.experimental.categories.Category;

import java.io.IOException;

/**
* Creates a simple PDF with images and text.
*/
@Category(IntegrationTest.class)
public class C2E1_SimplePdf extends GenericTest {
    public static final String FOX = "./src/test/resources/img/fox.bmp";
    public static final String DOG = "./src/test/resources/img/dog.bmp";

    public static final String DEST = "./target/test/resources/zugferd/chapter02/C2E1_SimplePdf.pdf";

    /**
     * Creates a simple PDF with images and text
     * @throws java.io.IOException
     */
    @Override
    protected void manipulatePdf(String dest ) throws IOException, InterruptedException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest), PdfVersion.PDF_1_7);
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());
        Paragraph p = new Paragraph();
        p.setFont(PdfFontFactory.createFont(FontConstants.HELVETICA)).setFontSize(20);
        Text text = new Text("The quick brown ");
        p.add(text);
        Image image = new Image(ImageFactory.getImage(FOX));
        p.add(image);

        text = new Text(" jumps over the lazy ");
        p.add(text);
        image = new Image(ImageFactory.getImage(DOG));
        p.add(image);
        doc.add(p);
        doc.close();
    }
}