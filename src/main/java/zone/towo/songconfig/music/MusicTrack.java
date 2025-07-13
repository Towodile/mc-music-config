package zone.towo.songconfig.music;

import net.minecraft.client.sound.Sound;
import net.minecraft.text.Text;
import zone.towo.songconfig.MusicConfigMod;

public class MusicTrack {
    public final Text title;
    public final Sound sound;
    public final int frequency;
    public final String source;
    public MusicTrack(Sound sound) {
        String translationKey = sound.getIdentifier().toShortTranslationKey();
        this.title = Text.translatable(translationKey.replace("/", "."));
        MusicConfigMod.LOGGER.info("Found music track: " + this.title.getString());
        this.sound = sound;
        this.source = sound.getIdentifier().getNamespace();
        this.frequency = sound.getWeight();
    }
}
