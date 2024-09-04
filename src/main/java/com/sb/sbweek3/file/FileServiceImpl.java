package com.sb.sbweek3.file;

import com.sb.sbweek3.common.FileUtils;
import com.sb.sbweek3.dto.FileInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FileServiceImpl {

    private final FileMapper fileMapper;
    private final FileUtils fileUtils;

    /**
     * 파일 저장
     * @param boardId - 파일 저장할 게시글 번호 pk
     * @param multiFiles - 파일
     */

    public void saveFiles(int boardId, List<MultipartFile> multiFiles) {
        List<FileInfoDTO> files = fileUtils.uploadFiles(multiFiles);

        if (CollectionUtils.isEmpty(files)) {
            return;
        }
        for (FileInfoDTO file : files) {
            file.setBoardId(boardId);
        }

        fileMapper.saveFiles(files);
    }

    public List<FileInfoDTO> findFileIdByBoardId(int boardId) {
        List<FileInfoDTO> fileIds = fileMapper.findFileIdByBoardId(boardId);
        System.out.println("filesIds 길이는? "+fileIds.size());
        if (fileIds.size() >= 2) { //다중 파일
            if (CollectionUtils.isEmpty(fileIds)) {
                return Collections.emptyList();
            }
            return fileMapper.findAllByIFileIds(fileIds);
        } else {
            return fileMapper.findAllByBoardId(boardId);
        }
    }

    /**
     *
     * @param deleteFileIds : 삭제 파일 pk 리스트
     * @return : 삭제할 파일 FileInfoDTO 리스트
     */
    public List<FileInfoDTO> findFileByFileIds(List<Integer> deleteFileIds) {
        return fileMapper.findFileByFileIds(deleteFileIds);
    }


    /**
     * 파일 삭제 (from Database)
     * @param deleteFileIds - fileInfo의 pk
     */
    public void deleteFileByFileIds(List<Integer> deleteFileIds) {
        if (CollectionUtils.isEmpty(deleteFileIds)) {
            return;
        }
        fileMapper.deleteFileByFileIds(deleteFileIds);
    }

    public FileInfoDTO findFileByFileId(int fileId) {
        return fileMapper.findFileByFileId(fileId);
    }
}
