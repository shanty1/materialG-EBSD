package per.sc.tool.util.web;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.io.*;
public class ResponseEntityUtil {

    public static final String separator = File.separator;

    /**
 	* 响应文件 ( 下载文件，返回文件时使用)
     * @param filePath 文件上级目录
     * @param fileName 文件名
     * @param newName  下载的展示文件名
     * @return 响应
     * @throws FileNotFoundException 
     * @throws UnsupportedEncodingException 
     */
    public static ResponseEntity<InputStreamResource> getResponseEntity(File file) throws FileNotFoundException, UnsupportedEncodingException {
        ResponseEntity<InputStreamResource> response = null;
        InputStream inputStream = new FileInputStream(file);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition",
                "attachment; filename="
                        + new String(file.getName().getBytes("UTF-8"), "iso8859-1"));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        response = ResponseEntity
        		.ok()
        		.headers(headers)
        		.contentLength(file.length()) /*必须要设置文件大小，否则响应后会重复请求抛异常*/
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(inputStream));
        return response;
    }
    public static ResponseEntity<String> getResponseEntity(String content) {
        ResponseEntity<String> response = null;
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "text/html;charset=UTF-8");
        response = ResponseEntity.ok()
        		.headers(headers)
        		.contentLength(content.getBytes().length) /*必须要设置文件大小，否则响应后会重复请求抛异常*/
        		.contentType(MediaType.TEXT_HTML)
        		.body(content);
        return response;
    }
}

