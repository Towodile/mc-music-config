package zone.towo.songconfig.music;

import net.minecraft.client.sound.Sound;
import net.minecraft.text.Text;
import zone.towo.songconfig.util.SoundExtension;

public class MusicTrack {
    private final Text title;
    private final Sound sound;
    private final String source;
    private int frequency;
    private final int initialFrequency;

    public MusicTrack(Sound sound) {
        this(sound, sound.getWeight());
    }

    public MusicTrack(Sound sound, int frequency) {
        String translationKey = sound.getIdentifier().toShortTranslationKey();
        this.title = Text.translatable(translationKey.replace("/", "."));
        this.sound = sound;
        this.source = sound.getIdentifier().getNamespace();

        this.initialFrequency = sound.getWeight();
        this.frequency = frequency;
        if (frequency != initialFrequency) {
            ((SoundExtension)sound).setWeight(frequency);
        }
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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
        ((SoundExtension)sound).setWeight(frequency);
    }

    /**
     * @return the frequency after resetting (its initial value)
     */
    public int resetFrequency() {
        this.setFrequency(this.initialFrequency);
        return this.frequency;
    }
}
