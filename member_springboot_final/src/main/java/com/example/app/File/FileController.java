package com.example.app.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/file")
@CrossOrigin(origins = "http://localhost:8081")
public class FileController {

    //上傳檔案
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        File uploadDir = new File("uploads");

        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // 自動建立資料夾
        }

        try {
            String fileName = file.getOriginalFilename();

            if (fileName == null || fileName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("檔案名稱無效");
            }

            Path path = Paths.get(uploadDir.getAbsolutePath(), fileName);
            file.transferTo(path.toFile());

            System.out.println("成功儲存檔案：" + path.toAbsolutePath()); //  列印儲存路徑

            return ResponseEntity.ok("上傳成功: " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("上傳失敗: " + e.getMessage());
        }
    }

    //下載檔案
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        File file = new File("uploads",fileName);

        if(!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Resource resource = new FileSystemResource(file);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");

        //顯示檔案內容，設置標頭以及屬性
        return ResponseEntity.ok().headers(httpHeaders).contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    // 列出 uploads 資料夾中的所有檔案名稱
    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles() {
        File folder = new File("uploads");

        if (!folder.exists() || !folder.isDirectory()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        // 僅列出檔案（排除資料夾）
        String[] fileNames = folder.list((dir, name) -> new File(dir, name).isFile());

        return ResponseEntity.ok(Arrays.asList(fileNames));
    }
}

