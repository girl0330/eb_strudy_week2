package com.sb.sbweek3.file;

import com.sb.sbweek3.dto.FileInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {

    void saveAll(List<FileInfoDTO> files);
}
