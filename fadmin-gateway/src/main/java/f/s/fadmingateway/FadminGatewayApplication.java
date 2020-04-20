package f.s.fadmingateway;

import f.s.fadmingateway.filter.AdminAccessFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.MultipartConfigElement;

@EnableFeignClients("f.s.fadmingateway.feign")
@EnableZuulProxy
@EnableCircuitBreaker
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class FadminGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FadminGatewayApplication.class, args);
    }
    @Bean
    public AdminAccessFilter adminAccessFilter(){   return new AdminAccessFilter(); }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize("5MB"); //KB,MB
        //设置总上传数据总大小
        factory.setMaxRequestSize("8MB");
        return factory.createMultipartConfig();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}
