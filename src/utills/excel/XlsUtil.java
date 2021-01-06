package utills.excel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;

import java.io.*;
import java.util.List;

/**
 * 提供Excel表格的读取和填充
 */
public class XlsUtil {
    public static void main(String[] args) {
        try {
            File file = new File("d:\\test.xls");

            Scheme scheme = new Scheme();
            scheme.addField(new Field(0,"f1","姓名",Field.STRING,100));
            scheme.addField(new Field(1,"f2","年龄",Field.INT,100));
            scheme.addField(new Field(2,"f3","成绩",Field.FLOAT,100));

            //写数据测试
            FileOutputStream os = new FileOutputStream(file);
            String sheetNames = "test1";
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

            XlsUtil.write(os,sheetNames,scheme,data);

            //读数据测试
//            FileInputStream is = new FileInputStream(file);
//            JSONArray data = XlsUtil.read(is,scheme,1);
//            System.out.println(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取xls文件数据（多sheet）
     * @param is 文件输入流
     * @param schemes 表结构集合
     * @param rowIndexs 表数据起始行号集合，从0开始
     * @return 表数据集合
     */
    public static JSONArray[] read(InputStream is, Scheme []schemes, int []rowIndexs){
        try {
            JSONArray[]datas = new JSONArray[schemes.length];
            Workbook book = Workbook.getWorkbook(is);
            for(int i=0; i<schemes.length; i++){
                Sheet sheet = book.getSheet(i);
                datas[i] = new JSONArray();
                readSheet(sheet,schemes[i],rowIndexs[i],datas[i]);
            }
            book.close();
            return datas;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取xls文件数据(单个sheet)
     * @param is 文件输入流
     * @param scheme 表结构
     * @param rowIndex 表数据起始行号，从0开始
     * @return 表数据
     */
    public static JSONArray read(InputStream is, Scheme scheme, int rowIndex){
        try {
            JSONArray data = new JSONArray();
            Workbook book = Workbook.getWorkbook(is);
            Sheet sheet = book.getSheet(0);
            readSheet(sheet,scheme,rowIndex,data);
            book.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 向xls写入数据（单sheet）
     * @param os 输出流
     * @param sheetName sheet名
     * @param scheme 表结构定义
     * @param data 表数据
     */
    public static void write(OutputStream os, String sheetName, Scheme scheme, JSONArray data){
        try {
            WritableWorkbook book = Workbook.createWorkbook(os);
            WritableSheet sheet = book.createSheet(sheetName, 0);
            writeSheet(sheet,scheme,data);
            book.write();
            book.close();
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
    public static void write(OutputStream os, String []sheetNames, Scheme []schemes, JSONArray[]datas){
        try {
            WritableWorkbook book = Workbook.createWorkbook(os);
            for(int i=0; i<sheetNames.length; i++){
                WritableSheet sheet = book.createSheet(sheetNames[i], i);
                writeSheet(sheet,schemes[i],datas[i]);
            }
            book.write();
            book.close();
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
    private static boolean readSheet(Sheet sheet, Scheme scheme, int rowIndex, JSONArray records) {
        List<Field> fields = scheme.getFields();
        int rows = sheet.getRows();
        for(int row=rowIndex; row<rows ; row++) {
            JSONObject record = new JSONObject();//对应表格的一条记录
            for (Field f:fields) {
                Cell cell = sheet.getCell(f.col, row);
                String v = cell.getContents();

                //如果该字段不允许为空，而字段为空则读取失败
                if (!f.isNull && v.isEmpty()) {
                    return false;
                }

                switch (f.type) {
                    case Field.STRING:
                        record.put(f.name, v);
                        break;
                    case Field.ENUM:
                        record.put(f.name, Integer.parseInt(v.split("_")[1]));
                        break;
                    case Field.INT:
                        record.put(f.name, v.isEmpty() ? 0 : Integer.parseInt(v));
                        break;
                    case Field.LONG:
                        record.put(f.name, v.isEmpty() ? 0 : Long.parseLong(v));
                        break;
                    case Field.FLOAT:
                        record.put(f.name, v.isEmpty() ? 0 : Float.parseFloat(v));
                        break;
                    case Field.DOUBLE:
                        record.put(f.name, v.isEmpty() ? 0 : Double.parseDouble(v));
                        break;
                }
            }
            records.add(record);
        }
        return true;
    }

    /**
     * 向sheet写入数据
     * @param sheet 表
     * @param scheme 表结构
     * @param data 表数据
     * @throws WriteException
     */
    private static void writeSheet(WritableSheet sheet, Scheme scheme, JSONArray data) throws WriteException {
        List<Field> fields = scheme.getFields();
        WritableCellFormat cf1 = new WritableCellFormat(NumberFormats.FLOAT);
        WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.INTEGER);
        for (Field f:fields) {
            sheet.addCell(new Label(f.col, 0, f.title));//表头

            String fieldName = f.name;
            for (int row = 0; row < data.size(); row++) {
                JSONObject record = (JSONObject) data.get(row);
                WritableCell cell;
                switch (f.type){
                    case Field.DOUBLE:
                        cell = new Number(f.col,row+1, record.getDouble(fieldName),cf1);
                        break;
                    case Field.FLOAT:
                        cell = new Number(f.col,row+1, record.getFloat(fieldName),cf1);
                        break;
                    case Field.INT:
                        cell = new Number(f.col,row+1, record.getInteger(fieldName),cf2);
                        break;
                    case Field.LONG:
                        cell = new Number(f.col,row+1, record.getLong(fieldName),cf2);
                        break;
                    default:
                        cell = new Label(f.col, row+1, record.getString(fieldName));
                }
                sheet.addCell(cell);
            }
        }
    }
}
