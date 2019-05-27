package org.openbpm.bpm.api.exception;

import org.openbpm.base.api.constant.IStatusCode;

public enum BpmStatusCode implements IStatusCode {
    SUCCESS("200", "成功"),
    SYSTEM_ERROR("500", "系统异常"),
    TIMEOUT("401", "访问超时"),
    NO_ACCESS("403", "访问受限"),
    PARAM_ILLEGAL("100", "参数校验不通过"),
    ACTIONCMD_ERROR("20000", "线程ActionCmd 数据异常"),
    NO_PERMISSION("20001", "没有操作权限！"),
    DEF_FORBIDDEN("30000", "流程定义被禁用"),
    DEF_LOST("30001", "流程定义丢失"),
    TASK_NOT_FOUND("30001", "查找不到当前任务，任务可能已经被处理完成"),
    NO_TASK_USER("30002", "任务候选执行人为空"),
    USER_CALC_ERROR("30003", "人员计算出现异常"),
    NO_TASK_ACTION("30004", "任务处理ACTION查找不到"),
    HANDLER_ERROR("30005", "执行URL表单处理器处理器失败，请检查"),
    TASK_ACTION_BTN_ERROR("30006", "任务处理器生成按钮异常"),
    VARIABLES_NO_SYNC("30007", "流程变量尚未同步,不应该存在的获取时机"),
    NO_ASSIGN_USER("30008", "任务尚未分配候选人"),
    NO_BACK_TARGET("30101", "驳回节点未知"),
    CANNOT_BACK_NODE("30102", "不支持的驳回节点"),
    PLUGIN_ERROR("31000", "执行插件异常"),
    PLUGIN_USERCALC_RULE_CONDITION_ERROR("31100", "用户计算插件前置条件解析异常"),
    GATEWAY_ERROR("30051", "网关分支判断脚本异常"),
    PARSER_FLOW_ERROR("30601", "流程解析器异常"),
    PARSER_NODE_ERROR("30602", "流程节点解析器异常"),
    FLOW_DATA_GET_BUTTONS_ERROR("30701", "获取流程按钮失败"),
    FLOW_DATA_EXECUTE_SHOWSCRIPT_ERROR("30702", "执行初始化脚本失败"),
    FLOW_BUS_DATA_PK_LOSE("40101", "流程数据保存异常主键缺失"),
    FLOW_FORM_LOSE("50101", "流程配置的表单查找不到"),
    FLOW_BUS_DATA_LOSE("50101", "流程关联的业务数据丢失"),
    ERROR_UNKNOWN("30100", "未知异常");
    
    private String code;
    private String desc;
    private String system;

    private BpmStatusCode(String code2, String description) {
        setCode(code2);
        setDesc(description);
        setSystem("BASE");
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code2) {
        this.code = code2;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String msg) {
        this.desc = msg;
    }

    public String getSystem() {
        return this.system;
    }

    public void setSystem(String system2) {
        this.system = system2;
    }
}
