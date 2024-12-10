package splash.dev.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import splash.dev.util.VertexConsumerDuck;

@Mixin(DrawContext.class)
public class DrawContextMixin implements VertexConsumerDuck {

    @Shadow @Final private VertexConsumerProvider.Immediate vertexConsumers;

    @Override
    public VertexConsumerProvider.Immediate vertexConsumers() {
        return vertexConsumers;
    }
}