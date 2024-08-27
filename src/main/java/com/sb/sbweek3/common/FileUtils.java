package com.sb.sbweek3.common;

import com.sb.sbweek3.dto.FileInfoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

        System.out.println("넘어온 파일들 확인 : "+multipartFiles);
        List<FileInfoDTO> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                continue;
            }
            files.add(uploadFile(multipartFile));
        }
        System.out.println("파일 확인 함;;;"+files);
        return files;
    }

    /**
     * @param multipartFile - 파일 객체
     * @return DB에 저장할 파일 정보
     */
    public FileInfoDTO uploadFile(final MultipartFile multipartFile) {

        System.out.println("업로드 하기 위한 과정까지 넘어왔어?");
        if (multipartFile.isEmpty()) {
            return null;
        }

        String savedName = generateSaveFilename(multipartFile.getOriginalFilename()); //저장될 파일 이름(uuid + 현재 날짜 + 확장자)
        String fileType = separateFileType(multipartFile.getOriginalFilename()); //확장자
        String uploadPath = path;

        try {
            File uploadDirFile = new File(uploadPath);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs(); // 디렉토리 생성
            }

            File targetFile = new File(uploadDirFile, savedName);
            multipartFile.transferTo(targetFile); // 파일 저장
        } catch (IOException e) {
            e.printStackTrace();
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
     * @param files - 삭제할 파일 정보 List
     */
    public void deleteFiles(final List<FileInfoDTO> files) {
        System.out.println("disk에서 파일 삭제 : "+files);
        if (CollectionUtils.isEmpty(files)) {
            return;
        }
        for (FileInfoDTO file : files) {
            System.out.println("삭제할 파일 이름 확인 : "+file.getSavedFilename());
            deleteFile(file.getSavedFilename());
        }
    }

    /**
     * 파일 삭제 (from Disk)
     * @param savedFileName - 저장된 이름
     */
    private void deleteFile(final String savedFileName) {
        File file = new File(path + File.separator + savedFileName);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("파일 삭제 성공: " + savedFileName);
            } else {
                System.out.println("파일 삭제 실패: " + savedFileName);
            }
        } else {
            System.out.println("파일이 존재하지 않습니다: " + savedFileName);
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