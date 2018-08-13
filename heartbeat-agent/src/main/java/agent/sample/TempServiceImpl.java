package agent.sample;

import agent.sample.TempService;
import java.util.UUID;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */
public class TempServiceImpl implements TempService {

    public TempServiceImpl() {
        System.out.println("## TempServiceImpl() is called...");
    }

    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
