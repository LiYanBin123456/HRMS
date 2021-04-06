var collectionUtil = {
    /**
     * 在集合中按照字段查找元素
     * @param {Array} arr 数据集合
     * @param {String} key 字段名
     * @param {*} value 值
     */
    getElement:function(arr,key,value){
        for(var i in arr){
            if(arr[i][key] == value){
                return arr[i];
            }
        }
        return undefined;
    },

    /**
     * 在集合中按照字段查找元素
     * @param {Array} arr 数据集合
     * @param {String} key 字段名
     * @param {*} value 值
     */
    exist:function(arr,key,value){
        return this.getElement(arr,key,value)!=undefined;
    },

    /**
     * 获取由对象数组指定属性组成的数组，如[1,2,3]
     * @param array 对象数组
     * @param key 属性
     * @return {Array}
     */
    getKeyArray:function(array,key){
        var keys = new Array();
        for(var i in array){
            if($.inArray(array[i][key],keys) == -1) {
                keys.push(array[i][key]);
            }
        }
        return keys;
    },

    /**
     * 获取由对象数组指定属性组成的字符串序列，如"1,2,3"
     * @param array 对象数组
     * @param key 属性
     * @return {Array}
     */
    getKeySerial:function(array,key){
        return this.getKeyArray(array,key).toString();
    },

    /**
     * 获取对象数组的投影
     * @param array
     * @param keys 属性集合
     * @return {Array}
     */
    map:function (array,keys) {
        var res = [];
        for(var i in array){
            var o = {};
            for(var j in keys){
                var key = keys[j];
                o[key] = array[i][key];
            }
            res.push(o);
        }
        return res;
    }

}