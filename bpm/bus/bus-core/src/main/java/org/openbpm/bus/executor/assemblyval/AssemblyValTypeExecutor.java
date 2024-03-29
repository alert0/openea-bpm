package org.openbpm.bus.executor.assemblyval;

import java.util.Date;
import java.util.Map.Entry;

import org.openbpm.bus.model.BusinessData;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.api.constant.ColumnType;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.bus.model.BusinessColumn;

import cn.hutool.core.date.DateUtil;

/**
 * <pre>
 * 描述：businessData.data的值按照字段类型转成formDefData.data所需的
 * </pre>
 */
@Service
public class AssemblyValTypeExecutor extends AssemblyValExecuteChain {

	@Override
	public int getSn() {
		return 0;
	}

	@Override
	protected void run(AssemblyValParam param) {
		BusinessData businessData = param.getBusinessData();
		JSONObject data = param.getData();

		for (Entry<String, Object> entry : businessData.getData().entrySet()) {
			BusinessColumn column = businessData.getBusTableRel().getTable().getColumnByKey(entry.getKey());
			if(column == null) {
				throw new BusinessException("businessData中出现了非业务对象的字段，请核查："+entry.getKey());
			}
			// 目前就日期需要格式化成字符串到前端formDefData
			if (ColumnType.DATE.equalsWithKey(column.getType()) && entry.getValue() != null) {
				JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
				data.put(column.getKey(), DateUtil.format((Date) entry.getValue(), config.getString("format")));
			} else {
				data.put(entry.getKey(), entry.getValue());
			}
		}
	}

}
