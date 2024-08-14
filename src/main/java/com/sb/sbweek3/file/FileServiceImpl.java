package com.sb.sbweek3.file;

import com.sb.sbweek3.dto.FileInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl {

    private final FileMapper fileMapper;

    @Transactional
    public void saveFiles(final int postId, final List<FileInfoDTO> files) {
        if (CollectionUtils.isEmpty(files)) {
            return;
        }
        for (FileInfoDTO file : files) {
            file.setBoardId(postId);
        }
        fileMapper.saveAll(files);
    }
}
