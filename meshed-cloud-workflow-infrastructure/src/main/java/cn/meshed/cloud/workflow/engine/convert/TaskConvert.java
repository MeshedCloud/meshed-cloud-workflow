package cn.meshed.cloud.workflow.engine.convert;

import cn.meshed.cloud.workflow.domain.engine.Task;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.history.HistoricTaskInstance;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public class TaskConvert {



    public static Task toTask(TaskInfo source){
        Task target = new Task();
        target.setId(source.getId());
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setName(source.getName());
        target.setCreateTime(source.getCreateTime());
        target.setDefinitionId(source.getProcessDefinitionId());
        target.setInstanceId(source.getProcessInstanceId());
        target.setVariables(source.getProcessVariables());
        target.setAssignee(source.getAssignee());
        return target;
    }

    public static Task toTask(org.flowable.task.api.Task source){
        return toTask((TaskInfo)source);
    }

    public static Task toTask(HistoricTaskInstance source){
        return toTask((TaskInfo)source);
    }

    public static Task toTask(ProcessInstance source){
        Task target = new Task();
        target.setId(source.getId());
        target.setName(source.getLocalizedName());
        target.setAssignee(source.getStartUserId());
        target.setDescription(source.getDescription());
        target.setName(source.getName());
        target.setCreateTime(source.getStartTime());
        target.setDefinitionId(source.getProcessDefinitionId());
        target.setInstanceId(source.getProcessInstanceId());
        target.setVariables(source.getProcessVariables());
        return target;
    }
}
