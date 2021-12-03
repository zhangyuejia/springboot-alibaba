package org.zhangyj.db.insert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.zhangyj.db.insert.entity.InsertMsg;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhangyj
 * @since 2021-12-02
 */
public interface InsertMsgMapper extends BaseMapper<InsertMsg> {

    /**
     * 清空表
     */
    void truncateTable();
}
