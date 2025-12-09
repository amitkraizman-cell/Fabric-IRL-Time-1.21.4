package amit.irltime;

import amit.irltime.IRLTime;
import amit.irltime.IRLTimeConfig;
import amit.irltime.IRLTimeInitialize;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Tooltip;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class IRLTimeConfigScreen {

    public static Screen create(Screen parent) {

        IRLTimeConfig cfg = IRLTimeInitialize.CONFIG;
        IRLTimeConfig.Corner currentCorner =
                (cfg.corner != null ? cfg.corner : IRLTimeConfig.Corner.TOP_RIGHT);

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("IRL Time Settings"));

        builder.setSavingRunnable(() -> {
            // save config
            ConfigManager.saveConfig(IRLTimeInitialize.CONFIG);
        });

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Enabled toggle
        general.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Enabled"), cfg.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> cfg.enabled = newValue)
                .build()
        );

        // 24h format
        general.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Use 24-hour format"), cfg.use24h)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> cfg.use24h = newValue)
                .build()
        );

        // Show seconds
        general.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Show seconds"), cfg.showSeconds)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> cfg.showSeconds = newValue)
                .build()
        );
        //change position
        general.addEntry(entryBuilder
                .startEnumSelector(
                        Text.literal("Corner"),
                        IRLTimeConfig.Corner.class,
                        currentCorner
                )
                .setDefaultValue(IRLTimeConfig.Corner.TOP_RIGHT)
                .setSaveConsumer(newValue -> cfg.corner = newValue)
                .build()
        );


        return builder.build();
    }
}
