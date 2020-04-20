package f.s.fadminperm.mapper;

import f.s.fadminperm.entity.Menu;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface MenuMapper extends Mapper<Menu> {
    public List<Menu> selectMenuByAuthorityId(@Param("authorityId") String authorityId, @Param("authorityType") String authorityType);

    /**
     * 根据用户和组的权限关系查找用户可访问菜单
     * @param userId
     * @return
     */
    public List<Menu> selectAuthorityMenuByUserId(@Param("userId") int userId);

    /**
     * 根据用户和组的权限关系查找用户可访问的系统
     * @param userId
     * @return
     */
    public List<Menu> selectAuthoritySystemByUserId(@Param("userId") int userId);

    /**
     * 查询拥有审核权限的用户
     * @return
     */
    List<Map<String, Object>> findReviewPerm();
}