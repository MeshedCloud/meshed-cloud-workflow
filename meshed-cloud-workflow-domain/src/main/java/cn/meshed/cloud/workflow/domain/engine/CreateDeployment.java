package cn.meshed.cloud.workflow.domain.engine;

import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.IdUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;

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
    @Setter(AccessLevel.NONE)
    private String category;
    private InputStream xmlIn;
    private InputStream svgIn;

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
}
