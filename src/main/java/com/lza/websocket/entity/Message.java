package com.lza.websocket.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lza
 * @since 2020-07-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Message对象", description="")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    private String accountId;

    @ApiModelProperty(value = "是否已读:0 否;1 是")
    private Boolean isRead;

    @ApiModelProperty(value = "是否推送:0 否;1 是")
    private Boolean isSend;

    @ApiModelProperty(value = "1.硬件预警 2.围栏预警 3.系统消息")
    private Integer msgType;

    @ApiModelProperty(value = "如果是硬件预警，填写alert_id")
    private Integer alertId;

    @ApiModelProperty(value = "如果是围栏报警，填写fence_alert_id")
    private Integer fenceAlertId;

    @ApiModelProperty(value = "消息内容")
    private String content;

    @ApiModelProperty(value = "消息生成时间")
    private Date createTime;


}
