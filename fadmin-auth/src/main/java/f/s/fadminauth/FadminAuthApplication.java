package f.s.fadminauth;

import f.s.jcache.Cache;
import f.s.jcache.CacheRedis;
import f.s.utils.spring.ErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * Created by Ace on 2017/6/2.
 */

@EnableFeignClients
@SpringBootApplication
@ComponentScan("f.s")
public class FadminAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(FadminAuthApplication.class, args);
    }
    @ControllerAdvice
    public static class myErrorHandler extends ErrorHandler {
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**");

            }
        };
    }
    /**
     * 注入redis连接工厂
     * @param connectionFactory
     * @return
     */

    @Bean
    public Cache getRedisCache(RedisConnectionFactory connectionFactory){
        return new CacheRedis(connectionFactory);
    }

}
