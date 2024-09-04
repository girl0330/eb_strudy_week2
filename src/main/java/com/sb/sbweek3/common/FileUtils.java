package com.sb.sbweek3.common;

import com.sb.sbweek3.dto.FileInfoDTO;
import com.sb.sbweek3.exception.CustomException;
import com.sb.sbweek3.exception.ExceptionErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//TODO : 파일을 저장할 경로에 날짜를 넣어서 만들어줄 필요가 있을까?
@Component
public class FileUtils {

    @Value("${image.upload.dir}")
    private String path;

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

        String savedName = generateSaveFilename(multipartFile.getOriginalFilename()); //저장될 파일 이름(uuid + 현재 날짜 + 확장자)
        String fileType = separateFileType(multipartFile.getOriginalFilename()); //확장자
        String uploadPath = path;

        File uploadDirFile = new File(uploadPath);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs(); // 디렉토리 생성
        }

        try {
            File targetFile = new File(uploadDirFile, savedName);
            multipartFile.transferTo(targetFile); // 파일 저장
        } catch (IOException e) {
            throw new CustomException(ExceptionErrorCode.GENERIC_ERROR, "파일 저장 중 오류가 발생했습니다.");
        }

        return FileInfoDTO.builder()
                .originalFilename(multipartFile.getOriginalFilename())
                .savedFilename(savedName)
                .fileSize(multipartFile.getSize())
                .filePath(path)
                .fileType(fileType)
                .build();

    }

    /** 저장될 파일 이름 생성
     * @param originalFilename - 원본 파일명
     * @return dto에 전달할 파일명
     */
    private String generateSaveFilename(final String originalFilename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")).toString(); //업로드 경로에 현재 날짜 추출
        String extension = StringUtils.getFilenameExtension(originalFilename);
        return uuid + today + "." + extension;
    }

    /** 확장자 추출
     * @param originalFilename - 원본 파일명
     * @return dto에 저장할 파일 타입
     */
    private String separateFileType(final String originalFilename) {
        return StringUtils.getFilenameExtension(originalFilename); // 파일 확장자를 추출하는 역할
    }

    /**
     * 파일 삭제 (from Disk)
     * @param files - 삭제할 파일 FileInfoDTO 리스트
     */
    public void deleteFiles(final List<FileInfoDTO> files) throws IOException {
        if (CollectionUtils.isEmpty(files)) {
            return;
        }
        for (FileInfoDTO file : files) {
            deleteFile(file.getSavedFilename());
        }
    }

    /**
     * 파일 삭제 (from Disk)
     * @param savedFileName : 삭제할 파일 이름
     */

    private void deleteFile(final String savedFileName) throws IOException {
        File file = new File(path + File.separator + savedFileName);
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("파일 삭제 실패: " + savedFileName);
            }
        } else {
            throw new FileNotFoundException("파일이 존재하지 않습니다: " + savedFileName);
        }
    }

    /**
     * 파일 다운로드 부분
     */
    public Resource readFileAsResource(FileInfoDTO file) {
        String fileName = file.getSavedFilename();
        Path filePath = Paths.get(path, fileName);
        try {

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() == false || resource.isFile() == false) {
                throw new RuntimeException("file not found : "+filePath.toString());
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new RuntimeException("file not found : "+ filePath.toString());
        }
    }

}