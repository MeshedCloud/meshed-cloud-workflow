package cn.meshed.cloud.workflow.flow.gatewayimpl.database.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author by Vincent Vic
 * @since 2023-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("m_flow_designer")
public class FlowDesignerDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程设计数据
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 设计图数据
     */
    private String graph;


}
