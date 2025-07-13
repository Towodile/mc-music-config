package zone.towo.songconfig.screen.widget;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.sound.MusicInstance;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
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
        super(client, parent.width, parent.layout.getContentHeight(), parent.layout.getHeaderHeight(), 20);
        this.allGroupEntries = new ArrayList<>();
        for (MusicGroup group : musicGroups) {
            List<MusicEntry> children = new ArrayList<>();
            for (MusicTrack track : group.tracks()) {
                children.add(new MusicEntry(track));
            }
            this.allGroupEntries.add(new MusicGroupEntry(group.name(), group.groupedMusic(), children));
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
    public int getRowWidth() {
        return 340;
    }

    @Override
    protected int getScrollbarX() {
        return this.getRowWidth() + 80;
    }

    public  class MusicEntry extends Entry {
        private final MusicTrack track;
        private final MusicFrequencySliderWidget frequencySlider;
        private boolean visible;

        public MusicEntry(MusicTrack track) {
            this.visible = true;
            this.track = track;
            this.frequencySlider = new MusicFrequencySliderWidget(track, 0, 0, 150, 20,
                    Text.translatable("options.sounds.musicconfig.frequency", track.getTitle()), track.getFrequency());
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            frequencySlider.setPosition(x+185,y-8);
            frequencySlider.render(context, mouseX, mouseY, tickDelta);
            context.drawText(MinecraftClient.getInstance().textRenderer, track.getTitle().asTruncatedString(29), x+25, y, Color.WHITE.getRGB(), false);
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

    public class MusicGroupEntry extends Entry {
        private final String groupName;
        private final MusicSound sound;
        private List<MusicEntry> children;
        private final ButtonWidget btn;

        public MusicGroupEntry(String groupName, MusicSound sound, List<MusicEntry> children) {
            this.groupName = groupName;
            this.sound = sound;
            this.children = children;
            this.btn = ButtonWidget.builder(Text.literal("►"), (button -> {
                        MusicTracker musicTracker = MinecraftClient.getInstance().getMusicTracker();
                        musicTracker.stop();
                        musicTracker.play(new MusicInstance(sound));
            }))
                    .dimensions(0, 0, 10, 10)
                    .build();

        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            btn.setPosition(x,y);
            btn.render(context, mouseX, mouseY, tickDelta);
            context.drawText(MinecraftClient.getInstance().textRenderer,
                    groupName + " (" + sound.sound().getKey().orElse(null).getValue().toString() + ")", x+15, y, Color.gray.getRGB(), false);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(btn);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(btn);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (btn.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
            this.children.forEach(entry -> entry.setVisible(!entry.isVisible()));
            MusicListWidget.this.populate();
            return super.mouseClicked(mouseX, mouseY, button);
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
