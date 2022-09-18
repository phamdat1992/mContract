package vn.amitgroup.digitalsignatureapi.utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.springframework.web.multipart.MultipartFile;

import vn.amitgroup.digitalsignatureapi.dto.FileInformation;
import vn.amitgroup.digitalsignatureapi.dto.response.ERROR;
import vn.amitgroup.digitalsignatureapi.exception.ApiException;

public class UploadUtils {
    public static FileInformation upload(MultipartFile part,  String path) throws IOException {
        System.out.println("uploadRootPath=" + path);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
        String strDate = dateFormat.format(date);
        File uploadRootDir = new File(path);
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        String name=part.getOriginalFilename().replaceAll("\\s", "_");
        String fileName = strDate+part.getOriginalFilename().replaceAll("\\s", "_");
        File file= new File(path +"/"+ fileName);
        try(InputStream is = part.getInputStream()){
            try(OutputStream os = new FileOutputStream(file)){
                int len = 0;
                byte[] bytes = new byte[1024];
                while ( ( len = is.read(bytes, 0, 1024)) > 0) {
                    os.write(bytes, 0, len);
                }
            }
        }
        FileInformation fileInformation= new FileInformation();
        fileInformation.setName(name);
        fileInformation.setPath(fileName);
        return fileInformation;
    }
    public static ByteArrayOutputStream convertToPdf(MultipartFile multipartFile) throws IOException, ApiException { 
        if (multipartFile.getSize() == 0){
            throw new ApiException(ERROR.INVALIDDATA);
        }
        String[] nameParts = multipartFile.getOriginalFilename().split("\\.(?=[^\\.]+$)");
        String fileExt = nameParts[1];
        if(!(fileExt.equals("docx")||fileExt.equals("doc"))){
            throw new ApiException(ERROR.INVALIDDATA);
        }
            if(fileExt.equals("docx")){
                 return ConvertUtil.docxToPdf(multipartFile);
               
            }

            else{
                    return   ConvertUtil.docToPdf(multipartFile);

            }
        
       
    }
    public static String getSizeFile(long size){

		String sizeFile;

        DecimalFormat newFormat = new DecimalFormat("#.#");
		double sizeKb = Double.valueOf(newFormat.format(size/1024));;
		double sizeMb = Double.valueOf(newFormat.format(sizeKb/1024));;
		double sizeGb = Double.valueOf(newFormat.format(sizeMb/1024));;
 
		 if (sizeGb > 1){
            sizeFile =  sizeGb + " gb";
	        }else{
                if(sizeMb > 1){
                    sizeFile =  sizeMb+ " mb";
                }else{
                    sizeFile =  sizeKb + " kb";
                }
            }
            return	 sizeFile;    
    }
    public static long getInputLength(InputStream inputStream) {
        try {
            if (inputStream instanceof FilterInputStream) {
                FilterInputStream filtered = (FilterInputStream)inputStream;
                Field field = FilterInputStream.class.getDeclaredField("in");
                field.setAccessible(true);
                InputStream internal = (InputStream) field.get(filtered);
                return getInputLength(internal);
            } else if (inputStream instanceof ByteArrayInputStream) {
                ByteArrayInputStream wrapper = (ByteArrayInputStream)inputStream;
                Field field = ByteArrayInputStream.class.getDeclaredField("buf");
                field.setAccessible(true);
                byte[] buffer = (byte[])field.get(wrapper);
                return buffer.length;
            } else if (inputStream instanceof FileInputStream) {
                FileInputStream fileStream = (FileInputStream)inputStream;
                return fileStream.getChannel().size();
            }
        } catch (NoSuchFieldException | IllegalAccessException | IOException exception) {
            // Ignore all errors and just return -1.
        }
        return -1;
    }
}
