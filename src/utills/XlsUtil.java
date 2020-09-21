package utills;

import com.alibaba.fastjson.JSONObject;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供Excel表格的读取和填充
 */
public class XlsUtil {
    public static void main(String[] args) {
        try {
            InputStream in = new FileInputStream("D:/dnrz/student.xls");
            InputStreamReader reader = new InputStreamReader(in, "UTF-8");
            List<JSONObject> data = read(in,"学员信息表","元数据");
            String str = JSONObject.toJSONString(data);
            System.out.println(str);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取xls文件数据
     * @param is 文件输入流
     * @param sheetName_data xls文件中提供数据的表格名称
     * @param sheetName_meta xls文件中提供元数据（表结构）的表格名称
     * @return
     */
    public static List<JSONObject> read(InputStream is, String sheetName_data, String sheetName_meta){
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //读取元数据（表格结构数据）
        Sheet sheet_meta = workbook.getSheet(sheetName_meta);
        if(null == sheet_meta){
            return null;
        }
        List<Map<String,String>> meta = readMeta(sheet_meta);

        //读取数据
        Sheet sheet_data = workbook.getSheet(sheetName_data);
        if(null == sheet_data){
            return null;
        }
        return readData(sheet_data,meta);
    }

    /**
     * 按照元数据解析数据
     * @param sheet 存储有数据的表
     * @param meta 元数据
     * @return 数据集合（键值对格式）
     */
    private static List<JSONObject> readData(Sheet sheet, List<Map<String, String>> meta) {
        List<JSONObject> data = new ArrayList<>();
        out:for(int line=1; ; line++) {
            int colIndex = 0;
            JSONObject o = new JSONObject();

            for(Map<String,String> map:meta){//遍历元数据
                String v = null;//这里经常会出现数组越界问题
                try {
                    Cell cell = sheet.getCell(colIndex++,line);
                    v = cell.getContents();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(v==null){//判断数据是否为空，为空则跳出循环，这里导致所填数据字段不能为空
                    break out;
                }

                String field = map.get("field");
                String type = map.get("type");
                String isNull = map.get("isNull");

                if(isNull.equals("false") && v.trim().isEmpty()){//判断数据是否为空，为空则跳出循环，这里导致所填数据字段不能为空
                    break out;
                }

                Object value = convert(v, type);//数据类型转换
                o.put(field, value);//封装对象
            }
            data.add(o);
        }
        return data;
    }

    /**
     * 读取元数据
     * @param sheet 存储有元数据的表
     * @return 元数据
     */
    private static List<Map<String,String>> readMeta(Sheet sheet){
        List<Map<String,String>> meta = new ArrayList<>();
        String field,type,isNull;
        for(int i=1; i<sheet.getRows(); i++) {
            field = sheet.getCell(0, i).getContents();//字段名
            type = sheet.getCell(1, i).getContents();//类型
            isNull = sheet.getCell(2, i).getContents().toLowerCase();//是否允许为空
            if(field.isEmpty()){//如果字段为空，跳出循环
                break;
            }
            Map<String,String> map = new HashMap<>();//使用map封装字段名和类型
            map.put("field",field);
            map.put("type",type);
            map.put("isNull",isNull);
            meta.add(map);
        }
        return meta;
    }

    /**
     * 数据类型转换，对于枚举类型的原始数据的形式可能是“显示值_枚举值”，只提取后面部分的，前面的往往是给用户看的
     * @param value 原始数据
     * @param type 目标类型
     * @return 转换之后的数据
     */
    private static Object convert(String value, String type) {
        switch (type){
            case "string":
                return value;
            case "enum":
                return Integer.parseInt(value.split("_")[1]);
            case "integer":
                return value.isEmpty()?0:Integer.parseInt(value);
            case "float":
                return value.isEmpty()?0:Float.parseFloat(value);
            case "double":
                return value.isEmpty()?0:Double.parseDouble(value);
        }
        return null;
    }
}
