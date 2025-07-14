package zone.towo.musicconfig.mixin.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.SoundOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zone.towo.musicconfig.screen.MusicConfigScreen;

import java.util.List;


@Mixin(SoundOptionsScreen.class)
public class SoundOptionsScreenMixin extends GameOptionsScreen {

    public SoundOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(method = "addOptions", at = @At("RETURN"))
    private void addMyButton(CallbackInfo ci) {
        ButtonWidget buttonWidget = ButtonWidget.builder(Text.of("Music Config..."), (btn) -> {
            MinecraftClient.getInstance().setScreen(new MusicConfigScreen(this, MinecraftClient.getInstance()));
        }).build();
        this.body.addAll(List.of(buttonWidget));
    }

    @Shadow
    protected void addOptions() {

    }
}
