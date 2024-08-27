package com.sb.sbweek3.file;

import com.sb.sbweek3.dto.FileInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {

    void saveFiles(List<FileInfoDTO> files);

    List<FileInfoDTO> findFileIdByBoardId(int boardId);

    /**
     * 파일 리스트 조회
     * @param boardId - 게시글 번호 (FK)
     * @return 파일 리스트
     */
    List<FileInfoDTO> findAllByBoardId(int boardId);

    /**
     * 파일 리스트 조회
     * @param fileIds - PK 리스트
     * @return 파일 리스트
     */
    //todo : 제네릭타입에 원시타입을 사용할 수 없어서 mapper타입으로 사용했는데 통일성을 위해서 모두 integer로 바꿔줘야 하나?
    List<FileInfoDTO> findAllByIFileIds(List<FileInfoDTO> fileIds);

    List<FileInfoDTO> findAllFileByIds(List<Integer> deleteFileIds);

    void deleteAllByFileIds(List<Integer> deleteFileIds);

    FileInfoDTO findFileByFileId(int fileId);
}
