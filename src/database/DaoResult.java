package database;

import com.alibaba.fastjson.JSONObject;

public class DaoResult {
    public boolean success;//操作是否成功
    public String msg;//操作结果补充信息，尤其是不成功时返回更多信息
    public Object extra;//附加信息

    /**
     * 生成错误返回结果的json字符串
     * @param msg 错误信息
     * @return
     */
    public static String fail(String msg){
        JSONObject json = new JSONObject();
        json.put("success", false);
        json.put("msg", msg);
        return json.toJSONString();
    }
}
