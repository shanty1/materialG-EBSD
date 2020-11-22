package per.sc.tool.service.scheduled;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import per.sc.tool.util.base.TimeUtil;

@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
public class DemoScheduleTask {
    //3.添加定时任务
    @Scheduled(cron = "0 0 3 * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        System.err.println("执行静态定时任务时间: "+TimeUtil.getCurDateTime() );
    }
}