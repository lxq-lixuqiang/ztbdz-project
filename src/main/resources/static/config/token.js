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
    refundReason  : decoded("6K+35Y+R6YCB6YKu566x5YiwIOKAnDI4MTc4OTk3OTRxcS5jb23igJ3vvIzor7TmmI7pgIDotLnljp/lm6DlubbpmYTkuIog4oCc5pSv5LuY5oiQ5Yqf55qE5oiq5Zu+4oCdIOWSjOaIquWbvumhtemdouaciSDigJzmiqXlkI3kuI3pgJrov4figJ0g55qE6aG555uuIOi/m+ihjOmAgOi0ueWuoeaguO+8gQ=="), //....退费流程说明 "请发送邮箱到 “2817899794qq.com”，说明退费原因并附上 “支付成功的截图” 和截图页面有 “报名不通过” 的项目 进行退费审核！"
    consultPhone : decoded("6aG555uu5ZKo6K+i55S16K+d77yaMDI4LTg2NzE2Mjg5"), //....咨询电话 "项目咨询电话：028-86716289"
    skillPhone : decoded("5bmz5Y+w5oqA5pyv55S16K+d77yaMTkxLTQxMjYtOTkzNQ=="), //....技术电话 "平台技术电话：191-4126-9935"
    pay : {
        receivingAccount : decoded("5Zub5bed5Lit5aSp6Ziz5YWJ5oub5qCH5Luj55CG5pyJ6ZmQ5YWs5Y+4"), //....收款账户 "四川中天阳光招标代理有限公司"
        bankAccountNumber : decoded("NjMxMCAwNTcxNg=="), //....银行账号 "6310 05716"
        bankDeposit : decoded("5Lit5Zu95rCR55Sf6ZO26KGM5oiQ6YO96ZOB5YOP5a+65pSv6KGM"), //....开户银行 "中国民生银行成都铁像寺支行"
        qrCode : decoded("aW1hZ2VzL3BheW1lbnQtcXIuanBn") //....微信付款二维码图片 "images/payment-qr.jpg"
    }
}

// 正则表达式校验
var regBox = {
    regEmail : /^([a-zA-Z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/,   //....邮箱："邮箱格式错误"
    regName : /^[a-zA-Z0-9_-]{2,16}$/,                       //....用户名："用户名必须是2~16位的字母数字_-"
    regPassword : /^[a-zA-Z0-9@$_-]{6,16}$/,                 //....密码："密码必须是6~16位的字母数字@$_-"
    regIdCard : /^[1-9]([0-9]{13}|[0-9]{16})[0-9xX]$/,       //....身份证号："身份证号格式错误"
    regMobilePhone : /^0?1[3|4|5|8][0-9]\d{8}$/,                 //....手机："手机号格式错误"
    regTelephone : /^0[\d]{2,3}-[\d]{7,8}$/,                    //....电话："电话号码格式错误"
    regMoney : /^[0-9]\d*$/                                  //....金额："不能包含小数和负数"
}

// 解密
function decoded(encryptionStr){
    return decodeURIComponent(escape(atob(encryptionStr)));
}

// 校验token是否有效
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
            alert("token失效，请重新登陆！");
            location.href = "/login.html";
            removeToken();
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

window.onload = function()  {
    // 退出登录
    $("body").on("click", "a:contains('退出')", function() {
        $.ajax({
            url: "/user/logout",
            type: "POST",
            contentType: "application/json",
            success:function(e) {
                if(e.status != 200){
                    alert("退出失败："+e.message);
                }
            }
        });
        removeToken();
    });
};


// 上传图片
function uploadImg(files) {
    var formData = new FormData();
    for(var i=0;i<files.length;i++){
        formData.append('files', files[i]); // 'file'是后端接收的文件参数名
    }
    var fileId = "";
    $.ajax({
        url: '/file/uploadImg',
        type: 'POST',
        data: formData,
        async: false,
        contentType: false, // 告诉jQuery不要去设置Content-Type请求头
        processData: false, // 告诉jQuery不要去处理发送的数据
        success: function (e) {
            if(e.status == 200){
                fileId = e.data;
            }else{
                alert("上传图片失败！")
            }
        },
        error: function () {
            alert("上传图片失败！")
        }
    });
    return fileId;
}

// 上传文件
function uploadFile(files) {
    var formData = new FormData();
    for(var i=0;i<files.length;i++){
        formData.append('files', files[i]); // 'file'是后端接收的文件参数名
    }
    var fileId = "";
    $.ajax({
        url: '/file/upload',
        type: 'POST',
        data: formData,
        async: false,
        contentType: false, // 告诉jQuery不要去设置Content-Type请求头
        processData: false, // 告诉jQuery不要去处理发送的数据
        success: function (e) {
            if(e.status == 200){
                fileId = e.data;
            }else{
                alert("上传图片失败！")
            }
        },
        error: function () {
            alert("上传文件失败！")
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
function pageButton(data){
    if(data.navigatepageNums.length>0){
        var pageInfo = "<span style=\"margin:0 10px;\">共&nbsp;"+data.total+"&nbsp;条</span>";
        if(1!= data.navigateFirstPage){
            pageInfo +="<button style=\"width:40px;height:40px;margin-right:10px;cursor: pointer;\" onclick=\"pageShow('1')\">1</button>";
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
            pageInfo +="<button style=\"width:40px;height:40px;margin-right:10px;"+noDisabled+"\" "+disabled+"  onclick=\"pageShow('"+data.navigatepageNums[i]+"')\" >"+data.navigatepageNums[i]+"</button>";
        }
        if(data.pages!= data.navigateLastPage){
            pageInfo +="<span style=\"margin:0 10px;\">...</span>";
            pageInfo +="<button style=\"width:40px;height:40px;margin-right:10px;cursor: pointer;\" onclick=\"pageShow('"+data.pages+"')\">"+data.pages+"</button>";
        }
        $("#pageInfo").html(pageInfo);
    }else{
        localStorage.setItem('page',1);
        $("#pageInfo").html("");
    }

    var pageThead = document.getElementById("pageThead"); // 放到 table标签下的thead里的id
    if(pageThead && pageThead.getBoundingClientRect().top<0){ // 如果滚动条超过了标签就为负数开启进行跳转
        pageThead.scrollIntoView({ behavior: 'smooth' });
    }
}
function pageShow(page){
    localStorage.setItem('page', page);
    initPageShow(JSON.parse(localStorage.getItem('data')));
}


