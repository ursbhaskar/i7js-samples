package com.itextpdf.samples.book.part1.chapter05;

import com.itextpdf.basics.font.FontConstants;
import com.itextpdf.basics.geom.PageSize;
import com.itextpdf.basics.geom.Rectangle;
import com.itextpdf.canvas.PdfCanvas;
import com.itextpdf.core.color.Color;
import com.itextpdf.core.color.DeviceRgb;
import com.itextpdf.core.font.PdfFont;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.model.Document;
import com.itextpdf.model.Property;
import com.itextpdf.model.element.AreaBreak;
import com.itextpdf.model.element.Cell;
import com.itextpdf.model.element.Table;
import com.itextpdf.model.renderer.CellRenderer;
import com.itextpdf.model.renderer.DrawContext;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;
import com.lowagie.filmfestival.Screening;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_05_03_RunLengthEvent extends GenericTest {
    public static final String DEST =
            "./target/test/resources/book/part1/chapter05/Listing_05_03_RunLengthEvent.pdf";

    public static void main(String args[]) throws IOException, SQLException {
        new Listing_05_03_RunLengthEvent().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, SQLException {
        // create the database connection
        DatabaseConnection connection = new HsqldbConnection("filmfestival");

        FileOutputStream fos = new FileOutputStream(dest);
        PdfWriter writer = new PdfWriter(fos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc, new PageSize(PageSize.A4).rotate());

        List<Date> days = PojoFactory.getDays(connection);
        int d = 1;
        for (Date day : days) {
            if (1 != d) {
                doc.add(new AreaBreak());
            }
            doc.add(getTable(connection, day));
            d++;
        }
        doc.close();
        connection.close();
    }

    public Table getTable(DatabaseConnection connection, Date day) throws UnsupportedEncodingException, SQLException {
        Table table = new Table(new float[]{2, 1, 2, 5, 1});
        table.setWidthPercent(100);
        // TODO No facility to set default cell properties
        // table.getDefaultCell().setPadding(3);
        // table.getDefaultCell().setUseAscender(true);
        // table.getDefaultCell().setUseDescender(true);
        // table.getDefaultCell().setColspan(5);
        // table.getDefaultCell().setBackgroundColor(BaseColor.RED);
        // table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addHeaderCell(new Cell(1, 5)
                .add(day.toString())
                .setPadding(3)
                .setBackgroundColor(Color.RED)
                .setTextAlignment(Property.TextAlignment.CENTER));
        // table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        // table.getDefaultCell().setColspan(1);
        // table.getDefaultCell().setBackgroundColor(BaseColor.YELLOW);
        table.addHeaderCell(new Cell()
                .add("Location")
                .setPadding(3)
                .setBackgroundColor(Color.YELLOW)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addHeaderCell(new Cell()
                .add("Time")
                .setPadding(3)
                .setBackgroundColor(Color.YELLOW)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addHeaderCell(new Cell()
                .add("Run Length")
                .setPadding(3)
                .setBackgroundColor(Color.YELLOW)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addHeaderCell(new Cell()
                .add("Title")
                .setPadding(3)
                .setBackgroundColor(Color.YELLOW)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addHeaderCell(new Cell()
                .add("Year")
                .setPadding(3)
                .setBackgroundColor(Color.YELLOW)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addFooterCell(new Cell()
                .add("Location")
                .setPadding(3)
                .setBackgroundColor(Color.YELLOW)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addFooterCell(new Cell()
                .add("Time")
                .setPadding(3)
                .setBackgroundColor(Color.YELLOW)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addFooterCell(new Cell()
                .add("Run Length")
                .setPadding(3)
                .setBackgroundColor(Color.YELLOW)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addFooterCell(new Cell()
                .add("Title")
                .setPadding(3)
                .setBackgroundColor(Color.YELLOW)
                .setTextAlignment(Property.TextAlignment.LEFT));
        table.addFooterCell(new Cell()
                .add("Year")
                .setPadding(3)
                .setBackgroundColor(Color.YELLOW)
                .setTextAlignment(Property.TextAlignment.LEFT));
        List<Screening> screenings = PojoFactory.getScreenings(connection, day);
        Movie movie;
        Cell runLength;
        for (Screening screening : screenings) {
            movie = screening.getMovie();
            table.addCell(screening.getLocation());
            table.addCell(String.format("%1$tH:%1$tM", screening.getTime()));
            runLength = new Cell();
            runLength.setNextRenderer(new FilmCellRenderer(runLength, movie.getDuration(), false));
            runLength.add(String.format("%d '", movie.getDuration()));
            if (screening.isPress()) {
                runLength.setNextRenderer(new FilmCellRenderer(runLength, movie.getDuration(), true));
            }
            table.addCell(runLength);
            table.addCell(movie.getMovieTitle());
            table.addCell(String.valueOf(movie.getYear()));
        }
        return table;
    }


    private class FilmCellRenderer extends CellRenderer {
        private int duration;
        private boolean isPressPreview;

        public FilmCellRenderer(Cell modelElement, int duration, boolean isPressPreview) {
            super(modelElement);
            this.duration = duration;
            this.isPressPreview = isPressPreview;
        }

        @Override
        public void drawBackground(DrawContext drawContext) {
            PdfCanvas canvas = drawContext.getCanvas();
            canvas.saveState();
            if (duration < 90) {
                canvas.setFillColor(new DeviceRgb(0x7C, 0xFC, 0x00));
            } else if (duration > 120) {
                canvas.setFillColor(new DeviceRgb(0x8B, 0x00, 0x00));
            } else {
                canvas.setFillColor(new DeviceRgb(0xFF, 0xA5, 0x00));
            }
            Rectangle rect = getOccupiedAreaBBox();
            canvas.rectangle(rect.getLeft(), rect.getBottom(),
                    rect.getWidth() * duration / 240, rect.getHeight());
            canvas.fill();
            canvas.restoreState();
            super.drawBackground(drawContext);
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            if (isPressPreview) {
                PdfCanvas canvas = drawContext.getCanvas();
                canvas.beginText();
                try {
                    canvas.setFontAndSize(PdfFont.createStandardFont(FontConstants.HELVETICA), 12);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // TODO No showTextAligned on canvas
                Rectangle rect = getOccupiedAreaBBox();
                canvas.moveText(rect.getLeft() + rect.getWidth() / 4, rect.getBottom() + 4.5f);
                canvas.showText("PRESS PREVIEW");
                canvas.endText();
            }
        }

        @Override
        public CellRenderer getNextRenderer() {
            return new FilmCellRenderer(getModelElement(), duration, isPressPreview);
        }
    }
}

