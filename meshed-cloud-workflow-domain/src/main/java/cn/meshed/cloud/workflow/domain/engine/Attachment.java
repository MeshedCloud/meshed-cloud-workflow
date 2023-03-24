package cn.meshed.cloud.workflow.domain.engine;

import lombok.Data;

import java.util.Date;

/**
 * <h1>附件VO</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Data
public class Attachment {

    /**
     * unique id for this attachment
     */
    private String id;

    /**
     * free user defined short (max 255 chars) name for this attachment
     */
    private String name;

    /**
     * long (max 255 chars) explanation what this attachment is about in context of the task and/or process instance it's linked to.
     */
    private String description;

    /**
     * indication of the type of content that this attachment refers to. Can be mime type or any other indication.
     */
    private String type;

    /**
     * reference to the task to which this attachment is associated.
     */
    private String taskId;

    /**
     * reference to the process instance to which this attachment is associated.
     */
    private String processInstanceId;

    /**
     * the remote URL in case this is remote content. If the attachment content was uploaded with an
     * input stream}, then this method returns null and the content can be fetched with.
     */
    private String url;

    /**
     * reference to the user who created this attachment.
     */
    private String userId;

    /**
     * timestamp when this attachment was created
     */
    private Date time;


}
