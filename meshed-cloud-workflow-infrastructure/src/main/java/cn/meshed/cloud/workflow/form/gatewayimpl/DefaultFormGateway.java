package cn.meshed.cloud.workflow.form.gatewayimpl;

import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.GzipUtils;
import cn.meshed.cloud.utils.PageUtils;
import cn.meshed.cloud.workflow.domain.form.Form;
import cn.meshed.cloud.workflow.domain.form.gateway.FormGateway;
import cn.meshed.cloud.workflow.form.enums.FormStatusEnum;
import cn.meshed.cloud.workflow.form.gatewayimpl.database.dataobject.FormDO;
import cn.meshed.cloud.workflow.form.gatewayimpl.database.mapper.FormMapper;
import cn.meshed.cloud.workflow.form.query.FormPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.exception.SysException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.pagehelper.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.FormService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * <h1>默认表单网关实现</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class DefaultFormGateway implements FormGateway {

    private final FormMapper formMapper;
    private final FormService formService;

    /**
     * 发布表单
     *
     * @param formId 表单ID
     * @return {@link Boolean}
     */
    @Override
    public Boolean publish(String formId) {
        //查询出发布的版本
        FormDO formDO = getFormDO(formId);
        //先下降全部的key（除上架表单以外）
        LambdaUpdateWrapper<FormDO> luw = new LambdaUpdateWrapper<>();
        //不等于表单ID 且 等于表单key，设置状态为下架
        luw.ne(FormDO::getId, formId).eq(FormDO::getKey, formDO.getKey()).set(FormDO::getStatus, FormStatusEnum.DISCARD);
        formMapper.update(null, luw);
        //修改当前发布状态
        formDO.setStatus(FormStatusEnum.RUN);
        return formMapper.updateById(formDO) > 0;
    }

    /**
     * 拷贝副本
     *
     * @param formId 表单ID
     * @return
     */
    @Override
    public String copy(String formId) {
        FormDO formDO = getFormDO(formId);
        AssertUtils.isTrue(formDO != null, "表单不存在");
        assert formDO != null;
        AssertUtils.isTrue(formDO.getStatus() != FormStatusEnum.EDIT, "表单编辑中可直接存在");
        formDO.setId(null);
        formDO.setVersion(formDO.getVersion() + 1);
        formDO.setStatus(FormStatusEnum.EDIT);
        AssertUtils.isTrue(formMapper.insert(formDO) > 0, "拷贝失败");
        return formDO.getId();
    }

    /**
     * 保存表单数据
     *
     * @param formId 表单ID
     * @param schema schema
     * @return
     */
    @Override
    public Boolean saveSchema(String formId, String schema) {
        AssertUtils.isTrue(StringUtils.isNotBlank(formId), "表单编码不能为空");
        AssertUtils.isTrue(StringUtils.isNotBlank(schema), "表单设计数据不能为空");
        FormDO formDO = formMapper.selectById(formId);
        AssertUtils.isTrue(formDO != null, "表单不存在");
        assert formDO != null;
        AssertUtils.isTrue(formDO.getStatus() == FormStatusEnum.EDIT, "编辑中的表单才允许修改");
        String compress = GzipUtils.compress(schema);
        LambdaUpdateWrapper<FormDO> luq = new LambdaUpdateWrapper<>();
        luq.set(FormDO::getSchema,compress.getBytes())
                .eq(FormDO::getId,formId);
        return formMapper.update(null,luq) > 0;
    }

    /**
     * 废弃表单
     *
     * @param formId 表单ID
     * @return {@link Boolean}
     */
    @Override
    public Boolean discard(String formId) {
        return updateState(formId,FormStatusEnum.DISCARD);
    }

    /**
     * 恢复表单
     *
     * @param formId 表单ID
     * @return {@link Boolean}
     */
    @Override
    public Boolean resume(String formId) {
        FormDO formDO = getFormDO(formId);
        Form form = getFormByKey(formDO.getKey());
        AssertUtils.isTrue(Objects.equals(formDO.getId(), form.getId()), "仅支持动态最新的一个恢复");
        return updateState(formId,FormStatusEnum.RUN);
    }

    /**
     * 提供KEY获取表单基础信息
     *
     * @param key key
     * @return {@link Form}
     */
    @Override
    public Form getFormByKey(String key) {
        LambdaQueryWrapper<FormDO> lqw = new LambdaQueryWrapper<>();
        lqw.eq(FormDO::getKey, key).orderByDesc(FormDO::getVersion).last("LIMIT 1");
        return CopyUtils.copy(formMapper.selectOne(lqw), Form.class);
    }

    /**
     * 获取表单数据
     *
     * @param formId  表单ID
     * @param formKey 表单唯一标识
     * @return 表单设计数据
     */
    @Override
    public String getSchema(String formId, String formKey) {
        LambdaQueryWrapper<FormDO> lqw = new LambdaQueryWrapper<>();
        lqw.select(FormDO::getSchema);
        //key存在带版本号也存在不带版本号
        if (StringUtils.isNotBlank(formId)) {
            lqw.eq(FormDO::getId, formId);
        } else if (StringUtils.isNotBlank(formKey)) {
            lqw.eq(FormDO::getKey, formKey).orderByDesc(FormDO::getVersion).last("limit 1");
        } else {
            throw new SysException("查询参数不能为空");
        }
        FormDO formDO = formMapper.selectOne(lqw);
        if (formDO != null) {
            return GzipUtils.uncompress(new String(formDO.getSchema()));
        }
        return null;
    }

    /**
     * 废弃表单
     *
     * @param formId 表单ID
     * @param status
     * @return {@link Boolean}
     */
    public Boolean updateState(String formId, FormStatusEnum status) {
        AssertUtils.isTrue(StringUtils.isNotBlank(formId), "表单编码不能为空");
        LambdaUpdateWrapper<FormDO> luw = new LambdaUpdateWrapper<>();
        luw.eq(FormDO::getId, formId).set(FormDO::getStatus, status);
        return formMapper.update(null, luw) > 0;
    }

    /**
     * 变更状态
     *
     * @param key 表单唯一标识
     * @return {@link Boolean}
     */
    @Override
    public Boolean existFormKey(String key) {
        AssertUtils.isTrue(StringUtils.isNotBlank(key), "表单标识不能为空");
        LambdaQueryWrapper<FormDO> lqw = new LambdaQueryWrapper<>();
        lqw.select(FormDO::getId).eq(FormDO::getKey, key);
        Long count = formMapper.selectCount(lqw);
        System.out.println(count);
        return count > 0;
    }

    /**
     * 获取表单选项信息
     *
     * @return {@link List <Form>}
     */
    @Override
    public List<Form> select() {
        return CopyUtils.copyListProperties(formMapper.select(), Form.class);
    }

    /**
     * 获取启动流程表单key
     *
     * @param definitionId 定义ID
     * @return 表单key
     */
    @Override
    public String getStartFormKey(String definitionId) {
        AssertUtils.isTrue(StringUtils.isNotBlank(definitionId), "定义ID不能为空");
        return formService.getStartFormKey(definitionId);
    }

    /**
     * <h1>保存对象</h1>
     *
     * @param form 表单数据
     * @return {@link Boolean}
     */
    @Override
    public String save(Form form) {
        AssertUtils.isTrue(!existFormKey(form.getKey()), "表单标识已经存在");
        FormDO formDO = new FormDO();
        formDO.setName(form.getName());
        formDO.setDescription(form.getDescription());
        formDO.setKey(form.getKey());
        formDO.setVersion(1);
        formDO.setStatus(FormStatusEnum.EDIT);
        if (StringUtils.isBlank(formDO.getDescription())) {
            formDO.setDescription(formDO.getName());
        }
        AssertUtils.isTrue(formMapper.insert(formDO) > 0, "保存失败");
        return formDO.getId();
    }

    /**
     * <h1>更新</h1>
     *
     * @param form 表单
     * @return {@link Boolean}
     */
    @Override
    public Boolean update(Form form) {
        FormDO formDO = getFormDO(form.getId());
        AssertUtils.isTrue(formDO != null, "表单不存在");
        assert formDO != null;
        AssertUtils.isTrue(formDO.getStatus() == FormStatusEnum.RUN, "运行中的表单不允许修改");
        formDO.setName(form.getName());
        formDO.setDescription(form.getDescription());
        return formMapper.updateById(formDO) > 0;
    }

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry 分页参数
     * @return {@link PageResponse<Form>}
     */
    @Override
    public PageResponse<Form> searchList(FormPageQry pageQry) {
        Page<Object> page = PageUtils.startPage(pageQry);
        LambdaQueryWrapper<FormDO> lqw = new LambdaQueryWrapper<>();
        lqw.select(FormDO::getId, FormDO::getKey, FormDO::getName,
                        FormDO::getDescription, FormDO::getStatus, FormDO::getVersion)
                .orderByDesc(FormDO::getCreateTime)
                .eq(pageQry.getStatus() != null, FormDO::getStatus, pageQry.getStatus())
                .like(StringUtils.isNotBlank(pageQry.getKeyword()), FormDO::getKey, pageQry.getKeyword())
                .like(StringUtils.isNotBlank(pageQry.getKeyword()), FormDO::getName, pageQry.getKeyword());
        return PageUtils.of(formMapper.selectList(lqw), page, Form::new);
    }

    /**
     * 查询
     *
     * @param formId 表单ID
     * @return {@link Form}
     */
    @Override
    public Form query(String formId) {
        return CopyUtils.copy(getFormDO(formId), Form.class);
    }

    private FormDO getFormDO(String formId) {
        AssertUtils.isTrue(StringUtils.isNotBlank(formId), "表单编码不能为空");
        return formMapper.selectById(formId);
    }

    /**
     * <h1>删除对象</h1>
     *
     * @param formId 表单ID
     * @return {@link Boolean}
     */
    @Override
    public Boolean delete(String formId) {
        FormDO formDO = getFormDO(formId);
        AssertUtils.isTrue(formDO != null, "表单不存在");
        assert formDO != null;
        AssertUtils.isTrue(formDO.getStatus() == FormStatusEnum.DISCARD || formDO.getStatus() == FormStatusEnum.EDIT,
                "表单运行中不允许删除，请废弃后操作");
        return formMapper.deleteById(formId) > 0;
    }
}
