package server.agent;

import java.util.EnumSet;
import java.util.Set;
import org.springframework.util.StringUtils;

/**
 * @author zacconding
 * @Date 2018-08-17
 * @GitHub : https://github.com/zacscoding
 */
public enum ActionType {

    UNKNOWN, START, STOP, RESTART;

    private static final Set<ActionType> TYPES = EnumSet.allOf(ActionType.class);

    public static ActionType getType(String name) {
        if (StringUtils.hasText(name)) {
            for (ActionType type : TYPES) {
                if (type.name().equalsIgnoreCase(name)) {
                    return type;
                }
            }
        }

        return UNKNOWN;
    }
}