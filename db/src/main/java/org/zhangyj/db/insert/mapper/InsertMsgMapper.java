package org.zhangyj.db.insert.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ResultHandler;
import org.zhangyj.db.bean.entity.InsertMsg;

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

    void flowPageQuery(@Param("minId") long minId, @Param("maxId") long maxId, ResultHandler<InsertMsg> handler);
}
