package splash.dev.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import splash.dev.util.ducks.SkinTexturesDuck;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin implements SkinTexturesDuck {
    @Unique
    Identifier texture;

    @Override
    public void setSkin(Identifier skinTextures) {
        this.texture = skinTextures;
    }

    @ModifyReturnValue(method = "getSkinTextures", at = @At(value = "RETURN"))
    public SkinTextures getSkinTextures(SkinTextures original) {
        SkinTextures textures = new SkinTextures(Identifier.of("", "")
                , original.textureUrl(), original.capeTexture(), texture, original.model(), false);
        return original;
    }
}
