// moneyFormatter = function(val){
// //金额转换 分->元 保留2位小数 并每隔3位用逗号分开 1,234.56
//     var str = (val/100).toFixed(2) + '';
//     var intSum = str.substring(0,str.indexOf(".")).replace( /\B(?=(?:\d{3})+$)/g, ',' );//取到整数部分
//     var dot = str.substring(str.length,str.indexOf("."))//取到小数部分搜索
//     var ret = intSum + dot;
//     return ret;
// }

function moneyFormatter(val) {
    if(val == '' || val == null || val == undefined) {
        return "0";
    }

    var str = parseFloat(val).toFixed(2) + '';
    var intSum = str.substring(0,str.indexOf(".")).replace( /\B(?=(?:\d{3})+$)/g, ',' );//取到整数部分
    var dot = str.substring(str.length,str.indexOf("."))//取到小数部分搜索
    var ret = intSum + dot;
    return ret;
}