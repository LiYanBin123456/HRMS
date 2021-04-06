var dateUtil = {


    /**
     * 日期解析
     * @param 日期字符串 "yyyy-mm-dd"
     * @return {Date} 日期对象，解析失败则返回当前日期
     */
    parse:function(s){
        if (!s) return new Date();
        var ss = (s.split('-'));
        var y = parseInt(ss[0],10);
        var m = parseInt(ss[1],10);
        var d = parseInt(ss[2],10);
        if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
            return new Date(y,m-1,d);
        } else {
            return new Date();
        }
    },

    /**
     * 日期格式化
     * @param date 日期对象
     * @return {string} 日期字符串"yyyy-mm-dd"
     */
    format:function(date) {
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
    },


    format_date:function (timestamp) {
        if(timestamp) {
            var d = new Date(timestamp);
            var year = d.getFullYear();
            var month = d.getMonth() + 1;
            var date = d.getDate();
            return year + "-" + month + "-" + date;
        }
        return "";
    },

    format_dateTime:function(timestamp) {
        var time = new Date(timestamp);
        var year=time.getFullYear();
        var month=time.getMonth()+1;
        var date=time.getDate();
        var hour=time.getHours();
        var minute=time.getMinutes();
        return year+"-"+month+"-"+date+" "+hour+":"+minute;
    },

    format_month: function(timestamp) {
        if(timestamp) {
            var d = new Date(timestamp);
            var year = d.getFullYear();
            var month = d.getMonth() + 1;
            return year + "-" + month;
        }
        return "";
    }

}