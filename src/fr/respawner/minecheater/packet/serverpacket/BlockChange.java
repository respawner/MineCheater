package fr.respawner.minecheater.packet.serverpacket;

import java.io.IOException;

import fr.respawner.minecheater.packet.Packet;
import fr.respawner.minecheater.worker.IHandler;

public final class BlockChange extends Packet {
    private int x;
    private byte y;
    private int z;
    private byte type;
    private byte metadata;

    public BlockChange(IHandler handler) {
        super(handler, (byte) 0x35);
    }

    @Override
    public void read() throws IOException {
        this.x = this.readInt();
        this.y = this.readByte();
        this.z = this.readInt();
        this.type = this.readByte();
        this.metadata = this.readByte();
    }

    @Override
    public void write() throws IOException {
        /*
         * We don't write this packet.
         */
    }

    @Override
    public void process() {
        /*
         * Nothing to do.
         */
    }

    @Override
    public Packet response() {
        /*
         * We don't send a response to this packet.
         */
        return null;
    }

    @Override
    public String getDataAsString() {
        final StringBuilder builder;

        builder = new StringBuilder();

        builder.append("Position: x = ");
        builder.append(this.x);
        builder.append(", y = ");
        builder.append(this.y);
        builder.append(", z = ");
        builder.append(this.z);
        builder.append(" | Type = ");
        builder.append(this.type);
        builder.append(" | Metadata = ");
        builder.append(this.metadata);

        return builder.toString();
    }
}
