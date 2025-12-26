$(function() {
    $("input[value=0]").click(function(){
        $("#type1").show();
        $("#type2").hide();
    })
    $("input[value=1]").click(function(){
        $("#type1").hide();
        $("#type2").show();
    })
    uploadFileInfo("fileUpload","accountCodeFileId");
    uploadFileInfo("fileUpload2","qualificationCertificate01");
    uploadFileInfo("fileUpload3","qualificationCertificate01_01");
    uploadFileInfo("fileUpload4","qualificationCertificate02");
    uploadFileInfo("fileUpload5","qualificationCertificate03");

    // 注册按钮
    $('#reg').on('submit', function(e) {
        e.preventDefault();

        $("button[type=submit]").attr("disabled","true");
        var username = $("input[name=username]").val();
        var password = $("input[name=password]").val();
        var userType = $("select[name=user_type]").find("option:selected").val();
        var accountCode = $("input[name=accountCode]").val();
        var phone = $("input[name=phone]").val();
        var email = $("input[name=email]").val();
        var memberType = $("input[name=memberType]:checked").val();
        var accountCodeFileId = $("#fileUpload").attr("data-file-id");
        if(!username || !password || !userType || !accountCode || !phone || !email || !memberType || !accountCodeFileId) {
            alert("请填写带 * 字段为必填项！");
            $("button[type=submit]").removeAttr("disabled");
            return;
        }else if(!regBox.regPassword.regEx.test(password)){
            alert(regBox.regPassword.message);
            $("button[type=submit]").removeAttr("disabled");
            return ;
        }else if(!regBox.regEmail.regEx.test(email)){
            alert(regBox.regEmail.message);
            $("button[type=submit]").removeAttr("disabled");
            return ;
        }else if(!regBox.regMobilePhone.regEx.test(phone)){
            alert(regBox.regMobilePhone.message);
            $("button[type=submit]").removeAttr("disabled");
            return ;
        }
        var idCard="";
        var qualificationCertificate01="";
        var qualificationCertificate02 = "";
        var qualificationCertificate03 = "";
        var accountUser = "";
        var accountUser2 = "";
        if(memberType=='0'){
            // 法人
            accountUser = $("input[name=accountUser]").val();
            accountUser2 = accountUser;
            idCard = $("input[name=idCard]").val();
            qualificationCertificate01 = $("#fileUpload2").attr("data-file-id");
            if(!accountUser || !idCard || !qualificationCertificate01) {
                alert("请填写带 * 字段为必填项！");
                $("button[type=submit]").removeAttr("disabled");
                return;
            }
            if(!regBox.regIdCard.regEx.test(idCard)){
                alert(regBox.regIdCard.message);
                $("button[type=submit]").removeAttr("disabled");
                return ;
            }
        }else{
            // 授权代表
            accountUser = $("input[name=accountUser2]").val();
            idCard = $("input[name=idCard2]").val();
            qualificationCertificate01 = $("#fileUpload3").attr("data-file-id");
            qualificationCertificate02 = $("#fileUpload4").attr("data-file-id");
            qualificationCertificate03 = $("#fileUpload5").attr("data-file-id");
            if(!accountUser || !idCard || !qualificationCertificate01 || !qualificationCertificate02 || !qualificationCertificate03) {
                alert("请填写带 * 字段为必填项！");
                $("button[type=submit]").removeAttr("disabled");
                return;
            }
            if(!regBox.regIdCard.regEx.test(idCard)){
                alert(regBox.regIdCard.message);
                $("button[type=submit]").removeAttr("disabled");
                return ;
            }
        }

        var account = {accountName:username.trim(),accountUser:accountUser2,accountUserId:idCard,accountType:"0",accountCode:accountCode,member:accountUser,phone:phone,email:email,accountCodeFileId:accountCodeFileId};
        var role = {type:userType};
        var member = {name:accountUser,account:account,role:role};
        var data = {member:member,
                    notCheckShow:password.trim(),
                    memberType:memberType,
                    idCard:idCard,
                    qualificationCertificate01:qualificationCertificate01,
                    qualificationCertificate02:qualificationCertificate02,
                    qualificationCertificate03:qualificationCertificate03};
        $.ajax({
            url: "/bidderInfo/register",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(data),
            success:function(e) {
                if(e.status == 200){
                    alert(e.message);
                    location.href = "/login.html";
                }else{
                    $("button[type=submit]").removeAttr("disabled");
                    alert(e.message);
                }
            }
        });
    });
});
function showUploadDialog(name) {
    document.getElementById(name).click();
}