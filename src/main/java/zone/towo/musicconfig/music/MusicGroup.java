package zone.towo.musicconfig.music;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import zone.towo.musicconfig.file.SaveableMusicConfig;
import zone.towo.musicconfig.mixin.sound.WeightedSoundSetAccessor;

import java.util.*;

public record MusicGroup(String name, MusicSound groupedMusic, ArrayList<MusicTrack> tracks) {

    // TODO: ability to add more music to a group
    public static ArrayList<MusicGroup> getAll(MinecraftClient client, Random random) {
        Optional<SaveableMusicConfig> savedData = SaveableMusicConfig.fromFile();

        SoundManager soundManager = client.getSoundManager();
        ArrayList<MusicGroup> musicGroups = new ArrayList<>();

        for (var key : Registries.SOUND_EVENT.getKeys()) {
            if (!key.getValue().getPath().startsWith("music.")) {
                continue;
            }
            ArrayList<MusicTrack> tracks = new ArrayList<>();
            WeightedSoundSet soundSet = soundManager.get(key.getValue());
            WeightedSoundSetAccessor accessor = (WeightedSoundSetAccessor) soundSet;
            List<SoundContainer<Sound>> sounds = accessor.getSounds();
            String formattedName = key.getValue().getPath().replace("music.", "").replace(".", ": ").replace("_", " ");

            for (SoundContainer<Sound> sound : sounds) {
                if (savedData.isPresent()) {
                    Optional<Integer> frequency = savedData.get().getFrequencyForSound(key, sound.getSound(random).getIdentifier());
                    if (frequency.isPresent()) {
                        MusicTrack track = new MusicTrack(sound.getSound(random), frequency.get());
                        tracks.add(track);
                        continue;
                    }
                }

                tracks.add(new MusicTrack(sound.getSound(random)));
            }

            Optional<RegistryEntry.Reference<SoundEvent>> soundEvent = Registries.SOUND_EVENT.getEntry(key.getValue());
            MusicSound musicSound = soundEvent.isPresent() ? new MusicSound(soundEvent.get(), Integer.MAX_VALUE, 0,true) : null;
            if (!tracks.isEmpty()) musicGroups.add(new MusicGroup(formattedName, musicSound, tracks));
        }
        musicGroups.sort(Comparator.comparing(mg -> mg.name().toLowerCase()));
        return musicGroups;
    }
}
