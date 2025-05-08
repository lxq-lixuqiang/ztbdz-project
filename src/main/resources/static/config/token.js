// 存储token到localStorage
function setToken(token) {
    localStorage.setItem('userToken', token);
}

function setMember(member) {
    localStorage.setItem('member', member);
}

// 从localStorage获取token
function getToken() {
    return localStorage.getItem('userToken');
}

// 从localStorage获取member
function getMember() {
    return localStorage.getItem('member');
}
// 删除token
function removeToken() {
    localStorage.removeItem('userToken');
    localStorage.removeItem('member');
}

// 校验token是否有效
function verifyLogin(){
    $.ajax({
        url: "/user/verifyLogin",
        type: "POST",
        async: false,
        headers: {'token': getToken()},
        contentType: "application/json",
        success:function(e) {
            if(e.status == 200){
                setMember(JSON.stringify(e.data.member));
            }else{
                alert("校验token失败："+e.message);
                location.href = "/login.html";
            }
        },error:function(e) {
            alert("token失效，请重新登陆！");
            location.href = "/login.html";
        }
    });
}

// 退出登录
$("body").on("click", "a:contains('退出')", function() {
    var token = getToken();
    removeToken();
    $.ajax({
        url: "/user/logout",
        type: "POST",
        headers: {'token': token},
        contentType: "application/json",
        success:function(e) {
            if(e.status != 200){
                alert("退出失败："+e.message);
            }
        }
    });
});

// 上传文件
function uploadFileWithJQuery(file) {
    var formData = new FormData();
    formData.append('file', file); // 'file'是后端接收的文件参数名
    var fileId = "";
    $.ajax({
        url: '/file/upload',
        type: 'POST',
        headers: {'token': getToken()},
        data: formData,
        async: false,
        contentType: false, // 告诉jQuery不要去设置Content-Type请求头
        processData: false, // 告诉jQuery不要去处理发送的数据
        success: function (e) {
            fileId = e.data;
        },
        error: function () {
            alert("上传文件失败！")
        }
    });
    return fileId;
}
