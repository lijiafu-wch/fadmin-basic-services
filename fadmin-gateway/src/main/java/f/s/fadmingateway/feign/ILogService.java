package f.s.fadmingateway.feign;
import f.s.fadmingateway.fallback.MyFallbackProvider;
import f.s.fadmingateway.vo.LogInfo;
import feign.codec.Decoder;
import feign.jackson.JacksonDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 写入日志
 */
@Component
@FeignClient(value = "permService", url = "${service.permService.url}",configuration=ILogService.Configuration.class,fallback = MyFallbackProvider.class)
public interface ILogService {
  @RequestMapping(value="/admin/perm/log/save",method = RequestMethod.POST)
  public void saveLog(LogInfo info);

  public static class Configuration {
    @Bean
    public Decoder feignJacksonDecoder() {
      return new JacksonDecoder();
    }
  }
}
