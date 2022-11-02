import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class SpringRedisTest {

    private RedisTemplate redisTemplate = new StringRedisTemplate();

    public void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("spring_redis", "spring");
    }

}
