package org.dnsge.fortprac.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.dnsge.fortprac.FortressPracticeMod;
import org.dnsge.fortprac.util.Point2D;
import org.dnsge.fortprac.util.Rect2D;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The TitleScreenMixin handles rendering the "Practice Nether Fortresses" button
 * next to the play button. This feature was heavily inspired by Void_X_Walker's
 * Atum mod made for fast resetting speedrun worlds.
 */
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    private static final Identifier BUTTON_IMAGE = new Identifier("textures/item/blaze_rod.png");

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    private ButtonWidget resetButton;

    private Rect2D buttonRect() {
        return new Rect2D(this.width / 2 - 124, this.height / 4 + 48, 20, 20);
    }

    private ButtonWidget createFortressPracticeButton() {
        assert this.client != null;

        Rect2D rect = this.buttonRect();
        return new ButtonWidget(
                rect.position.x(),
                rect.position.y(),
                rect.width,
                rect.height,
                new LiteralText(""),
                (button) -> {
                    FortressPracticeMod.setCurrentlyPracticing(true);
                    this.client.openScreen(new CreateWorldScreen(this));
                });
    }


    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo info) {
        assert this.client != null;

        if (FortressPracticeMod.getCurrentlyPracticing()) {
            // Currently practicing and just closed the previous world, make a new one
            this.client.openScreen(new CreateWorldScreen(this));
            return;
        }

        this.resetButton = this.addButton(this.createFortressPracticeButton());
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void blazeRodIcon(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        assert this.client != null;

        Rect2D rect = this.buttonRect().inset(2);
        Point2D center = rect.center();
        this.client.getTextureManager().bindTexture(BUTTON_IMAGE);
        drawTexture(matrices, rect.position.x(), rect.position.y(), 0.0F, 0.0F, rect.width, rect.height, rect.width, rect.height);
        if (this.resetButton.isHovered()) {
            drawCenteredString(matrices, client.textRenderer, "Practice Nether Fortresses", center.x(), center.y() - rect.height - 10, 0xFFFFFF);
        }
    }

}
