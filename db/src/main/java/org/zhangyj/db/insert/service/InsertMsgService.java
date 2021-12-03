package org.zhangyj.db.insert.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.zhangyj.db.bean.entity.InsertMsg;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhangyj
 * @since 2021-12-02
 */
public interface InsertMsgService extends IService<InsertMsg> {

    /**
     * 插入线程数
     */
    int threadCount = 10;

    /**
     * 插入
     * @param count 条数
     */
    void insertMsg(int count) throws Exception;


}
