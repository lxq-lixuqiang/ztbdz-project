// 存储token到localStorage
function setToken(token) {
    localStorage.setItem('userToken', token);
}

function setMember(member) {
    localStorage.setItem('member', JSON.stringify(member));
}

// 从localStorage获取token
function getToken() {
    return localStorage.getItem('userToken');
}

// 从localStorage获取member
function getMember() {
    return JSON.parse(localStorage.getItem('member'));
}
// 删除token
function removeToken() {
    localStorage.removeItem('userToken');
    localStorage.removeItem('member');
}

// 配置信息 加密：btoa(unescape(encodeURIComponent("需要加密的内容")))
var config = {
    refundReason  : decoded("6K+355S16K+d5ZKo6K+iIOKAnDAyOC02NTczMTg4Me+8jDAyOC04NjY2MTgxMOKAnei/m+ihjOmhueebrumAgOi0ue+8gQ=="), //....退费流程说明 "请电话咨询 “028-65731881，028-86661810”进行项目退费！"
    consultPhone : decoded("6aG555uu5ZKo6K+i55S16K+d77yaMDI4LTY1NzMxODgxJiMxMDswMjgtODY2NjE4MTA="), //....咨询电话 "项目咨询电话：028-65731881&#10;028-86661810"
    skillPhone : decoded("5bmz5Y+w5oqA5pyv55S16K+d77yaMDI4LTY1NzMxODgx"), //....技术电话 "平台技术电话：028-65731881"
    pay : {
        receivingAccount : decoded("Lw=="), //....收款账户 "/"
        bankAccountNumber : decoded("Lw=="), //....银行账号 "/"
        bankDeposit : decoded("Lw=="), //....开户银行 "/"
        qrCode : decoded("aW1hZ2VzL3BheW1lbnQtcXJfeHkuanBn") //....微信付款二维码图片 "images/payment-qr_xy.jpg"
    },
    title : decoded("5Zub5bed6L2p6L6V55S15a2Q5oub5qCH5bmz5Y+w"), //....公司标题 "四川轩辕电子招标平台"
    name : decoded("5Zub5bed6L2p6L6V5oub5qCH5Luj55CG5pyJ6ZmQ5YWs5Y+4"), //....公司名称 "四川轩辕招标代理有限公司"
    logo :{
        img : decoded("aW1hZ2VzL3RvcGJhci5qcGc="), //....公司首页logo "images/topbar.jpg"
        style : decoded("d2lkdGg6MjU5cHg7bWFyZ2luLWxlZnQ6IDIwcHg7") , // ....公司首页logo样式 "width:259px;margin-left: 20px;"
        icon : decoded("aW1hZ2VzL2ljb24ucG5n") // ....公司首页icon "images/icon.png"
    }
};

initInit();
window.onload = function()  {
    // 退出登录
    var logoutName = decoded("6YCA5Ye6");// 退出
    $("body").on("click", "a:contains('"+logoutName+"')", function() {
        if($(this).text() == logoutName){
            $.ajax({
                url: "/user/logout",
                type: "POST",
                contentType: "application/json",
                success:function(e) {
                    if(e.status != 200){
                        alert(e.message);
                    }
                }
            });
            removeToken();
        }
    });
};

function initInit(){
    loadCSS("plugin/loading/loading.css");//加载loading的css
    var oldInfo = decoded("QUnmmbrog73ljJbor4TmoIfns7vnu58=");// AI智能化评标系统
    $("div>div:contains('"+oldInfo+"')").filter(function() { return $(this).text() === oldInfo; }).text(config.title);
    $("title:contains('"+oldInfo+"')").filter(function() { return $(this).text() === oldInfo; }).text(config.name);
    var logo = $(".logo");
    if(logo.length>0) logo.attr("src",config.logo.img).attr("style",config.logo.style);
    if(config.logo.icon){
        loadLink(config.logo.icon,'shortcut icon','image/x-icon');
    }
}

