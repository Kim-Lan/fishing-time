package com.kimlan.fishingtime.mixin;

import static com.kimlan.fishingtime.FishingTime.CONFIG;
import static com.kimlan.fishingtime.FishingTime.LOGGER;

import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(FishingBobberEntity.class)
abstract class FishingBobberEntityMixin {
    @Shadow
    private int waitCountdown;

    @Shadow
    private final int lureLevel;

    public FishingBobberEntityMixin(int lureLevel) {
        this.lureLevel = lureLevel;
    }

    @ModifyArgs(
            method = "tickFishingLogic",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;nextInt(Lnet/minecraft/util/math/random/Random;II)I",
                    ordinal = 1
            )
    )
    private void modifyFishTravelTime(Args args) {
        args.set(1, CONFIG.fishTravelTimeRange().get(0));
        args.set(2, CONFIG.fishTravelTimeRange().get(1));
    }

    @ModifyArgs(
            method = "tickFishingLogic",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;nextInt(Lnet/minecraft/util/math/random/Random;II)I",
                    ordinal = 2
            )
    )
    private void modifyWaitTime(Args args) {
        args.set(1, CONFIG.waitTimeRange().get(0));
        args.set(2, CONFIG.waitTimeRange().get(1));
    }

    @Inject(
            method = "tickFishingLogic",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/util/math/MathHelper;nextInt(Lnet/minecraft/util/math/random/Random;II)I",
                    ordinal = 2
            ),
            cancellable = true)
    private void modifyLureLevelFactor(CallbackInfo ci) {
         this.waitCountdown -= Math.max((int) 0, this.lureLevel * CONFIG.lureLevelFactor());
         this.waitCountdown = Math.max((int) 1, this.waitCountdown);
         LOGGER.info("wait countdown: " + this.waitCountdown);
         ci.cancel();
    }
}
