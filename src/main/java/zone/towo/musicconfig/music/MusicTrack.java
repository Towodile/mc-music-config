package zone.towo.musicconfig.music;

import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;
import zone.towo.musicconfig.util.SoundExtension;

public class MusicTrack {
    private final Text title;
    private final Sound sound;
    private final String source;
    private int frequency;
    private final int initialFrequency;
    private boolean vanilla;

    public MusicTrack(Sound sound, boolean vanilla) {
        this(sound, sound.getWeight(), vanilla);
    }

    public MusicTrack(Sound sound, int frequency, boolean vanilla) {
        String translationKey = sound.getIdentifier().toShortTranslationKey();
        this.title = Text.translatable(translationKey.replace("/", "."));
        this.sound = sound;
        this.source = sound.getIdentifier().getNamespace();
        this.vanilla = vanilla;

        this.initialFrequency = vanilla ? sound.getWeight() : 0;
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

    public boolean isVanilla() {
        return vanilla;
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
