/*
 * This example was written by Bruno Lowagie
 * in the context of the book: iText 7 layout objects
 */
package com.itextpdf.highlevel.chapter03;

import com.itextpdf.highlevel.util.CsvTo2DList;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author iText
 */
public class JekyllHydeTabsV1 {
    
    public static final String SRC = "src/main/resources/data/jekyll_hyde.csv";
    public static final String DEST = "results/chapter03/jekyll_hyde_tabs1.pdf";
       
    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new JekyllHydeTabsV1().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException {
        PdfDocument pdf = new PdfDocument(
            new PdfWriter(new FileOutputStream(dest)));
        Document document = new Document(pdf, PageSize.A4.rotate());
        
        List<List<String>> resultSet = CsvTo2DList.convert(SRC, "|");
        for (List<String> record : resultSet) {
            Paragraph p = new Paragraph();
            p.add(record.get(0).trim()).add(new Tab())
                .add(record.get(1).trim()).add(new Tab())
                .add(record.get(2).trim()).add(new Tab())
                .add(record.get(3).trim()).add(new Tab())
                .add(record.get(4).trim()).add(new Tab())
                .add(record.get(5).trim());
            document.add(p);
        }
        document.close();
    }
}