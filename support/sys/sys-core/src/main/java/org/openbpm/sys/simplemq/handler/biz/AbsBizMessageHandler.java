package org.openbpm.sys.simplemq.handler.biz;

import org.openbpm.sys.api.jms.JmsHandler;

import java.io.Serializable;
/**
 * 做公共逻辑,如日志等
 *
 */
public abstract class AbsBizMessageHandler<T extends Serializable> implements JmsHandler<T> {

}
