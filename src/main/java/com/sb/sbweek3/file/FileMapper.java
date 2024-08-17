package com.sb.sbweek3.file;

import com.sb.sbweek3.dto.FileInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {

    void saveAll(List<FileInfoDTO> files);

    List<FileInfoDTO> findFileIdByBoardId(int boardId);

    /**
     * 파일 리스트 조회
     * @param boardId - 게시글 번호 (FK)
     * @return 파일 리스트
     */
    List<FileInfoDTO> findAllByBoardId(int boardId);

    /**
     * 파일 리스트 조회
     * @param fileId - PK 리스트
     * @return 파일 리스트
     */
    //todo : 제네릭타입에 원시타입을 사용할 수 없어서 mapper타입으로 사용했는데 통일성을 위해서 모두 integer로 바꿔줘야 하나?
    List<FileInfoDTO> findAllByIFileIds(List<FileInfoDTO> fileId);

    /**
     * 파일 삭제
     * @param fileId - PK 리스트
     */
    void deleteAllByFileIds(List<Integer> fileId);
}
