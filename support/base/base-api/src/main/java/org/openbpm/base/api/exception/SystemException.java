package org.openbpm.base.api.exception;

import org.openbpm.base.api.constant.BaseStatusCode;
import org.openbpm.base.api.constant.IStatusCode;

/**
 *  TODO delete
 *  
 */
public class SystemException extends RuntimeException {
    private static final long serialVersionUID = -7289238698048230824L;
    public IStatusCode statusCode = BaseStatusCode.SYSTEM_ERROR;

    public SystemException(String msg) {
        super(msg);
    }

    public SystemException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public SystemException(Throwable throwable) {
        super(throwable);
    }

    public SystemException(String msg, IStatusCode errorCode) {
        super(msg);
        this.statusCode = errorCode;
    }

    public SystemException(IStatusCode errorCode) {
        super(errorCode.getDesc());
        this.statusCode = errorCode;
    }

    public SystemException(IStatusCode errorCode, Throwable throwable) {
        super(errorCode.getDesc(), throwable);
        this.statusCode = errorCode;
    }

    public SystemException( String msg,IStatusCode errorCode, Throwable throwable) {
        super(errorCode.getDesc() + ":" + msg, throwable);
        this.statusCode = errorCode;
    }

    public String getStatuscode() {
        if (statusCode == null) return "";
        return statusCode.getCode();
    }


    public IStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(IStatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
