package cn.meshed.cloud.workflow.domain.engine;

import com.alibaba.cola.exception.SysException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.flowable.common.engine.impl.de.odysseus.el.ExpressionFactoryImpl;
import org.flowable.common.engine.impl.de.odysseus.el.util.SimpleContext;
import org.flowable.common.engine.impl.javax.el.ExpressionFactory;
import org.flowable.common.engine.impl.javax.el.PropertyNotFoundException;
import org.flowable.common.engine.impl.javax.el.ValueExpression;

import java.util.HashMap;
import java.util.Map;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Slf4j
public class ExpressionConverter {

    /**
     * 原生的解析表达式
     *
     * @param params 变量的值
     * @param exp    表达式
     * @return
     */
    public static  String converter(Map<String, Object> params, String exp) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        if (MapUtils.isNotEmpty(params)){
            params.forEach((k, v) -> {
                if (v instanceof ObjectNode){
                    JSONObject jsonObject = JSONObject.parseObject(v.toString());
                    Map<String, Object> vs = new HashMap<>();
                    for (String objkey : jsonObject.keySet()) {
                        vs.put(objkey, jsonObject.get(objkey));
                    }
                    context.setVariable(k, factory.createValueExpression(vs, Map.class));
                } else {
                    context.setVariable(k, factory.createValueExpression(v, Object.class));
                }
            });
        }
        try {
            ValueExpression e = factory.createValueExpression(context, exp, String.class);
            return (String) e.getValue(context);
        } catch (PropertyNotFoundException e) {
            log.error("流程变量的属性找不到，请确认!", e);
            throw new SysException("流程变量的属性找不到，请确认!", e);
        }
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("xx","xxx");
        ExpressionConverter.converter(map,"${xx}");
    }


}
