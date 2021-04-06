var stringUtil = {
    /**
     * 字符串格式化
     * @param src 参数列表 形如"x={0},y={1}",x,y
     * @return 格式化之后的字符串
     */
    format:function(src){
        if (arguments.length == 0) return null;
        var args = Array.prototype.slice.call(arguments, 1);
        return src.replace(/\{(\d+)\}/g, function(m, i){
            return args[i];
        });
    }
}