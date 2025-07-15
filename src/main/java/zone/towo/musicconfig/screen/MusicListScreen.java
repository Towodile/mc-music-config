package zone.towo.musicconfig.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import zone.towo.musicconfig.music.MusicResource;
import zone.towo.musicconfig.screen.widget.list.MusicListWidget;

import java.util.Comparator;
import java.util.List;

public class MusicListScreen extends GameOptionsScreen {

    private MusicListWidget listWidget;
    private final List<MusicResource> musicResources;
    private final SelectMusicTrackAction selectAction;

    public MusicListScreen(List<MusicResource> musicResources, Screen parent, GameOptions gameOptions, SelectMusicTrackAction selectAction) {
        super(parent, gameOptions, Text.translatable("options.sounds.musicconfig.musiclist.title"));
        this.musicResources = musicResources;
        this.selectAction = selectAction;
    }


    @Override
    protected void initBody() {

        var tracks = new java.util.ArrayList<>(musicResources.stream().map(MusicResource::createTrack).toList());
        tracks.sort(Comparator.comparing(track -> track.getTitle().getString()));

        this.listWidget = this.layout.addBody(new MusicListWidget(
                tracks,
                this.client,
                this.width,
                (int) (this.layout.getHeight() / 1.35),
                10
        ));
    }

    @Override
    protected void initFooter() {
        DirectionalLayoutWidget footer = DirectionalLayoutWidget.horizontal().spacing(8);
        footer.add(ButtonWidget.builder(ScreenTexts.CANCEL, (button) -> {
            this.close();
        }).width(200).build());

        footer.add(ButtonWidget.builder(Text.translatable("options.sounds.musicconfig.musiclist.confirm"), (button) -> {
            this.selectAction.processSelection(this.listWidget.getSelection());
            this.close();
        }).width(200).build());
        this.layout.addFooter(footer);
    }

    @Override
    protected void addOptions() {

    }
}
