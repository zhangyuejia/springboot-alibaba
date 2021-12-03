package org.zhangyj.db.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

/**
 * @author zhangyj
 */
@Slf4j
public class StopWatcher {

    private StopWatcher(){}

    public interface ITask{

        /**
         * 执行任务
         * @throws Exception 异常
         */
        void exec() throws Exception;
    }

    public static StopWatch watch(ITask iTask) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        iTask.exec();
        stopWatch.stop();
        return stopWatch;
    }
}
