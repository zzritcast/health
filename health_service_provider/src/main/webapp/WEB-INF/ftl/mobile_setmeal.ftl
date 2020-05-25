<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0,user-scalable=no,minimal-ui">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../img/asset-favico.ico">
    <title>预约</title>
    <link rel="stylesheet" href="../css/page-health-order.css" />
</head>
<body data-spy="scroll" data-target="#myNavbar" data-offset="150">
<div class="app" id="app">
    <!-- 页面头部 -->
    <div class="top-header">
        <span class="f-left"><i class="icon-back" onclick="history.go(-1)"></i></span>
        <span class="center">传智健康</span>
        <span class="f-right"><i class="icon-more"></i></span>
    </div>
    <!-- 页面内容 -->
    <div class="contentBox">
        <div class="list-column1">
            <ul class="list">
                <#if setmealList?? && setmealList?size > 0 >
                    <#list setmealList as setmeal>
                        <li class="list-item">
                            <#if checkgroup.checkItems??>
                            <a class="link-page" href="setmeal_detail_${setmeal.id}.html">
                            </#if>
                                <#if setmeal.img??>
                                <img class="img-object f-left" src="http://q9y0joq9n.bkt.clouddn.com/${setmeal.img}" alt="">
                                </#if>
                                <div class="item-body">
                                     <#if setmeal.name??>
                                    <h4 class="ellipsis item-title">${setmeal.name}</h4>
                                     </#if>
                                    <#if setmeal.remark??>
                                    <p class="ellipsis-more item-desc">${setmeal.remark}</p>
                                    </#if>

                                    <p class="item-keywords">
                                        <#if setmeal.sex??>
                                        <span>
                                            <#if setmeal.sex == '0'>
                                                性别不限
                                                <#else>
                                                    <#if setmeal.sex == '1'>
                                                    男
                                                    <#else>
                                                    女
                                                    </#if>
                                            </#if>
                                        </span>
                                        </#if>
                                        <#if setmeal.age??>
                                        <span>${setmeal.age}</span>
                                        </#if>
                                    </p>
                                </div>
                            </a>
                        </li>
                    </#list>
                </#if>

            </ul>
        </div>
    </div>
</div>
<!-- 页面 css js -->
<script src="../plugins/vue/vue.js"></script>
<script src="../plugins/vue/axios-0.18.0.js"></script>
</body>