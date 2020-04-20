package f.s.fadminperm.runner;

import f.s.fadmincommon.util.authUtil.jwt.RsaKeyHelper;
import f.s.fadminperm.config.UserAuthConfig;
import f.s.fadminperm.feign.PubkeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author ace
 * @create 2017/12/17.
 */
@Configuration
public class AuthServerRunner implements CommandLineRunner {
    private static Logger log = LoggerFactory.getLogger(AuthServerRunner.class);
    @Autowired
    private UserAuthConfig userAuthConfig;
    @Resource
    private PubkeyService pubkeyService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Override
    public void run(String... args) throws Exception {
        while (true){
            log.info("start get pubkey..................");
            if(userAuthConfig.getPubKeyByte()==null){
                if(pubkeyService.findPubKey().getData()!=null){
                    userAuthConfig.setPubKeyByte(RsaKeyHelper.toBytes(pubkeyService.findPubKey().getData().toString()));
                }else{
                    Thread.sleep(1000);
                }
            }else{
                break;
            }
        }
    }
}
