package zone.towo.songconfig.music;

import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundContainer;
import net.minecraft.sound.SoundEvent;

public record MusicTrack(String title, SoundContainer<Sound> sound, int frequency) {
}
