package me.bettd.pterolink.tasks;

import com.mattmalec.pterodactyl4j.PowerAction;
import me.bettd.pterolink.exceptions.InvalidPowerActionException;
import me.bettd.pterolink.exceptions.InvalidTaskTypeException;

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
