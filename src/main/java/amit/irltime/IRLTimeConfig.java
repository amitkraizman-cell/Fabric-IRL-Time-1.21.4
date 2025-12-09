package amit.irltime;

public class IRLTimeConfig {

    public enum Corner {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }

    public boolean enabled = true;
    public boolean use24h = true;
    public boolean showSeconds = true;
    public Corner corner =Corner.TOP_RIGHT;
}
