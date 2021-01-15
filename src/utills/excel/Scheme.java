package utills.excel;

import java.util.ArrayList;
import java.util.List;

//Excel表结构
public class Scheme {
    private List<Field> fields;

    public Scheme() {
        fields = new ArrayList<>();
    }

    public void addField(Field field){
        fields.add(field);
    }

    public List<Field> getFields() {
        return fields;
    }
}
