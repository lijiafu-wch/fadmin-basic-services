package f.s.fadminperm.rpc;

import com.ace.cache.annotation.Cache;
import f.s.fadminperm.config.UserAuthConfig;
import f.s.fadminperm.error.PermErrors;
import f.s.fadminperm.rpc.service.PermissionService;
import f.s.fadminperm.vo.PermissionInfo;
import f.s.fadminperm.vo.UserInfo;
import f.s.jerror.BaseError;
import f.s.utils.ResultModel;
import f.s.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-21 8:15
 */
@RestController
@RequestMapping("/admin/perm")
public class UserRest {
    @Autowired
    private PermErrors permErrors;
    @Autowired
    private PermissionService permissionService;
    @Cache(key="permission",expire = 1)
    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    public @ResponseBody
    List<PermissionInfo> getAllPermission(){
        return permissionService.getAllPermission();
    }

    @Cache(key="permission:u{1}",expire = 1)
    @RequestMapping(value = "/user/un/{username}/permissions", method = RequestMethod.GET)
    public @ResponseBody List<PermissionInfo> getPermissionByUsername(@PathVariable("username") String username){
        return permissionService.getPermissionByUsername(username);
    }

    @RequestMapping(value = "/user/validate", method = RequestMethod.POST)
    public @ResponseBody
    UserInfo validate(@RequestBody Map<String,String> body){
        return permissionService.validate(body.get("username"),body.get("password"));
    }

    /**
     * 重置密码
     * @param body
     * @return
     * @throws BaseError
     */
    @RequestMapping(value = "/user/resetPassword", method = RequestMethod.POST)
    public @ResponseBody
    ResultModel resetPassword(@RequestBody Map<String,String> body) throws BaseError {
        Validator.notEmpty(body.get("id"),permErrors.MissingArgument("id"));
        Validator.notEmpty(body.get("oldPassword"),permErrors.MissingArgument("oldPassword"));
        Validator.notEmpty(body.get("newPassword"),permErrors.MissingArgument("newPassword"));
        return permissionService.resetPassword(body.get("id"),body.get("oldPassword"),body.get("newPassword"));
    }

    /**
     * 查询拥有审核权限的用户信息
     */
    @RequestMapping(value = "/user/reviewPerm", method = RequestMethod.POST)
    public @ResponseBody
    ResultModel resetPassword() throws BaseError {
        return permissionService.reviewPerm();
    }

}