// 正则表达式校验
var regBox = {
    regEmail : { //....邮箱："邮箱格式错误"
        regEx : /^([a-zA-Z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,
        message : decoded("6YKu566x5qC85byP6ZSZ6K+v")
    },
    regName : { //....用户名："用户名必须是2~16位的字母数字_-"
        regEx : /^[a-zA-Z0-9_-]{2,16}$/,
        message : decoded("55So5oi35ZCN5b+F6aG75pivMn4xNuS9jeeahOWtl+avjeaVsOWtl18t")
    },
    regPassword : { //....密码："密码必须是6~16位的字母数字@$_-"
        regEx : /^[a-zA-Z0-9@$_-]{6,16}$/,
        message : decoded("5a+G56CB5b+F6aG75pivNn4xNuS9jeeahOWtl+avjeaVsOWtl0AkXy0=")
    },
    regIdCard : { //....身份证号："身份证号格式错误"
        regEx : /^[1-9]([0-9]{13}|[0-9]{16})[0-9xX]$/,
        message : decoded("6Lqr5Lu96K+B5Y+35qC85byP6ZSZ6K+v")
    },
    regMobilePhone : { //....手机："手机号格式错误"
        regEx : /^0?1[3-9][0-9]\d{8}$/,
        message : decoded("5omL5py65Y+35qC85byP6ZSZ6K+v")
    },
    regTelephone : { //....电话："电话号码格式错误"
        regEx : /^0[\d]{2,3}-[\d]{7,8}$/,
        message : decoded("55S16K+d5Y+356CB5qC85byP6ZSZ6K+v")
    },
    regMoney :{ //....金额："金额格式错误"
        regEx : /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
        message : decoded("6YeR6aKd5qC85byP6ZSZ6K+v")
    },
    regMoneyInt :{ //....整数金额："只能为整数"
        regEx : /(^[1-9]([0-9]+)?(\.[0]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0]([0])?$)/,
        message : decoded("5Y+q6IO95Li65pW05pWw")
    }
}

// 解密
function decoded(encryptionStr){
    return decodeURIComponent(escape(atob(encryptionStr)));
}

// 校验token是否有效
var isSystemError = false;
function verifyLogin(){
    if(!getToken()){
        location.href = "/login.html";
        return;
    }
    $.ajaxSetup({
        headers: {
            'token': getToken(),
            'url' : window.location.pathname
        },
        error:function(e) {
            if(!e) return;
            if(isSystemError){
                return;
            }
            isSystemError = true;
            if(e.responseText.indexOf("token")>-1 ||
                e.responseText.indexOf(decoded("5a+G56CB"))>-1){
                alert(e.responseText);
                location.href = "/login.html";
                removeToken();
            }else{
                alert(decoded("57O757uf6ZSZ6K+v77ya")+e.responseText); // 系统错误：
                setTimeout(function(){
                    isSystemError = false;
                },1000);
            }
        }
    });
    $.ajax({
        url: "/user/verifyLogin",
        type: "POST",
        async: false,
        contentType: "application/json",
        success:function(e) {
            if(e.status == 200){
                // setToken(e.data.token); // 刷新token保持活跃状态
                setMember(e.data.member);
            }else{
                alert(e.message);
                location.href = "/login.html";
            }
        }
    });
}

// 上传文件
function uploadFileInfo(uploadName,showName){
    $("body").on('change','#'+uploadName, function () {
        if(this.files.length==0){
            return;
        }
        var fileId = uploadFile(this.files);
        var datafileId = $("#"+uploadName).attr("data-file-id");
        if(datafileId && datafileId.length>0){
            datafileId+=",";
        }else{
            datafileId="";
        }
        datafileId+=fileId;
        $("#"+uploadName).attr("data-file-id",datafileId);
        var fileIds = fileId.split(",");
        var fileName = $("#"+showName).html();
        for(var i=0;i<this.files.length;i++){
            fileName+="<p><a style=\"padding:5px 10px; background:#2196F3; color:white; border:none; cursor:pointer; text-decoration:none;font-size: 13px;display:inline-block;\" target=\"_blank\" href='/file/preview/"+fileIds[i]+"'>"+this.files[i].name+"（"+convertSize(this.files[i].size)+"）<span onclick='deleteUploadFileInfo(\""+fileIds[i]+"\",\""+uploadName+"\",\""+showName+"\")' class='delete"+fileIds[i]+"' style='background-color: red;width: 15px;height: 16px;display: inline-block;padding: 0;text-align: center;vertical-align: middle;border-radius: 15%;line-height: 15px;'>×</span></a></p>";
        }
        $("#"+showName).html(fileName);
    });
}
//删除上传文件
function deleteUploadFileInfo(id,uploadName){
    $(".delete"+id).parent().removeAttr("href");
    $(".delete"+id).parent().parent().remove();
    var ids = $("#"+uploadName).attr("data-file-id");
    var idArray = ids.split(",");
    var newIds="";
    for(var i=0;i<idArray.length;i++){
        if(idArray[i] != id){
            if(newIds.length>0) newIds+=",";
            newIds+=idArray[i];
        }
    }
    $("#"+uploadName).attr("data-file-id",newIds);
    alert(decoded("5Yig6Zmk5oiQ5Yqf77yB"));
}

// 同步上传图片
function uploadImg(files) {
    var error = decoded("5LiK5Lyg5Zu+54mH5aSn5bCP5LiN6IO96LaF6L+HMTAwTe+8gQ=="); // 上传图片大小不能超过100M！
    return upload(files,"img",error);
}
// 同步上传文件
function uploadFile(files) {
    var error = decoded("5LiK5Lyg5paH5Lu25aSn5bCP5LiN6IO96LaF6L+HMTAwTe+8gQ=="); //上传文件大小不能超过100M！
    return upload(files,"file",error);
}
// 同步上传
function upload(files,url,errorMagger){
    var formData = new FormData();
    for(var i=0;i<files.length;i++){
        if(files[i].size>(100*1024*1024)){
            alert(errorMagger);
            throw errorMagger;
        }
        formData.append('files', files[i]); // 'file'是后端接收的文件参数名
    }
    if(url == "img"){
        url = "/file/uploadImg";
    }else if(url == "file"){
        url = "/file/upload";
    }
    var fileId = "";
    $.ajax({
        url: url,
        type: 'POST',
        data: formData,
        async: false,
        contentType: false,
        processData: false,
        success: function (e) {
            if(e.status == 200){
                fileId = e.data;
                alert(decoded("5LiK5Lyg5oiQ5Yqf77yB")); // 上传成功！
            }else{
                alert(e.message)
            }
        },error: function(e){
            if(!e) return;
            alert(e.responseText)
        }
    });
    return fileId;
}
// 异步上传文件
function asyncUpload(files,name){
    uploadLoading();//显示上传loading
    var errorMagger = decoded("5LiK5Lyg5paH5Lu25aSn5bCP5LiN6IO96LaF6L+HNTAwTe+8gQ=="); //上传文件大小不能超过500M！
    var formData = new FormData();
    for(var i=0;i<files.length;i++){
        if(files[i].size>(500*1024*1024)){
            alert(errorMagger);
            throw errorMagger;
        }
        formData.append('files', files[i]); // 'file'是后端接收的文件参数名
    }
    var fileId = "";
    $.ajax({
        url: "/file/upload",
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function (e) {
            if(e.status == 200){
                fileId = e.data;
                closeLoading(); // 关闭loading
                asyncUploadExecute(fileId,name); // 回调函数
                alert(decoded("5LiK5Lyg5oiQ5Yqf77yB")); // 上传成功！
            }else{
                alert(e.message)
            }
        },error: function(e){
            closeLoading(); // 关闭loading
            if(!e) return;
            alert(e.responseText)
        }
    });
    return fileId;
}



// 计算天数
function daysBetweenDates(startDate, endDate) {
    if(!(startDate || endDate)){
        return "";
    }
    // 将日期转换为毫秒
    var timeDiff = Math.abs(startDate.getTime() - endDate.getTime());
    // 计算天数差
    var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24));
    return diffDays;
}

