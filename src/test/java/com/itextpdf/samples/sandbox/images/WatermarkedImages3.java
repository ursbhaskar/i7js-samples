/**
 * This code sample was written by Bruno Lowagie in answer to this question:
 * http://stackoverflow.com/questions/28515474/how-to-add-text-on-the-last-page-through-pdfcontentbyte
 */
package com.itextpdf.samples.sandbox.images;

import com.itextpdf.basics.geom.Rectangle;
import com.itextpdf.basics.image.ImageFactory;
import com.itextpdf.core.color.DeviceGray;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.core.pdf.xobject.PdfFormXObject;
import com.itextpdf.core.testutils.annotations.type.SampleTest;
import com.itextpdf.model.Canvas;
import com.itextpdf.model.Document;
import com.itextpdf.model.Property;
import com.itextpdf.model.element.Cell;
import com.itextpdf.model.element.Image;
import com.itextpdf.model.element.Paragraph;
import com.itextpdf.model.element.Table;
import com.itextpdf.samples.GenericTest;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class WatermarkedImages3 extends GenericTest {
    public static final String IMAGE1 = "./src/test/resources/sandbox/images/bruno.jpg";
    public static final String DEST = "./target/test/resources/sandbox/images/watermarked_images3.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new WatermarkedImages3().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);
        Table table = new Table(1).setWidthPercent(80);
        for (int i = 0; i < 30; i++) {
            table.addCell(new Cell().add(new Paragraph("rahlrokks doesn't listen to what people tell him")));
        }
        table.addCell(new Cell().add(getWatermarkedImage(doc, new Image(ImageFactory.getImage(IMAGE1)), "Bruno").setAutoScale(true)));
        doc.add(table);
        doc.showTextAligned("Bruno knows best", 260, 400, Property.TextAlignment.CENTER, 45f * (float)Math.PI / 180f);
        doc.close();
    }

    public Image getWatermarkedImage(Document doc, Image img, String watermark) {
        float width = img.getImageScaledWidth();
        float height = img.getImageScaledHeight();
        PdfFormXObject template = new PdfFormXObject(new Rectangle(width, height));
        new Canvas(template, doc.getPdfDocument()).
                add(img).
                setProperty(Property.FONT_COLOR, DeviceGray.WHITE).
                showTextAligned(watermark, width / 2, height / 2, Property.TextAlignment.CENTER, (float) Math.PI * 30f / 180f);
        return new Image(template);
    }
}
