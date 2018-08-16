package agent.action;

import agent.util.StringUtil;
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
        if (StringUtil.isNotEmpty(name)) {
            for (ActionType type : TYPES) {
                if (type.name().equalsIgnoreCase(name)) {
                    return type;
                }
            }
        }

        return UNKNOWN;
    }
}
