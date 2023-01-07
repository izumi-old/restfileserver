package org.emerald.restfileserver.mapper;

import org.emerald.restfileserver.dto.FileDto;
import org.emerald.restfileserver.entity.File;
import org.springframework.stereotype.Component;

@Component
public class FileMapper implements BiMapper<File, FileDto> {

    @Override
    public File mapToFirst(FileDto s) {
        return File.builder()
                .name(s.getName())
                .path(s.getPath())
                .contentType(s.getContentType())
                .build();
    }

    @Override
    public FileDto mapToSecond(File f) {
        return FileDto.builder()
                .name(f.getName())
                .path(f.getPath())
                .contentType(f.getContentType())
                .build();
    }
}
