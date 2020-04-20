package f.s.fadminperm.mapper;

import f.s.fadminperm.entity.User;
import f.s.fadminperm.vo.UserDto;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
public interface UserMapper extends Mapper<User> {
    public List<User> selectMemberByGroupId(@Param("groupId") int groupId);
    public List<User> selectLeaderByGroupId(@Param("groupId") int groupId);
    public List<UserDto> namesByIds(@Param("ids")List<Integer> ids);
}