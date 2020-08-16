package vn.inspiron.mcontract.modules.FileManagement.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.inspiron.mcontract.modules.FileManagement.service.FileManageService;
import vn.inspiron.mcontract.modules.FileManagement.service.UrlService;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URL;

@RestController
public class PdfController {
    private final static String PDF_URL_1 = "https://www.w3docs.com/uploads/media/default/0001/01/540cb75550adf33f281f29132dddd14fded85bfc.pdf";
    private final static String PDF_URL_2 = "https://docs.spring.io/spring-boot/docs/current/reference/pdf/spring-boot-reference.pdf";

    @Autowired
    private UrlService urlService;

    @Autowired
    private FileManageService fileManageService;

    @GetMapping(value = "/generate_pdf_url")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getPdfFile(@RequestParam(value = "order", required=false)  String order) {
        String id = null;
        if (order == null) {
            id = "1";
        } else {
            id = order;
        }
        String url = "localhost:9293" + urlService.generateExpirationUrl(id, "/pdf/");

        return ResponseEntity.ok(url);
    }

    @GetMapping("/pdf/{encrypt}")
    public void getPdf(@PathVariable String encrypt, HttpServletResponse response) {
        String file = urlService.getDataFromUrl(encrypt);
        if (file == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        try {
            String pdfUrl = getPdfUrl(file);
            response.setContentType("application/pdf");
            URL url = new URL(pdfUrl);
            InputStream is = url.openStream();
            int nRead;
            while ((nRead = is.read()) != -1) {
                response.getWriter().write(nRead);
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @PostMapping("/upload_pdf")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("user-id") Long userId) {
        String uploadedPath = fileManageService.storeFile(file, userId);

        return ResponseEntity.ok(uploadedPath);
    }

    private String getPdfUrl(String id) {
        if (id.equalsIgnoreCase("1")) {
            return PDF_URL_1;
        }
        return PDF_URL_2;
    }
}
