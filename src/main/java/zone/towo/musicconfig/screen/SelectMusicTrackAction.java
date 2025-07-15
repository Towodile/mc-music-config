package zone.towo.musicconfig.screen;

import zone.towo.musicconfig.music.MusicTrack;

import java.util.List;

public interface SelectMusicTrackAction {
    void processSelection(List<MusicTrack> tracks);
}
