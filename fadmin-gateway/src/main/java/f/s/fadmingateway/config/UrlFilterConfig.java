package f.s.fadmingateway.config;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 阿里云配置
 * @author lijiafu
 * @date 2019/12/31 14:44
 * @since 1.0
 */
@Component
@ConfigurationProperties(prefix="gate.ignore")
@Data
public class UrlFilterConfig {
    //需要过滤的url
    private List<String> startWiths;

    @PostConstruct
    public void init() {
        List<String> populated = new LinkedList<>();
        for (String path : startWiths) {
            populated.add(path.replace("**", ""));
        }
        this.startWiths = populated;
    }


}
