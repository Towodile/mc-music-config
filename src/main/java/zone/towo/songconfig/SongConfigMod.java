package zone.towo.songconfig;
import com.google.gson.Gson;

import com.google.gson.GsonBuilder;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zone.towo.songconfig.music.MusicGroup;

import java.util.List;

public class SongConfigMod implements ClientModInitializer {
	public static final String MOD_ID = "song-config";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private boolean listed = false;

	@Override
	public void onInitializeClient() {

		ClientTickEvents.START_WORLD_TICK.register((world) -> {
			if (!listed) {
				List<MusicGroup> groups = MusicGroup.getAll(MinecraftClient.getInstance());
				LOGGER.info("SongConfigMod initialized with {} music groups.", groups.size());

				GsonBuilder builder = new GsonBuilder();
				builder.setPrettyPrinting();
				LOGGER.info(builder.create().toJson(groups));
				listed = true;
			}
		});
	}


}