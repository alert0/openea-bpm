package org.openea.bpm.adaptor.model;

import lombok.Data;
import org.openbpm.org.api.model.IGroup;

@Data
public class EapGroup implements IGroup {

    protected  String groupId;
    /**
     * 名字
     */
    protected  String groupName;
    /**
     * 父ID
     */
    protected  String parentId;
    /**
     * 编码
     */
    protected  String groupCode;
    /**
     * 类型
     */
    protected  String groupType;
    /**
     * 描述
     */
    protected  String desc;

}