// 文件大小转换
function convertSize(size){
    if(size<1024){
        return size+"B";
    }else if(size<1024 * 1024){
        return (size / 1024).toFixed(2)+"KB";
    }else if(size<1024 * 1024 * 1024){
        return (size / (1024 * 1024)).toFixed(2)+"MB";
    }else if(size<1024 * 1024 * 1024 * 1024){
        return (size / (1024 * 1024 * 1024)).toFixed(2)+"GB";
    }
}

// 分页按钮展示
localStorage.removeItem('page'); // 分页页码
localStorage.removeItem('data'); // 搜索参数
function pageButton(data,pageName,pageTheadName){
    if(!pageName) pageName = "pageInfo";
    if(!pageTheadName) pageTheadName = "pageThead";
    if(data.navigatepageNums.length>0){
        var pageInfo = "<span style=\"margin:0 10px;\">共&nbsp;"+data.total+"&nbsp;"+decoded("5p2h")+"</span>"; // 条
        if(1!= data.navigateFirstPage){
            pageInfo +="<button style=\"width:40px;height:40px;margin-right:10px;cursor: pointer;\" onclick=\"pageShow('1','"+pageName+"')\">1</button>";
            pageInfo +="<span style=\"margin:0 10px;\">...</span>";
        }
        for(var i=0;i<data.navigatepageNums.length;i++){
            var disabled = "";
            if(data.navigatepageNums[i] == data.pageNum){
                disabled = "disabled=true";
            }
            var noDisabled = "";
            if(data.navigatepageNums[i] != data.pageNum){
                noDisabled = "cursor: pointer;";
            }
            pageInfo +="<button style=\"margin-top:5px;width:40px;height:40px;margin-right:10px;"+noDisabled+"\" "+disabled+"  onclick=\"pageShow('"+data.navigatepageNums[i]+"','"+pageName+"')\" >"+data.navigatepageNums[i]+"</button>";
        }
        if(data.pages!= data.navigateLastPage){
            pageInfo +="<span style=\"margin:0 10px;\">...</span>";
            pageInfo +="<button style=\"width:40px;height:40px;margin-right:10px;cursor: pointer;\" onclick=\"pageShow('"+data.pages+"','"+pageName+"')\">"+data.pages+"</button>";
        }
        $("#"+pageName).html(pageInfo);
    }else{
        localStorage.setItem('page',1);
        $("#"+pageName).html("");
    }

    var pageThead = document.getElementById(pageTheadName); // 放到 table标签下的thead里的id
    if(pageThead && pageThead.getBoundingClientRect().top<0){ // 如果滚动条超过了标签就为负数开启进行跳转
        pageThead.scrollIntoView({ behavior: 'smooth' });
    }
}
function pageShow(page,pageName){
    localStorage.setItem('page', page);
    var data = localStorage.getItem('data');
    if(!data){
        initPageShow("",pageName);
    }else if(data.indexOf(":")>-1){
        initPageShow(JSON.parse(data),pageName);
    }else{
        initPageShow(data,pageName);
    }
}

