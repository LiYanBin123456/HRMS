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
    public List<Items> getItemList(){
        List<Items> itemList = new ArrayList<>();
        String str = getItems();
        if(str!=null&&str.length()>0){
            String maps[] = str.split(";");
            for (String map:maps){
                String value[] = map.split(",");
                Items items = new Items();
                items.setField(value[0]);
                items.setType(Short.parseShort(value[1]));
                itemList.add(items);
            }
        }
        return itemList;
    }
    
}
