<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="/WEB-INF/pager.tld"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%@include file="/WEB-INF/view/common/common.jsp"%>
    <script type="text/javascript">
    var vm_expressReceiveReOrder=avalon.define({
        $id:'expressReceiveReOrder',
        methods:{
        	validAndSubmit:'func'
        }
    });
    </script>
    <title>收件补单</title>
</head>
<body :controller="expressReceiveReOrder">
    <div class="mt-30">
        <form id="expressReceiveReOrderForm" action="${ctx}/express/expressReceive/reOrder.do" method="post">
             <div class="row cl mb-15">
                <label class="form-label col-xs-4 col-sm-4 text-r">补单对象：</label>
                <div class="formControls col-xs-8 col-sm-8">
                	<input type="hidden" id="expressReceiveId" name="expressReceiveId" value="${expressReceive.id}">
                    <p>${expressReceive.receiverName}&emsp;(手机号：${expressReceive.receiverPhone})</p>
                </div>
            </div>
             <div class="row cl mb-15">
                <label class="form-label col-xs-4 col-sm-4 text-r">服务费补单金额：</label>
                <div class="formControls col-xs-8 col-sm-8">
                    <input class="input-text" autocomplete="off" placeholder="" name="reOrderServiceAmt" value="" type="text">
                </div>
            </div>
        </form>
    </div>    
<script type="text/javascript" src="${ctx}/static/lib/requirejs/requirejs.js" data-main="${ctx}/static/requirejs.config.js"></script>
<script type="text/javascript" src="${ctx}/static/model/express/expressReceiveReOrder.js"></script>
</body>
</html>