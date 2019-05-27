package org.openbpm.sys.core.manager;

import java.io.InputStream;

import org.openbpm.base.manager.Manager;
import org.openbpm.sys.core.model.SysFile;

/**
 * 系统附件 Manager处理接口
 */
public interface SysFileManager extends Manager<String, SysFile>{
	
	/**
	 * <pre>
	 * 上传附件
	 * </pre>	
	 * @param is
	 * @param fileName
	 * @return
	 */
	SysFile upload(InputStream is, String fileName);
	
	/**
	 * <pre>
	 * 下载附件
	 * 返回流
	 * </pre>	
	 * @param fileId
	 * @return
	 */
	InputStream download(String fileId);
	
	/**
	 * <pre>
	 * 删除附件
	 * 包括流信息
	 * </pre>	
	 * @param fileId
	 */
	void delete(String fileId);
	
}
