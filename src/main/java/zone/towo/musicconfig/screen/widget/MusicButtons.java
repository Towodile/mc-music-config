package zone.towo.musicconfig.screen.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.MusicInstance;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
import net.minecraft.text.Text;
import zone.towo.musicconfig.MusicConfigMod;
import zone.towo.musicconfig.music.MusicTrack;

public abstract class MusicButtons {
    public static ButtonWidget groupPlayButton(MusicSound music, Text text, int x, int y, int width, int height) {
        return ButtonWidget.builder(text, (button -> {
            MusicTracker musicTracker = MinecraftClient.getInstance().getMusicTracker();
            musicTracker.stop();
            musicTracker.play(new MusicInstance(music));
        }))
                .dimensions(x, y, width, height)
                .build();
    }

    public static ButtonWidget groupPlayButton(MusicSound music, int x, int y, int width, int height) {
        return groupPlayButton(music, Text.literal("▶"), x, y, width, height);
    }


    public static ButtonWidget trackPlayButton(MusicTrack track, Text text, int x, int y, int width, int height) {
        return ButtonWidget.builder(text, (button -> {
                    MusicConfigMod.getMusicPlayer().play(MinecraftClient.getInstance(), track);
                }))
                .dimensions(x, y, width, height)
                .build();
    }

    public static ButtonWidget trackPlayButton(MusicTrack track, int x, int y, int width, int height) {
        return trackPlayButton(track, Text.literal("▶"), x, y, width, height);
    }
}
