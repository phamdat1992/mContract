package vn.amitgroup.digitalsignatureapi.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.lowagie.text.Paragraph;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import fr.opensagres.poi.xwpf.converter.core.XWPFConverterException;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;

public class ConvertUtil {
    public static String convertStringToNormalized(String str) {
        str = str.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
        str = str.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
        str = str.replaceAll("ì|í|ị|ỉ|ĩ", "i");
        str = str.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
        str = str.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
        str = str.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
        str = str.replaceAll("đ", "d");

        str = str.replaceAll("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A");
        str = str.replaceAll("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E");
        str = str.replaceAll("Ì|Í|Ị|Ỉ|Ĩ", "I");
        str = str.replaceAll("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O");
        str = str.replaceAll("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U");
        str = str.replaceAll("Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y");
        str = str.replaceAll("Đ", "D");
        return str;
    }

    public static ByteArrayOutputStream docxToPdf(MultipartFile file) throws IOException {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XWPFDocument document = new XWPFDocument(file.getInputStream());
            PdfOptions options = PdfOptions.create();
            PdfConverter.getInstance().convert(document, outputStream, options);
            return outputStream;
        } catch (XWPFConverterException | IOException e) {
            throw new ApiException(ERROR.CONVERTFAIL);
        }
    }

    public static ByteArrayOutputStream docToPdf(MultipartFile file) {
        try {
            POIFSFileSystem fs = new POIFSFileSystem(file.getInputStream());
            Document document = new Document();

            HWPFDocument doc = new HWPFDocument(fs);
            WordExtractor we = new WordExtractor(doc);

            ByteArrayOutputStream result = new ByteArrayOutputStream();

            PdfWriter writer = PdfWriter.getInstance(document, result);

            document.open();
            writer.setPageEmpty(true);
            document.newPage();
            writer.setPageEmpty(true);

            String[] paragraphs = we.getParagraphText();
            for (int i = 0; i < paragraphs.length; i++) {
                paragraphs[i] = paragraphs[i].replaceAll("\\cM?\r?\n", "");

                document.add(new Paragraph(paragraphs[i]));
            }

            document.close();
            return result;
            // return new ByteArrayInputStream(result.toByteArray());

        } catch (DocumentException | IOException e) {
            throw new ApiException(ERROR.CONVERTFAIL);
        }
    }

}
