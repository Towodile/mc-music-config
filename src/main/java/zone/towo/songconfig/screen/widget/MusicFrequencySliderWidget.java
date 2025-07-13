package zone.towo.songconfig.screen.widget;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import zone.towo.songconfig.music.MusicTrack;

public class MusicFrequencySliderWidget extends SliderWidget {
    private final MusicTrack musicTrack;

    public MusicFrequencySliderWidget(MusicTrack track, int x, int y, int width, int height, Text text, double value) {
        super(x, y, width, height, text, value);
        this.musicTrack = track;
    }


    @Override
    protected void updateMessage() {
        String message = Text.translatable("options.sounds.musicconfig.frequency").getString() + ": " + FrequencyName.of(value).text.getString() + " (" + (int)(value * 100) + ")";
        this.setMessage(Text.literal(message));
    }

    public void reset() {
        musicTrack.resetFrequency();
        this.value = musicTrack.getFrequency();
        this.applyValue();
        this.updateMessage();
    }

    @Override
    protected void applyValue() {
        musicTrack.setFrequency((float)this.value);
    }

    private enum FrequencyName {
        NEVER(0.0f, Text.translatable("options.sounds.musicconfig.frequency.never")),
        RARELY(0.25f, Text.translatable("options.sounds.musicconfig.frequency.rarely")),
        SOMETIMES(0.5f, Text.translatable("options.sounds.musicconfig.frequency.sometimes")),
        OFTEN(0.75f, Text.translatable("options.sounds.musicconfig.frequency.often"));
        private final float value;
        private final Text text;
        FrequencyName(float value, Text text) {
            this.value = value;
            this.text = text;
        }

        static FrequencyName of(double value) {
            FrequencyName[] all = FrequencyName.values();
            for (int i = 0; i < all.length; i++) {
                FrequencyName frequency = all[i];
                if (frequency.value == value) {
                    return frequency;
                }
                if (value < frequency.value) {
                    return i == 0 ? all[0] : all[i - 1];
                }
            }
            return all[all.length - 1];
        }
    }
}
