package org.zhangyj.db.insert.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.zhangyj.db.bean.StopWatcher;
import org.zhangyj.db.bean.entity.InsertMsg;
import org.zhangyj.db.insert.mapper.InsertMsgMapper;
import org.zhangyj.db.insert.service.InsertMsgService;
import org.zhangyj.db.util.CloseUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhangyj
 * @since 2021-12-02
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class InsertMsgServiceImpl extends ServiceImpl<InsertMsgMapper, InsertMsg> implements InsertMsgService {



    public static CountDownLatch latch = new CountDownLatch(threadCount);

    private final DataSource dataSource;

    @Override
    public void insertMsg(int count) throws Exception {
        if(count <= 0){
            return;
        }
//        String exData = "12月2日，海淀区1名哈尔滨市来京人员核酸检测结果为阳性。经初步调查，该阳性人员常住哈尔滨市，11月26日与哈尔滨市确诊病例在密闭空间有接触，11月28日乘坐CZ2628航班从哈尔滨太平国际机场到达北京大兴国际机场，在京暂住地为海淀区琨御府东区7号楼。期间涉及在京行程轨迹如下：11月28日，21:40到达北京大兴国际机场，后乘私家车到在京住所，22:42分左右在琨御府9号楼底商多点超市购物，之后返回住所未外出。\n" +
//                "11月29日，上午未外出；下午13:00左右乘私家车到达朝阳区十里河灯具城，在二楼看灯具，期间上过一次一楼洗手间；16:00左右开车返回住所后未外出。12月2日，海淀区1名哈尔滨市来京人员核酸检测结果为阳性。经初步调查，该阳性人员常住哈尔滨市，11月26日与哈尔滨市确诊病例在密闭空间有接触，11月28日乘坐CZ2628航班从哈尔滨太平国际机场到达北京大兴国际机场，在京暂住地为海淀区琨御府东区7号楼。期间涉及在京行程轨迹如下：11月28日，21:40到达北京大兴国际机场，后乘私家车到在京住所，22:42分左右在琨御府9号楼底商多点超市购物，之后返回住所未外出。12月2日，海淀区1名哈尔滨市来京人员核酸检测结果为阳性。经初步调查，该阳性人员常住哈尔滨市，11月26日与哈尔滨市确诊病例在密闭空间有接触，11月28日乘坐CZ2628航班从哈尔滨太平国际机场到达北京大兴国际机场，在京暂住地为海淀区琨御府东区7号楼。期间涉及在京行程轨迹如下：11月28日，21:40到达北京大兴国际机场，后乘私家车到在京住所，22:42分左右在琨御府9号楼底商多点超市购物，之后返回住所未外出。12月2日，海淀区1名哈尔滨市来京人员核酸检测结果为阳性。经初步调查，该阳性人员常住哈尔滨市，11月26日与哈尔滨市确诊病例在密闭空间有接触，11月28日乘坐CZ2628航班从哈尔滨太平国际机场到达北京大兴国际机场，在京暂住地为海淀区琨御府东区7号楼。期间涉及在京行程轨迹如下：11月28日，21:40到达北京大兴国际机场，后乘私家车到在京住所，22:42分左右在琨御府9号楼底商多点超市购物，之后返回住所未外出。12月2日，海淀区1名哈尔滨市来京人员核酸检测结果为阳性。经初步调查，该阳性人员常住哈尔滨市，11月26日与哈尔滨市确诊病例在密闭空间有接触，11月28日乘坐CZ2628航班从哈尔滨太平国际机场到达北京大兴国际机场，在京暂住地为海淀区琨御府东区7号楼。期间涉及在京行程轨迹如下：11月28日，21:40到达北京大兴国际机场，后乘私家车到在京住所，22:42分左右在琨御府9号楼底商多点超市购物，之后返回住所未外出。\n" +
//                "x12月2日，海淀区1名哈尔滨市来京人员核酸检测结果为阳性。经初步调查，该阳性人员常住哈尔滨市，11月26日与哈尔滨市确诊病例在密闭空间有接触，11月28日乘坐CZ2628航班从哈尔滨太平国际机场到达北京大兴国际机场，在京暂住地为海淀区琨御府东区7号楼。期间涉及在京行程轨迹如下：11月28日，21:40到达北京大兴国际机场，后乘私家车到在京住所，22:42分左右在琨御府9号楼底商多点超市购物，之后返回住所未外出。12月2日，海淀区1名哈尔滨市来京人员核酸检测结果为阳性。经初步调查，该阳性人员常住哈尔滨市，11月26日与哈尔滨市确诊病例在密闭空间有接触，11月28日乘坐CZ2628航班从哈尔滨太平国际机场到达北京大兴国际机场，在京暂住地为海淀区琨御府东区7号楼。期间涉及在京行程轨迹如下：11月28日，21:40到达北京大兴国际机场，后乘私家车到在京住所，22:42分左右在琨御府9号楼底商多点超市购物，之后返回住所未外出。12月2日，海淀区1名哈尔滨市来京人员核酸检测结果为阳性。经初步调查，该阳性人员常住哈尔滨市，11月26日与哈尔滨市确诊病例在密闭空间有接触，11月28日乘坐CZ2628航班从哈尔滨太平国际机场到达北京大兴国际机场，在京暂住地为海淀区琨御府东区7号楼。期间涉及在京行程轨迹如下：11月28日，21:40到达北京大兴国际机场，后乘私家车到在京住所，22:42分左右在琨御府9号楼底商多点超市购物，之后返回住所未外出。12月2日，海淀区1名哈尔滨市来京人员核酸检测结果为阳性。经初步调查，该阳性人员常住哈尔滨市，11月26日与哈尔滨市确诊病例在密闭空间有接触，11月28日乘坐CZ2628航班从哈尔滨太平国际机场到达北京大兴国际机场，在京暂住地为海淀区琨御府东区7号楼。期间涉及在京行程轨迹如下：11月28日，21:40到达北京大兴国际机场，后乘私家车到在京住所，22:42分左右在琨御府9号楼底商多点超市购物，之后返回住所未外出。\n"
//                ;
        String exData = "12月2日，海淀区1名哈尔滨市来京人员核酸检测结果为阳性。经初步调查，该阳性人员常住哈尔滨市，11月26日与哈尔滨市确诊病例在密闭空间有接触，11月28日乘坐CZ2628航班从哈尔滨太平国际机场到达北京大兴国际机场，在京暂住地为海淀区琨御府东区7号楼。期间涉及在京行程轨迹如下：";
        StopWatch stopWatch = StopWatcher.watch(() -> {
            insertMsgByService(count, exData);
            latch.countDown();
//        insertMsgByJDBC(count, exData);
        });
        log.info(Thread.currentThread().getName() + "插入条数：{} 耗时：{}", count, stopWatch.getTotalTimeSeconds());
    }

    private void insertMsgByJDBC(int count, String exData) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into INSERT_MSG(name, type, exdata) values(?,?,?)";
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < count; i++) {
                ps.setString(1, "name" + i);
                ps.setInt(2, i%6 + 1);
                ps.setString(3, i + exData);
                ps.addBatch();
                if(i % 1000 == 0){
                    log.info("插入条数：{}", i);
                    ps.executeBatch();
                    ps.clearBatch();
                }
            }
            ps.executeBatch();
            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            throw new RuntimeException(e);
        } finally {
            CloseUtil.close(ps);
            CloseUtil.close(conn);
        }
    }

    private void insertMsgByService(int count, String exData) {
        List<InsertMsg> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            InsertMsg insertMsg = new InsertMsg();
            insertMsg.setName("name" + i);
            insertMsg.setType(i%6 + 1);
            insertMsg.setExdata(i + exData);
            list.add(insertMsg);
            if(i % 1000 == 0){
                saveBatch(list);
                log.info("插入条数：{}", i);
                list.clear();
            }
        }
        if(list.size()> 0){
            saveBatch(list);
        }
    }


    @Override
    public List<InsertMsg> pageInsertMsg(AtomicInteger i, long minId, long maxId) {
        Page<InsertMsg> page;
        List<InsertMsg> pageRecords = null;
        List<InsertMsg> records = new ArrayList<>();
        do{
            LambdaQueryWrapper<InsertMsg> wrapper = new LambdaQueryWrapper<>();
            // >
            wrapper.gt(InsertMsg::getId, minId);
            if(maxId > 0){
                // <=
                wrapper.le(InsertMsg::getId, maxId);
            }
            page = new Page<>();
            page.setSearchCount(false);
            page.setAsc("id");
            page.setSize(pageSize);
            page.setCurrent(1);
            page(page, wrapper);
            pageRecords = page.getRecords();
            if(CollectionUtils.isEmpty(pageRecords)){
                break;
            }
//            records.addAll(pageRecords);
            int count = i.addAndGet(pageRecords.size());
            minId = pageRecords.get(pageRecords.size() - 1).getId();
            log.info("总条数：{} 查询条数：{} 当前分页：{} 最大id:{}", count, pageRecords.size(), page.getCurrent(), minId);
        }while (pageRecords.size() == page.getSize());
        return records;
    }

    @Override
    public List<InsertMsg> flowPageInsertMsg(AtomicInteger i, long minId, long maxId) {
        List<InsertMsg> list = new ArrayList<>();
        baseMapper.flowPageQuery(minId, maxId, resultContext -> {
            InsertMsg msg = resultContext.getResultObject();
            // 你可以看自己的项目需要分批进行处理或者单个处理，这里以分批处理为例
            list.add(msg);
            int count = i.incrementAndGet();
            if (count % pageSize == 0) {
                handle(i, list);
            }
        });
        handle(i, list);
        log.info("总条数：{} 查询条数：{}", i.get(), list.size());
        return null;
    }

    /**
     * 数据处理
     */
    private void handle(AtomicInteger i, List<InsertMsg> list){
        log.info("总条数：{} 查询条数：{}", i.get(), list.size());
        try{
            // 在这里可以对你获取到的批量结果数据进行需要的业务处理
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            list.clear();
        }
    }
}
