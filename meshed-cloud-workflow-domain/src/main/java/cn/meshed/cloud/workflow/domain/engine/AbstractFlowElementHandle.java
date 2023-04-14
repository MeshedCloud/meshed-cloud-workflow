package cn.meshed.cloud.workflow.domain.engine;

import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.bpmn.model.FieldExtension;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.ServiceTask;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.EXPRESSION;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_CLASS;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_ID;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_LABEL;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SCS_NAMESPACE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SKIP_EXPRESSION;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.STRING;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public abstract class AbstractFlowElementHandle {

    protected void addExtensionElement(FlowElement flowElement, String namespace, String name, String value) {
        if (StringUtils.isNotBlank(value)){
            flowElement.addExtensionElement(getExtensionElement(namespace, name, value));
        }
    }

    protected ExtensionElement getExtensionElement(String namespace, String name, String value) {
        ExtensionElement extensionElement = new ExtensionElement();
        extensionElement.setName(name);
        extensionElement.setNamespace(namespace);
        extensionElement.setNamespacePrefix(NODE_SCS_NAMESPACE);
        extensionElement.setElementText(value);

        return extensionElement;
    }

    /**
     * 添加字段扩展信息 表达式类型
     *
     * @param fieldExtensions 字段扩展列表
     * @param node            节点
     * @param key             key
     */
    protected void addFieldExpression(List<FieldExtension> fieldExtensions, Map<String, Object> node, String key) {
        addField(fieldExtensions, node, key, EXPRESSION);
    }

    /**
     * 添加字段扩展信息 字符串类型
     *
     * @param fieldExtensions 字段扩展列表
     * @param node            节点
     * @param key             key
     */
    protected void addFieldString(List<FieldExtension> fieldExtensions, Map<String, Object> node, String key) {
        addField(fieldExtensions, node, key, STRING);
    }

    /**
     * 服务
     *
     * @param node
     * @return
     */
    protected ServiceTask getServiceTask(Map<String, Object> node) {
        ServiceTask serviceTask = new ServiceTask();
        serviceTask.setSkipExpression(getNodeString(node, NODE_SKIP_EXPRESSION));
        serviceTask.setId(getNodeString(node, NODE_ID));
        serviceTask.setName(getNodeString(node, NODE_LABEL));
        return serviceTask;
    }

    /**
     * 添加字段扩展信息
     *
     * @param fieldExtensions 字段扩展列表
     * @param node            节点
     * @param key             key
     * @param type            类型
     */
    protected void addField(List<FieldExtension> fieldExtensions, Map<String, Object> node, String key, String type) {
        FieldExtension fieldExtension = new FieldExtension();
        String value = getNodeString(node, key);
        if (StringUtils.isNotBlank(value)) {
            fieldExtension.setFieldName(key);
            if (STRING.equals(type)) {
                fieldExtension.setStringValue(value);
            } else if (EXPRESSION.equals(type)) {
                fieldExtension.setExpression(value);
            }
            fieldExtensions.add(fieldExtension);
        }
    }

    protected void setServiceListener(ServiceTask serviceTask, String event, String implementation) {
        FlowableListener httpTaskListener = new FlowableListener();
        httpTaskListener.setEvent(event);
        httpTaskListener.setImplementationType(NODE_CLASS);
        httpTaskListener.setImplementation(implementation);
        serviceTask.setExecutionListeners(Collections.singletonList(httpTaskListener));
    }

    protected List<String> getNodeList(Map<String, Object> node, String key) {
        return (List<String>) node.get(key);
    }


    protected String getNodeString(Map<String, Object> node, String key) {
        String str = (String) node.get(key);
        return StringUtils.isNotBlank(str) ? str : null;
    }

}
