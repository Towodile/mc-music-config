package zone.towo.songconfig.file;

import zone.towo.songconfig.music.MusicGroup;

import java.util.List;

public record SaveableMusicGroup(String soundEvent, List<SaveableMusicTrack> tracks) {
    public static SaveableMusicGroup of(MusicGroup original) {
        List<SaveableMusicTrack> tracks = original.tracks().stream().map(SaveableMusicTrack::of).toList();
        return new SaveableMusicGroup(original.groupedMusic().sound().getIdAsString(), tracks);
    }
}
