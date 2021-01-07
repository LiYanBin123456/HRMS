package database;

import java.util.ArrayList;
import java.util.List;

public class QueryConditions {
    public List<QueryKey> keys = new ArrayList<>();
    public String extra;//附加的条件，一般用于模糊查询

    public void add(List<QueryKey> keys){
        this.keys.addAll(keys);
    }

    public void add(String k, String o, Object v){
        QueryKey c = new QueryKey();
        c.set(k,o,v);
        keys.add(c);
    }

    @Override
    public String toString() {
        if(keys.size() == 0){
            return "true";
        }

        String condition = "";
        for(int i=0; i<keys.size(); i++){
            if(i == 0){
                condition += keys.get(i).toString();
            }else{
                condition += (" and "+keys.get(i).toString());
            }
        }

        return  condition;
    }

    /**
     * 从参数键值对集合中提取值域集合
     * @return 值域集合 [v1,v2]
     */
    public List<Object> extraValues(){
        List<Object> values = new ArrayList<>();
        for (QueryKey key:keys) {
            if(!key.getO().equals("in")) {
                values.add(key.getV());
            }
        }
        return values;
    }
}
