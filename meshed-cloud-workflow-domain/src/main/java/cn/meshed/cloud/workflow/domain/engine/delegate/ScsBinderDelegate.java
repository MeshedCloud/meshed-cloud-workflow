package cn.meshed.cloud.workflow.domain.engine.delegate;

import cn.hutool.json.JSONUtil;
import cn.meshed.cloud.stream.StreamBridgeSender;
import cn.meshed.cloud.workflow.domain.engine.ExpressionConverter;
import com.alibaba.cola.domain.DomainFactory;
import com.alibaba.cola.exception.SysException;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.List;
import java.util.Map;

import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SCS_BINDING;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SCS_BODY;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SCS_NAMESPACE;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public class ScsBinderDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        StreamBridgeSender sender = DomainFactory.create(StreamBridgeSender.class);
        ServiceTask serviceTask = (ServiceTask) execution.getCurrentFlowElement();
        Map<String, List<ExtensionElement>> extensionElements = serviceTask.getExtensionElements();
        List<ExtensionElement> bindingElements = extensionElements.get(NODE_SCS_BINDING);
        List<ExtensionElement> bodyElements = extensionElements.get(NODE_SCS_BODY);
        String binding = bindingElements.get(0).getElementText();
        String body = bodyElements.get(0).getElementText();
        String message = ExpressionConverter.converter(execution.getVariables(), body);
        if (StringUtils.isNotBlank(message)){
            sender.send(binding,message);
        } else {
            throw new SysException("消息丢失无法正常产生事件");
        }

    }


}
