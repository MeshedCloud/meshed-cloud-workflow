package cn.meshed.cloud.workflow;

import com.alibaba.cola.dto.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RestController
public class TestWebHook {

    @PostMapping("/webhook")
    public Response webhook(@RequestBody LinkedHashMap<String, String> params){
        System.out.println("webhook => " + params);
        return Response.buildSuccess();
    }
}
