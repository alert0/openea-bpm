package org.openbpm.sys.groovy;

import org.openbpm.base.core.util.AppUtil;
import org.openbpm.sys.api.groovy.IGroovyScriptEngine;
import org.openbpm.sys.api.groovy.IScript;
import groovy.lang.GroovyShell;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 脚本引擎用于执行groovy脚本。<br/>
 * 实现了IScript接口的类。 可以在脚本中使用。
 */
@Component
public class GroovyScriptEngine implements IGroovyScriptEngine, ApplicationListener<ContextRefreshedEvent>{

    private Log logger = LogFactory.getLog(GroovyScriptEngine.class);
    private GroovyBinding groovyBinding = new GroovyBinding();
    
    @Override
    public void execute(String script) {
        executeObject(script, null);
    }

    @Override
    public void execute(String script, Map<String, Object> vars) {
        executeObject(script, vars);
    }

    @Override
    public boolean executeBoolean(String script, Map<String, Object> vars) {
        return (Boolean) executeObject(script, vars);
    }
 
    @Override
    public String executeString(String script, Map<String, Object> vars) {
        return (String) executeObject(script, vars);
    }

   
    @Override
    public int executeInt(String script, Map<String, Object> vars) {
        return (Integer) executeObject(script, vars);
    }
 
    @Override
    public float executeFloat(String script, Map<String, Object> vars) {
        return (Float) executeObject(script, vars);
    }

    @Override
    public Object executeObject(String script, Map<String, Object> vars) {
        groovyBinding.setThreadVariables(vars);
        
        if(logger.isDebugEnabled()) {
        	logger.debug("执行:" + script);
        	logger.debug("variables:" +vars+"");
        }
        
        GroovyShell shell = new GroovyShell(groovyBinding);

        script = script.replace("&apos;", "'").replace("&quot;", "\"")
                .replace("&gt;", ">").replace("&lt;", "<")
                .replace("&nuot;", "\n").replace("&amp;", "&");

        Object rtn = shell.evaluate(script);
        return rtn;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            Map<String, IScript> scirptImpls =	AppUtil.getImplInstance(IScript.class);
            for(Entry<String, IScript> scriptMap : scirptImpls.entrySet()) {
                groovyBinding.setProperty(scriptMap.getKey(), scriptMap.getValue());
            }
        }
    }
}
