package f.s.fadminperm.rest;
import f.s.fadmincommon.rest.BaseController;
import f.s.fadminperm.biz.MenuBiz;
import f.s.fadminperm.biz.UserBiz;
import f.s.fadminperm.config.UserAuthConfig;
import f.s.fadminperm.entity.Menu;
import f.s.fadminperm.entity.User;
import f.s.fadminperm.rpc.service.PermissionService;
import f.s.fadminperm.vo.FrontUser;
import f.s.fadminperm.vo.MenuTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 11:51
 */
@RestController
@RequestMapping("/admin/perm/user")
public class UserController extends BaseController<UserBiz, User> {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private MenuBiz menuBiz;

    @Autowired
    private UserAuthConfig userAuthConfig;

    @RequestMapping(value = "/front/info", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getUserInfo(String token) throws Exception {
        //获取公匙

        FrontUser userInfo = permissionService.getUserInfo(token);
        if(userInfo==null) {
            return ResponseEntity.status(401).body(false);
        } else {
            return ResponseEntity.ok(userInfo);
        }
    }

    @RequestMapping(value = "/front/menus", method = RequestMethod.GET)
    public @ResponseBody
    List<MenuTree> getMenusByUsername(String token) throws Exception {
        return permissionService.getMenusByUsername(token);
    }

    @RequestMapping(value = "/front/menu/all", method = RequestMethod.GET)
    public @ResponseBody
    List<Menu> getAllMenus() throws Exception {
        Example example=new Example(Menu.class);
        example.orderBy("orderNum").desc();
        return menuBiz.selectListAll();
    }
}
