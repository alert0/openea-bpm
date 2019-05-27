<<#if iviewGenerator.isMultilayer(relation)>Modal width="80%"<#else>Card</#if> ${iviewGenerator.getSubAttrs(relation)} v-ab-permission:show="tablePermission.${relation.busObj.key}.${relation.tableKey}" >
    <div <#if iviewGenerator.isMultilayer(relation)>slot="header"<#else>slot="title"</#if> ><span class="title">${relation.tableComment}</span>
        <ab-sub-add class="ivu-btn ivu-btn-primary" v-model="${iviewGenerator.getScopePath(relation)}" v-bind:init-data="initData.${relation.busObj.key}.${relation.tableKey}" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}">添加 </ab-sub-add>
    </div>
    <div>
        <table class="form-table">
            <thead>
            <tr>
                <#list group.columnList as groupColumn>
                    <th v-ab-permission:show="permission.${relation.busObj.key}.${groupColumn.tableKey}.${groupColumn.key}">${groupColumn.comment}</th>
                </#list>
                <th>操作</th>
            </tr>
            </thead>
            <tr v-for="(${relation.tableKey},index) in ${iviewGenerator.getScopePath(relation)}">
                <#list group.columnList as groupColumn>
                    <td v-ab-permission:show="permission.${relation.busObj.key}.${groupColumn.tableKey}.${groupColumn.key}">${iviewGenerator.getColumn(group,groupColumn)}</td>
                </#list>
                <td> <button-group>${getOne2ManyChild(relation)}
                        <ab-sub-del  class="ivu-btn ivu-btn-error" v-model="${iviewGenerator.getScopePath(relation)}" v-bind:index="index" v-ab-permission:edit="tablePermission.${relation.busObj.key}.${relation.tableKey}">移除</ab-sub-del>
                    </button-group>
                </td>
            </tr>
        </table>
    </div>
    </<#if iviewGenerator.isMultilayer(relation)>Modal<#else>Card</#if>>