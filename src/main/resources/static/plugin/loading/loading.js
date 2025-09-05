function showLoading(content){
    $("body").append("<div id='loading' class=\"loader\">\n" +
        "  <h1></h1>\n" +
        "  <div class=\"loading_box\">\n" +
        "    <div class=\"symbol\">\n" +
        "      <p>"+content+"...</p>\n" +
        "      <div></div>\n" +
        "    </div>\n" +
        "  </div>\n" +
        "</div>");
}

function loading(){
    showLoading("加载中");
}
function importLoading(){
    showLoading("导入数据中");
}

function closeLoading(){
    $("#loading").remove();
}