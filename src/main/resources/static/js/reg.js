$(function() {

    // 注册按钮
    $('#reg').on('submit', function(e) {
        e.preventDefault();

        $("button[type=submit]").attr("disabled","true");
        var username = $("input[name=username]").val();
        var password = $("input[name=password]").val();
        var userType = $("select[name=user_type]").find("option:selected").val();
        if(username == "" || password == ""){
            alert("用户名和密码不能为空！");
            $("button[type=submit]").removeAttr("disabled");
            return;
        }
        if(userType == ""){
            alert("请选择用户类型！");
            $("button[type=submit]").removeAttr("disabled");
            return;
        }
        // if(!regBox.regName.regEx.test(username)){
        //     alert(regBox.regName.message);
        //     $("button[type=submit]").removeAttr("disabled");
        //     return ;
        // }
        if(!regBox.regPassword.regEx.test(password)){
            alert(regBox.regPassword.message);
            $("button[type=submit]").removeAttr("disabled");
            return ;
        }

        var account = {accountName:""};
        var role = {type:userType};
        var member = {name:username.trim(),account:account,role:role};
        var data = {member:member,password:password.trim(),username:username.trim()};
        $.ajax({
            url: "/user/register",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            success:function(e) {
                if(e.status == 200){
                    alert(e.message);
                    location.href = "/login.html";
                }else{
                    $("button[type=submit]").removeAttr("disabled");
                    alert("注册失败："+e.message);
                }
            }
        });
    });



});