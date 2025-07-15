package zone.towo.musicconfig.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import zone.towo.musicconfig.MusicConfigMod;
import zone.towo.musicconfig.music.MusicGroup;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public record SaveableMusicConfig(List<SaveableMusicGroup> musicGroups) {
    private static final Path GAME_DIR = MinecraftClient.getInstance().runDirectory.toPath();
    private static final Path CONFIG_PATH = GAME_DIR.resolve("music_config.json");

    public Optional<Integer> getFrequencyForSoundEvent(RegistryKey<SoundEvent> soundEvent, Identifier sound) {
        for (SaveableMusicGroup group : musicGroups) {
            if (group.soundEvent().equalsIgnoreCase(soundEvent.getValue().toString())) {
                for (SaveableMusicTrack track : group.tracks()) {
                    if (track.soundFile().equalsIgnoreCase(sound.toString())) {
                        return Optional.of(track.frequency());
                    }
                }
            }
        }
        return Optional.empty();
    }

    public static SaveableMusicConfig of(List<MusicGroup> original) {
        return new SaveableMusicConfig(original.stream().map(SaveableMusicGroup::of).toList());
    }

    public static Optional<SaveableMusicConfig> fromFile() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                return Optional.of(fromJson(json));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            MusicConfigMod.LOGGER.warn("Couldn't read music config file. Using default settings. \n(Cause: {})", e.getMessage());
            return Optional.empty();
        }
    }

    public boolean save() {
        try {
            String json = toJson();
            Files.writeString(CONFIG_PATH, json);
            return true;
        } catch (Exception e) {
            MusicConfigMod.LOGGER.error(e.getMessage());
            return false;
        }
    }

    private String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    private static SaveableMusicConfig fromJson(String json) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, SaveableMusicConfig.class);
    }
}
