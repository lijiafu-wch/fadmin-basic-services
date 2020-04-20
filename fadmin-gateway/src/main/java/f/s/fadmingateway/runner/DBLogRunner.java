package f.s.fadmingateway.runner;
import f.s.fadmingateway.util.DBLog;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;


/**
 * 启动线程插入日志
 * @author ace
 * @create 2017/12/17.
 */
@Configuration
public class DBLogRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
      while (true){
          if(DBLog.getInstance().isAlive()){
              break;
          }else {
              DBLog.getInstance().start();
          }
      }
    }
}
