package org.openbpm.bus.executor.parseval;

import org.springframework.stereotype.Service;

import org.openbpm.base.core.util.StringUtil;
import org.openbpm.bus.model.BusinessColumn;

/**
 * <pre>
 * 描述：formDefData.data的值按照字段类型转成businessData.data所需的
 * </pre>
 */
@Service
public class ParseValTypeExecutor extends ParseValExecuteChain {

	@Override
	public int getSn() {
		return 0;
	}

	@Override
	protected void run(ParseValParam param) {
		String key = param.getKey();
		Object value = param.getValue();

		if (value == null||StringUtil.isEmpty(value.toString())) {
			return;
		}

		BusinessColumn column = param.getBusTableRel().getTable().getColumnByKey(key);
		if(column == null) {
			param.getData().put(key,value);
			return;
		}
		
		param.getData().put(column.getKey(), column.value(value.toString()));

	}

}
