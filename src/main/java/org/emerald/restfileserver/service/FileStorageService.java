package org.emerald.restfileserver.service;

import org.emerald.restfileserver.dto.FileDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileStorageService {
    FileDto save(MultipartFile file);
    Optional<Resource> read(String path);
    FileDto getByPath(String path);
}

