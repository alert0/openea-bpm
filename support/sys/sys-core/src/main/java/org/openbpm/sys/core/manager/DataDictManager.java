package org.openbpm.sys.core.manager;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import org.openbpm.base.manager.Manager;
import org.openbpm.sys.core.model.DataDict;

/**
 * 数据字典 Manager处理接口
 */
public interface DataDictManager extends Manager<String, DataDict>{

	List<DataDict> getDictNodeList(String dictKey, Boolean hasRoot);

	JSONArray getDictTree();
	
}
