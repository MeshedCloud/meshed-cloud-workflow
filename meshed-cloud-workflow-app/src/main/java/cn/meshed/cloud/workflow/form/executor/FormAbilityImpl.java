package cn.meshed.cloud.workflow.form.executor;

import cn.meshed.cloud.workflow.domain.form.ability.FormAbility;
import cn.meshed.cloud.workflow.form.command.FormCmd;
import cn.meshed.cloud.workflow.form.command.FormSchemaCmd;
import cn.meshed.cloud.workflow.form.data.FormDTO;
import cn.meshed.cloud.workflow.form.data.FormOptionDTO;
import cn.meshed.cloud.workflow.form.executor.command.FormCmdExe;
import cn.meshed.cloud.workflow.form.executor.command.FormCopyCmdExe;
import cn.meshed.cloud.workflow.form.executor.command.FormDelExe;
import cn.meshed.cloud.workflow.form.executor.command.FormDiscardCmdExe;
import cn.meshed.cloud.workflow.form.executor.command.FormPublishCmdExe;
import cn.meshed.cloud.workflow.form.executor.command.FormResumeCmdExe;
import cn.meshed.cloud.workflow.form.executor.command.FormSchemaCmdExe;
import cn.meshed.cloud.workflow.form.executor.query.FormAvailableKeyQryExe;
import cn.meshed.cloud.workflow.form.executor.query.FormPageQryExe;
import cn.meshed.cloud.workflow.form.executor.query.FormSchemaQryExe;
import cn.meshed.cloud.workflow.form.executor.query.FormSelectQryExe;
import cn.meshed.cloud.workflow.form.query.FormPageQry;
import cn.meshed.cloud.workflow.form.query.FormSchemaQry;
import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <h1>表单能力实现</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
public class FormAbilityImpl implements FormAbility {

    private final FormCmdExe formCmdExe;
    private final FormDelExe formDelExe;
    private final FormSchemaCmdExe formSchemaCmdExe;
    private final FormPublishCmdExe formPublishCmdExe;
    private final FormDiscardCmdExe formDiscardCmdExe;
    private final FormResumeCmdExe formResumeCmdExe;
    private final FormCopyCmdExe formCopyCmdExe;
    private final FormSchemaQryExe formSchemaQryExe;
    private final FormAvailableKeyQryExe formAvailableKeyQryExe;
    private final FormSelectQryExe formSelectQryExe;
    private final FormPageQryExe formPageQryExe;

    /**
     * 保存表单数据
     *
     * @param formSchemaCmd 保存表单
     * @return {@link Response}
     */
    @Override
    public Response saveSchema(FormSchemaCmd formSchemaCmd) {
        return formSchemaCmdExe.execute(formSchemaCmd);
    }

    /**
     * 发布表单
     *
     * @param formId 表单ID
     * @return {@link Response}
     */
    @Override
    public Response publish(String formId) {
        return formPublishCmdExe.execute(formId);
    }

    /**
     * 废弃表单
     *
     * @param formId 表单ID
     * @return {@link Response}
     */
    @Override
    public Response discard(String formId) {
        return formDiscardCmdExe.execute(formId);
    }

    /**
     * 恢复表单
     *
     * @param formId 表单ID
     * @return {@link Response}
     */
    @Override
    public Response resume(String formId) {
        return formResumeCmdExe.execute(formId);
    }

    /**
     * 删除表单
     *
     * @param formId 表单ID
     * @return {@link Response}
     */
    @Override
    public Response delete(String formId) {
        return formDelExe.execute(formId);
    }

    /**
     * 拷贝副本
     *
     * @param formId 表单ID
     * @return 副本表单ID {@link SingleResponse <String>}
     */
    @Override
    public SingleResponse<String> copy(String formId) {
        return formCopyCmdExe.execute(formId);
    }

    /**
     * 表单设计数据查询
     *
     * @param formSchemaQry 表单设计数据查询条件
     * @return 表单设计数据
     */
    @Override
    public SingleResponse<String> getSchema(FormSchemaQry formSchemaQry) {
        return formSchemaQryExe.execute(formSchemaQry);
    }

    /**
     * 变更状态
     *
     * @param key 表单唯一标识
     * @return {@link Response}
     */
    @Override
    public Response availableKey(String key) {
        return formAvailableKeyQryExe.execute(key);
    }

    /**
     * 获取表单选项信息
     *
     * @return {@link MultiResponse< FormDTO >}
     */
    @Override
    public MultiResponse<FormOptionDTO> select() {
        return formSelectQryExe.execute(null);
    }

    /**
     * <h1>保存对象</h1>
     *
     * @param formCmd 表单数据
     * @return {@link Response}
     */
    @Override
    public Response save(FormCmd formCmd) {
        return formCmdExe.execute(formCmd);
    }

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry 查询参数
     * @return {@link PageResponse<FormDTO>}
     */
    @Override
    public PageResponse<FormDTO> searchList(FormPageQry pageQry) {
        return formPageQryExe.execute(pageQry);
    }
}
