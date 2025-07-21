package zone.towo.musicconfig.screen.widget;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import zone.towo.musicconfig.MusicConfigMod;
import zone.towo.musicconfig.music.MusicTrack;

public class MusicFrequencySliderWidget extends SliderWidget {
    private final MusicTrack musicTrack;

    public MusicFrequencySliderWidget(MusicTrack track, int x, int y, int width, int height, Text text, double value) {
        super(x, y, width, height, text, value/10);
        this.musicTrack = track;
    }


    @Override
    protected void updateMessage() {
        String message = Text.translatable("options.sounds.musicconfig.frequency").getString() + ": " + FrequencyName.of((int) (value*10)).text.getString() + " (" + (int)(value*10) + ")";
        this.setMessage(Text.literal(message));
    }

    public void reset() {
        this.value = (double) musicTrack.resetFrequency() / 10;
        this.applyValue();
        this.updateMessage();
    }

    @Override
    protected void applyValue() {
        musicTrack.setFrequency((int) (this.value * 10));
    }

    private enum FrequencyName {
        NEVER(0, Text.translatable("options.sounds.musicconfig.frequency.never")),
        RARELY(1, Text.translatable("options.sounds.musicconfig.frequency.rarely")),
        SOMETIMES(4, Text.translatable("options.sounds.musicconfig.frequency.sometimes")),
        OFTEN(6, Text.translatable("options.sounds.musicconfig.frequency.often")),
        VERY_OFTEN(9, Text.translatable("options.sounds.musicconfig.frequency.very_often"));
        private final float value;
        private final Text text;
        FrequencyName(int value, Text text) {
            this.value = value;
            this.text = text;
        }

        static FrequencyName of(int value) {
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
