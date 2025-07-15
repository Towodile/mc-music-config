package zone.towo.musicconfig.screen.widget.list;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ElementListWidget;
import zone.towo.musicconfig.music.MusicTrack;

import java.awt.*;
import java.util.List;

public class MusicListWidget extends ElementListWidget<MusicListWidget.MusicTrackEntry> {
    public MusicListWidget(List<MusicTrack> allTracks, MinecraftClient minecraftClient, int i, int j, int k) {
        super(minecraftClient, i, j, k, 20);
        allTracks.forEach(track -> this.addEntry(new MusicTrackEntry(track)));
    }

    public List<MusicTrack> getSelection() {
        return this.children().stream()
                .filter(entry -> entry.selected)
                .map(entry -> entry.track)
                .toList();
    }

    public static class MusicTrackEntry extends ElementListWidget.Entry<MusicTrackEntry> {

        private boolean selected;
        private final MusicTrack track;
        public MusicTrackEntry(MusicTrack track) {
            this.track = track;
            this.selected = false;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of();
        }

        @Override
        public List<? extends Element> children() {
            return List.of();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickProgress) {
            var textRenderer = MinecraftClient.getInstance().textRenderer;

            if (selected) {
                context.drawBorder(x - 3, y - 4, entryWidth + 3, entryHeight + 1, Color.white.getRGB());
            }

            context.drawText(
                    textRenderer,
                    track.getTitle(),
                    x,
                    y,
                    Color.white.getRGB(),
                    false
            );
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.selected = !selected;
            return true;
        }
    }

}
