package database;

import com.alibaba.fastjson.JSONObject;

public class QueryParameter {
    public static void main(String[] args) {
        QueryParameter p = new QueryParameter();
        p.order.set(false);
        p.pagination.set(false);
        p.addCondition("status","!=","Finished");
        p.conditions.extra = "xx";

        System.out.println(JSONObject.toJSONString(p));
    }

    public QueryConditions conditions;//查询条件
    public QueryOrder order;//排序设置
    public Pagination pagination;//分页设置

    public QueryParameter() {
        this.conditions = new QueryConditions();
        this.order = new QueryOrder();
        this.pagination = new Pagination();
        this.order.set(false);
        this.pagination.set(false);
    }

    public void addCondition(String k, String o, Object v){
        conditions.add(k,o,v);
    }
}
