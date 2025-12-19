$(function(){
    keypress13('username','loginBtn');
    keypress13('password','loginBtn');

    // 登录按钮
    $('#login').on('submit', function(e){
        e.preventDefault();
        var username = $("input[name=username]").val();
        var password = $("input[type=password]").val();
        if(username.trim() == "" || password.trim() == ""){
            alert("用户名和密码不能为空！");
            return;
        }
        $.ajax({
            url: "/user/login",
            type: "POST",
            data: JSON.stringify({"username":username.trim(),"password":password.trim()}),
            contentType: "application/json",
            success:function(e) {
                if(e.status == 200){
                    if(e.data.isPassword){
                        var newPassword = prompt("检测到使用的是默认密码，保障账号安全请设置新密码:");
                        if(newPassword.trim()){
                            if(!regBox.regPassword.regEx.test(newPassword)){
                                alert(regBox.regPassword.message);
                                return;
                            }
                            $.ajax({
                                url: "/user/updatePassword?userId="+e.data.userId+"&newPassword="+newPassword,
                                type: "POST",
                                contentType: "application/json",
                                success:function(e2) {
                                    if(e2.status == 200) {
                                        alert("更新密码成功，请使用新密码登录！");
                                    }else{
                                        alert("更新失败："+e2.message);
                                    }
                                }
                            });
                        }
                    }else{
                        setToken(e.data.token);
                        location.href = "/"+e.data.url+".html";
                    }
                }else{
                    alert("登录失败："+e.message);
                }
            }
        });
    });
})

var announcementModalHTML = $("#announcementModal").html();
uploadFileInfo("field1","fileId1");
uploadFileInfo("field2","fileId2");
function forgetPassword(){
    $("#announcementModal").show();
}

function closeModal(){
    $("#announcementModal").hide();
}

function showUploadDialog(name) {
    document.getElementById(name).click();
}

function submit(){
    $("#gonggao").attr("disabled","true");
    var username = $("#username").val();
    var newPassword = $("#newPassword").val();
    var remark = $("#remark").val();
    var field1 = $("#field1").attr("data-file-id");
    var field2 = $("#field2").attr("data-file-id");

    if(!username || !newPassword || !field1 || !field2) {
        alert("请填写带 * 字段为必填项！");
        $("#gonggao").removeAttr("disabled");
        return;
    }

    if(!regBox.regPassword.regEx.test(newPassword)){
        alert(regBox.regPassword.message);
        $("#gonggao").removeAttr("disabled");
        return ;
    }

    $.ajax({
        url: "/retrievePassword/addORupdate",
        type: "POST",
        data: JSON.stringify({"username":username.trim(),"newPassword":newPassword.trim(),"businessLicenseId":field1,"socialSecurityCertificateId":field2,"remark":remark}),
        contentType: "application/json",
        success:function(e) {
            $("#gonggao").removeAttr("disabled");
            if(e.status == 200){
                closeModal();
                $("#announcementModal").html(announcementModalHTML);
                alert("已提交审核中，可通过“查询审核状态”按钮可查看审核状态！");
            }else{
                alert("提交失败："+e.message);
            }
        }
    });
}

function selectState(){
    var username  = prompt("请输入公司名称（用户名）:");
    if(!username.trim()){
        alert("必须填写公司名称（用户名）");
        selectState();
        return ;
    }
    $.ajax({
        url: "/retrievePassword/selectReview/"+username.trim(),
        type: "GET",
        contentType: "application/json",
        success:function(e) {
            if(e.status == 200){
                alert(e.message);
            }else{
                alert("查询失败："+e.message);
            }
        }
    });


}