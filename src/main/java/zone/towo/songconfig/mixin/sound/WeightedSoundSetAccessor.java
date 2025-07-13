package zone.towo.songconfig.mixin.sound;

import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.client.sound.SoundContainer;
import net.minecraft.client.sound.Sound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(WeightedSoundSet.class)
public interface WeightedSoundSetAccessor {
	@Accessor("sounds")
	List<SoundContainer<Sound>> getSounds();
}
