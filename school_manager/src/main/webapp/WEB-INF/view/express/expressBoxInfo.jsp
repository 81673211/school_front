 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
	<%@include file="/WEB-INF/view/common/common.jsp" %>
    <title>取件列表</title>
    <script type="text/javascript">
      var vm_expressBoxInfoManage=avalon.define({
          $id:'expressBoxInfoManage',
          data:${listData},
          expressBoxInfoStatusMap:${expressBoxInfoStatusMap},
//           expressTypes:${expressTypes},
          inputStatus:"${searchParams.status}",
          inputText:"${searchParams.keyword}",
          methods:{
              query:'func',//搜索事件
              detail:'func',//查看
              add:'func',//新增
              edit:'func',//编辑
              del:'func',//删除
              detailUrl:"${detailUrl}",//详情url
              editUrl:"${editUrl}",//编辑url
              delUrl:"${delUrl}"//删除url
          }
      });
    </script>
</head>

<body :controller="expressBoxInfoManage">
    <nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 快递管理 <span class="c-gray en">&gt;</span> 取件列表 <a class="btn btn-success radius r"
            style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新"><i class="Hui-iconfont">&#xe68f;</i></a></nav>
    <div class="page-container">
        <div class="bk-gray pd-20 clearfix">
            <div class="revi-staus f-l">
                <span class="f-l lh-30">状态：</span>
                <span class="select-box radius f-l" style="width: 120px;">
                 <select class="select" size="1" name="status" :duplex="@inputStatus">
                 	<option value="">全部</option>
                    <option :for="(k,v) in @expressBoxInfoStatusMap" :attr="{value:k}">{{v}}</option> 
                 </select>
             </span>
            </div>
            <div class="tran-serch f-l ml-50">
                <input type="text" name="keywordSearch" id="" placeholder="快递单号/柜子编号" style="width:200px" class="input-text" :duplex="@inputText">
                <button name="" id="" class="btn btn-primary radius" type="button" :click="@methods.query"><i class="Hui-iconfont"></i> 搜索</button>
            </div>
            <div class="f-r">
                <btn:hasUrlPerm link="${editUrl}">
                	<a class="btn btn-danger ml-10" data-title="新建" :click="@methods.add(@methods.editUrl)" href="javascript:;"><i class="Hui-iconfont">&#xe600;</i> 新增</a>
                </btn:hasUrlPerm>
	        </div>
        </div>
        <div class="mt-20">
            <table class="table table-bexpressBoxInfo table-bexpressBoxInfoed table-hover table-bg table-sort" id="reviwtable">
                <thead>
                    <tr class="text-c">
                        <th width="25">编号</th>
                        <th width="100">快递单号</th>
                        <th width="100">柜子组编号</th>
                        <th width="100">柜子编号</th>
                        <th width="100">操作人</th>
                        <th width="100">创建时间</th>
                        <th width="100">状态</th>
                        <th width="50">操作</th>
                    </tr>
                </thead>
                <tbody>
                    <tr class="text-c" :for="index,el in @data">
                        <td>{{index+1}}</td>
                        <td>{{el.expressCode}}</td>
                        <td>{{el.boxGroup}}</td>
                        <td>{{el.boxNo}}</td>
                        <td>{{el.operName}}</td>
                        <td>{{el.createdTime|date('yyyy-MM-dd HH:mm:ss')}}</td>
                        <td>{{@expressBoxInfoStatusMap[el.status]}}</td>
                        <td class="but_xq">
                        	<btn:hasUrlPerm link="${detailUrl}">
	                           	<a class="btn btn-primary-outline size-S radius" :click="@methods.detail(el.id,@methods.detailUrl)">详情</a>
                            </btn:hasUrlPerm>
                            <btn:hasUrlPerm link="${editUrl}">
                            	<a class="ml-5 btn btn-primary-outline size-S radius" :click="@methods.edit(el.id, @methods.editUrl)">编辑</a>
                            </btn:hasUrlPerm>
                            <btn:hasUrlPerm link="${delUrl}">
                            	<a class="ml-5 btn btn-danger-outline size-S radius" :click="@methods.del(el.id, @methods.delUrl)">删除</a>
                            </btn:hasUrlPerm>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <!--分页-->
		<div class="pag-box withdarwal-pag" id="page">
			<form method="post" id="searchForm"
				action="${ctx}/expressBoxInfo/list.do">
				<input name="search_status" id="testForm_state"
					type="hidden"  :attr="{value:@inputStatus}" />
				<input name="search_type" id="testForm_type" type="hidden"
					:attr="{value:@inputType}" />
				<input name="search_keyword" id="testForm_text" type="hidden"
					:attr="{value:@inputText}" />
			</form>
			<page:createPager pageSize="${pageSize}" totalPage="${pageCount}"
					totalCount="${totalCount}" curPage="${pageNo}" formId="searchForm" />
		</div>
    </div>
    <script type="text/javascript" src="${ctx}/static/lib/requirejs/requirejs.js" data-main="${ctx}/static/requirejs.config.js"></script>
    <script type="text/javascript" src="${ctx}/static/model/express/expressBoxInfo.js"></script>
</body>
</html>