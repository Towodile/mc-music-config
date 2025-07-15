package zone.towo.musicconfig.screen.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.MusicInstance;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
import net.minecraft.text.Text;

public abstract class MusicButtons {
    public static ButtonWidget playButton(MusicSound music, Text text, int x, int y, int width, int height) {
        return ButtonWidget.builder(text, (button -> {
            MusicTracker musicTracker = MinecraftClient.getInstance().getMusicTracker();
            musicTracker.stop();
            musicTracker.play(new MusicInstance(music));
        }))
                .dimensions(x, y, width, height)
                .build();
    }

    public static ButtonWidget playButton(MusicSound music, int x, int y, int width, int height) {
        return playButton(music, Text.literal("▶"), x, y, width, height);
    }

}
