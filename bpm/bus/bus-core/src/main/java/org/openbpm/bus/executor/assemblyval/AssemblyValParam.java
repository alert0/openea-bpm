package org.openbpm.bus.executor.assemblyval;

import com.alibaba.fastjson.JSONObject;
import org.openbpm.bus.model.BusinessData;

/**
 * <pre>
 * 描述：
 * </pre>
 */
public class AssemblyValParam {
	private JSONObject data;
	private BusinessData businessData;

	public AssemblyValParam(JSONObject data, BusinessData businessData) {
		super();
		this.data = data;
		this.businessData = businessData;
	}

	public JSONObject getData() {
		return data;
	}

	public BusinessData getBusinessData() {
		return businessData;
	}

}
