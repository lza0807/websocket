package com.lza.websocket.service.impl;

import com.lza.websocket.entity.Message;
import com.lza.websocket.mapper.MessageMapper;
import com.lza.websocket.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lza
 * @since 2020-07-16
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
