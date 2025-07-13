package zone.towo.songconfig.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import zone.towo.songconfig.music.MusicGroup;

import java.util.ArrayList;

public class MusicConfigScreen extends GameOptionsScreen {
    private static ArrayList<MusicGroup> musicGroups;
    public MusicConfigScreen(Screen parent, MinecraftClient client) {
        super(parent, client.options, Text.translatable("options.sounds.musicconfig.title"));
        if (musicGroups == null) {
            musicGroups = MusicGroup.getAll(client, Random.create());
        }
    }

    @Override
    protected void addOptions() {
        this.body.addAll(this.gameOptions.getMusicFrequency(), this.gameOptions.getShowNowPlayingToast());
    }
}
