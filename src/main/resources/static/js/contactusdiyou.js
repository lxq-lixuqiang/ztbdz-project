$(function() {
    var isClose= true;
    $(".contactusdiyou").hover(function() {
        if(isClose) return;
        $(".hoverimg").attr("src","images/hoverbtnbg1.gif");
        $('.diyoumask').fadeIn();
        $('.contactusdiyou').animate({right:'0'},300);
    }, function() {
        $(".hoverimg").attr("src","images/hoverbtnbg.gif");
        $('.contactusdiyou').animate({right:'-230px'},300,function(){});
        $('.diyoumask').fadeOut();
    });

    $("body").click(function(){
        if(!isClose) return;
        $(".hoverimg").attr("src","images/hoverbtnbg.gif");
        $('.contactusdiyou').animate({right:'-230px'},300,function(){});
        $('.diyoumask').fadeOut();
        isClose= false;
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
        '\t\t</div>\n' +
        '\t</div>';
    $(".contactusdiyou").html(contactusdiyou);
});