package org.zhangyj.db.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangyj
 */
@Slf4j
public class CloseUtil {

    public static void close(AutoCloseable autoCloseable){
        if(autoCloseable == null){
            return;
        }
        try {
            autoCloseable.close();
        } catch (Exception e) {
            log.error("关闭对象异常", e);
            throw new RuntimeException(e);
        }
    }
}
