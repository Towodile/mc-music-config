package zone.towo.songconfig.screen.widget;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.sound.MusicInstance;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.screen.ScreenTexts;
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
    private MusicConfigScreen parent;
    public MusicListWidget(ArrayList<MusicGroup> musicGroups, MusicConfigScreen parent, MinecraftClient client) {
        super(client, (int) (parent.layout.getWidth() / 1.1), (int) (parent.layout.getHeight() /1.68), parent.layout.getHeaderHeight(), 20);
        this.parent = parent;
        this.allGroupEntries = new ArrayList<>();
        for (MusicGroup group : musicGroups) {
            List<MusicEntry> children = new ArrayList<>();
            for (MusicTrack track : group.tracks()) {
                children.add(new MusicEntry(track));
            }
            this.allGroupEntries.add(new MusicGroupEntry(group.name(), group.groupedMusic(), children));
        }
        this.populate();
    }

    public void populate(String searchTerm) {
        this.clearEntries();
        for (MusicGroupEntry groupEntry : allGroupEntries) {
            this.addEntry(groupEntry);
            for (MusicEntry entry : groupEntry.tracks) {
                if (entry.isVisible() && conformsToSearch(entry, searchTerm)) {
                    this.addEntry(entry);
                }
            }
        }
    }

    private void populate() {
        this.populate(this.parent.getCurrentSearchTerm());
    }

    private boolean conformsToSearch(MusicEntry entry, String searchTerm) {
        return searchTerm.isEmpty() || entry.track.getTitle().getString().toLowerCase().contains(searchTerm.toLowerCase());
    }

    public void resetIfConfirmed() {
        this.client.setScreen(new ConfirmScreen((confirmed) -> {
            if (confirmed) {
                for (int i = 0; i < this.getEntryCount(); i++) {
                    Entry entry = this.getEntry(i);
                    if (entry instanceof MusicEntry musicEntry) {
                        musicEntry.frequencySlider.reset();
                    }
                }
            }

            this.client.setScreen(this.parent);
        }, Text.translatable("options.sounds.musicconfig.reset"), Text.translatable("options.sounds.musicconfig.reset.question.all"), Text.translatable("options.sounds.musicconfig.reset.confirm"), ScreenTexts.CANCEL));
    }

    @Override
    public int getRowWidth() {
        return 340;
    }

    @Override
    protected int getScrollbarX() {
        return this.getRowRight() + 30;
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
        private final List<MusicEntry> tracks;
        private final ButtonWidget playButton;
        private final ButtonWidget resetButton;
        private boolean collapsed;

        public MusicGroupEntry(String groupName, MusicSound sound, List<MusicEntry> tracks) {
            this.groupName = groupName;
            this.sound = sound;
            this.tracks = tracks;
            this.playButton = ButtonWidget.builder(Text.literal("▶"), (button -> {
                        MusicTracker musicTracker = MinecraftClient.getInstance().getMusicTracker();
                        musicTracker.stop();
                        musicTracker.play(new MusicInstance(sound));
            }))
                    .dimensions(0, 0, 10, 10)
                    .build();

            Text playText = MusicListWidget.this.client.player != null ? Text.translatable("options.sounds.musicconfig.preview") :
                    Text.translatable("options.sounds.musicconfig.preview").append("\n").append(Text.translatable("options.sounds.musicconfig.preview.inmenu").withColor(Color.yellow.getRGB()));

            playButton.setTooltip(Tooltip.of(playText));
            this.resetButton = ButtonWidget.builder(Text.literal(" ↶ "), (button -> resetTracksIfConfirmed()))
                    .dimensions(0, 0, 10, 10)
                    .build();

            resetButton.setTooltip(Tooltip.of(Text.translatable("options.sounds.musicconfig.reset")));
            this.collapsed = false;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            playButton.setPosition(x,y);
            resetButton.setPosition(MusicListWidget.this.getScrollbarX() - 50,y);
            playButton.render(context, mouseX, mouseY, tickDelta);
            resetButton.render(context, mouseX, mouseY, tickDelta);
            String collapseIcon = collapsed ? "▶" : "▼";
            context.drawText(MinecraftClient.getInstance().textRenderer,
                      collapseIcon + " " + groupName + " (" + sound.sound().getKey().orElse(null).getValue().toString() + ")", x+15, y, Color.gray.getRGB(), false);
        }

        @Override
        public List<? extends Element> children() {
            return ImmutableList.of(playButton);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return ImmutableList.of(playButton);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (playButton.mouseClicked(mouseX, mouseY, button) || resetButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
            this.collapse();
            return super.mouseClicked(mouseX, mouseY, button);
        }

        private void collapse() {
            this.collapsed = !this.collapsed;
            this.tracks.forEach(entry -> entry.setVisible(!entry.isVisible()));
            MusicListWidget.this.populate();
        }

        public void resetTracksIfConfirmed() {
            MusicListWidget.this.client.setScreen(new ConfirmScreen((confirmed) -> {
                if (confirmed) {
                    for (Entry entry : this.tracks) {
                        if (entry instanceof MusicEntry musicEntry) {
                            musicEntry.frequencySlider.reset();
                        }
                    }
                }

                MusicListWidget.this.client.setScreen(MusicListWidget.this.parent);
            }, Text.translatable("options.sounds.musicconfig.reset"), Text.translatable("options.sounds.musicconfig.reset.question", groupName), Text.translatable("options.sounds.musicconfig.reset.confirm"), ScreenTexts.CANCEL));
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
