package org.openbpm.form.generator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.openbpm.base.core.util.ThreadMapUtil;
import org.openbpm.bus.api.constant.BusColumnCtrlType;
import org.openbpm.bus.api.constant.BusTableRelType;
import org.openbpm.bus.api.model.IBusTableRel;
import org.openbpm.bus.api.model.IBusinessColumn;
/**
 * 自定义表单控件生成器父类，提供生成表单通用公共方法<br>
 */
public abstract class AbsFormElementGenerator {
	public abstract String getGeneratorName();
	
	/**
	 * 获取字段的html内容
	 * @param column
	 * @param relation
	 * @return HTML
	 */
	public String getColumn(IBusinessColumn column,IBusTableRel relation) {
		BusColumnCtrlType columnType = BusColumnCtrlType.getByKey(column.getCtrl().getType());
		String boCode = relation.getBusObj().getKey();
		ThreadMapUtil.put("boCode", boCode);
		ThreadMapUtil.put("relation", relation);
		
		switch (columnType) {
		
			case ONETEXT: return getColumnOnetext(column);
			
			case DATE: return getColumnDate(column);
			
			case DIC: return getColumnDic(column);
			
			case SERIALNO: return getColumnIdentity(column);
			
			case MULTITEXT: return getColumnMultitext(column);
			
			case CHECKBOX: return getColumnCheckBox(column);
				
			//case MULTISELECT: return getColumnSelect(column,true);	
			
			case RADIO: return getColumnRadio(column);	
			
			case SELECT: return getColumnSelect(column,false);	
			
			case FILE: return getColumnFile(column);
			
			default: return "";
		
		}
		
	}

	protected abstract String getColumnOnetext(IBusinessColumn column);
	
	protected abstract String getColumnDate(IBusinessColumn column);
	
	protected abstract String getColumnDic(IBusinessColumn column);
	
	protected abstract String getColumnIdentity(IBusinessColumn column);
	
	protected abstract String getColumnMultitext(IBusinessColumn column);
	
	protected abstract String getColumnCheckBox(IBusinessColumn column);
	
	protected abstract String getColumnRadio(IBusinessColumn column);
	
	protected abstract String getColumnSelect(IBusinessColumn column,Boolean isMultiple);
	
	protected abstract String getColumnFile(IBusinessColumn column);
	
	/**
	 * 构建一个 element
	 * @param type
	 * @return
	 */
	protected Element getElement(String type) {
		Document doc = Jsoup.parse("");
		Element element = doc.createElement(type);
		return element;
	}
	/**
	 * 权限指令
	 * @param element
	 * @param column
	 */
	protected void handlePermission(Element element,IBusinessColumn column) {
		element.attr("ab-basic-permission", getPermissionPath(column));
		element.attr("desc", column.getComment());
	}
	
	public String getPermissionPath(IBusinessColumn column,IBusTableRel relation) {
		String boCode = relation.getBusObj().getKey();
		return "permission."+ boCode + "." + column.getTable().getKey() + "." + column.getKey();
	}
	
	/**
	 * 权限路径
	 * @param column
	 * @return
	 */
	protected String getPermissionPath(IBusinessColumn column) {
		String boCode = (String) ThreadMapUtil.get("boCode");
		return "permission."+ boCode + "." + column.getTable().getKey() + "." + column.getKey();
	}
	
	/**
	 * 校验指令
	 * @param element
	 * @param column
	 */
	protected void handleValidateRules(Element element,IBusinessColumn column) {
		String rulesStr = column.getCtrl().getValidRule();
		if(StringUtils.isEmpty(rulesStr)) return ;
		
		JSONArray rules = JSONArray.parseArray(rulesStr);
		//[{"name":"time","title":"时间"},{"name":"required","title":"必填"}]
		// to {time:true,required:true}
		JSONObject validateRule = new JSONObject();
		for (int i = 0; i < rules.size(); i++) {
			JSONObject rule = rules.getJSONObject(i);
			
			validateRule.put(rule.getString("name"), true);
		}
		
		if (column.isRequired()) {
			validateRule.put("required", true);
		}
		IBusTableRel relation = (IBusTableRel) ThreadMapUtil.get("relation");
		element.attr("ab-validate", validateRule.toJSONString());
		//为了validateRule提示
		element.attr("desc",relation.getTableComment() + "-" + column.getComment());
	}
	
	
	/**
	 * <pre>
	 * 获取子表的路径
	 * 一直向上递归、若上级为主表、或者一对多的子表则停止。
	 * eg: mian.subaList[1].subbList[1].name  那subb的path为 subbList
	 * eg: mian.suba.subbList[1].name 那subb的path 为 main.suba.subbList
	 * eg: main.subaList[1].subb.name 那 subb的path 为 suba.subb.name
	 * eg: main.suba.subb.name 那subb的path为 mian.suba.subb.name
	 * 子表会存在单独作用域所以查询到子表那里即可
	 * </pre>
	 * @param relation
	 * @return
	 */
	public String getScopePath(IBusTableRel relation) {
		if(relation.getType().equals(BusTableRelType.MAIN.getKey())) {
			return "data."+relation.getBusObj().getKey();
		}
		
		StringBuilder sb = new StringBuilder();
		// 一对一是对象名字 
		sb.append(relation.getTableKey());
		// 如果是一对多则添加List
		if(relation.getType().equals(BusTableRelType.ONE_TO_MANY.getKey())){
			sb.append("List");
			//第三层后面均为表单模板，模板会从subTempData中获取中间值。
			if(isThreeChildren(relation)) {
				sb.insert(0, "subTempData.");
				return sb.toString();
			}
		}
		
		
		getParentPath(relation.getParent(),sb);
		
		return sb.toString();
	}
	
	protected void getParentPath(IBusTableRel parent,StringBuilder sb) {
		if(parent == null) return;
		//上级是一对多则将scope的name 返回
		if(parent.getType().equals(BusTableRelType.ONE_TO_MANY.getKey())) {
			sb.insert(0, parent.getTableKey()+".");
			return ;
		}
		//一对一子表
		if(parent.getType().equals(BusTableRelType.ONE_TO_ONE.getKey())) {
			sb.insert(0, parent.getTableKey()+".");
		}
		// 主表则是boCode
		if(parent.getType().equals(BusTableRelType.MAIN.getKey())) {
			sb.insert(0, "data."+parent.getBusObj().getKey()+".");
		}
		
		getParentPath(parent.getParent(), sb);
	}
	
	// id="boCode-tableKey" ab-basic-permission="boCode.table.tableKey" ...
	public abstract String getSubAttrs(IBusTableRel rel) ;
	
	//如果父类不是主表、那么当前子表则一定是第三层
	public boolean isThreeChildren(IBusTableRel rel) {
		if(rel.getType().equals(BusTableRelType.ONE_TO_MANY.getKey()) 
				&& !rel.getParent().getType().equals(BusTableRelType.MAIN.getKey())) {
			return true;
		}
		return false;
	}
}
