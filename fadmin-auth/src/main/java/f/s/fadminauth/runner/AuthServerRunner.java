package f.s.fadminauth.runner;

import f.s.fadminauth.configuration.KeyConfiguration;
import f.s.fadmincommon.util.authUtil.jwt.RsaKeyHelper;
import f.s.jcache.Cache;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

//import f.s.jcache.Cache;
/**
 * @author ace
 * @create 2017/12/17.
 */
@Configuration
public class AuthServerRunner implements CommandLineRunner{

    @Resource
    private Cache cacheRedis;
    private static final String REDIS_USER_PRI_KEY = "fadmin-auth:JWT:PRI";
    private static final String REDIS_USER_PUB_KEY = "fadmin-auth:JWT:PUB";

    @Autowired
    private KeyConfiguration keyConfiguration;
    @Override
    public void run(String... args) throws Exception {
        if (cacheRedis.exists(REDIS_USER_PRI_KEY)&&cacheRedis.exists(REDIS_USER_PUB_KEY)) {
            keyConfiguration.setUserPriKey(RsaKeyHelper.toBytes(cacheRedis.get(REDIS_USER_PRI_KEY).toString()));
            keyConfiguration.setUserPubKey(RsaKeyHelper.toBytes(cacheRedis.get(REDIS_USER_PUB_KEY).toString()));
            keyConfiguration.setPubkey(cacheRedis.get(REDIS_USER_PUB_KEY).toString());
            keyConfiguration.setPrikey(cacheRedis.get(REDIS_USER_PRI_KEY).toString());
        } else {
            Map<String, byte[]> keyMap = RsaKeyHelper.generateKey(keyConfiguration.getUserSecret());
            keyConfiguration.setUserPriKey(keyMap.get("pri"));
            keyConfiguration.setUserPubKey(keyMap.get("pub"));
            keyConfiguration.setPubkey(RsaKeyHelper.toHexString(keyMap.get("pub")));
            keyConfiguration.setPrikey(RsaKeyHelper.toHexString(keyMap.get("pri")));
            cacheRedis.set(REDIS_USER_PRI_KEY, RsaKeyHelper.toHexString(keyMap.get("pri")));
            cacheRedis.set(REDIS_USER_PUB_KEY, RsaKeyHelper.toHexString(keyMap.get("pub")));

        }
    }
}