// 打印pdf页面
function printHtml(html){
    var printWindow = window.open('', '_blank');
    printWindow.document.write(html);
    printWindow.document.close();
    printWindow.focus();
    setTimeout(function(){
        printWindow.print();
        printWindow.close();
    }, 500);
}

// 下载word
function downWord(blob,fileName){
    var link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = fileName+'.doc';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    alert(decoded("5q2j5Zyo5LiL6L29d29yZOaWh+S7tu+8gQ=="));// 正在下载word文件！
}

// 项目经理添加返回工作台
function manage(){
    var a = $("<a></a>").attr("href","manage.html").attr("style","text-decoration:none; color:#333;").text(decoded("6L+U5Zue5bel5L2c5Y+w")); // 返回工作台
    var user = getMember();
    if(user.role.type=="manage"){
        var returnHome = decoded("6L+U5Zue6aaW6aG1");// 返回首页
        $("a:contains('"+returnHome+"')").filter(function() {
            var bol = $(this).text() === returnHome;
            if(bol){
                a.insertBefore($("a:contains('"+returnHome+"')"));
            }
            return bol;
        })
    }
}

// 输入框识别键盘键
function keypress13(searchInput,searchBtn){
    $('#'+searchInput).on('keypress', function(e) {
        if (e.which === 13) { // 回车键
            $('#' + searchBtn).click();
        }
    });
}

// loading显示
function showLoading(content,isHide){
    if($("#loading").length>0){
        $("#loading p").text(content+"...");
    }else{
        $("body").append("<div id='loading' class=\"loader\">\n" +
            "  <h1></h1>\n" +
            "  <div class=\"loading_box\">\n" +
            "    <div class=\"symbol\">\n" +
            "      <p>"+content+"...</p>\n" +
            "      <div></div>\n" +
            "    </div>\n" +
            "  </div>\n" +
            "</div>");
    }
}
function loading(){
    showLoading(decoded("5Yqg6L295Lit")); // 加载中
}
function importLoading(){
    showLoading(decoded("5a+85YWl5pWw5o2u5Lit")); // 导入数据中
}
function uploadLoading(){
    showLoading(decoded("5LiK5Lyg5paH5Lu25Lit")); // 上传文件中
}
function closeLoading(){
    $("#loading").remove();
}

// 动态引用CSS
function loadCSS(url) {
    loadLink(url,'stylesheet','text/css');
}
function loadLink(url,rel,type) {
    var link = document.createElement('link');
    link.rel = rel;
    link.href = url;
    link.type = type;
    document.head.appendChild(link);
}

// 校验人员访问权限
function verifyMember(memberId) {
    if(getMember().id != memberId){
        alert(decoded("6K6/6Zeu5p2D6ZmQ5LiN6Laz77yB")); // 访问权限不足！
        window.history.back();
        return false;
    }
    return true;
}

