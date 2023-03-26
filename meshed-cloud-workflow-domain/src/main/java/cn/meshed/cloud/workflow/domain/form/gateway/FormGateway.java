package cn.meshed.cloud.workflow.domain.form.gateway;

import cn.meshed.cloud.core.IQuery;
import cn.meshed.cloud.core.ISave;
import cn.meshed.cloud.core.ISearchList;
import cn.meshed.cloud.core.IUpdate;
import cn.meshed.cloud.workflow.domain.form.Form;
import cn.meshed.cloud.workflow.form.query.FormPageQry;
import com.alibaba.cola.dto.PageResponse;

import java.util.List;

/**
 * <h1>表单网关</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface FormGateway extends ISearchList<FormPageQry, PageResponse<Form>>, ISave<Form,Boolean>,
        IQuery<String,Form>, IUpdate<Form,Boolean> {

    /**
     * 发布表单
     *
     * @param formId 表单ID
     * @return {@link Boolean}
     */
    Boolean publish(String formId);

    /**
     * 拷贝副本
     *
     * @param formId 表单ID
     * @return
     */
    String copy(String formId);

    /**
     * 保存表单数据
     *
     * @param formId 表单ID
     * @param schema schema
     * @return
     */
    Boolean saveSchema(String formId,String schema);

    /**
     * 获取表单数据
     *
     * @param formId 表单ID
     * @param formKey 表单唯一标识
     * @return 表单设计数据
     */
    String getSchema(String formId,String formKey);

    /**
     * 废弃表单
     *
     * @param formId 表单ID
     * @return {@link Boolean}
     */
    Boolean discard(String formId);

    /**
     * 变更状态
     *
     * @param key 表单唯一标识
     * @return {@link Boolean}
     */
    Boolean existFormKey(String key);

    /**
     * 获取表单选项信息
     *
     * @return {@link List<Form>}
     */
    List<Form> select();
}
