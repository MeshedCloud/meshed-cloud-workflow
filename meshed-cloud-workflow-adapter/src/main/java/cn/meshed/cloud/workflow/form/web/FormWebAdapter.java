package cn.meshed.cloud.workflow.form.web;

import cn.meshed.cloud.workflow.domain.form.ability.FormAbility;
import cn.meshed.cloud.workflow.flow.data.DraftDTO;
import cn.meshed.cloud.workflow.form.FormAdapter;
import cn.meshed.cloud.workflow.form.command.FormCmd;
import cn.meshed.cloud.workflow.form.command.FormSchemaCmd;
import cn.meshed.cloud.workflow.form.data.FormDTO;
import cn.meshed.cloud.workflow.form.data.FormOptionDTO;
import cn.meshed.cloud.workflow.form.query.FormPageQry;
import cn.meshed.cloud.workflow.form.query.FormSchemaQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <h1>表单适配器</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
public class FormWebAdapter implements FormAdapter {

    private final FormAbility formAbility;

    /**
     * 表单分页列表
     *
     * @param formPageQry 表单分页查询参数
     * @return {@link PageResponse<FormDTO>}
     */
    @Override
    public PageResponse<FormDTO> list(@Valid FormPageQry formPageQry) {
        return formAbility.searchList(formPageQry);
    }

    /**
     * 表单分页列表
     *
     * @return {@link PageResponse<FormOptionDTO>}
     */
    @Override
    public SingleResponse<List<FormOptionDTO>> select() {
        return formAbility.select();
    }

    /**
     * 保存表单
     *
     * @param formCmd 保存表单
     * @return {@link Response}
     */
    @Override
    public Response save(@Valid FormCmd formCmd) {
        return formAbility.save(formCmd);
    }

    /**
     * 创建表单副本表单
     *
     * @param formId 表单唯一标识
     * @return {@link SingleResponse<String>}
     */
    @Override
    public SingleResponse<String> copy(String formId) {
        return formAbility.copy(formId);
    }


    /**
     * 保存表单数据
     *
     * @param formSchemaCmd 保存表单
     * @return {@link Response}
     */
    @Override
    public Response saveSchema(@Valid FormSchemaCmd formSchemaCmd) {
        return formAbility.saveSchema(formSchemaCmd);
    }

    /**
     * 获取表单设计数据
     *
     * @param formId 表单ID
     * @return {@link Response}
     */
    @Override
    public Response getSchemaById(String formId) {
        FormSchemaQry formSchemaQry = new FormSchemaQry();
        formSchemaQry.setId(formId);
        return formAbility.getSchema(formSchemaQry);
    }

    /**
     * 获取表单设计数据
     *
     * @param key 表单唯一标识
     * @return {@link Response}
     */
    @Override
    public Response getSchemaByKey(String key) {
        FormSchemaQry formSchemaQry = new FormSchemaQry();
        formSchemaQry.setKey(key);
        return formAbility.getSchema(formSchemaQry);
    }

    /**
     * 发布表单
     *
     * @param formId 表单ID
     * @return {@link Response}
     */
    @Override
    public Response publish(String formId) {
        return formAbility.publish(formId);
    }

    /**
     * 废弃表单
     *
     * @param formId 表单ID
     * @return {@link Response}
     */
    @Override
    public Response discard(String formId) {
        return formAbility.discard(formId);
    }

    /**
     * 检查表单标识是否可用
     *
     * @param key 表单标识
     * @return {@link Response}
     */
    @Override
    public Response availableKey(String key) {
        return formAbility.availableKey(key);
    }

}
