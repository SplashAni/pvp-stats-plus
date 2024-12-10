package splash.dev.util;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public class Renderer2D {


    public static void fillGradient(DrawContext context, int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
        VertexConsumer vertexConsumer = ((VertexConsumerDuck) context).vertexConsumers().getBuffer(RenderLayer.getGui());
        fillGradient(context.getMatrices(), vertexConsumer, startX, startY, endX, endY, colorStart, colorEnd);
    }

    private static void fillGradient(MatrixStack matrices, VertexConsumer vertexConsumer,
                                     int x1, int y1, int x2, int y2, int colorStart, int colorEnd) {
        // Modified from DrawContext#fillGradient
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        vertexConsumer.vertex(matrix4f, x1, y1, 0).color(colorStart);
        vertexConsumer.vertex(matrix4f, x1, y2, 0).color(colorStart);

        vertexConsumer.vertex(matrix4f, x2, y2, 0).color(colorEnd);
        vertexConsumer.vertex(matrix4f, x2, y1, 0).color(colorEnd);
    }
}
