package cn.meshed.cloud.workflow.form.gatewayimpl;

import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.PageUtils;
import cn.meshed.cloud.workflow.domain.form.Form;
import cn.meshed.cloud.workflow.domain.form.gateway.FormGateway;
import cn.meshed.cloud.workflow.form.enums.FormStatusEnum;
import cn.meshed.cloud.workflow.form.gatewayimpl.database.dataobject.FormDO;
import cn.meshed.cloud.workflow.form.gatewayimpl.database.mapper.FormMapper;
import cn.meshed.cloud.workflow.form.query.FormPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.SysException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

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
        luw.ne(FormDO::getId,formId).eq(FormDO::getKey,formDO.getKey()).set(FormDO::getStatus,FormStatusEnum.DISCARD);
        formMapper.update(null,luw);
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
        formDO.setId(null);
        formDO.setVersion(formDO.getVersion() + 1);
        AssertUtils.isTrue(formMapper.insert(formDO) > 0,"拷贝失败");
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
        AssertUtils.isTrue(formDO.getStatus() == FormStatusEnum.RUN, "运行中的表单不允许修改");
        formDO.setSchema(schema);
        return formMapper.updateById(formDO) > 0;
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
        }else if (StringUtils.isNotBlank(formKey)){
            lqw.eq(FormDO::getKey, formKey).orderByDesc(FormDO::getVersion).last("limit 1");
        } else {
            throw new SysException("查询参数不能为空");
        }
        FormDO formDO = formMapper.selectOne(lqw);

        AssertUtils.isTrue(formDO != null, "表单不存在");
        assert formDO != null;
        return formDO.getSchema();
    }

    /**
     * 废弃表单
     *
     * @param formId 表单ID
     * @return {@link Boolean}
     */
    @Override
    public Boolean discard(String formId) {
        AssertUtils.isTrue(StringUtils.isNotBlank(formId), "表单编码不能为空");
        LambdaUpdateWrapper<FormDO> luw = new LambdaUpdateWrapper<>();
        //等于表单ID，设置状态为下架
        luw.eq(FormDO::getId,formId).set(FormDO::getStatus,FormStatusEnum.DISCARD);
        return formMapper.update(null,luw) > 0;
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
        return formMapper.selectCount(lqw) > 0;
    }

    /**
     * 获取表单选项信息
     *
     * @return {@link List <Form>}
     */
    @Override
    public List<Form> select() {
        return CopyUtils.copyListProperties(formMapper.select(),Form.class);
    }

    /**
     * <h1>保存对象</h1>
     *
     * @param form 表单数据
     * @return {@link Boolean}
     */
    @Override
    public Boolean save(Form form) {
        AssertUtils.isTrue(existFormKey(form.getKey()), "表单标识已经存在");
        FormDO formDO = new FormDO();
        formDO.setName(form.getName());
        formDO.setDescription(form.getDescription());
        formDO.setKey(form.getKey());
        formDO.setVersion(1);
        formDO.setStatus(FormStatusEnum.EDIT);
        if (StringUtils.isBlank(formDO.getDescription())) {
            formDO.setDescription(formDO.getName());
        }
        return formMapper.insert(formDO) > 0;
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
        lqw.select(FormDO::getId,FormDO::getKey,FormDO::getName,
                        FormDO::getDescription,FormDO::getStatus,FormDO::getVersion)
                .eq(pageQry.getStatus() != null, FormDO::getStatus, pageQry.getStatus())
                .like(StringUtils.isNotBlank(pageQry.getKey()), FormDO::getKey, pageQry.getKey())
                .like(StringUtils.isNotBlank(pageQry.getName()), FormDO::getName, pageQry.getName());
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
}
