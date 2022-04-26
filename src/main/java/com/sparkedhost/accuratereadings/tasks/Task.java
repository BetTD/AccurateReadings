package com.sparkedhost.accuratereadings.tasks;

import com.mattmalec.pterodactyl4j.PowerAction;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Builder
@Getter
public class Task {
    private String name;
    private TaskType type;
    private String threshold;
    private String payload;
    private PowerAction powerAction;

    Task(String name, TaskType type, String threshold, @Nullable String payload) {
        this.name = name;
        this.type = type;
        this.threshold = threshold;
        this.payload = payload;
    }

    Task(String name, TaskType type, String threshold, @Nullable PowerAction payload) {
        this.name = name;
        this.type = type;
        this.threshold = threshold;
        this.powerAction = payload;
    }
}
