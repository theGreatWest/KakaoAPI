package kr.book.search.util.pdf;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import kr.book.search.model.Book;

import java.io.*;
import java.util.*;
import java.net.MalformedURLException;
public class PdfGenerator {
    public static void generateBookListPdf(List<Book> books, String fileName) {
        try{
            PdfWriter writer = new PdfWriter("src/main/resources/pdf/"+fileName+".pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.setFontSize(12);

            // 폰트 생성 및 추가
            PdfFont font=null;
            try {
                font = PdfFontFactory.createFont("src/main/resources/font/NanumPenScript-Regular.ttf", PdfEncodings.IDENTITY_H, true);
            } catch (IOException e1) {}

            document.setFont(font);
            // 타이틀 추가
            Paragraph titleParagraph = new Paragraph("도서 목록");
            titleParagraph.setFontSize(24);
            titleParagraph.setTextAlignment(TextAlignment.CENTER);
            titleParagraph.setBold();
            document.add(titleParagraph);

            // 도서 정보 테이블 생성
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setMarginTop(20);

            // 테이블 헤더 추가
            table.addHeaderCell(createCell("제목", true));
            table.addHeaderCell(createCell("저자", true));
            table.addHeaderCell(createCell("출판사", true));
            table.addHeaderCell(createCell("이미지", true));

            // 도서 정보를 테이블에 추가
            for (Book book : books) {
                table.addCell(createCell(book.getTitle(), false));
                table.addCell(createCell(book.getAuthors(), false));
                table.addCell(createCell(book.getPublisher(), false));

                // 이미지 추가
                try {
                    ImageData imageData = ImageDataFactory.create(book.getThumbnail());
                    Image image = new Image(imageData);
                    image.setAutoScale(true);
                    table.addCell(new Cell().add(image).setPadding(5));
                } catch (MalformedURLException e) {
                    table.addCell(createCell("이미지 불러오기 실패", false));
                }
            }

            document.add(table);
            document.close();
        }catch (FileNotFoundException e){}
        catch (IOException e){}
    }

    private static Cell createCell(String content, boolean isHeader) {
        Paragraph paragraph = new Paragraph(content);
        Cell cell = new Cell().add(paragraph);
        cell.setPadding(5);
        if (isHeader) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
            cell.setFontSize(14);
            cell.setBold();
        }
        return cell;
    }
}
