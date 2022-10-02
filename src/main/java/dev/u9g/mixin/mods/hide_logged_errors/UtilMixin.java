package dev.u9g.mixin.mods.hide_logged_errors;

import dev.u9g.PrisonsModConfig;
import net.minecraft.util.Util;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Mixin(Util.class)
public class UtilMixin {
    @Inject(method = "executeTask", at = @At("HEAD"), cancellable = true)
    private static <V> void shutUpErrors(FutureTask<V> task, Logger logger, CallbackInfoReturnable<V> cir) {
        if (!PrisonsModConfig.INSTANCE.debug.noExecuteTaskLogging) return;
        cir.cancel();
        try {
            task.run();
            cir.setReturnValue(task.get());
        }
        catch (final ExecutionException | InterruptedException ignored) {}
        cir.setReturnValue(null);
    }
}
