package zone.towo.musicconfig.file;

import zone.towo.musicconfig.music.MusicTrack;

public record SaveableMusicTrack(String soundFile, int frequency) {

    public static SaveableMusicTrack of(MusicTrack original) {
        return new SaveableMusicTrack(original.getSound().getIdentifier().toString(), original.getFrequency());
    }
}
