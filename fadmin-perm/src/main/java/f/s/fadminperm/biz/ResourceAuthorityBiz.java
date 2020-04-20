package f.s.fadminperm.biz;
import f.s.fadmincommon.biz.BaseBiz;
import f.s.fadminperm.entity.ResourceAuthority;
import f.s.fadminperm.mapper.ResourceAuthorityMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Ace on 2017/6/19.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceAuthorityBiz extends BaseBiz<ResourceAuthorityMapper, ResourceAuthority> {
}
