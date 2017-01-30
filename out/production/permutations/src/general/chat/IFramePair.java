package general.chat;

/**
 * Created by garad on 2016-12-23.
 */
public class IFramePair {
    private String top;
    private String bottom;
    public IFramePair(String top, String bottom)
    {
        this.top = top;
        this.bottom = bottom;
    }

    public String getBottom() {
        return bottom;
    }

    public String getTop() {
        return top;
    }

    public void setBottom(String bottom) {
        this.bottom = bottom;
    }

    public void setTop(String top) {
        this.top = top;
    }
    @Override
    public String toString() {
        return this.top+this.bottom;
    }
}
