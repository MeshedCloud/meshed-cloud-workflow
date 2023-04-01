package cn.meshed.cloud.workflow.domain.engine;

import cn.hutool.json.JSONUtil;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.IdUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.CustomProperty;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.ExtensionAttribute;
import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.ParallelGateway;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
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
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.SVG_FILE_SUFFIX;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Data
public class CreateDeployment implements Serializable {

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
        //解析节点
        List<FlowElement> elements = parsingElements();
        AssertUtils.isTrue(CollectionUtils.isNotEmpty(elements), "设计不存在任何节点无效发布");
        //检测合法性
        validityVerification(elements);
        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.addProcess(getProcess(elements));
        //自动布局
        BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(bpmnModel);
        bpmnAutoLayout.setTaskHeight(120);
        bpmnAutoLayout.setTaskWidth(120);
        bpmnAutoLayout.execute();
        return bpmnModel;
    }

    private Process getProcess(List<FlowElement> elements) {
        Process process = new Process();
        process.setId(this.key);
        process.setName(this.name);
        elements.forEach(process::addFlowElement);
        return process;
    }

    private void validityVerification(List<FlowElement> elements) {
        long startCount = elements.stream().filter(flowElement -> flowElement instanceof StartEvent).count();
        AssertUtils.isTrue(startCount == 1, "开始节点数仅支持一个");
        long endCount = elements.stream().filter(flowElement -> flowElement instanceof StartEvent).count();
        AssertUtils.isTrue(endCount == 1, "结束节点数仅支持一个");
    }

    private List<FlowElement> parsingElements() {
        Map<String, Object> map = JSONUtil.toBean(this.json, Map.class);
        //处理节点
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) map.get("nodes");
        List<FlowElement> elements = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(nodes)) {
            nodes.forEach(node -> {
                String nodeType = getNodeString(node, "renderKey");
                switch (Objects.requireNonNull(nodeType)) {
                    case "startEvent":
                        elements.add(getStartEvent(node));
                        break;
                    case "endEvent":
                        elements.add(getEndEvent(node));
                        break;
                    case "userTask":
                        elements.add(getUserTask(node));
                        break;
                    case "initiatorEvent":
                        elements.add(getInitiatorEvent(node));
                        break;
                    case "exclusiveGateway":
                        elements.add(getExclusiveGateway(node));
                        break;
                    case "parallelGateway":
                        elements.add(getParallelGateway(node));
                        break;
                    default:
                }
            });

        }
        //处理边
        List<Map<String, Object>> edges = (List<Map<String, Object>>) map.get("edges");
        if (CollectionUtils.isNotEmpty(edges)) {
            edges.forEach(edge -> {
                elements.add(getSequenceFlow(edge));
            });
        }
        return elements;
    }

    private SequenceFlow getSequenceFlow(Map<String, Object> edge) {
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId("sid-"+IdUtils.randomUUID());
        sequenceFlow.setSourceRef(getMapInnerValue(edge, "source", "cell"));
        sequenceFlow.setTargetRef(getMapInnerValue(edge, "target", "cell"));
        sequenceFlow.setConditionExpression(getNodeString(edge, "conditionExpression"));
        AssertUtils.isTrue(StringUtils.isNotBlank(sequenceFlow.getSourceRef()),"流程连线缺少数据");
        AssertUtils.isTrue(StringUtils.isNotBlank(sequenceFlow.getTargetRef()),"流程连线缺少数据");
        return sequenceFlow;
    }

    private String getMapInnerValue(Map<String, Object> edge, String key, String innerKey) {
        Map<String, Object> source = (Map<String, Object>) edge.get(key);
        if (source != null) {
            return (String) source.get(innerKey);
        }
        return null;
    }

    /**
     * 得到发起人事件节点模型
     * 本质是用户节点
     *
     * @param node 节点原始数据
     * @return
     */
    private UserTask getInitiatorEvent(Map<String, Object> node) {
        UserTask userTask = new UserTask();
        userTask.setId(getNodeString(node, "id"));
        userTask.setName(getNodeString(node, "label"));
        userTask.setAssignee("${INITIATOR}");
        //todo 后期调整为监听完成任务
        Boolean autoComplete = (Boolean) node.get("autoComplete");
        if (autoComplete){
            userTask.setSkipExpression("${true}");
        }
        return userTask;
    }

    private UserTask getUserTask(Map<String, Object> node) {
        UserTask userTask = new UserTask();
        userTask.setId(getNodeString(node, "id"));
        userTask.setName(getNodeString(node, "label"));
        String userType = getNodeString(node, "userType");
        String dataType = getNodeString(node, "dataType");

        if ("assignee".equals(userType)) {
            if ("dynamic".equals(dataType)){
                userTask.setAssignee(getNodeString(node, "dynamicExpression"));
            } else {
                userTask.setAssignee(getNodeString(node, "assignee"));
            }
        } else if ("candidateUsers".equals(userType)) {
            if ("dynamic".equals(dataType)){
                userTask.setCandidateUsers(Collections.singletonList(getNodeString(node, "dynamicExpression")));
            } else {
                userTask.setCandidateUsers(getNodeList(node, "candidateUsers"));
            }

        } else if ("candidateGroups".equals(userType)) {
            if ("dynamic".equals(dataType)){
                userTask.setCandidateGroups(Collections.singletonList(getNodeString(node, "dynamicExpression")));
            } else {
                userTask.setCandidateGroups(getNodeList(node, "candidateGroups"));
            }
        }
        userTask.setSkipExpression(getNodeString(node, "skipExpression"));
        return userTask;
    }

    private EndEvent getEndEvent(Map<String, Object> node) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(getNodeString(node, "id"));
        endEvent.setName(getNodeString(node, "label"));
        return endEvent;
    }

    private ExclusiveGateway getExclusiveGateway(Map<String, Object> node) {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(getNodeString(node, "id"));
        exclusiveGateway.setName(getNodeString(node, "label"));
        return exclusiveGateway;
    }

    private ParallelGateway getParallelGateway(Map<String, Object> node) {
        ParallelGateway parallelGateway = new ParallelGateway();
        parallelGateway.setId(getNodeString(node, "id"));
        parallelGateway.setName(getNodeString(node, "label"));
        return parallelGateway;
    }

    private StartEvent getStartEvent(Map<String, Object> node) {
        StartEvent startEvent = new StartEvent();
        startEvent.setFormKey(getNodeString(node, "formKey"));
        startEvent.setId(getNodeString(node, "id"));
        startEvent.setInitiator("INITIATOR");
        startEvent.setName(getNodeString(node, "label"));
        return startEvent;
    }

    private String getNodeString(Map<String, Object> node, String key) {
        String str = (String) node.get(key);
        return StringUtils.isNotBlank(str) ? str : null;
    }

    private List<String> getNodeList(Map<String, Object> node, String key) {
        return (List<String>) node.get(key);
    }

}
