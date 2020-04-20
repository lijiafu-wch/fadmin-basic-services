package f.s.fadminperm.feign;

/**
 * @program: fadmin-basic-services
 * @description: 查询公匙key
 * @author: Mr.Huang
 * @create: 2018-12-19 17:18
 **/

import f.s.fadmincommon.msg.ObjectRestResponse;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//查询公匙
@Component
@FeignClient(value = "authService", url = "${service.authService.url}",configuration=PubkeyService.Configuration.class)
public interface PubkeyService {
    @RequestMapping(value = "/admin/auth/jwt/pubkey", method = RequestMethod.POST)
    public ObjectRestResponse<String> findPubKey();

    public static class Configuration {
        @Bean
        public Decoder feignJacksonDecoder() {
            return new JacksonDecoder();
        }
    }
}