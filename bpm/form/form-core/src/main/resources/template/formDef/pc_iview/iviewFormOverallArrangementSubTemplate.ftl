<<#if iviewGenerator.isMultilayer(relation)>Modal width="80%"<#else>Card</#if> ${iviewGenerator.getSubAttrs(relation)} v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >
    <div <#if iviewGenerator.isMultilayer(relation)>slot="header"<#else>slot="title"</#if> ><span class="title">${group.comment}</span>
        <ab-sub-add href="javascript:void(0)" class="btn btn-primary btn-sm fa fa-plus" v-model="${iviewGenerator.getScopePath(relation)}" v-bind:init-data="initData.${relation.busObj.key}.${relation.tableKey}" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}">添加 </ab-sub-add>
    </div>
    <div v-for="(${relation.tableKey},index) in ${iviewGenerator.getScopePath(relation)}" <#if iviewGenerator.isMultilayer(relation)> class="ivu-form ivu-form-label-right" </#if> >
        ${getOne2ManyChild(relation)}
        <ab-sub-del  class="ivu-btn ivu-btn-error" v-model="${iviewGenerator.getScopePath(relation)}" v-bind:index="index" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}">移除</ab-sub-del>
        <div class="ivu-row">
            <#list group.columnList as groupColumn>
                <div class="ivu-col ivu-col-span-${24/groupColumn.row} ivu-form-item" v-ab-permission:show="permission.${relation.busObj.key}.${groupColumn.tableKey}.${groupColumn.key}">
                    <label class="ivu-form-item-label" style="width: 100px;">${groupColumn.comment}</label>
                    <div class="ivu-form-item-content" style="margin-left: 100px;">${iviewGenerator.getColumn(group,groupColumn)}</div>
                </div>
            </#list>
        </div>
    </div>
    </<#if iviewGenerator.isMultilayer(relation)>Modal<#else>Card</#if>>

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
