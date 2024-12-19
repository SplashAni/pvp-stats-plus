package splash.dev.util.ducks;

import net.minecraft.client.render.VertexConsumerProvider;

public interface VertexConsumerDuck {
    VertexConsumerProvider.Immediate vertexConsumers();
}
