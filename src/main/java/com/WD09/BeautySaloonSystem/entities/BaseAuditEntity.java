package com.WD09.BeautySaloonSystem.entities;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseAuditEntity {
    private LocalDateTime createdAt = LocalDateTime.now();

    public LocalDateTime getCreatedAt() { return createdAt; }

}