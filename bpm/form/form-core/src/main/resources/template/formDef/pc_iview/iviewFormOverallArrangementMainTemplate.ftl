<#if group.hasTitle()>
<Card>
    <div slot="title">
        <span class="title">${group.comment} ${One2OneChildsOne2ManyRelations(group)}</span>
    </div>
    </#if>
    <div class="ivu-row">
        <#list group.columnList as groupColumn>
            <div class="ivu-col ivu-col-span-${24/groupColumn.row} ivu-form-item" v-ab-permission:show="permission.${relation.busObj.key}.${groupColumn.tableKey}.${groupColumn.key}">
                <label class="ivu-form-item-label" style="width: 100px;">${groupColumn.comment}</label>
                <div class="ivu-form-item-content" style="margin-left: 100px;"> ${iviewGenerator.getColumn(group,groupColumn)} </div>
            </div>
        </#list>
    </div>
    <#if group.hasTitle()></Card></#if>

<#function One2OneChildsOne2ManyRelations group>
    <#assign relationList = group.getOne2OneChildsOne2ManyRelations()>
    <#assign rtn>
        <#if relationList?? && (relationList?size > 0) >
            <#list relationList as relation>
                <ab-sub-detail class="ivu-btn ivu-btn-default"  v-model="${iviewGenerator.getScopePath(relation.parent)}<#if relation.parent.type =='oneToMany'>[index]</#if>" table-key="${relation.getBusObj().getKey()}_${relation.tableKey}" v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >${relation.tableComment}详情</ab-sub-detail>
            </#list>
        </#if>
    </#assign>
    <#return rtn>
</#function>
