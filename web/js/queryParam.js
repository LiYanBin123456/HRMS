var QueryParam = function () {
    this.reset();
};

QueryParam.prototype = {
    constructor: QueryParam,

    reset: function () {
        this.conditions = {keys:[],extra:""};
        this.order = {need:false};
        this.pagination =  {need:false};
    },

    findCondition:function (k) {
        for(var i in this.conditions.keys){
            var key = this.conditions.keys[i];
            if(key.k == k){
                return i;
            }
        }
        return -1;
    },

    addCondition:function (k, o, v) {
        var index = this.findCondition(k);
        if(index == -1){
            this.conditions.keys.push({k:k,o:o,v:v});
        }else{
            var key = this.conditions.keys[index];
            key.o = o;
            key.v = v;
        }
    },

    removeCondition:function (k) {
      var index = this.findCondition(k);
      if(index != -1){
          this.conditions.keys.splice(index,1);
      }
    },

    addKeyword:function (keyword) {
        this.conditions.extra = keyword;
    },

    setOrder:function(field,direction){
        if(field){
            this.order.need = true;
            this.order.field = field;
            this.order.direction = direction;
        }
    },

    resetOrder:function () {
        this.order.need = false;
        this.order.field = undefined;
        this.order.direction = undefined;
    },

    setPagination:function(page,size){
        if(page && size){
            this.pagination.need = true;
            this.pagination.page = page;
            this.pagination.size = size;
        }
    },

    resetPagination:function () {
        this.pagination.need = false;
        this.pagination.page = undefined;
        this.pagination.size = undefined;
    },

    getPage:function () {
        return this.pagination.page;
    }
};