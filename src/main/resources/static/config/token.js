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
            removeToken();
            alert("token失效，请重新登陆！");
            location.href = "/login.html";
        }
    });
    $.ajax({
        url: "/user/verifyLogin",
        type: "POST",
        async: false,
        contentType: "application/json",
        success:function(e) {
            if(e.status == 200){
                setToken(e.data.token); // 刷新token保持活跃状态
                setMember(e.data.member);
            }else{
                alert(e.message);
                location.href = "/login.html";
            }
        }
    });
}

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

