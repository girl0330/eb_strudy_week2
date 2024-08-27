package com.sb.sbweek3.file;

import com.sb.sbweek3.common.FileUtils;
import com.sb.sbweek3.dto.FileInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FileController {

        private final FileServiceImpl fileServiceImpl;
        private final FileUtils fileUtils;
    @ResponseBody
    @GetMapping("/ajax/{boardId}/filesDetail")
    public ResponseEntity<List<FileInfoDTO>> findFileIdByBoardId(@PathVariable int boardId) {
        System.out.println("파일 조회");
        List<FileInfoDTO> files = fileServiceImpl.findFileIdByBoardId(boardId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/ajax/files/{fileId}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable int fileId) {
        System.out.println("다운로드");
        FileInfoDTO file = fileServiceImpl.findFileByFileId(fileId);
        Resource resource = fileUtils.readFileAsResource(file);
        try {
            String filename = URLEncoder.encode(file.getOriginalFilename(), "UTF-8");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + filename + "\";")
                    .header(HttpHeaders.CONTENT_LENGTH, file.getFileSize() + "")
                    .body(resource);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("filename encoding failed : " + file.getOriginalFilename());
        }
    }
}
