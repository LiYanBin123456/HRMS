package utills;

import bean.insurance.Product;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {
    public static void main(String[] args) {
        /*byte b = 0;
        Product d1 = new Product(1L,1L,"name1",0,0,0,b,b,b,b,"");
        Product d2 = new Product(2L,1L,"name2",0,0,0,b,b,b,b,"");
        Product d3 = new Product(3L,1L,"name3",0,0,0,b,b,b,b,"");
        List<Product> products = new ArrayList<>();
        products.add(d1);
        products.add(d2);
        products.add(d3);

        Product d = CollectionUtil.getElement(products,"id",4L);
        System.out.println(d);*/

    }

    /**
     * 从集合中按照主键获取元素
     * @param collection 集合
     * @param key 键名，要保证元素有对应的get方法，如key="id",则应有方法名getId
     * @param value 键值，应为long或者int类型
     * @param <T> 元素类型
     * @return 获取的元素，没有则返回null，
     */
    public static <T> T getElement(List<T> collection,String key,long value){
        Method method = null;
        long v;
        try {
            for(T o:collection){
                if(method == null) {
                    String functionName = String.format("get%s%s",key.substring(0,1).toUpperCase(),key.substring(1));
                    method = o.getClass().getMethod(functionName);
                }
                v = (long) method.invoke(o);
                if(v == value){
                    return o;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 从集合中按照主键获取元素
     * @param collection 集合
     * @param key 键名，要保证元素有对应的get方法，如key="id",则应有方法名getId
     * @param value 键值，应为long或者int类型
     * @param <T> 元素类型
     * @return 获取的元素，没有则返回null，
     */
    public static <T> T getElement(List<T> collection,String key,String value){
        Method method = null;
        String v;
        try {
            for(T o:collection){
                if(method == null) {
                    String functionName = String.format("get%s%s",key.substring(0,1).toUpperCase(),key.substring(1));
                    method = o.getClass().getMethod(functionName);
                }
                v = (String) method.invoke(o);
                if(value.equals(v)){
                    return o;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static<T> List<Object> getKeyArray(List<T> collection,String key){
        Method method = null;
        List<Object> res = new ArrayList<>();
        try {
            for(T o:collection){
                if(method == null) {
                    String functionName = String.format("get%s%s",key.substring(0,1).toUpperCase(),key.substring(1));
                    method = o.getClass().getMethod(functionName);
                }
                Object v = method.invoke(o);
                res.add(v);
            }
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    public static<T> String getKeySerial(List<T> collection,String key){
        Method method = null;
        String res = "";
        try {
            for(T o:collection){
                if(method == null) {
                    String functionName = String.format("get%s%s",key.substring(0,1).toUpperCase(),key.substring(1));
                    method = o.getClass().getMethod(functionName);
                }
                long v = (long)method.invoke(o);
                res += (v+",");
            }
            return res.substring(0,res.length()-1);
        } catch (Exception e) {
            return null;
        }
    }
}
