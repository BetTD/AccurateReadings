package com.sparkedhost.accuratereadings.tasks;

import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Builder
@Getter
public class Task {
    private String name;
    private boolean active;
    private TaskType type;
    private String thresholdValue;
    private ResourceType thresholdType;
    private Object payload;

    Task(String name, boolean isActive, TaskType type, String thresholdValue, ResourceType thresholdType, @Nullable Object payload) {
        this.name = name;
        this.active = isActive;
        this.type = type;
        this.thresholdValue = thresholdValue;
        this.thresholdType = thresholdType;
        this.payload = payload;
    }
}
