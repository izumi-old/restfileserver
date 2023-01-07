package org.emerald.restfileserver.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.emerald.restfileserver.FilesProperties;
import org.emerald.restfileserver.Utils;
import org.emerald.restfileserver.dto.FileDto;
import org.emerald.restfileserver.entity.File;
import org.emerald.restfileserver.mapper.BiMapper;
import org.emerald.restfileserver.repository.FileRepository;
import org.emerald.restfileserver.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileSystemStorageService implements FileStorageService {
    private final BiMapper<File, FileDto> fileMapper;
    private final FilesProperties properties;
    private final FileRepository fileRepository;
    private String root;

    @PostConstruct
    protected void init() {
        root = properties.getLocation();
        mkdirs(root);
    }

    @Override
    public FileDto save(MultipartFile file) {
        final String fileName = file.getOriginalFilename();
        final String path = UUID.randomUUID() + "-" + fileName;

        final Path toSave = Paths.get(root + "/" + path);

        return Utils.silently(() -> {
            Files.copy(file.getInputStream(), toSave, StandardCopyOption.REPLACE_EXISTING);
            final File saved = fileRepository.save(File.builder()
                    .name(fileName)
                    .path(path)
                    .contentType(file.getContentType())
                    .build());
            return fileMapper.mapToSecond(saved);
        });
    }

    @Override
    public Optional<Resource> read(String path) {
        final Optional<File> fileOptional = fileRepository.findByPath(path);
        if (fileOptional.isEmpty()) {
            throw new RuntimeException(); //TODO: file not found
        }

        final File file = fileOptional.get();
        final Path toRead = Paths.get(root + "/" + file.getPath());
        final Resource resource = Utils.silently(() -> new UrlResource(toRead.toUri()));

        if (!resource.exists() || !resource.isReadable()) {
            return Optional.empty();
        }

        return Optional.of(resource);
    }

    @Override
    public FileDto getByPath(String path) {
        return fileRepository.findByPath(path)
                .map(fileMapper::mapToSecond)
                .orElseThrow(RuntimeException::new); //TODO: file not found
    }

    private void mkdirs(String path) {
        final String[] parts = path.split("/");
        final StringBuilder builder = new StringBuilder();

        for (String part : parts) {
            builder.append(part).append("/");
            final Path directory = Paths.get(builder.toString());
            if (Files.notExists(directory)) {
                Utils.silently(() -> Files.createDirectory(directory));
            }
        }
    }
}
