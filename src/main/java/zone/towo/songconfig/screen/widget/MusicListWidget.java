package zone.towo.songconfig.screen.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.sound.MusicInstance;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import zone.towo.songconfig.music.MusicGroup;
import zone.towo.songconfig.music.MusicTrack;
import zone.towo.songconfig.screen.MusicConfigScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MusicListWidget extends ElementListWidget<MusicListWidget.Entry> {
    private ArrayList<MusicGroupEntry> allGroupEntries;
    public MusicListWidget(ArrayList<MusicGroup> musicGroups, MusicConfigScreen parent, MinecraftClient client) {
        super(client, parent.width, parent.layout.getContentHeight(), 0, 20);
        this.allGroupEntries = new ArrayList<>();
        for (MusicGroup group : musicGroups) {
            List<MusicEntry> children = new ArrayList<>();
            for (MusicTrack track : group.tracks()) {
                children.add(new MusicEntry(track, this));
            }
            this.allGroupEntries.add(new MusicGroupEntry(group.name(), children));
        }

        populate();
    }

    private void populate() {
        this.clearEntries();
        for (MusicGroupEntry groupEntry : allGroupEntries) {
            this.addEntry(groupEntry);
            for (MusicEntry entry : groupEntry.children) {
                if (entry.isVisible()) {
                    this.addEntry(entry);
                }
            }
        }
    }

    public void resetAll() {
        for (int i = 0; i < this.getEntryCount(); i++) {
            Entry entry = this.getEntry(i);
            if (entry instanceof MusicEntry musicEntry) {
                musicEntry.frequencySlider.reset();
            }
        }
    }

    @Override
    protected int getScrollbarX() {
        return this.getRowRight() + 80;
    }

    public static class MusicEntry extends Entry {
        private final MusicTrack track;
        private final MusicFrequencySliderWidget frequencySlider;
        private boolean visible = true;
        private MusicListWidget parent;

        public MusicEntry(MusicTrack track, MusicListWidget parent) {
            this.visible = true;
            this.track = track;
            this.parent = parent;
            this.frequencySlider = new MusicFrequencySliderWidget(track, 0, 0, 150, 20,
                    Text.translatable("options.sounds.musicconfig.frequency", track.getTitle()), track.getFrequency());
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
            parent.populate();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            frequencySlider.setX(x + 120);
            frequencySlider.setY(y - 8);
            frequencySlider.render(context, mouseX, mouseY, tickDelta);
            context.drawText(MinecraftClient.getInstance().textRenderer, track.getTitle(), x - 65, y, Color.WHITE.getRGB(), false);
        }


        @Override
        public List<? extends Element> children() {
            return List.of();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            super.mouseClicked(mouseX, mouseY, button);
            return frequencySlider.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            return frequencySlider.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            super.mouseReleased(mouseX, mouseY, button);
            return frequencySlider.mouseReleased(mouseX, mouseY, button);
        }

        @Override
        public void mouseMoved(double mouseX, double mouseY) {
            frequencySlider.mouseMoved(mouseX, mouseY);
            super.mouseMoved(mouseX, mouseY);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of();
        }
    }

    public static class MusicGroupEntry extends Entry {
        private final String groupName;
        private List<MusicEntry> children;
        public MusicGroupEntry(String groupName, List<MusicEntry> children) {
            this.groupName = groupName;
            this.children = children;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawText(MinecraftClient.getInstance().textRenderer,
                    groupName, x - 75, y, Color.gray.getRGB(), false);
        }

        @Override
        public List<? extends Element> children() {
            return children;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.children.forEach(entry -> {
                entry.setVisible(!entry.isVisible());
            });
            return true;
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of();
        }
    }

    @Environment(EnvType.CLIENT)
    public abstract static class Entry extends ElementListWidget.Entry<MusicListWidget.Entry> implements AutoCloseable {
        public Entry() {
        }

        public void close() {
        }
    }
}
