package f.s.fadminperm.rest;

import f.s.fadminperm.biz.UserBiz;
import f.s.fadminperm.vo.UserDto;
import f.s.utils.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 11:51
 */
@RestController
@RequestMapping("/admin/inner/user")
public class InnerController{
    @Autowired
    private UserBiz userBiz;

    @RequestMapping(value = "/namesByIds", method = RequestMethod.POST)
    public ResultModel namesByIds(@RequestBody List<Integer> ids) throws Exception {
        if (ids==null||ids.size()==0){
            return ResultModel.success(null);
        }
        List<UserDto> users = userBiz.namesByIds(ids);
        return ResultModel.success(users);
    }
}
