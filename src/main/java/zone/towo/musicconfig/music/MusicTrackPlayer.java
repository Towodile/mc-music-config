package zone.towo.musicconfig.music;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicInstance;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundContainer;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import zone.towo.musicconfig.mixin.sound.WeightedSoundSetAccessor;
import zone.towo.musicconfig.music.custom.DynamicSoundEvents;
import zone.towo.musicconfig.util.SoundExtension;

public class MusicTrackPlayer {
    private static final Random random = net.minecraft.util.math.random.Random.create();
    private final WeightedSoundSetAccessor soundSet;

    private MusicTrackPlayer(WeightedSoundSetAccessor soundSet) {
        this.soundSet = soundSet;
    }

    public static MusicTrackPlayer create(MinecraftClient client) {
        RegistryEntry<SoundEvent> soundEvent = Registries.SOUND_EVENT.getEntry(DynamicSoundEvents.MUSIC_ALL);
        MusicSound sound = new MusicSound(soundEvent, Integer.MAX_VALUE, 0, true);
        var optKey = sound.sound().getKey();
        WeightedSoundSet soundSet = optKey.map(soundEventRegistryKey -> client.getSoundManager().get(soundEventRegistryKey.getValue())).orElse(null);
        MusicResource.getAll(client).forEach(resource -> {
            MusicTrack track = resource.createTrack(1f, 1f, 0);
            soundSet.add(track.getSound());
        });
        return new MusicTrackPlayer((WeightedSoundSetAccessor) soundSet);
    }

    public void play(MinecraftClient client, MusicTrack track) {
        String name = track.getSound().getIdentifier().toString();
        for (SoundContainer<Sound> sound : soundSet.getSounds()) {
            String currentSound = sound.getSound(random).getIdentifier().toString();
            if (currentSound.equals(name)) {
                ((SoundExtension)sound).setWeight(100);
            } else {
                ((SoundExtension)sound).setWeight(0);
            }
        }

        RegistryEntry<SoundEvent> soundEvent = Registries.SOUND_EVENT.getEntry(DynamicSoundEvents.MUSIC_ALL);
        client.getMusicTracker().stop();
        client.getMusicTracker().play(new MusicInstance(new MusicSound(soundEvent, Integer.MAX_VALUE, 0, true)));
    }
}
