package com.sparkedhost.accuratereadings.tasks;

import com.mattmalec.pterodactyl4j.PowerAction;
import com.sparkedhost.accuratereadings.exceptions.InvalidPowerActionException;
import com.sparkedhost.accuratereadings.exceptions.InvalidTaskTypeException;

public class TaskValidator {
    public static TaskType validateTaskType(String s) throws InvalidTaskTypeException {
        TaskType result;

        try {
            result = TaskType.valueOf(s);
        } catch (IllegalArgumentException e) {
            throw new InvalidTaskTypeException();
        }

        return result;
    }

    public static PowerAction validatePowerAction(String s) throws InvalidPowerActionException {
        PowerAction result;

        try {
            result = PowerAction.valueOf(s);
        } catch (IllegalArgumentException e) {
            throw new InvalidPowerActionException();
        }

        return result;
    }
}
