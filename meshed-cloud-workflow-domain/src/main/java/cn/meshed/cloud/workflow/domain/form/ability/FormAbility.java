package cn.meshed.cloud.workflow.domain.form.ability;

import cn.meshed.cloud.core.ISave;
import cn.meshed.cloud.core.ISearchList;
import cn.meshed.cloud.workflow.domain.form.Form;
import cn.meshed.cloud.workflow.form.command.FormCmd;
import cn.meshed.cloud.workflow.form.command.FormSchemaCmd;
import cn.meshed.cloud.workflow.form.data.FormDTO;
import cn.meshed.cloud.workflow.form.data.FormOptionDTO;
import cn.meshed.cloud.workflow.form.query.FormPageQry;
import cn.meshed.cloud.workflow.form.query.FormSchemaQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;

import java.util.List;

/**
 * <h1>表单能力</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface FormAbility extends ISearchList<FormPageQry, PageResponse<FormDTO>>, ISave<FormCmd, Response> {

    /**
     * 保存表单数据
     *
     * @param formSchemaCmd 保存表单
     * @return {@link Response}
     */
    Response saveSchema(FormSchemaCmd formSchemaCmd);

    /**
     * 发布表单
     *
     * @param formId 表单ID
     * @return {@link Response}
     */
    Response publish(String formId);

    /**
     * 废弃表单
     *
     * @param formId 表单ID
     * @return {@link Response}
     */
    Response discard(String formId);

    /**
     * 拷贝副本
     *
     * @param formId 表单ID
     * @return 副本表单ID {@link SingleResponse<String>}
     */
    SingleResponse<String> copy(String formId);

    /**
     * 表单设计数据查询
     *
     * @param formSchemaQry 表单设计数据查询条件
     * @return 表单设计数据
     */
    SingleResponse<String> getSchema(FormSchemaQry formSchemaQry);

    /**
     * 变更状态
     *
     * @param key 表单唯一标识
     * @return {@link Response}
     */
    Response availableKey(String key);

    /**
     * 获取表单选项信息
     *
     * @return {@link SingleResponse<List<FormOptionDTO>>}
     */
    SingleResponse<List<FormOptionDTO>> select();
}
