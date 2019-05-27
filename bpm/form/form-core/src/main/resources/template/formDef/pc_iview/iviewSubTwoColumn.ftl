<<#if iviewGenerator.isMultilayer(relation)>Modal width="800"<#else>Card</#if> ${iviewGenerator.getSubAttrs(relation)} v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >

    <div <#if iviewGenerator.isMultilayer(relation)>slot="header"<#else>slot="title"</#if>><span class="title">${relation.tableComment}</span>
        <ab-sub-add href="javascript:void(0)" class="btn btn-primary btn-sm fa fa-plus" v-model="${iviewGenerator.getScopePath(relation)}" v-bind:init-data="initData.${relation.busObj.key}.${relation.tableKey}" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}">添加 </ab-sub-add>
    </div>
    <div v-for="(${relation.tableKey},index) in ${iviewGenerator.getScopePath(relation)}"> ${getOne2ManyChild(relation)}
        <ab-sub-del  class="ivu-btn ivu-btn-error" v-model="${iviewGenerator.getScopePath(relation)}" v-bind:index="index" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}">移除</ab-sub-del>
        <table class="form-table">
            <#assign index=1>
            <#list relation.table.columnsWithOutHidden as column>
                <#if index==1>
                    <tr>
                </#if>
                <th>${column.comment}</th>
                <td ${getColspan(index,column_has_next)}>${iviewGenerator.getColumn(column,relation)}</td>
                <#if !column_has_next || index==2>
                    </tr>
                    <#assign index=0>
                </#if>
                <#assign index=index+1>
            </#list>
        </table>
        ${getOne2OneChild(relation)}
    </div>
    </<#if iviewGenerator.isMultilayer(relation)>Modal<#else>Card</#if>>

    <#function getOne2OneChild relation>
        <#assign relationList = relation.getChildren('oneToOne')>
        <#assign rtn>
            <#list relationList as relation>
                <Card ${iviewGenerator.getSubAttrs(relation)} >
                    <div slot="title"> <span class="title">${relation.tableComment} </span>
                        ${getOne2ManyChild(relation)}
                    </div>
                    <table class="form-table">
                        <#assign index=1>
                        <#list relation.table.columnsWithOutHidden as column>
                            <#if index==1>
                                <tr>
                            </#if>
                            <th>${column.comment}</th>
                            <td ${getColspan(index,column_has_next)}>${iviewGenerator.getColumn(column,relation)}</td>
                            <#if !column_has_next || index==2>
                                </tr>
                                <#assign index=0>
                            </#if>
                            <#assign index=index+1>
                        </#list>
                    </table>
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