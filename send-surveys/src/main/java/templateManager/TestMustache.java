package templateManager;

import java.util.Date;

public class TestMustache {

    private String title;
    private String text;
    private Date createdOn;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public TestMustache() {
    }

    public TestMustache(String title, String text, Date createdOn) {
        this.title = title;
        this.text = text;
        this.createdOn = createdOn;
    }
}
