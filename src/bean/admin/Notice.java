package bean.admin;

public class Notice {
    private long id;
    private String title;//标题
    private String brief;//摘要
    private String content;//正文
    private String publisher;//发布人
    private String date;//发布时间

    public Notice() {
    }

    public Notice(long id, String title, String brief, String content, String publisher, String date) {
        this.id = id;
        this.title = title;
        this.brief = brief;
        this.content = content;
        this.publisher = publisher;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", brief='" + brief + '\'' +
                ", content='" + content + '\'' +
                ", publisher='" + publisher + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
