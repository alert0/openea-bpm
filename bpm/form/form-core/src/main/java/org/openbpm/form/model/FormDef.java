package org.openbpm.form.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import org.openbpm.base.core.model.BaseModel;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.form.api.model.IFormDef;

/**
 * <pre>
 * 描述：表单对象
 * </pre>
 */
@Data
public class FormDef extends BaseModel implements IFormDef {
	/**
	 * 表单类型 FormType
	 */
	private String type;
    /**
     * key
     */
    @NotEmpty
    private String key;
    /**
     * 名字
     */
    @NotEmpty
    private String name;
    /**
     * 描述
     */
    private String desc;
    /**
     * 分组id
     */
    private String groupId;
    /**
     * 分组名称
     */
    private String groupName;
    /**
     * 业务对象key
     */
    private String boKey;
    /**
     * 业务对象名称
     */
    private String boName;
    /**
     * <pre>
     * 表单内容
     * </pre>
     */
    private String html;
    


    /* (non-Javadoc)
	 * @see org.openbpm.form.model.IFormDef#getHtml()
	 */
    @Override
	public String getHtml() {
    	if(StringUtil.isNotEmpty(html)) { 
    		String content = html.replaceAll("&apos;", "'").replaceAll("&#39;", "'")
    							.replaceAll("#ctx#", "ctx");
    		return content;
    	}//&#39;
    	
        return html;
    }

}
