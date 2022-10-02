package dev.u9g;

import com.google.gson.annotations.Expose;
import dev.u9g.configlib.config.Config;
import dev.u9g.configlib.config.GuiTextures;
import dev.u9g.configlib.config.annotations.Category;
import dev.u9g.configlib.config.annotations.ConfigEditorBoolean;
import dev.u9g.configlib.config.annotations.ConfigEditorDropdown;
import dev.u9g.configlib.config.annotations.ConfigOption;

public class PrisonsModConfig implements Config {
    public static PrisonsModConfig INSTANCE = new PrisonsModConfig();

    @Expose
    @Category(name = "Misc", desc = "Options that don't fit in any other category.")
    public Misc misc = new Misc();

    @Expose
    @Category(name = "Debug", desc = "Options that for the mod.")
    public Debug debug = new Debug();

    public static class Misc {
        @Expose
        @ConfigOption(
                name = "Sign Reader",
                desc = "Shows the text on a sign, even if standing behind, far away, or blocked by other players or mobs."
        )
        @ConfigEditorBoolean
        public boolean signReader = true;

        @Expose
        @ConfigOption(
                name = "Secret",
                desc = "Changes something..."
        )
        @ConfigEditorBoolean
        public boolean babyPlayers = false;

        @Expose
        @ConfigOption(
                name = "Secret2",
                desc = "Changes something..."
        )
        @ConfigEditorBoolean
        public boolean rideDragonWhileFlying = false;
    }

    public static class Debug {
        @Expose
        @ConfigOption(
                name = "Shutup Errors",
                desc = "Disable logging for errors from Util#executeTask."
        )
        @ConfigEditorBoolean
        public boolean noExecuteTaskLogging = true;
    }

    @Override
    public void executeRunnable(String runnableId) {

    }

    @Override
    public String getHeaderText() {
        return "Prisons v1.0.0";
    }

    @Override
    public void save() {

    }

    @Override
    public Badge[] getBadges() {
        return new Badge[] { new Badge(GuiTextures.GITHUB, "https://github.com/u9g") };
    }
}
