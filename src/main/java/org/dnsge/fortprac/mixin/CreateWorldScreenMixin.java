package org.dnsge.fortprac.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.MoreOptionsDialog;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.world.Difficulty;
import org.dnsge.fortprac.FortressPracticeMod;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;


/**
 * The CreateWorldScreenMixin automatically configures a new world when in
 * practicing mode.
 */
@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends Screen {
    @Shadow
    private TextFieldWidget levelNameField;

    @Shadow
    private Difficulty field_24289;
    @Shadow
    private Difficulty field_24290;

    @Shadow
    @Final
    public MoreOptionsDialog moreOptionsDialog; // currently unused

    protected CreateWorldScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected abstract void createLevel();


    @Inject(method = "init", at = @At("TAIL"))
    private void autogenerateWorld(CallbackInfo info) throws IOException {
        if (!FortressPracticeMod.getCurrentlyPracticing()) {
            return; // not currently practicing, don't modify any world settings
        }

        int practiceNumber = FortressPracticeMod.getAndIncrementPracticeNumber();
        this.levelNameField.setText(String.format("Fortress Practice #%d", practiceNumber));
        this.field_24289 = Difficulty.EASY;
        this.field_24290 = Difficulty.EASY;

        // Force world generation immediately
        createLevel();
    }
}
