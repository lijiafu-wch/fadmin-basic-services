package f.s.fadmingateway.filter;

import com.netflix.zuul.ZuulFilter;
import f.s.fadmincommon.context.BaseContextHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-23 8:25
 */
@Component
@Slf4j
public class ClearFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        BaseContextHandler.remove();
        return null;
    }

}
