package com.tjmxxo.daily;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author: Tjm
 * Date: 2020-04-14
 * Time: 19:24
 */
@RestController
public class FileSaveController {

    /**
     * 接收上传文件
     *
     * @param uploadFile
     * @param names
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/upload")
    public String uploads(MultipartFile[] uploadFile, String names) throws IOException {
        FileOutputStream fos = null;
        for (MultipartFile file : uploadFile) {
            try {
                File fileMkdir = new File(DailyApplication.ARGLIST.get(0));
                if (!fileMkdir.exists()) {
                    fileMkdir.mkdir();
                }
                System.out.println(file.getSize());
                String pathName = DailyApplication.ARGLIST.get(0) + "\\";//想要存储文件的地址
                String pname = file.getOriginalFilename();//获取文件名（包括后缀）
                pathName += pname;
                fos = new FileOutputStream(pathName);
                fos.write(file.getBytes()); // 写入文件

            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            } finally {
                fos.close();
            }
        }
        return "1";
    }


    /**
     * 发送下载文件
     *
     * @param uploadFile
     * @param names
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/download")
    public ResponseEntity<FileSystemResource> download(String name) throws IOException {
        File file = new File("temp.zip");
        FileOutputStream fos1 = new FileOutputStream(file);
        ZipUtils.toZip(DailyApplication.ARGLIST.get(0), fos1, true);
        return export(file);
    }


    public ResponseEntity<FileSystemResource> export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + System.currentTimeMillis() + ".xls");
        headers.add("Pragma", "no-cache");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream")).body(new FileSystemResource(file));
    }

    /**
     * 删除文件
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/delete")
    public String delete(String name) {
        try {
            if (name != null) {
                for (String s : name.split(",")) {
                    File file = new File(DailyApplication.ARGLIST.get(0) + "\\" + s);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return "1";
    }

}
