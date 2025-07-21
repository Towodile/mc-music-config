package zone.towo.musicconfig.music.custom;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import zone.towo.musicconfig.MusicConfigMod;

public class DynamicSoundEvents {
    public static final SoundEvent MUSIC_ALL = Registry.register(
            Registries.SOUND_EVENT,
            Identifier.of(MusicConfigMod.MOD_ID, "music.all"),
            SoundEvent.of(Identifier.of(MusicConfigMod.MOD_ID, "music.all")
            ));

    public static void register() {}
}
