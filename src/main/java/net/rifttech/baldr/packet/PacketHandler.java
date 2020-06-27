package net.rifttech.baldr.packet;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R3.PacketListenerPlayOut;
import net.rifttech.baldr.Baldr;
import net.rifttech.baldr.player.PlayerData;

import java.util.logging.Level;

@RequiredArgsConstructor
public class PacketHandler extends ChannelDuplexHandler {
    private final Baldr plugin = Baldr.getInstance();

    private final PlayerData playerData;

    @Override
    public void write(ChannelHandlerContext ctx, Object o, ChannelPromise promise) throws Exception {
        super.write(ctx, o, promise);

        try {
            playerData.handleOutboundPacket((Packet<PacketListenerPlayOut>) o);
        } catch (Throwable t) { // otherwise they'll get kicked
            plugin.getLogger().log(Level.SEVERE, "Unable to handle clientbound packet. ", t);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {
        super.channelRead(ctx, o);

        try {
            playerData.handleInboundPacket((Packet<PacketListenerPlayIn>) o);
        } catch (Throwable t) { // otherwise they'll get kicked
            plugin.getLogger().log(Level.SEVERE, "Unable to handle serverbound packet. ", t);
        }
    }
}
