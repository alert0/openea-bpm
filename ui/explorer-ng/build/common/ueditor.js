var __path = "../.."

var aryCss__=[]; //先用打包的

var aryJs__=[ "/assets/js/plugins/ueditor/ab.ueditor.config.js",
              "/assets/js/plugins/ueditor/ueditor.all.js",
              "/assets/js/plugins/ueditor/lang/zh-cn/zh-cn.js",
              "/assets/js/plugins/ueditor/editor_api.js"];


// 表单设计器内的东西
if(typeof(formType)!=="undefined"){
	var custDialogjs = "custDialog.js";
	if(formType && formType !== 'pc'){
		custDialogjs = "custDialog-"+formType+".js";
	}
	
	aryJs__.push("/form/formPlugin/datePlugin/datePlugin-"+formType+".js");
	aryJs__.push("/assets/js/platform/form/formDef/"+custDialogjs);
	aryJs__.push("/form/formPlugin/formCalculate/formCalculate.js");
	aryJs__.push("/form/formPlugin/inputTools.js");
}


/**
 * js引入时导入必须的js文件。
 */
for(var i=0;i<aryJs__.length;i++){
	var str="<script src=\""+__path + aryJs__[i] +"\"></script>";
	document.write(str);
}