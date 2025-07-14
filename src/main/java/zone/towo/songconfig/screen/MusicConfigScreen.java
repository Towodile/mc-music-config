package zone.towo.songconfig.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import zone.towo.songconfig.MusicConfigMod;
import zone.towo.songconfig.file.SaveableMusicConfig;
import zone.towo.songconfig.music.MusicGroup;
import zone.towo.songconfig.screen.widget.MusicListWidget;

import java.util.ArrayList;

public class MusicConfigScreen extends GameOptionsScreen {
    private static ArrayList<MusicGroup> musicGroups;
    private MusicListWidget musicList;

    public MusicConfigScreen(Screen parent, MinecraftClient client) {
        super(parent, client.options, Text.translatable("options.sounds.musicconfig.title"));
        if (musicGroups == null) {
            musicGroups = MusicGroup.getAll(client, Random.create());
        }
    }

    @Override
    protected void initBody() {
        this.musicList = this.layout.addBody(new MusicListWidget(musicGroups, this, this.client));
    }

    @Override
    protected void addOptions() {

    }

    protected void initFooter() {
        ButtonWidget resetAllButton = ButtonWidget.builder(Text.translatable("options.sounds.musicconfig.reset"), (button) -> {
            this.musicList.resetAll();
        }).build();

        DirectionalLayoutWidget directionalLayoutWidget = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
        directionalLayoutWidget.add(resetAllButton);
        directionalLayoutWidget.add(this.gameOptions.getMusicFrequency().createWidget(this.gameOptions));
        directionalLayoutWidget.add(this.gameOptions.getShowNowPlayingToast().createWidget(this.gameOptions));
        directionalLayoutWidget.add(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            SaveableMusicConfig.of(musicGroups).save();
            this.close();
        }).build());
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.musicList.render(context, mouseX, mouseY, delta);

    }

}
