package f.s.fadminperm;

import com.ace.cache.EnableAceCache;
import com.spring4all.swagger.EnableSwagger2Doc;
import f.s.utils.spring.ErrorHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.ControllerAdvice;
import tk.mybatis.spring.annotation.MapperScan;
@EnableCircuitBreaker
@EnableFeignClients
@EnableScheduling
@EnableAceCache()
@EnableTransactionManagement
@MapperScan("f.s.fadminperm.mapper")
@EnableSwagger2Doc
@SpringBootApplication
public class FadminPermApplication {

    public static void main(String[] args) {
        SpringApplication.run(FadminPermApplication.class, args);
    }

    @ControllerAdvice
    public static class myErrorHandler extends ErrorHandler {
    }
}

