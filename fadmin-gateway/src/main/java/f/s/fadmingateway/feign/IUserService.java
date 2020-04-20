package f.s.fadmingateway.feign;
import f.s.fadmingateway.fallback.MyFallbackProvider;
import f.s.fadmingateway.vo.PermissionInfo;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 查询全新接口
 */
@Component
@FeignClient(value = "permService", url = "${service.permService.url}",configuration=IUserService.Configuration.class,fallback = MyFallbackProvider.class)
public interface IUserService {
  @RequestMapping(value="/admin/perm/user/un/{username}/permissions",method = RequestMethod.GET)
  public List<PermissionInfo> getPermissionByUsername(@PathVariable("username") String username);
  @RequestMapping(value="/admin/perm/permissions",method = RequestMethod.GET)
  List<PermissionInfo> getAllPermissionInfo();
    public static class Configuration {
      @Bean
      public Decoder feignJacksonDecoder() {
        return new JacksonDecoder();
      }
  }
}
