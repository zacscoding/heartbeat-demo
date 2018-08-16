package agent.util;

/**
 * @author zacconding
 * @Date 2018-08-16
 * @GitHub : https://github.com/zacscoding
 */
public class StringUtil {

    public static boolean isEmpty(String value) {
        return (value == null) || (value.length() == 0);
    }

    public static boolean isNotEmpty(String value) {
        return (value != null) && (value.length() > 0);
    }
}
