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
            url: "/user/login?username="+username+"&password="+password,
            type: "POST",
            contentType: "application/json",
            success:function(e) {
                if(e.status == 200){
                    setToken(e.data.token);
                    var roleType = e.data.type;
                    if(roleType == "admin"){ // 管理员
                        location.href = "/audit.html";
                    }else if(roleType == "expert"){ // 专家
                        location.href = "/expert.html";
                    }else if(roleType == "tenderee"){ // 招标方
                        location.href = "/tenderee.html";
                    }else if(roleType == "bidder"){ // 投标方
                        location.href = "/bider.html";
                    }else if(roleType == "finance"){ // 财务
                        location.href = "/finance.html";
                    }else if(roleType == "manage"){ // 项目经理
                        location.href = "/manage.html";
                    }else if(roleType == "expertSelect"){ // 抽取专家员
                        location.href = "/expert-select.html";
                    }
                }else{
                    alert("登录失败："+e.message);
                }
            }
        });
    });

})