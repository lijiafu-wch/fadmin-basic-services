package f.s.fadminperm.rpc;

import f.s.fadminperm.biz.GateLogBiz;
import f.s.fadminperm.entity.GateLog;
import f.s.fadminperm.vo.LogInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-07-01 14:39
 */
@RequestMapping("/admin/perm")
@RestController
public class LogRest {
    @Autowired
    private GateLogBiz gateLogBiz;
    @RequestMapping(value="/log/save",method = RequestMethod.POST)
    public @ResponseBody void saveLog(@RequestBody LogInfo info){
        GateLog log = new GateLog();
        BeanUtils.copyProperties(info,log);
        gateLogBiz.insertSelective(log);
    }
}
