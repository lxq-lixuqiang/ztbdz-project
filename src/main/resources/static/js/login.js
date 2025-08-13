$(function(){
    // 登录按钮
    $("body").on("click", "button[type=submit]", function() {
        var username = $("input[name=username]").val();
        var password = $("input[type=password]").val();
        if(username == "" || password == ""){
            alert("用户名和密码不能为空！");
            return;
        }
        $.ajax({
            url: "/user/login",
            type: "POST",
            data: JSON.stringify({"username":username,"password":password}),
            contentType: "application/json",
            success:function(e) {
                if(e.status == 200){
                    setToken(e.data.token);
                    location.href = "/"+e.data.url+".html";
                }else{
                    alert("登录失败："+e.message);
                }
            }
        });
    });

})