package zone.towo.songconfig.music;

import net.minecraft.client.sound.Sound;
import net.minecraft.text.Text;

public class MusicTrack {
    private final Text title;
    private final Sound sound;
    private final String source;
    private double frequency;
    private final double initialFrequency;

    public MusicTrack(Sound sound) {
        String translationKey = sound.getIdentifier().toShortTranslationKey();
        this.title = Text.translatable(translationKey.replace("/", "."));
        this.sound = sound;
        this.source = sound.getIdentifier().getNamespace();
        this.frequency = sound.getWeight();
        this.initialFrequency = this.frequency;
    }

    public Text getTitle() {
        return title;
    }

    public Sound getSound() {
        return sound;
    }

    public String getSource() {
        return source;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }

    public void resetFrequency() {
        this.frequency = this.initialFrequency;
    }
}
