package org.dnsge.fortprac.mixin;

import net.minecraft.client.gui.screen.SaveLevelScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.dnsge.fortprac.FortressPracticeMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The OptionsScreenMixin adds the "Stop Practice & Quit" button under more options
 * that allows the user to break the reset cycle when practicing.
 */
@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void addStopPracticeButton(CallbackInfo ci) {
        if (FortressPracticeMod.getCurrentlyPracticing()) {
            this.addButton(new ButtonWidget(0, this.height - 20, 120, 20, new LiteralText("Stop Practice & Quit"), (button) -> this.stopPractice()));
        }
    }

    private void stopPractice() {
        assert this.client != null;
        assert this.client.world != null;

        FortressPracticeMod.setCurrentlyPracticing(false);
        this.client.world.disconnect();
        this.client.disconnect(new SaveLevelScreen(new TranslatableText("menu.savingLevel")));
        this.client.openScreen(new TitleScreen());
    }
}
