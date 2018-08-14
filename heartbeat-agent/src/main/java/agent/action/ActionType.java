package agent.action;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */
public enum ActionType {
    UNKNOWN, START, STOP, RESTART;

    private static final Set<ActionType> TYPES = EnumSet.allOf(ActionType.class);

    public static ActionType getType(String name) {
        if (name != null && name.length() > 0) {
            for (ActionType type : TYPES) {
                if (type.name().equalsIgnoreCase(name)) {
                    return type;
                }
            }
        }

        return UNKNOWN;
    }
}
