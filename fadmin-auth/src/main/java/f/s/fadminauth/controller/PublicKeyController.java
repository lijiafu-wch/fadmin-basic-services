package f.s.fadminauth.controller;
import f.s.fadminauth.configuration.KeyConfiguration;
import f.s.fadmincommon.msg.ObjectRestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * @program: fadmin-basic-services
 * @description: 查询密匙
 * @author: Mr.Huang
 * @create: 2018-12-19 17:13
 **/
@RestController
@RequestMapping("/admin/auth/jwt")
@Slf4j
public class PublicKeyController {
    @Autowired
    private KeyConfiguration keyConfiguration;
    @RequestMapping(value = "pubkey", method = RequestMethod.POST)
    public ObjectRestResponse<String> getPubKey() throws Exception {
        return new ObjectRestResponse<>().data(keyConfiguration.getPubkey());
    }
}
