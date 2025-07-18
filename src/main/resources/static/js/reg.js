$(function() {

    // 注册按钮
    $("body").on("click", "button[type=submit]", function() {
        var username = $("input[name=username]").val();
        var password = $("input[name=password]").val();
        var userType = $("select[name=user_type]").find("option:selected").val();
        if(username == "" || password == ""){
            alert("用户名和密码不能为空！");
            return;
        }
        if(userType == ""){
            alert("请选择用户类型！");
            return;
        }
        if(!regBox.regName.test(username)){
            alert("用户名必须是2~16位的字母数字_-");
            return ;
        }
        if(!regBox.regPassword.test(password)){
            alert("密码必须是6~16位的字母数字@$_-");
            return ;
        }

        var account = {accountName:username};
        var role = {type:userType};
        var member = {name:username,account:account,role:role};
        var data = {member:member,password:password,username:username};
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
                    alert("注册失败："+e.message);
                }
            }
        });
    });



});