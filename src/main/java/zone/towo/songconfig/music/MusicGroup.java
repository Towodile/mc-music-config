package zone.towo.songconfig.music;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.*;
import net.minecraft.registry.Registries;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.MusicType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;
import zone.towo.songconfig.MusicConfigMod;
import zone.towo.songconfig.mixin.WeightedSoundSetAccessor;

import java.util.ArrayList;
import java.util.List;

public record MusicGroup(String name, ArrayList<MusicTrack> tracks) {

    public static ArrayList<MusicGroup> getAll(MinecraftClient client, Random random) {
        SoundManager soundManager = client.getSoundManager();
        ArrayList<MusicGroup> musicGroups = new ArrayList<>();

        for (var key : soundManager.getKeys()) {
            if (!key.getPath().startsWith("music.")) {
                continue;
            }
            ArrayList<MusicTrack> tracks = new ArrayList<>();
            WeightedSoundSet soundSet = soundManager.get(key);
            WeightedSoundSetAccessor accessor = (WeightedSoundSetAccessor) soundSet;
            List<SoundContainer<Sound>> sounds = accessor.getSounds();
            String formattedName = key.getPath().replace("music.", "").replace(".", ": ").replace("_", " ");


            for (SoundContainer<Sound> sound : sounds) {
                tracks.add(new MusicTrack(sound.getSound(random)));
            }

            if (!tracks.isEmpty()) musicGroups.add(new MusicGroup(formattedName, tracks));
        }
        return musicGroups;
    }
}
