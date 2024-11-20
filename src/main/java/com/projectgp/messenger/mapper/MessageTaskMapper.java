package com.projectgp.messenger.mapper;

import java.time.LocalDateTime;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.projectgp.messenger.model.MessageTask;

public interface MessageTaskMapper extends BaseMapper<MessageTask> {

    // 自定义查询方法，如根据名称查询模板
    //MessageTemplate selectByName(String name);

    // 其他自定义方法
    @Update("UPDATE message_tasks SET ALIVE = 'NO' WHERE task_id = #{taskId}")
    void updateAliveToNo(@Param("taskId") long taskId);

    @Update("UPDATE message_tasks SET actual_send_time = #{actualSendTime} WHERE task_id = #{taskId}")
    int updateActualSendTime(@Param("taskId") Long taskId, @Param("actualSendTime") LocalDateTime actualSendTime);
}