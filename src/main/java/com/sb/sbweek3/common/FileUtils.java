package com.sb.sbweek3.common;

import com.sb.sbweek3.dto.FileInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//TODO : 파일을 저장할 경로에 날짜를 넣어서 만들어줄 필요가 있을까?
@Component
public class FileUtils {

    @Value("${image.upload.dir}")
    private String uploadPath;

    /**
     * @param multipartFiles - 파일 객체 List
     * @return DB에 저장할 파일 정보 List
     */
    public List<FileInfoDTO> uploadFiles(final List<MultipartFile> multipartFiles) {

        List<FileInfoDTO> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                continue;
            }
            files.add(uploadFile(multipartFile));
        }
        return files;
    }

    /**
     * @param multipartFile - 파일 객체
     * @return DB에 저장할 파일 정보
     */
    public FileInfoDTO uploadFile(final MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            return null;
        }

        String savedName = genrateSaveFilename(multipartFile.getOriginalFilename()); //저장될 파일 이름
        String fileType = separateFileType(multipartFile.getOriginalFilename()); //저장될 파일 확장자

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")).toString();
        String uploadPath = getUploadPath(today) + File.separator + savedName;

        File uploadfile = new File(uploadPath);

        try {
            multipartFile.transferTo(uploadfile); //지정된 경로에 새로운 파일로 생성
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return FileInfoDTO.builder()
                .originalFilename(multipartFile.getOriginalFilename())
                .savedFilename(savedName)
                .fileSize(multipartFile.getSize())
                .filePath(uploadPath)
                .fileType(fileType)
                .build();

    }

    /**
     * @param originalFilename - 원본 파일명
     * @return dto에 전달할 파일명
     */

    private String genrateSaveFilename(final String originalFilename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String extension = StringUtils.getFilenameExtension(originalFilename);
        return uuid + "." + extension;
    }

    /**
     * @param originalFilename - 원본 파일명
     * @return dto에 저장할 파일 타입
     */
    private String separateFileType(final String originalFilename) {
        String extension = StringUtils.getFilenameExtension(originalFilename); // 파일 확장자를 추출하는 역할
        return extension;
    }

    private String getUploadPath() {
        return makeDirectories(uploadPath);
    }

    /**
     * 업로드 경로
     * @param today - 새로운 경로를 만드는 역할
     * @return 파일이 저장될 경로
     */
    private String getUploadPath(final String today) { // 날짜를 받아서, 기본경로에 날짜를 추가한 경로를 생성
        return makeDirectories(uploadPath + File.separator + today);
    }

    /**
     * 업로드 폴더 생성
     * @param uploadPath - 업로드 경로
     * @return 저장'된' 경로
     */
    private String makeDirectories(final String uploadPath) {
        File dir = new File(uploadPath);
        if (dir.exists() == false) {
            dir.mkdir();
        }
        return dir.getParent();
    }
}