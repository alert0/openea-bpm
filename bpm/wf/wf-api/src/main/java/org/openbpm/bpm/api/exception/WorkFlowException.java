package org.openbpm.bpm.api.exception;

import org.openbpm.base.api.constant.IStatusCode;
import org.openbpm.base.api.exception.BusinessException;

public class WorkFlowException extends BusinessException {
    public WorkFlowException(String msg, IStatusCode errorCode) {
        super(msg, errorCode);
    }

    public WorkFlowException(IStatusCode errorCode) {
        super(errorCode);
    }

    public WorkFlowException(IStatusCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }
}
