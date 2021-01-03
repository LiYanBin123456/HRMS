package database;

public class QueryKey {
    private String k;
    private String o;
    private Object v;

    public void set(String k, String o, Object v){
        this.k = k;
        this.o = o;
        this.v = v;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public Object getV() {
        return v;
    }

    public void setV(Object v) {
        this.v = v;
    }

    @Override
    public String toString() {
        /*switch (o){
            case "like":
                return String.format("%s %s '%%?%%'", k, o);
            case "in":
                return String.format("%s %s (?)", k, o);
            default:
                return String.format("%s %s ?", k, o);
        }*/
        if(o.equals("in")){
            return String.format("%s %s (?)", k, o);
        }
        if(o.equals("like")){
            v = "%"+v+"%";
        }
        return String.format("%s %s ?", k, o);
    }
}
