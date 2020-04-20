package f.s.fadminperm.biz;

import f.s.fadmincommon.biz.BaseBiz;
import f.s.fadminperm.entity.GroupType;
import f.s.fadminperm.mapper.GroupTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-12 8:48
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupTypeBiz extends BaseBiz<GroupTypeMapper, GroupType> {
}
