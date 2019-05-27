package org.openbpm.sys.core.manager.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.openbpm.sys.core.dao.SysDataSourceDefDao;
import org.openbpm.sys.core.manager.SysDataSourceDefManager;
import org.openbpm.sys.core.model.SysDataSourceDef;
import org.openbpm.sys.core.model.def.SysDataSourceDefAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import org.openbpm.base.api.exception.BusinessException;
import org.openbpm.base.core.util.StringUtil;
import org.openbpm.base.manager.impl.BaseManager;

/**
 * @description 数据源模板 Manager处理实现类
 * @group dayDream
 * @email aschs@qq.com
 */
@Service
public class SysDataSourceDefManagerImpl extends BaseManager<String, SysDataSourceDef> implements SysDataSourceDefManager {
    @Autowired
    SysDataSourceDefDao sysDataSourceDefDao;

    @Override
    public List<SysDataSourceDefAttribute> initAttributes(String classPath) {
        try {
            List<SysDataSourceDefAttribute> attributes = new ArrayList<>();
            Class<?> cls = Class.forName(classPath);
            if (!DataSource.class.isAssignableFrom(cls)) {// 不是DataSource的子类就报错
                throw new Exception("classPath[" + classPath + "]不是javax.sql.DataSource的子类");
            }
            for (Method method : cls.getMethods()) {
                if (!method.getName().startsWith("set") || method.getParameters().length != 1) {
                    continue;
                }
                Parameter param = method.getParameters()[0];
                SysDataSourceDefAttribute attribute = new SysDataSourceDefAttribute();
                String fieldName = StringUtil.lowerFirst(method.getName().replace("set", ""));
                attribute.setComment(fieldName);
                attribute.setName(fieldName);
                attribute.setRequired(false);
                attribute.setType(param.getType().getName());
                attributes.add(attribute);
            }
            return attributes;
        } catch (Exception e) {
            throw new BusinessException("根据classPath[" + classPath + "]获取参数", e);
        }
    }

    public static void main(String[] args) {
        SysDataSourceDefManagerImpl impl = new SysDataSourceDefManagerImpl();
        //org.apache.commons.dbcp.BasicDataSource
        //com.alibaba.druid.pool.DruidDataSource
        System.out.println(JSON.toJSONString(impl.initAttributes("org.apache.commons.dbcp.BasicDataSource")));
    }
}
