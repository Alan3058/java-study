<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.alan.system.model.Subforum"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/jsp/common/header.jsp"></jsp:include>

<div class="container">
    <div class="row">
        <div class="span12">
            <table class="table table-bordered table-hover">
                <tr><#list fields as field>
                    <th>${field.columnRemark}</th>
                </#list>
                </tr>

                <c:forEach items="$<<change>>{${miniClassName}s }" var="var">
                    <tr>
                    <#list fields as field>
                        <td>$<
                            <change>>{var.${field.fieldName}}
                        </td>
                    </#list>
                        <td>
                            <a onclick="edit('$<<change>>{var.id }')">编辑</a>|
                            <a onclick="del('$<<change>>{var.id }',this)">删除</a>|
                            <a onclick="copy('$<<change>>{var.id }')">复制</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
    <div class="row">
        <div class="span12" style="display:none">
            <form id="form" action="${miniClassName}/add.shtml" class="form-horizontal"
                  method="post">
                <input name="id" type="hidden" id="id">
                <!-- <div class="control-group">
                    <div class="controls">
                        <label
                            style="font-size: 28px; font-weight: bolder; margin-top: 10px;"></label>
                    </div>
                </div> -->
            <#list fields as field>
                <div class="control-group">
                    <label class="control-label">${field.columnRemark}</label>
                    <div class="controls">
                        <input name="${field.fieldName}" type="text" id="${field.fieldName}" placeholder="">
                    </div>
                </div>
            </#list>

                <div class="control-group">
                    <div class="controls">
                        <input type="submit" class="btn" value="保存"/> <input
                            type="reset" class="btn" value="重填"/>
                    </div>
                </div>

            </form>
        </div>
    </div>
</div>
<jsp:include page="/jsp/common/footer.jsp"></jsp:include>
<script type="text/javascript" src="js/${miniClassName}.js"></script>
<script type="text/javascript" src="js/PopUtil.js"></script>