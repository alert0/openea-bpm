package org.openbpm.bpm.api.model.def;

import org.openbpm.base.api.model.IBaseModel;
import java.util.Date;

public interface IBpmDefinition extends IBaseModel {

    public static final class STATUS {
        public static final String DEPLOY = "deploy";
        public static final String DRAFT = "draft";
        public static final String FORBIDDEN = "forbidden";
    }

    String getActDefId();

    String getActDeployId();

    String getActModelId();

    String getCreateBy();

    String getCreateOrgId();

    Date getCreateTime();

    String getDefSetting();

    String getDesc();

    String getId();

    String getIsMain();

    String getKey();

    String getMainDefId();

    String getName();

    Integer getRev();

    String getStatus();

    Integer getSupportMobile();

    String getTypeId();

    String getUpdateBy();

    Date getUpdateTime();

    Integer getVersion();

    void setActDefId(String str);

    void setActDeployId(String str);

    void setActModelId(String str);

    void setCreateBy(String str);

    void setCreateOrgId(String str);

    void setCreateTime(Date date);

    void setIsMain(String str);

    void setMainDefId(String str);

    void setUpdateBy(String str);

    void setUpdateTime(Date date);

    void setVersion(Integer num);
}
