package com.sb.sbweek3.file;

import com.sb.sbweek3.dto.FileInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl {

    private final FileMapper fileMapper;

    @Transactional
    public void saveFiles(final int boardId, final List<FileInfoDTO> files) {
        System.out.println("boardId 확인 : "+boardId);
        if (CollectionUtils.isEmpty(files)) {
            return;
        }
        for (FileInfoDTO file : files) {
            file.setBoardId(boardId);
        }
        fileMapper.saveAll(files);
    }

    public List<FileInfoDTO> findFileIdByBoardId(int boardId) {
        List<FileInfoDTO> fileIds = fileMapper.findFileIdByBoardId(boardId);
        System.out.println("filesIds 길이는? "+fileIds.size());
        if (fileIds.size() > 1) { //다중 파일
            return findAllFileByFileIds(fileIds);
        } else {
            return findAllFileByBoardId(boardId);
        }
    }

    /**
     * 파일 리스트 조회
     * @param boardId - 게시글 번호 (FK)
     * @return 파일 리스트
     */
    public List<FileInfoDTO> findAllFileByBoardId(final int boardId) {
        System.out.println("단일 파일 확인 ::  "+boardId);
        return fileMapper.findAllByBoardId(boardId);
    }

    /**
     * 파일 리스트 조회
     * @param fileId - PK 리스트
     * @return 파일 리스트
     */
    public List<FileInfoDTO> findAllFileByFileIds(final List<FileInfoDTO> fileId) {
        System.out.println("다중파일 :: " + fileId);
        if (CollectionUtils.isEmpty(fileId)) {
            return Collections.emptyList();
        }
        return fileMapper.findAllByIFileIds(fileId);
    }

    /**
     * 파일 삭제 (from Database)
     * @param fileId - PK 리스트
     */
    @Transactional
    public void deleteAllFileByIds(final List<Integer> fileId) {
        if (CollectionUtils.isEmpty(fileId)) {
            return;
        }
        fileMapper.deleteAllByFileIds(fileId);
    }
}
