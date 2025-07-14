package zone.towo.songconfig.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
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
        this.layout.setFooterHeight(66);
        ButtonWidget resetAllButton = ButtonWidget.builder(
                Text.translatable("options.sounds.musicconfig.reset"),
                (button) -> this.musicList.resetIfConfirmed()
        ).build();

        ClickableWidget musicFrequencyButton = this.gameOptions.getMusicFrequency().createWidget(this.gameOptions);
        ClickableWidget toastToggleButton = this.gameOptions.getShowNowPlayingToast().createWidget(this.gameOptions);

        ButtonWidget doneButton = ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            SaveableMusicConfig.of(musicGroups).save();
            this.close();
        }).build();

        DirectionalLayoutWidget footer = this.layout.addFooter(DirectionalLayoutWidget.vertical().spacing(6));

        DirectionalLayoutWidget row1 = DirectionalLayoutWidget.horizontal().spacing(8);
        row1.add(toastToggleButton);
        row1.add(musicFrequencyButton);

        DirectionalLayoutWidget row2 = DirectionalLayoutWidget.horizontal().spacing(8);
        row2.add(resetAllButton);
        row2.add(doneButton);

        footer.add(row1);
        footer.add(row2);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.musicList.render(context, mouseX, mouseY, delta);

    }

}
