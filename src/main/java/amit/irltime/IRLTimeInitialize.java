package amit.irltime;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public class IRLTimeInitialize {
    static MinecraftClient client;
    static int screenWidth;
    static int x;
    static int y;
    static int intHour;
    static String text;

    public static IRLTimeConfig CONFIG;


    private static final DateTimeFormatter TIME_FORMATTER24 =
            DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter TIME_FORMATTER24_NO_SEC =
            DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter TIME_FORMATTER_HOUR =
            DateTimeFormatter.ofPattern("HH");
    private static final DateTimeFormatter TIME_FORMATTER_MINUTE =
            DateTimeFormatter.ofPattern("mm");
    private static final DateTimeFormatter TIME_FORMATTER_SECOND =
            DateTimeFormatter.ofPattern("ss");


    public static void registerIRLTimeInitialize(){
        IRLTime.LOGGER.info("Registering for " + IRLTime.MOD_ID);

        client = MinecraftClient.getInstance();

        CONFIG = ConfigManager.loadConfig();

        HudRenderCallback.EVENT.register((DrawContext drawContext, RenderTickCounter tickCounter) -> {
            if (client.options.hudHidden) return;
            if (!CONFIG.enabled) return;

            if(CONFIG.use24h){
                HourFormat24(drawContext);
            }
            else{
                HourFormat12(drawContext);
            }
        });
    }
    private static void HourFormat12(DrawContext drawContext){
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

        intHour=Integer.parseInt(LocalTime.now().format(TIME_FORMATTER_HOUR));
        intHour=ConvertTo12Hour(intHour);

        String localTime;

        if(!CONFIG.showSeconds){
            localTime=intHour+":";
            localTime+=LocalTime.now().format(TIME_FORMATTER_MINUTE);
            text ="Local Time: " + localTime + " " + AMorPM(LocalTime.now().getHour());
            ConfigurePlacement(text);
        }
        else {
            localTime=intHour+":";
            localTime+=LocalTime.now().format(TIME_FORMATTER_MINUTE)+":";
            localTime+=LocalTime.now().format(TIME_FORMATTER_SECOND);
            text ="Local Time: " + localTime + " " + AMorPM(LocalTime.now().getHour());
            ConfigurePlacement(text);
        }

        assert client.player != null;
        Collection<StatusEffectInstance> effects = client.player.getStatusEffects();
        moveText(effects);

        drawContext.drawText(renderer, text, x, y, 0xFFFFFF, true);
    }

    private static void HourFormat24(DrawContext drawContext){
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;

        String localTime;
        String text;

        if(CONFIG.showSeconds){
            localTime=LocalTime.now().format(TIME_FORMATTER24);
            text="Local Time: " + localTime;
            ConfigurePlacement(text);
        }else {
            localTime=LocalTime.now().format(TIME_FORMATTER24_NO_SEC);
            text="Local Time: " + localTime;
            ConfigurePlacement(text);
        }

        assert client.player != null;
        Collection<StatusEffectInstance> effects = client.player.getStatusEffects();
        moveText(effects);

        drawContext.drawText(renderer, text, x, y, 0xFFFFFF, true);
    }

    private static void ConfigurePlacement(String textToDraw){
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(textToDraw);
        int textHeight = 10;  // approximate height

        int margin = 10;

        IRLTimeConfig.Corner corner = CONFIG.corner;
        if (corner == null) {
            corner = IRLTimeConfig.Corner.TOP_RIGHT;
        }

        switch (corner) {
            case TOP_LEFT:
                x = margin;
                y = margin;
                break;

            case TOP_RIGHT:
                x = screenWidth - textWidth - margin;
                y = margin;
                break;

            case BOTTOM_LEFT:
                x = margin;
                y = screenHeight - textHeight - margin;
                break;

            case BOTTOM_RIGHT:
                x = screenWidth - textWidth - margin;
                y = screenHeight - textHeight - margin;
                break;

            default:
                // fallback in case config is invalid
                x = screenWidth - textWidth - margin;
                y = margin;
                break;
        }
    }

    //returns "AP" or "PM" accordingly
    private static String AMorPM(int hour){
        if (hour<12) {
            System.out.println(hour);
            return "AM";

        }
            return "PM";
    }
    //returns an integer in a 12-hour format
    private static int ConvertTo12Hour(int hour) {
        if (hour == 0 || hour == 12) {
            return 12;
        }
        return hour % 12;
    }
    //if there's an effect on the player, time moves sideways
    private static void moveText(Collection<StatusEffectInstance> effects) {
        if (CONFIG.corner == null || CONFIG.corner!=IRLTimeConfig.Corner.TOP_RIGHT) return;

        boolean hasTopRightEffects = effects.stream().anyMatch(effect ->
                effect.shouldShowIcon() && !isBottomHudEffect(effect)
        );

        if (hasTopRightEffects) {
            x -= 20;
        }
    }
    //some effects don't appear in the top right
    private static boolean isBottomHudEffect(StatusEffectInstance effect) {
        var type = effect.getEffectType();
        return type == StatusEffects.BAD_OMEN
                || type == StatusEffects.TRIAL_OMEN
                || type == StatusEffects.RAID_OMEN
                || type == StatusEffects.WIND_CHARGED
                || type == StatusEffects.WEAVING
                || type == StatusEffects.OOZING;
    }


}
