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
    private Object payload;

    Task(String name, TaskType type, String threshold, @Nullable Object payload) {
        this.name = name;
        this.type = type;
        this.threshold = threshold;
        this.payload = payload;
    }
}
