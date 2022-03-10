package org.qrl.mq.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author qr
 * @date 2022/3/10 13:24
 */
public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        log.error("Error Message!");
        log.warn("Warn Message!");
        log.info("Info Message!");
        log.debug("Debug Message!");
        log.trace("Trace Message!");
    }
}
