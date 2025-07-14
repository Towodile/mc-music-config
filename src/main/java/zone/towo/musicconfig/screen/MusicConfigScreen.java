package zone.towo.musicconfig.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;
import zone.towo.musicconfig.MusicConfigMod;
import zone.towo.musicconfig.file.SaveableMusicConfig;
import zone.towo.musicconfig.music.MusicGroup;
import zone.towo.musicconfig.screen.widget.MusicListWidget;

import java.util.ArrayList;

public class MusicConfigScreen extends GameOptionsScreen {
    private static ArrayList<MusicGroup> musicGroups;
    private MusicListWidget musicList;
    private String currentSearchTerm = "";

    public MusicConfigScreen(Screen parent, MinecraftClient client) {
        super(parent, client.options, Text.translatable("options.sounds.musicconfig.title"));
        if (musicGroups == null) {
            musicGroups = MusicGroup.getAll(client, Random.create());
        }
    }

    @Override
    protected void initHeader() {
        this.layout.setHeaderHeight((int) (this.layout.getHeight() / 6.2));
        DirectionalLayoutWidget header = this.layout.addHeader(DirectionalLayoutWidget.vertical().spacing(6));
        header.getMainPositioner().alignHorizontalCenter();
        header.add(new TextWidget(this.title, this.textRenderer));

        TextFieldWidget searchBox = new TextFieldWidget(
                this.textRenderer,
                (this.layout.getWidth()  / 4),
                15,
                this.layout.getWidth() / 2,
                15,
                Text.translatable("options.sounds.musicconfig.search"));

        searchBox.setChangedListener(term -> {
            this.currentSearchTerm = term;
            this.musicList.populate(term);
        });

        searchBox.setPlaceholder(Text.translatable("options.sounds.musicconfig.search"));
        header.add(searchBox);
    }

    public String getCurrentSearchTerm() {
        return currentSearchTerm;
    }

    @Override
    protected void initBody() {
        this.musicList = this.layout.addBody(new MusicListWidget(musicGroups, this, this.client));

    }

    @Override
    protected void addOptions() {

    }

    protected void initFooter() {
        this.layout.setFooterHeight(this.layout.getHeight() / 4);
        ButtonWidget resetAllButton = ButtonWidget.builder(
                Text.translatable("options.sounds.musicconfig.reset"),
                (button) -> this.musicList.resetIfConfirmed()
        ).build();

        ClickableWidget musicFrequencyButton = this.gameOptions.getMusicFrequency().createWidget(this.gameOptions);
        ClickableWidget toastToggleButton = this.gameOptions.getShowNowPlayingToast().createWidget(this.gameOptions);

        ButtonWidget doneButton = ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            boolean saved = SaveableMusicConfig.of(musicGroups).save();
            if (!saved) MusicConfigMod.LOGGER.error("Failed to save music settings!");
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
