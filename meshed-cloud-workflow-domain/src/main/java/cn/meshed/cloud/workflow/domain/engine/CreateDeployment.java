package cn.meshed.cloud.workflow.domain.engine;

import cn.hutool.json.JSONUtil;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.IdUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.bpmn.model.FieldExtension;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.ParallelGateway;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.BPMN_FILE_SUFFIX;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.CELL;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.CONTENT_TYPE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.CONTENT_TYPE_APPLICATION_FORM;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.END_EVENT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.EXCLUSIVE_GATEWAY;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.EXPRESSION;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.HEADER_FORMAT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.INITIATOR;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.INITIATOR_EL;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.INITIATOR_EVENT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.MAIL_EVENT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_ASSIGNEE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_AUTO_COMPLETE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_CANDIDATE_GROUPS;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_CANDIDATE_USERS;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_CLASS;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_CONDITION_EXPRESSION;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_DATA_TYPE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_DYNAMIC;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_DYNAMIC_EXPRESSION;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_EDGES;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_ID;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_LABEL;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_MAIL_BCC;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_MAIL_CC;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_MAIL_HTML_VAR;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_MAIL_SUBJECT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_MAIL_TO;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_RENDER_KEY;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_BODY;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_CONTENT_TYPE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_FAIL_CODES;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_HANDLE_CODES;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_HEADERS;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_METHOD;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_TIMEOUT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_URL;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SCS_BINDING;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SCS_BODY;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SCS_NAMESPACE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SKIP_EXPRESSION;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SOURCE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_TARGET;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_USER_TYPE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.PARALLEL_GATEWAY;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.SCS_BINDER_DELEGATE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.SCS_BINDER_EVENT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.SID_FORMAT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.START_EVENT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.STRING;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.SVG_FILE_SUFFIX;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.TRUE_EL;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.USER_TASK;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.WEB_HOOK_EVENT;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = false)
@Slf4j
@Data
public class CreateDeployment extends AbstractFlowElementHandle {

    private String name;
    private String key;
    /**
     * 归属系统
     */
    private String tenantId;
    @Setter(AccessLevel.NONE)
    private String category;
    private InputStream xmlIn;
    private InputStream svgIn;

    private String json;

    public CreateDeployment() {
        this.name = IdUtils.simpleUUID();
    }

    public void setCategory(String category) {
        if (StringUtils.isNotBlank(category)) {
            this.category = category;
        } else {
            this.category = "unknown";
        }

    }

    /**
     * 获取BPMN文件后缀
     */
    public String getBpmnName() {
        return name + BPMN_FILE_SUFFIX;
    }

    /**
     * 获取SVG文件后缀
     */
    public String getSvgName() {
        return name + SVG_FILE_SUFFIX;
    }

    /**
     * 设置XML
     *
     * @param xml xml字符串内容
     */
    public void setXml(String xml) {
        AssertUtils.isTrue(StringUtils.isNotBlank(xml), "设置内容不能为空");
        this.xmlIn = new ByteArrayInputStream(xml.getBytes());
    }

    /**
     * 设置XML
     *
     * @param xml xml文件
     */
    public void setXml(File xml) throws IOException {
        AssertUtils.isTrue(xml != null, "设置文件不能为空");
        assert xml != null;
        this.xmlIn = Files.newInputStream(xml.toPath());
    }

    /**
     * 设置SVG
     *
     * @param svg svg字符串内容
     */
    public void setSvg(String svg) {
        AssertUtils.isTrue(StringUtils.isNotBlank(svg), "设置内容不能为空");
        this.svgIn = new ByteArrayInputStream(svg.getBytes());
    }

    /**
     * 设置SVG
     *
     * @param svg svg文件
     */
    public void setSvg(File svg) throws IOException {
        AssertUtils.isTrue(svg != null, "设置文件不能为空");
        assert svg != null;
        this.svgIn = Files.newInputStream(svg.toPath());
    }

    /**
     * 获取定义模型
     *
     * @return {@link BpmnModel}
     */
    public boolean hasBpmnModel() {
        return StringUtils.isNotBlank(this.json);
    }

    /**
     * 获取定义模型
     *
     * @return {@link BpmnModel}
     */
    public BpmnModel getBpmnModel() {
        AssertUtils.isTrue(StringUtils.isNotBlank(this.json), "模型注册必须存在数据库");
        log.info("设计图 => {}", this.json);
        JsonToBpmnModel jsonToBpmnModel = new JsonToBpmnModel(this.name, this.key, this.json);
        return jsonToBpmnModel.getBpmnModel();
    }

}
