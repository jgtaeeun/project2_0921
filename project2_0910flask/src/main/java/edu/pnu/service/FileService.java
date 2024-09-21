package edu.pnu.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

@Service
public class FileService {

    private final String uploadDir = "C:/uploads/board/";

    public void saveFile(MultipartFile file) throws IOException {
        File destinationFile = new File(uploadDir + File.separator + file.getOriginalFilename());
        file.transferTo(destinationFile);
    }
}
