package com.rcore.database.mongo.domain.picture.port.model;

import com.rcore.domain.picture.entity.PictureEntity;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("pictureEntity")
@SuperBuilder
public class PictureDoc extends PictureEntity {
    public PictureDoc toEntity() {
        return this;
    }

    public static PictureDoc toDoc(PictureEntity entity) {
        return PictureDoc.builder()
                .id(entity.getId())
                .fileName(entity.getFileName())
                .filePath(entity.getFilePath())
                .isPrivate(entity.getIsPrivate())
                .sizes(entity.getSizes())
                .createdAt(entity.getCreatedAt())
                .timeZone(entity.getTimeZone())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
