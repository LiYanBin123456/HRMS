package bean.client;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

//客户自定义工资
public class MapSalary {
    private long cid;//合作客户id
    private String items;//自定义名称集
    private Date date;//更新时间

    public MapSalary() {
    }

    public MapSalary(long cid, String items, Date date) {
        this.cid = cid;
        this.items = items;
        this.date = date;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * 获取Items集合
     * 返回结果[{type:1,field:考勤扣款},{type:0,field:加班工资}]
     * @return itemList；
     */
    public List<SalaryItem> getItemList(){
        List<SalaryItem> salaryItems = new ArrayList<>();
        if(items!=null&&items.length()>0){
            String maps[] = items.split(";");
            for (String map:maps){
                String value[] = map.split(",");
                SalaryItem item = new SalaryItem();
                item.setField(value[0]);
                item.setType(Short.parseShort(value[1]));
                salaryItems.add(item);
            }
        }
        return salaryItems;
    }

    public int numberOfItems(){
        if(items!=null&&items.length()>0){
            return items.split(";").length;
        }
        return 0;
    }

    public class SalaryItem {
        private short type;//类型
        private String field;//字段值

        public short getType() {
            return type;
        }

        public void setType(short type) {
            this.type = type;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }
    
}
