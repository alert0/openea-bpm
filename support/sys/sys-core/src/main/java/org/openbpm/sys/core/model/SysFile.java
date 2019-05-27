package org.openbpm.sys.core.model;

import lombok.Data;
import org.openbpm.base.core.model.BaseModel;

/**
 * <pre>
 * 描述：系统附件信息
 * </pre>
 */

@Data
public class SysFile extends BaseModel {
	/**
	 * 附件名
	 */
	private String name;
	/**
	 * <pre>
	 * 这附件用的是上传器
	 * 具体类型可以看 IUploader 的实现类
	 * </pre>
	 */
	private String uploader;
	/**
	 * <pre>
	 * 路径，这个路径能从上传器中获取到对应的附件内容
	 * 所以也不一定是路径，根据不同上传器会有不同值
	 * </pre>
	 */
	private String path;


}
