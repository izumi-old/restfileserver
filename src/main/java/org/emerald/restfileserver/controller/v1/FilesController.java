package org.emerald.restfileserver.controller.v1;

import lombok.RequiredArgsConstructor;
import org.emerald.restfileserver.dto.FileDto;
import org.emerald.restfileserver.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class FilesController {
    private final FileStorageService storageService;

    @PostMapping("/upload_single")
    public ResponseEntity<FileDto> uploadSingle(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(storageService.save(file));
    }

    @PostMapping("/upload_multiple")
    public ResponseEntity<Collection<FileDto>> uploadMultiple(@RequestParam("files") MultipartFile[] files) {
        Collection<FileDto> dtos = Arrays.stream(files)
                .map(storageService::save)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }

    @GetMapping("/download/{path}")
    public ResponseEntity<Resource> download(@PathVariable String path) {
        final Optional<Resource> resourceOptional = storageService.read(path);
        if (resourceOptional.isEmpty()) {
            return ResponseEntity.internalServerError().body((Resource) null);
        }

        final Resource resource = resourceOptional.get();
        final ResponseEntity.BodyBuilder response = ResponseEntity.ok();
        response.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");

        final FileDto meta = storageService.getByPath(path);
        final String contentType = meta.getContentType();
        if (Objects.nonNull(contentType)) {
            response.contentType(MediaType.valueOf(contentType));
        }

        return response.body(resource);
    }

    @GetMapping("/meta/{path}")
    public ResponseEntity<FileDto> getMeta(@PathVariable String path) {
        return ResponseEntity.ok(storageService.getByPath(path));
    }
}
