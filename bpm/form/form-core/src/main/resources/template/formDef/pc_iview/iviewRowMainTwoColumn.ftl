<script>
    window.custFormComponentMixin ={
        data: function () {
            return {"test":"helloWorld"};
        },
        created:function(){
            console.log("脚本将会混入自定义表单组件中...");
        },methods:{
            testaaa:function(){alert(1)}
        }
    }
</script>
<div class="ivu-row">
    <#list relation.table.columnsWithOutHidden as column>
        <div class="ivu-col ivu-col-span-12 ivu-form-item">
            <label class="ivu-form-item-label" style="width: 100px;">${column.comment}</label>
            <div class="ivu-form-item-content" style="margin-left: 100px;">${iviewGenerator.getColumn(column,relation)}</div>
        </div>
    </#list>
</div>
${getOne2OneChild(relation)}

<#function getOne2OneChild relation>
    <#assign relationList = relation.getChildren('oneToOne')>
    <#assign rtn>
        <#list relationList as relation>
            <Card ${iviewGenerator.getSubAttrs(relation)} >
                <div slot="title"> <span class="title">${relation.tableComment} </span>
                    ${getOne2ManyChild(relation)}
                </div>
                <div class="ivu-row">
                    <#list relation.table.columnsWithOutHidden as column>
                        <div class="ivu-col ivu-col-span-12 ivu-form-item">
                            <label class="ivu-form-item-label" style="width: 100px;">${column.comment}</label>
                            <div class="ivu-form-item-content" style="margin-left: 100px;">${iviewGenerator.getColumn(column,relation)}</div>
                        </div>
                    </#list>
                </div>
                ${getOne2OneChild(relation)}
            </Card>
        </#list>
    </#assign>
    <#return rtn>
</#function>

<#function getOne2ManyChild relation>
    <#assign relationList = relation.getChildren('oneToMany')>
    <#assign rtn>
        <#if relationList?? && (relationList?size > 0) >
            <#list relationList as relation>
                <ab-sub-detail class="ivu-btn ivu-btn-default"  v-model="${iviewGenerator.getScopePath(relation.parent)}<#if relation.parent.type =='oneToMany'>[index]</#if>" table-key="${relation.getBusObj().getKey()}_${relation.tableKey}" v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >${relation.tableComment}详情</ab-sub-detail>
            </#list>
        </#if>
    </#assign>
    <#return rtn>
</#function>


<#function getColspan index,hasNext>
    <#assign rtn="">

    <#if !hasNext && index !=2>
        <#assign rtn="colspan='"+((2-index)*2+1)+"'">
    </#if>

    <#return rtn>
</#function>