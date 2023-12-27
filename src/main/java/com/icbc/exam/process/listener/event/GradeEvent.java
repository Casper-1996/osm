package com.icbc.exam.process.listener.event;

import com.icbc.exam.process.listener.entity.GradeEventModel;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @author lida
 * @title:
 * @projectName：plm_mgmt_baddebt
 * @description:
 * @date 2020/7/13
 */
@Data
public class GradeEvent extends ApplicationEvent {

    private GradeEventModel gradeEventModel;

    public GradeEvent(GradeEventModel gradeEventModel) {
        super(gradeEventModel);
        this.gradeEventModel = gradeEventModel;
    }
}
