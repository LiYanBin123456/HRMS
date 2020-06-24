package database;

public class Pagination {
    public boolean need;
    public int page;
    public int size;

    public void set(boolean need){
        this.need = need;
    }

    public void set(boolean need, int page, int size){
        this.need = need;
        this.page = page;
        this.size = size;
    }
}
