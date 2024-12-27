package splash.dev.mixin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import splash.dev.PVPStatsPlus;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Shadow
    private Channel channel;

    @Inject(method = "channelRead0*", at = @At("HEAD"))
    public void channelRead0(ChannelHandlerContext chc, Packet<?> packet, CallbackInfo ci) {
        if (this.channel.isOpen() && packet != null && PVPStatsPlus.getRecorder() != null) {
            if (PVPStatsPlus.getRecorder().recording) PVPStatsPlus.getRecorder().onPacketReceive(packet);
        }
    }

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", at = @At("TAIL"))
    private void onSendPacketTail(Packet<?> packet, @Nullable PacketCallbacks callbacks, CallbackInfo ci) {
        if (packet instanceof PlayerActionC2SPacket packet1) {
            System.out.println("sent action " + packet1.getAction());
        }
    }

}
