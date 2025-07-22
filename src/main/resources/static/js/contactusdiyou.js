$(function() {
    $(".contactusdiyou").hover(function() {
        $(".hoverimg").attr("src","images/hoverbtnbg1.gif");
        $('.diyoumask').fadeIn();
        $('.contactusdiyou').animate({right:'0'},300);
    }, function() {
        $(".hoverimg").attr("src","images/hoverbtnbg.gif");
        $('.contactusdiyou').animate({right:'-230px'},300,function(){});
        $('.diyoumask').fadeOut();
    });

    var contactusdiyou = '<div class="hoverbtn">\n' +
        '\t\t<span>联</span><span>系</span><span>我</span><span>们</span>\n' +
        '\t\t<img class="hoverimg" src="images/hoverbtnbg.gif" height="9" width="13">\n' +
        '\t</div>\n' +
        '\t<div class="conter">\n' +
        '\t\t<div class="con1"> \n' +
        '\t\t\t<dl class="fn_cle">\n' +
        '\t\t\t\t<dt><img src="images/tel.png" height="31" width="31"></dt>\n' +
        '\t\t\t\t<dd class="f1">'+config.consultPhone.split("：")[0]+'：</dd>\n' +
        '\t\t\t\t<dd class="f2"><span class="ph_num">'+config.consultPhone.split("：")[1]+'</span></dd>\n' +
        '\t\t\t\t<dd class="f1">'+config.skillPhone.split("：")[0]+'：</dd>\n' +
        '\t\t\t\t<dd class="f2"><span class="ph_num">'+config.skillPhone.split("：")[1]+'</span></dd>\n' +
        '\t\t\t</dl>\n' +
        '\t\t</div>\n' +
        // '\t\t<div class="blank0"></div>\n' +
        // '\t\t<div class="qqcall"> \n' +
        // '\t\t\t<dl class="fn_cle">\n' +
        // '\t\t\t\t<dt><img src="images/zxkfqq.png" height="31" width="31"></dt>\n' +
        // '\t\t\t\t<dd class="f1 f_s14">在线客服：</dd>\n' +
        // '\t\t\t\t<dd class="f2 kefuQQ">\n' +
        // '\t\t\t\t\t<span>客服一</span>\n' +
        // '\t\t\t\t\t<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=1141797433&amp;site=qq&amp;menu=yes"></a>\n' +
        // '\t\t\t\t\t<span>客服二</span>\n' +
        // '\t\t\t\t\t<a target="_blank" href="http://yyson.com/index.html"></a>\n' +
        // '\t\t\t\t</dd>\n' +
        // '\t\t\t</dl>\n' +
        // '\t\t\t<div class="blank0"></div>\n' +
        // '\t\t</div> \n' +
        // '\t\t<div class="blank0"></div>\n' +
        // '\t\t<div class="weixincall"> \n' +
        // '\t\t\t<dl class="fn_cle">\n' +
        // '\t\t\t\t<dt><img src="images/weixin.png" height="31" width="31"></dt>\n' +
        // '\t\t\t\t<dd class="f1">官方微信站：</dd>\n' +
        // '\t\t\t\t<dd class="f3"><img src="" height="73" width="73"></dd>\n' +
        // '\t\t\t</dl>\n' +
        // '\t\t</div> \n' +
        // '\t\t<div class="blank0"></div>\n' +
        // '\t\t<div class="dytimer">\n' +
        // '\t\t\t<span style="font-weight: bold;">公司地址：</span>\n' +
        // '\t\t\t<span>四川省成都市锦江区工业园区墨香路87号8栋4楼</span>\n' +
        // '\t\t</div>' +
        '\t\t</div>\n' +
        '\t</div>';
    $(".contactusdiyou").html(contactusdiyou);
});