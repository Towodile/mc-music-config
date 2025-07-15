package zone.towo.musicconfig.music;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.Sound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import zone.towo.musicconfig.file.SaveableMusicGroup;
import zone.towo.musicconfig.file.SaveableMusicTrack;
import zone.towo.musicconfig.mixin.sound.SoundManagerAccessor;

import java.util.ArrayList;
import java.util.Comparator;

public record MusicResource(Identifier resource) {
    private static ArrayList<MusicResource> ALL_MUSIC_RESOURCES = null;
    public static ArrayList<MusicResource> getAll(MinecraftClient client) {
        if (ALL_MUSIC_RESOURCES == null) {
            var allMusicResources = ((SoundManagerAccessor)(client.getSoundManager())).getSoundResources().keySet().stream().filter(
                    key -> key.getPath().startsWith("sounds/music/")
            ).toList();

            ALL_MUSIC_RESOURCES = new ArrayList<>();
            for (Identifier resource : allMusicResources) {
                String transformedKey = resource.getPath().replace("sounds/", "").replace(".ogg","");
                ALL_MUSIC_RESOURCES.add(new MusicResource(Identifier.of(resource.getNamespace(), transformedKey)));
            }
        }
        return ALL_MUSIC_RESOURCES;
    }


    public MusicTrack createTrack() {
        return createTrack(1f, 1f, 0);
    }

    public MusicTrack createTrack(float baseVolume, float basePitch, int weight) {
        Sound sound = new Sound(resource, ConstantFloatProvider.create(baseVolume), ConstantFloatProvider.create(basePitch), weight, Sound.RegistrationType.FILE, true, true, 16);
        return new MusicTrack(sound, false);
    }
}
