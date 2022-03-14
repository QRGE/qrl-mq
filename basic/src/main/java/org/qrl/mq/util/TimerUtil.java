package org.qrl.mq.util;

import lombok.SneakyThrows;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author qr
 * @date 2022/3/13 16:04
 */
public class TimerUtil {

    private final static Timer timer = new Timer();

    @SneakyThrows
    public static void work(TimerTask task, long delay, long period){
        timer.schedule(task, delay, period);
    }
}
