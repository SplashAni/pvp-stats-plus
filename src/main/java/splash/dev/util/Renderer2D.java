package splash.dev.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;
import splash.dev.util.ducks.VertexConsumerDuck;

import java.awt.*;

import static splash.dev.PVPStatsPlus.mc;

public class Renderer2D {


    public static void fillHorizontalGradient(DrawContext context, int startX, int startY, int endX, int endY, int colorStart, int colorEnd) {
        VertexConsumer vertexConsumer = ((VertexConsumerDuck) context).vertexConsumers().getBuffer(RenderLayer.getGui());
        fillHorizontalGradient(context.getMatrices(), vertexConsumer, startX, startY, endX, endY, colorStart, colorEnd);
    }

    public static void renderLineGraph(DrawContext context, int x, int y, int maxWidth, int[] points) {
        int prevX = x;
        int prevY = y + points[0];

        for (int i = 1; i < points.length; i++) {
            int nextX = x + (i * (maxWidth / (points.length - 1)));
            int nextY = y + points[i];

            int deltaX = nextX - prevX;
            int deltaY = nextY - prevY;

            float angle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));

            float length = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            renderLineAtAngle(context, prevX, prevY, (int) length, angle);

            prevX = nextX;
            prevY = nextY;
        }
    }


    public static void renderLineAtAngle(DrawContext context, int x, int y, int length, float degrees) {
        MatrixStack matrices = context.getMatrices();
        matrices.push();

        float radians = (float) Math.toRadians(degrees);

        matrices.translate(x, y, 0f);

        matrices.multiply(RotationAxis.POSITIVE_Z.rotation(radians));

        context.fill(0, 0, length, 1, -1);

        matrices.pop();
    }


    private static void fillHorizontalGradient(MatrixStack matrices, VertexConsumer vertexConsumer,
                                               int x1, int y1, int x2, int y2, int colorStart, int colorEnd) {
        // Modified from DrawContext#fillGradient
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();

        vertexConsumer.vertex(matrix4f, x1, y1, 0).color(colorStart);
        vertexConsumer.vertex(matrix4f, x1, y2, 0).color(colorStart);

        vertexConsumer.vertex(matrix4f, x2, y2, 0).color(colorEnd);
        vertexConsumer.vertex(matrix4f, x2, y1, 0).color(colorEnd);

    }

    public static void renderIndicator(DrawContext context, int x, int y, int scale) {

        Identifier texture = Identifier.of("splash", "icon/indicator.png");

        Color color = Color.RED;
        RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        RenderSystem.setShaderTexture(0, texture);
        context.drawTexture(texture, x, y, 0, 0, scale, scale, scale, scale);
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }

    public static int[] renderOptionsBox(DrawContext context, String heading) {
        int windowWidth = context.getScaledWindowWidth();
        int windowHeight = context.getScaledWindowHeight();
        int boxWidth = 250;
        int boxHeight = 90;
        int x1 = (windowWidth - boxWidth) / 2;
        int y1 = (windowHeight - boxHeight) / 2;
        int x2 = x1 + boxWidth;
        int y2 = y1 + boxHeight;
        int topBoxHeight = 15;
        int topY1 = y1 - topBoxHeight;
        int outlineThickness = 1;

        int outlineColor = new Color(190, 189, 189, 180).getRGB();
        context.fill(
                x1 - outlineThickness,
                topY1 - outlineThickness,
                x2 + outlineThickness,
                y2 + outlineThickness,
                outlineColor
        );

        context.fill(x1, y1, x2, y2, new Color(30, 30, 30, 220).getRGB());
        context.fill(x1, topY1, x2, y1, new Color(54, 54, 54, 255).getRGB());
        int textWidth = mc.textRenderer.getWidth(heading);
        int textX = x1 + (boxWidth / 2) - (textWidth / 2);
        int textY = topY1 + (topBoxHeight / 2) - (mc.textRenderer.fontHeight / 2) + 1;
        context.drawText(mc.textRenderer, heading, textX, textY, -1, true);
        int buttonY = y1 + 20;
        return new int[]{x1, buttonY};

    }
}
