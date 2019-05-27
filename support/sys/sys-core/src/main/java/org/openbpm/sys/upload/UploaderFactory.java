package org.openbpm.sys.upload;

import java.util.Map;
import java.util.Map.Entry;

import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.AppUtil;
import org.openbpm.base.core.util.PropertyUtil;

/**
 * <pre>
 * 描述：上传器工厂
 * </pre>
 */
public class UploaderFactory {
	private UploaderFactory() {

	}

	/**
	 * <pre>
	 * 获取上传器
	 * </pre>
	 * 
	 * @param type
	 * @return
	 */
	public static IUploader getUploader(String type) {
		Map<String, IUploader> map = AppUtil.getImplInstance(IUploader.class);
		for (Entry<String, IUploader> entry : map.entrySet()) {
			if (entry.getValue().type().equals(type)) {
				return entry.getValue();
			}
		}
		throw new BusinessException(String.format("找不到类型[%s]的上传器的实现类", type));
	}

	/**
	 * <pre>
	 * 返回默认的上传器
	 * </pre>
	 * 
	 * @return
	 */
	public static IUploader getDefault() {
		return getUploader(PropertyUtil.getProperty("uploader.default"));
	}
}
