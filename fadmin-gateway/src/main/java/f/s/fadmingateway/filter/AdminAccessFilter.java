package f.s.fadmingateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import f.s.fadmincommon.context.BaseContextHandler;
import f.s.fadmincommon.msg.auth.TokenErrorResponse;
import f.s.fadmincommon.msg.auth.TokenForbiddenResponse;
import f.s.fadmincommon.util.authUtil.jwt.IJWTInfo;
import f.s.fadmincommon.util.authUtil.jwt.RsaKeyHelper;
import f.s.fadmingateway.config.UrlFilterConfig;
import f.s.fadmingateway.config.UserAuthConfig;
import f.s.fadmingateway.feign.ILogService;
import f.s.fadmingateway.feign.IUserService;
import f.s.fadmingateway.feign.PubkeyService;
import f.s.fadmingateway.util.DBLog;
import f.s.fadmingateway.util.UserAuthUtil;
import f.s.fadmingateway.vo.LogInfo;
import f.s.fadmingateway.vo.PermissionInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
/**
 * 拦截认证
 */
@Component
@Slf4j
public class AdminAccessFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(AdminAccessFilter.class);
    private static List<LogInfo> logInfoQueue = new ArrayList<>();
    @Autowired
    private IUserService userService;
    @Autowired
    private ILogService logService;
    @Autowired
    private UrlFilterConfig urlFilterConfig;
    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private UserAuthConfig userAuthConfig;

    @Autowired
    private PubkeyService pubkeyService;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }


    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        final String requestUri = request.getRequestURI();
        final String method = request.getMethod();
        BaseContextHandler.setToken(null);
        // 不进行拦截的地址
        if (isStartWith(requestUri)) {
            return null;
        }
        IJWTInfo user = null;
        try {
            user = getJWTUser(request, ctx);
        } catch (Exception e) {
            setFailedRequest(JSON.toJSONString(new TokenErrorResponse("token auth error")), 201);
            return null;
        }

        /**
         * 将用户id 及用户昵称放入head
         */
        ctx.addZuulRequestHeader("userName", user.getUniqueName());
        ctx.addZuulRequestHeader("userId", user.getId());
        log.info("user-lineness-info,userId:[{}],userName:({})",user.getId(), user.getUniqueName());

        List<PermissionInfo> permissionIfs = userService.getAllPermissionInfo();
        // 判断资源是否启用权限约束
        List<PermissionInfo> result = getPermissionIfs(requestUri, method, permissionIfs);
        PermissionInfo[] permissions = result.toArray(new PermissionInfo[]{});
        if (permissions.length > 0) {
            if (checkUserPermission(permissions, user,request)) {
                setFailedRequest(JSON.toJSONString(new TokenForbiddenResponse("User Forbidden!Does not has Permission!")), 403);
            }
        }
        return null;
    }

    /**
     * 获取目标权限资源
     *
     * @param requestUri
     * @param method
     * @param serviceInfo
     * @return
     */
    private List<PermissionInfo> getPermissionIfs(final String requestUri, final String method, List<PermissionInfo> serviceInfo) {
        List<PermissionInfo>permissionInfos=new ArrayList<>();
        for (PermissionInfo permissionInfo:serviceInfo) {
            String uri = permissionInfo.getUri();
            if (uri.indexOf("{") > 0) {
                uri = uri.replaceAll("\\{\\*\\}", "[a-zA-Z\\\\d]+");
            }
            String regEx = "^" + uri + "$";
            if(Pattern.compile(regEx).matcher(requestUri).find() && method.equals(permissionInfo.getMethod())){
                permissionInfos.add(permissionInfo);
            }
        }
        return  permissionInfos;
    }
    /**
     * 返回token中的用户信息
     *
     * @param request
     * @param ctx
     * @return
     */
    private IJWTInfo getJWTUser(HttpServletRequest request, RequestContext ctx) throws Exception {
        //设置pubkey
        if(userAuthConfig.getPubKeyByte()==null){
            log.info("start get pubkey...................");
            userAuthConfig.setPubKeyByte(RsaKeyHelper.toBytes(pubkeyService.findPubKey().getData()));
        }
        String authToken = request.getHeader(userAuthConfig.getTokenHeader());
        if (StringUtils.isBlank(authToken)) {
            authToken = request.getParameter("token");
        }
        ctx.addZuulRequestHeader(userAuthConfig.getTokenHeader(), authToken);
        BaseContextHandler.setToken(authToken);
        return userAuthUtil.getInfoFromToken(authToken);
    }



    private boolean checkUserPermission(PermissionInfo[] permissions, IJWTInfo user,HttpServletRequest request) {
        List<PermissionInfo> permissionInfos = userService.getPermissionByUsername(user.getUniqueName());
        PermissionInfo current = null;
        for (PermissionInfo info : permissions) {
            boolean anyMatch = permissionInfos.parallelStream().anyMatch(new Predicate<PermissionInfo>() {
                @Override
                public boolean test(PermissionInfo permissionInfo) {
                    return permissionInfo.getCode().equals(info.getCode());
                }
            });
            if (anyMatch) {
                current = info;
                break;
            }
        }
        if (current == null) {
            setFailedRequest(JSON.toJSONString(new TokenForbiddenResponse("Token Forbidden!")), 200);
            return true;
        } else {
                //切入日志
                setCurrentUserInfoAndLog(request, user, current);
            return false;
        }
    }


    /**
     * URI是否以什么打头
     *
     * @param requestUri
     * @return
     */
    private boolean isStartWith(String requestUri) {
        boolean flag = false;
        List<String> startWiths = urlFilterConfig.getStartWiths();
        for (String path : startWiths) {
            if (requestUri.startsWith(path)){
                return true;
            }
        }
        return flag;
    }

    /**
     * 写入操作日志
     * @param
     * @param user
     * @param pm
     */
    private void setCurrentUserInfoAndLog(HttpServletRequest request, IJWTInfo user, PermissionInfo pm) {
        String host = request.getRemoteHost();
        LogInfo logInfo = new LogInfo(pm.getMenu(), pm.getName(), pm.getUri(), new Date(), user.getId(), user.getName(), host);
        DBLog.getInstance().setLogService(logService).offerQueue(logInfo);
    }
    /**
     * 网关抛异常
     *
     * @param message
     * @param code
     */
    private void setFailedRequest(String message, int code, RequestContext ctx) {
        HttpServletResponse response = ctx.getResponse();
        response.setContentType("application/json; charset=utf8");
        response.setStatus(code);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.print("{\"code\":" + code + ",\"message\":\""+ message +"\"}");
        } catch (IOException e) {
            log.info("User token is null or empty"+e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    /**
     * 网关抛异常
     *
     * @param body
     * @param code
     */
    private void setFailedRequest(String body, int code) {
        log.debug("Reporting error ({}): {}", code, body);
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(code);
        if (ctx.getResponseBody() == null) {
            ctx.setResponseBody(body);
            ctx.setSendZuulResponse(false);
        }
    }

}
