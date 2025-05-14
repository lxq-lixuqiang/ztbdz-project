//算术加
Handlebars.registerHelper("addition", function(v1, v2, options) {
    return v1 + v2;
});

//算术减
Handlebars.registerHelper("subtraction", function(v1, v2, options) {
    return v1 - v2;
});

//相等比较器
Handlebars.registerHelper("eq", function(v1, v2, options) {
    if(v1 == v2) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});

//不等比较器
Handlebars.registerHelper("ne", function(v1, v2, options) {
    if(v1 != v2) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});

//大于比较器
Handlebars.registerHelper("gt", function(v1, v2, options) {
    if(v1 > v2) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});

//大于等于比较器
Handlebars.registerHelper("ge", function(v1, v2, options) {
    if(v1 >= v2) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});

//小于比较器
Handlebars.registerHelper("lt", function(v1, v2, options) {
    if(v1 < v2) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});

//小于等于比较器
Handlebars.registerHelper("le", function(v1, v2, options) {
    if(v1 <= v2) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }
});

//日期格式化
Handlebars.registerHelper("formatDate", function(date, pattern) {
    return moment(date).format(pattern);
});

//是否大于当前日期 比较器
Handlebars.registerHelper("gtNowDate", function(v1, options) {
    var nowDate = new Date().getTime();
    var v1Date = new Date(v1).getTime();
    if(v1Date >= nowDate){
        return options.fn(this);
    }else {
        return options.inverse(this);
    }
});
