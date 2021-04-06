var validator = {
    /**
     * 长度验证，要求元素定义两个属性：
     * lay-length,其取值为"2-4",表示最小为2，最大为4
     * lay-title,数据项的名称，在提示错误信息需要用到
     * @param value 待验证的值
     * @param element 待验证的表单元素
     */
    length:function(value, element) {
        try{
            value = Number(value);
            var length = element.getAttribute('lay-length');
            var title = element.getAttribute('lay-title');
            var min = Number(length.split("-")[0]);
            var max = Number(length.split("-")[1]);
            if(value.length<min || value.length>max){
                return title+"的长度范围应为["+min+","+max+"]";
            }
        }catch (err){
            return "元素定义错误";
        }
    },

    range:function (value,element) {
        try{
            value = Number(value);
            var range = element.getAttribute('lay-range');
            var title = element.getAttribute('lay-title');
            var min = Number(range.split("-")[0]);
            var max = Number(range.split("-")[1]);
            if(value<min || value>max){
                $(element).focus();
                return title+"的取值范围应为["+min+","+max+"]";
            }
        }catch (err){
            return "元素定义错误";
        }
    }
};