package zone.towo.musicconfig.music;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import zone.towo.musicconfig.MusicConfigMod;
import zone.towo.musicconfig.file.SaveableMusicConfig;
import zone.towo.musicconfig.file.SaveableMusicGroup;
import zone.towo.musicconfig.mixin.sound.WeightedSoundSetAccessor;

import java.util.*;

public record MusicGroup(String name, MusicSound groupedMusic, ArrayList<MusicTrack> tracks) {

    private static ArrayList<MusicGroup> ALL;

    public static void initialize(MinecraftClient client, Random random) {

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
                    Optional<Integer> frequency = savedData.get().getFrequencyForSoundEvent(key, sound.getSound(random).getIdentifier());
                    if (frequency.isPresent()) {
                        MusicTrack track = new MusicTrack(sound.getSound(random), frequency.get(), true);
                        tracks.add(track);
                        continue;
                    }
                }

                tracks.add(new MusicTrack(sound.getSound(random), true));
            }

            Optional<RegistryEntry.Reference<SoundEvent>> soundEvent = Registries.SOUND_EVENT.getEntry(key.getValue());
            MusicSound musicSound = soundEvent.isPresent() ? new MusicSound(soundEvent.get(), Integer.MAX_VALUE, 0, true) : null;
            if (!tracks.isEmpty()) musicGroups.add(new MusicGroup(formattedName, musicSound, tracks));
        }

        for (MusicGroup group : musicGroups) {
            List<MusicResource> missingResources = MusicResource.getAll(client);
            for (MusicResource resource : missingResources) {
                int frequency = 0;
                if (savedData.isPresent()) {
                    Optional<Integer> optFrequency = savedData.get().getFrequencyForSoundEvent(group.groupedMusic.sound().getKey().get(), resource.resource());
                    frequency = optFrequency.orElse(frequency);
                }
                group.addTracks(soundManager, resource.createTrack(1f, 1f, frequency));
            }
        }
        musicGroups.forEach(mg -> mg.tracks.sort(
                        Comparator
                                .comparing(tr -> (!((MusicTrack) tr).isVanilla()))
                                .thenComparing(tr -> ((MusicTrack) tr).getTitle().getString())
                )
        );
        musicGroups.sort(Comparator.comparing(mg -> mg.name().toLowerCase()));
        ALL = musicGroups;
        SaveableMusicConfig.of(ALL).save();
        MusicConfigMod.LOGGER.info("Initialized {} music groups.", ALL.size());
    }

    public static ArrayList<MusicGroup> getAll() {
        if (ALL == null) {
            MusicConfigMod.LOGGER.error("Music groups have not been initialized, cannot retrieve data.");
            return null;
        }
        return ALL;
    }

    public void addTracks(SoundManager soundManager, MusicTrack... newTracks) {
        for (MusicTrack track : newTracks) {
            if (!this.has(track.getSound())) {
                this.asSoundSet(soundManager).add(track.getSound());
                tracks.add(track);
            }
        }
    }

    private boolean has(Sound sound) {
        for (MusicTrack track : tracks) {
            if (track.getSound().getIdentifier().equals(sound.getIdentifier())) {
                return true;
            }
        }
        return false;
    }

    private WeightedSoundSet asSoundSet(SoundManager soundManager) {
        var optKey = this.groupedMusic.sound().getKey();
        return optKey.map(soundEventRegistryKey -> soundManager.get(soundEventRegistryKey.getValue())).orElse(null);
    }
}
