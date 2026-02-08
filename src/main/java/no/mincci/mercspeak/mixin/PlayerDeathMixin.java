package no.mincci.mercspeak.mixin;

import net.minecraft.server.level.ServerPlayer;import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import no.mincci.mercspeak.PlayerDeathCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class PlayerDeathMixin {
    @Inject(at = @At(value = "TAIL"), method = "die", cancellable = true)
    private void onPlayerDeath(DamageSource damageSource, CallbackInfo ci) {
        InteractionResult result = PlayerDeathCallback.EVENT.invoker().interact(((ServerPlayer) (Object) this), damageSource);

        if (result == InteractionResult.FAIL) {
            ci.cancel();
        }
    }
}