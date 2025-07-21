package zone.towo.musicconfig;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zone.towo.musicconfig.music.MusicGroup;
import zone.towo.musicconfig.music.MusicTrackPlayer;
import zone.towo.musicconfig.music.custom.DynamicSoundEvents;

public class MusicConfigMod implements ClientModInitializer {
	public static final String MOD_ID = "music-config";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static MusicTrackPlayer musicPlayer;

	private boolean initialized = false;

	@Override
	public void onInitializeClient() {
		DynamicSoundEvents.register();
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.currentScreen instanceof TitleScreen && !initialized) {
				initialized = true;
				musicPlayer = MusicTrackPlayer.create(client);
				MusicGroup.initialize(client, Random.create());
			}
		});
	}

	public static MusicTrackPlayer getMusicPlayer() {
		if (musicPlayer == null) {
			throw new IllegalStateException("Music Player not initialized yet!");
		}
		return musicPlayer;
	}


}