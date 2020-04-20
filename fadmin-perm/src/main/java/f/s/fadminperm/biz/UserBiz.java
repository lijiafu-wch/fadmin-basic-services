package f.s.fadminperm.biz;

import com.ace.cache.annotation.Cache;
import com.ace.cache.annotation.CacheClear;
import f.s.fadmincommon.biz.BaseBiz;
import f.s.fadmincommon.constant.UserConstant;
import f.s.fadminperm.entity.User;
import f.s.fadminperm.mapper.MenuMapper;
import f.s.fadminperm.mapper.UserMapper;
import f.s.fadminperm.util.UserAuthUtil;
import f.s.fadminperm.vo.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 16:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBiz<UserMapper, User> {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserAuthUtil userAuthUtil;
    @Autowired
    private UserMapper userMapper;
    @Override
    public void insertSelective(User entity) {
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(entity.getPassword());
        entity.setPassword(password);
        super.insertSelective(entity);
    }

    @Override
    @CacheClear(pre="user{1.username}")
    public void updateSelectiveById(User entity) {
        super.updateSelectiveById(entity);
    }

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Cache(key="user{1}",expire = 1)
    public User getUserByUsername(String username){
        User user = new User();
        user.setUsername(username);
        return mapper.selectOne(user);
    }


    public  void  updatePasswordById(Integer id,String newPassword){
        String password = new BCryptPasswordEncoder(UserConstant.PW_ENCORDER_SALT).encode(newPassword);
        Example example=new Example(User.class);
        example.createCriteria().andEqualTo("id",id);
        User user=new User();
        user.setPassword(password);
        userMapper.updateByExampleSelective(user,example);
    }

    public List<Map<String,Object>> findReviewPerm(){
     return menuMapper.findReviewPerm();
    }

    /**
     * 根据userId列表获取用户信息
     * @param ids
     * @return
     */
    public List<UserDto> namesByIds(List<Integer> ids){
        return userMapper.namesByIds(ids);
    }
}
