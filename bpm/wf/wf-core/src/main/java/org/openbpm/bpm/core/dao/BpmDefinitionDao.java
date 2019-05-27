package org.openbpm.bpm.core.dao;

import org.openbpm.base.api.query.QueryFilter;
import org.openbpm.base.dao.BaseDao;
import org.openbpm.bpm.core.model.BpmDefinition;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

@MapperScan
public interface BpmDefinitionDao extends BaseDao<String, BpmDefinition> {
    BpmDefinition getByActDefId(String str);

    List<BpmDefinition> getByKey(String str);

    List<BpmDefinition> getDefByActModelId(String str);

    BpmDefinition getMainByDefKey(String str);

    List<BpmDefinition> getMyDefinitionList(QueryFilter queryFilter);

    void updateActResourceEntity(@Param("deploymentId") String str, @Param("resName") String str2, @Param("bpmnBytes") byte[] bArr);

    void updateToMain(String str);
}
