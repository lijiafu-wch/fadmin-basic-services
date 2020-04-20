package f.s.fadminauth.feign;

import f.s.fadminauth.util.user.JwtAuthenticationRequest;
import f.s.fadminauth.vo.UserInfo;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-21 8:11
 */
@Component
@FeignClient(value = "permService", url = "${service.permService.url}",configuration=IUserService.Configuration.class)
public interface IUserService {
  @RequestMapping(value = "admin/perm/user/validate", method = RequestMethod.POST)
  public UserInfo validate(@RequestBody JwtAuthenticationRequest authenticationRequest);
  public static class Configuration {
    @Bean
    public Decoder feignJacksonDecoder() {
      return new JacksonDecoder();
    }
  }
}
