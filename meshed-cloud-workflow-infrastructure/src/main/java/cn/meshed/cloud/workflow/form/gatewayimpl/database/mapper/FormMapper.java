package cn.meshed.cloud.workflow.form.gatewayimpl.database.mapper;

import cn.meshed.cloud.workflow.form.enums.FormStatusEnum;
import cn.meshed.cloud.workflow.form.gatewayimpl.database.dataobject.FormDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author by Vincent Vic
 * @since 2023-03-25
 */
public interface FormMapper extends BaseMapper<FormDO> {

    /**
     * 查询表单最新版本选项
     *
     * @return
     */
    List<FormDO> select();
}
