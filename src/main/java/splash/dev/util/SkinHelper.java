package splash.dev.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import splash.dev.PVPStatsPlus;
import splash.dev.saving.Savable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

import static splash.dev.PVPStatsPlus.mc;

public class SkinHelper {

    public static void saveSkin(AbstractClientPlayerEntity entity) { // todo: update skin if chaneged 0::
        if (entity == null) return;

        new Thread(() -> {
            try {
                URL skinUrl = new URL("https://crafatar.com/skins/" + entity.getGameProfile().getId().toString().replace("-", ""));

                HttpURLConnection connection = (HttpURLConnection) skinUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                try (InputStream inputStream = connection.getInputStream()) {

                    File outputFile = new File(Savable.skinsFolder, entity.getGameProfile().getName() + ".png");

                    BufferedImage image = ImageIO.read(inputStream);

                    ImageIO.write(image, "PNG", outputFile);

                    PVPStatsPlus.LOGGER.info("Saved " + entity.getGameProfile().getName() + "'s skin.");
                }
            } catch (IOException e) {
                PVPStatsPlus.LOGGER.error("Failed to save skin for {}", entity.getGameProfile().getName(), e);
            }
        }).start();
    }


    public static AbstractClientPlayerEntity getSkinTarget(String username) {
        return new FakeEntity(new GameProfile(UUID.randomUUID(), username));
    }

    public static InputStream fromInput(File file) {
        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class FakeEntity extends AbstractClientPlayerEntity {


        public FakeEntity(GameProfile gameProfile) {
            super(Objects.requireNonNull(mc.world), gameProfile);
        }

        @Override
        public SkinTextures getSkinTextures() { // im a genius (literally)
            File skinFile = new File(Savable.skinsFolder, getGameProfile().getName() + ".png");

            if (skinFile.exists()) {
                try (InputStream inputStream = fromInput(skinFile)) {
                    Identifier textureId = Identifier.of("splash", "skins/" + (new Random().nextInt(420) + 1) + ".png");
                    assert inputStream != null;
                    NativeImage nativeImage = NativeImage.read(inputStream);
                    NativeImageBackedTexture skinTexture = new NativeImageBackedTexture(nativeImage);

                    mc.getTextureManager().registerTexture(textureId, skinTexture);

                    return new SkinTextures(textureId, "", textureId, textureId, SkinTextures.Model.fromName("slim"), false);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return mc.player != null ? mc.player.getSkinTextures() : super.getSkinTextures();
        }
    }
}