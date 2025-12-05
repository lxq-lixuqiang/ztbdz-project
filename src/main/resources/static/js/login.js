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
                                url: "/user/updatePassword?userId="+e.data.userId+"&password=123456&newPassword="+newPassword,
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