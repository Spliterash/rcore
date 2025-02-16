package com.rcore.database.mongo.commons.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Data
public class BaseDocument {
    protected ObjectId id = new ObjectId();
    protected Instant createdAt = Instant.now();
    protected Instant updatedAt = Instant.now();
}
