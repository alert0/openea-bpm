package org.openbpm.sys.upload;

/**
 * <pre>
 * 描述：oss上传器
 * TODO
 * </pre>
 */
public abstract class OssUploader extends AbstractUploader {

	@Override
	public String type() {
		return "oss";
	}

}
