import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class JedisTest {

    public void test(){
        Jedis jedis = new Jedis("192.168.106.133", 6379);

        jedis.auth("root");

        jedis.set("test", "test");

        Map<String, String> map = new HashMap<>();
        map.put("map1", "111");
        jedis.hmset("map", map);

        System.out.println(jedis.keys("*"));

        jedis.close();
    }

}
