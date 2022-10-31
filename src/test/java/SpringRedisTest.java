import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringRedisTest {

    private RedisTemplate redisTemplate = new StringRedisTemplate();

    @Test
    public void test(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("spring_redis", "spring");
    }

}
