package utills.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;

import java.io.*;
import java.lang.Boolean;
import java.util.List;

/**
 * 提供Excel表格的读取和填充
 */
public class XlsUtil {
    public static void main(String[] args) {
        try {
            File file = new File("d:\\EmployeeContract.xls");

            Scheme scheme = new Scheme();
            scheme.addField(new Field(0,"f1","姓名",Field.STRING,100));
            scheme.addField(new Field(1,"f2","年龄",Field.INT,100));
            scheme.addField(new Field(2,"f3","成绩",Field.FLOAT,100));

            //写数据测试
            /*FileOutputStream os = new FileOutputStream(file);
            String []sheetNames = {"test1"};
            JSONObject o1 = new JSONObject();
            o1.put("f1","张三");
            o1.put("f2",12.0);
            o1.put("f3",98.5);
            JSONObject o2 = new JSONObject();
            o2.put("f1","李四");
            o2.put("f2",13.0);
            o2.put("f3",67.3);
            JSONArray data = new JSONArray();
            data.add(o1);
            data.add(o2);
            JSONArray []datas = {data};

            XlsUtil.write(os,sheetNames,schemes,datas);*/

            //读数据测试
            FileInputStream is = new FileInputStream(file);
            //JSONArray data = XlsUtil.read(is,scheme,1);
            JSONArray data =XlsUtil.read(is,"信息表","元数据");
            System.out.println(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取xls文件数据
     * @param is 文件输入流
     * @param sheetName_data xls文件中提供数据的表格名称
     * @param sheetName_meta xls文件中提供元数据（表结构）的表格名称
     * @return 表数据
     */
    public static JSONArray read(InputStream is, String sheetName_data, String sheetName_meta){
        JSONArray data = new JSONArray();
        try {
            Workbook book = Workbook.getWorkbook(is);
            //读取元数据（表格结构数据）
            Sheet sheet_meta = book.getSheet(sheetName_meta);
            Sheet sheet_data = book.getSheet(sheetName_data);
            if(null==sheet_meta || null==sheet_data){
                return null;
            }
            Scheme scheme = readMeta(sheet_meta);
            readSheet(sheet_data,scheme,1,data);
            book.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 读取xls文件数据（多sheet）
     * @param is 文件输入流
     * @param schemes 表结构集合
     * @param rowIndexs 表数据起始行号集合，从0开始
     * @return 表数据集合
     */
    public static JSONArray[] read(InputStream is, Scheme []schemes, int []rowIndexs){
        JSONArray[]datas = new JSONArray[schemes.length];
        try {
            Workbook book = Workbook.getWorkbook(is);
            for(int i=0; i<schemes.length; i++){
                Sheet sheet = book.getSheet(i);
                datas[i] = new JSONArray();
                readSheet(sheet,schemes[i],rowIndexs[i],datas[i]);
            }
            book.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    /**
     * 读取xls文件数据(单个sheet)
     * @param is 文件输入流
     * @param scheme 表结构
     * @param rowIndex 表数据起始行号，从0开始
     * @return 表数据
     */
    public static JSONArray read(InputStream is, Scheme scheme, int rowIndex){
        JSONArray data = new JSONArray();
        try {
            Workbook book = Workbook.getWorkbook(is);
            Sheet sheet = book.getSheet(0);
            readSheet(sheet,scheme,rowIndex,data);
            book.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 向xls写入数据（单sheet）
     * @param os 输出流
     * @param sheetName sheet名
     * @param scheme 表结构定义
     * @param data 表数据
     */
    public static void write(OutputStream os,String title, String sheetName, Scheme scheme, JSONArray data){
        try {
            WritableWorkbook book = Workbook.createWorkbook(os);
            WritableSheet sheet = book.createSheet(sheetName, 0);
            writeSheet(sheet,title,scheme,data);
            book.write();
            book.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取已有excel表然后写数据，不需要表头
     * @param os 文件输出流
     * @param template 模板文件
     * @param scheme 表结构
     * @param data 表数据
     */
    public static void write(OutputStream os,String template, Scheme scheme, JSONArray data){
        try {
            File file = new File(template);
            Workbook book_template = Workbook.getWorkbook(file);
            WritableWorkbook book = Workbook.createWorkbook(os,book_template);
            WritableSheet sheet = book.getSheet( 0);
            writeSheet2(sheet,scheme,data);
            book.write();
            book.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 向xls写入数据（多sheet）
     * @param os 输出流
     * @param sheetNames sheet名集合（支持多个sheet）
     * @param schemes 表结构定义集合（与sheet名集合对应，一个sheet对应一个表结构）
     * @param datas 表数据集合（与sheet名集合对应，一个sheet对应一个表数据）
     */
    public static void write(OutputStream os, String []sheetNames,String []titles, Scheme []schemes, JSONArray []datas){
        try {
            WritableWorkbook book = Workbook.createWorkbook(os);
            for(int i=0; i<sheetNames.length; i++){
                WritableSheet sheet = book.createSheet(sheetNames[i], i);
                writeSheet(sheet,titles[i],schemes[i],datas[i]);
            }
            book.write();
            book.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取sheet数据
     * @param sheet 工作表
     * @param scheme 表结构
     * @param records 读取之后的数据
     */
    private static void readSheet(Sheet sheet, Scheme scheme, int rowIndex, JSONArray records) {
        List<Field> fields = scheme.getFields();
        int rows = sheet.getRows();
        out:for(int row=rowIndex; row<rows ; row++) {
            JSONObject record = new JSONObject();//对应表格的一条记录
            for (Field f:fields) {
                Cell cell = sheet.getCell(f.col, row);
                String v = cell.getContents();

                //如果该字段不允许为空，而字段为空则结束读取
                if (!f.isNull && v.isEmpty()) {
                    break out;
                }

                record.put(f.name, convert(v,f.type));
            }
            records.add(record);
        }
    }

    /**
     * 向sheet写入数据
     * @param sheet 表
     * @param scheme 表结构
     * @param data 表数据
     * @throws WriteException
     */
    private static void writeSheet(WritableSheet sheet,String title, Scheme scheme, JSONArray data) throws WriteException {
        List<Field> fields = scheme.getFields();
        WritableCellFormat cf1 = new WritableCellFormat(NumberFormats.FLOAT);
        WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.INTEGER);
        int start = 0;//表头开始的行号
        if(!title.isEmpty()){
            start++;
            sheet.addCell(new Label(0, 0, title));//标题
            sheet.mergeCells(0,0,fields.size()-1,0);//合并单元格
        }
        for (Field f:fields) {
            sheet.addCell(new Label(f.col, start, f.title));//表头

            String fieldName = f.name;
            for (int row = 0; row < data.size(); row++) {
                JSONObject record = (JSONObject) data.get(row);
                WritableCell cell;
                switch (f.type){
                    case Field.DOUBLE:
                        cell = new Number(f.col,row+start+1, record.getDouble(fieldName),cf1);
                        break;
                    case Field.FLOAT:
                        cell = new Number(f.col,row+start+1, record.getFloat(fieldName),cf1);
                        break;
                    case Field.INT:
                        cell = new Number(f.col,row+start+1, record.getInteger(fieldName),cf2);
                        break;
                    case Field.LONG:
                        cell = new Number(f.col,row+start+1, record.getLong(fieldName),cf2);
                        break;
                    default:
                        cell = new Label(f.col, row+start+1, record.getString(fieldName));
                }
                sheet.addCell(cell);
            }
        }
    }

    /**
     * 向sheet写入数据,不需要写表头
     * @param sheet 表
     * @param scheme 表结构
     * @param data 表数据
     * @throws WriteException
     */
    private static void writeSheet2(WritableSheet sheet, Scheme scheme, JSONArray data) throws WriteException {
        List<Field> fields = scheme.getFields();
        WritableCellFormat cf1 = new WritableCellFormat(NumberFormats.FLOAT);
        WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.INTEGER);
        int start = 0;//表头开始的行号
        for (Field f:fields) {

            String fieldName = f.name;
            for (int row = 0; row < data.size(); row++) {
                JSONObject record = (JSONObject) data.get(row);
                WritableCell cell;
                switch (f.type){
                    case Field.DOUBLE:
                        cell = new Number(f.col,row+start+1, record.getDouble(fieldName),cf1);
                        break;
                    case Field.FLOAT:
                        cell = new Number(f.col,row+start+1, record.getFloat(fieldName),cf1);
                        break;
                    case Field.INT:
                        cell = new Number(f.col,row+start+1, record.getInteger(fieldName),cf2);
                        break;
                    case Field.LONG:
                        cell = new Number(f.col,row+start+1, record.getLong(fieldName),cf2);
                        break;
                    case Field.NULL:
                        cell = new Label(f.col, row+start+1, "");
                        break;
                    default:
                        cell = new Label(f.col, row+start+1, record.getString(fieldName));
                }
                sheet.addCell(cell);
            }
        }
    }

    /**
     * 读取元数据
     * @param sheet 存储有元数据的表
     * @return 元数据
     */
    private static Scheme readMeta(Sheet sheet){
        Scheme scheme = new Scheme();
        String name,type,isNull;
        for(int i=1; i<sheet.getRows(); i++) {
            name = sheet.getCell(0, i).getContents();//字段名
            type = sheet.getCell(1, i).getContents();//类型
            isNull = sheet.getCell(2, i).getContents().toLowerCase();//是否允许为空
            if(name.isEmpty()){//如果字段为空，跳出循环
                break;
            }
            Field field = new Field(i-1,name,type, Boolean.parseBoolean(isNull));
            scheme.addField(field);
        }
        return scheme;
    }

    /**
     * 数据类型转换，对于枚举类型的原始数据的形式可能是“显示值_枚举值”，只提取后面部分的，前面的往往是给用户看的
     * @param value 原始数据
     * @param type 目标类型
     * @return 转换之后的数据
     */
    private static Object convert(String value, String type) {
        switch (type){
            case Field.STRING:
                return value;
            case Field.ENUM:
                return Integer.parseInt(value.split("_")[1]);
            case Field.INT:
                return value.isEmpty()?0:Integer.parseInt(value);
            case Field.FLOAT:
                return value.isEmpty()?0:Float.parseFloat(value);
            case Field.DOUBLE:
                return value.isEmpty()?0:Double.parseDouble(value);
            case Field.LONG:
                return value.isEmpty()?0:Long.parseLong(value);
        }
        return null;
    }
}
