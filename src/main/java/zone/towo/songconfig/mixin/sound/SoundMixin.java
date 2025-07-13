package zone.towo.songconfig.mixin.sound;

import net.minecraft.client.sound.Sound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import zone.towo.songconfig.util.SoundExtension;

@Mixin(Sound.class)
public class SoundMixin implements SoundExtension {
    @Shadow
    @Final
    @Mutable
    private int weight;

    @Override
    public void setWeight(int newWeight) {
        this.weight = newWeight;
    }
}
