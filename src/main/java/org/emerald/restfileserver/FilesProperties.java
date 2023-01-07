package org.emerald.restfileserver;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "files.root")
public class FilesProperties {
    private String location;

    @PostConstruct
    protected void init() {
        if (location == null) {
            location = System.getProperty("user.dir");
        }
    }
}
