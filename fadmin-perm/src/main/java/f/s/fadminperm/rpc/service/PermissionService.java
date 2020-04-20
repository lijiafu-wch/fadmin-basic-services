package f.s.fadminperm.rpc.service;

import f.s.fadmincommon.constant.CommonConstants;
import f.s.fadmincommon.util.TreeUtil;
import f.s.fadminperm.biz.ElementBiz;
import f.s.fadminperm.biz.MenuBiz;
import f.s.fadminperm.biz.UserBiz;
import f.s.fadminperm.constant.AdminCommonConstant;
import f.s.fadminperm.entity.Element;
import f.s.fadminperm.entity.Menu;
import f.s.fadminperm.entity.User;
import f.s.fadminperm.error.PermErrors;
import f.s.fadminperm.util.UserAuthUtil;
import f.s.fadminperm.vo.FrontUser;
import f.s.fadminperm.vo.MenuTree;
import f.s.fadminperm.vo.PermissionInfo;
import f.s.fadminperm.vo.UserInfo;
import f.s.jerror.BaseError;
import f.s.utils.ResultModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ace on 2017/9/12.
 */
@Service
public class PermissionService {
    @Autowired
    private UserBiz userBiz;
    @Autowired
    private MenuBiz menuBiz;
    @Autowired
    private ElementBiz elementBiz;
    @Autowired
    private UserAuthUtil userAuthUtil;
    @Autowired
    private PermErrors permErrors;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserInfo getUserByUsername(String username) {
        UserInfo info = new UserInfo();
        User user = userBiz.getUserByUsername(username);
        BeanUtils.copyProperties(user, info);
        info.setId(user.getId().toString());
        return info;
    }

    public UserInfo validate(String username,String password){
        UserInfo info = new UserInfo();
        User user = userBiz.getUserByUsername(username);
        if (encoder.matches(password, user.getPassword())) {
            BeanUtils.copyProperties(user, info);
            info.setId(user.getId().toString());
        }
        return info;
    }

    public List<PermissionInfo> getAllPermission() {
        List<Menu> menus = menuBiz.selectListAll();
        List<PermissionInfo> result = new ArrayList<PermissionInfo>();
        PermissionInfo info = null;
        menu2permission(menus, result);
        List<Element> elements = elementBiz.getAllElementPermissions();
        element2permission(result, elements);
        return result;
    }

    private void menu2permission(List<Menu> menus, List<PermissionInfo> result) {
        PermissionInfo info;
        for (Menu menu : menus) {
            if (StringUtils.isBlank(menu.getHref())) {
                menu.setHref("/" + menu.getCode());
            }
            info = new PermissionInfo();
            info.setCode(menu.getCode());
            info.setType(AdminCommonConstant.RESOURCE_TYPE_MENU);
            info.setName(AdminCommonConstant.RESOURCE_ACTION_VISIT);
            String uri = menu.getHref();
            if (!uri.startsWith("/")) {
                uri = "/" + uri;
            }
            info.setUri(uri);
            info.setMethod(AdminCommonConstant.RESOURCE_REQUEST_METHOD_GET);
            result.add(info
            );
            info.setMenu(menu.getTitle());
        }
    }

    public List<PermissionInfo> getPermissionByUsername(String username) {
        User user = userBiz.getUserByUsername(username);
        List<Menu> menus = menuBiz.getUserAuthorityMenuByUserId(user.getId());
        List<PermissionInfo> result = new ArrayList<PermissionInfo>();
        PermissionInfo info = null;
        menu2permission(menus, result);
        List<Element> elements = elementBiz.getAuthorityElementByUserId(user.getId() + "");
        element2permission(result, elements);
        return result;
    }

    private void element2permission(List<PermissionInfo> result, List<Element> elements) {
        PermissionInfo info;
        for (Element element : elements) {
            info = new PermissionInfo();
            info.setCode(element.getCode());
            info.setType(element.getType());
            info.setUri(element.getUri());
            info.setMethod(element.getMethod());
            info.setName(element.getName());
            info.setMenu(element.getMenuId());
            result.add(info);
        }
    }


    private List<MenuTree> getMenuTree(List<Menu> menus, int root) {
        List<MenuTree> trees = new ArrayList<MenuTree>();
        MenuTree node = null;
        for (Menu menu : menus) {
            node = new MenuTree();
            BeanUtils.copyProperties(menu, node);
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root);
    }

    public FrontUser getUserInfo(String token) throws Exception {
        String username = userAuthUtil.getInfoFromToken(token).getUniqueName();
        if (username == null) {
            return null;
        }
        UserInfo user = this.getUserByUsername(username);
        FrontUser frontUser = new FrontUser();
        BeanUtils.copyProperties(user, frontUser);
        List<PermissionInfo> permissionInfos = this.getPermissionByUsername(username);
        Stream<PermissionInfo> menus = permissionInfos.parallelStream().filter((permission) -> {
            return permission.getType().equals(CommonConstants.RESOURCE_TYPE_MENU);
        });
        frontUser.setMenus(menus.collect(Collectors.toList()));
        Stream<PermissionInfo> elements = permissionInfos.parallelStream().filter((permission) -> {
            return !permission.getType().equals(CommonConstants.RESOURCE_TYPE_MENU);
        });
        frontUser.setElements(elements.collect(Collectors.toList()));
        return frontUser;
    }

    public List<MenuTree> getMenusByUsername(String token) throws Exception {
        String username = userAuthUtil.getInfoFromToken(token).getUniqueName();
        if (username == null) {
            return null;
        }
        User user = userBiz.getUserByUsername(username);
        Example example=new Example(Menu.class);
        example.orderBy("orderNum").desc();
        List<Menu> menus = menuBiz.getUserAuthorityMenuByUserId(user.getId());
        return getMenuTree(menus,AdminCommonConstant.ROOT);
    }

    public ResultModel resetPassword(String id, String oldPassword,String newPassword) throws BaseError {
      User user =userBiz.selectById(id);
       //验证输入的旧密码是否一致
        boolean flag=encoder.matches(oldPassword, user.getPassword());
        if (flag) {
            //重置密码
            userBiz.updatePasswordById(Integer.valueOf(id.toString()),newPassword);
        }
        else{
           throw  permErrors.OldPasswordError("The old password you entered is incorrect ");
        }
        return ResultModel.success();
    }


    public ResultModel reviewPerm() {
        List<Map<String,Object>>list=userBiz.findReviewPerm();
        return  ResultModel.success(list);
    }
}
