package f.s.fadminauth.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 连接池配置
 */
@Configuration
public class JedisConfig extends CachingConfigurerSupport{

    private Logger log = LoggerFactory.getLogger(JedisConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    //TODO 配置待完善

    @Bean
    public JedisPool redisPoolFactory(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //最大连接数等,闲置链接数等
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, null);
        log.info("redis地址：{},端口：{}", host, port);
        return jedisPool;
    }
}
