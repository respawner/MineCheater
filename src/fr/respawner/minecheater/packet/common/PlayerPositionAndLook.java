package fr.respawner.minecheater.packet.common;

import java.io.IOException;

import fr.respawner.minecheater.packet.Packet;
import fr.respawner.minecheater.structure.PositionAndLook;
import fr.respawner.minecheater.worker.PacketsHandler;

public final class PlayerPositionAndLook extends Packet {
    private double x;
    private double y;
    private double stance;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;

    private PositionAndLook instance;

    public PlayerPositionAndLook(PacketsHandler handler) {
        super(handler, (byte) 0x0D);
    }

    @Override
    public void read() throws IOException {
        this.x = this.readDouble();
        this.stance = this.readDouble();
        this.y = this.readDouble();
        this.z = this.readDouble();
        this.yaw = this.readFloat();
        this.pitch = this.readFloat();
        this.onGround = this.readBoolean();
    }

    @Override
    public void write() throws IOException {
        /*
         * From Client to Server, 'stance' is sent after 'y'.
         */
        this.writeByte(this.id);
        this.writeDouble(this.x);
        this.writeDouble(this.y);
        this.writeDouble(this.stance);
        this.writeDouble(this.z);
        this.writeFloat(this.yaw);
        this.writeFloat(this.pitch);
        this.writeBoolean(this.onGround);
    }

    @Override
    public void process() {
        this.instance = new PositionAndLook(this.x, this.y, this.z,
                this.stance, this.yaw, this.pitch, this.onGround);
        this.getWorld().setPosition(this.instance);
    }

    @Override
    public Packet response() {
        final PlayerPositionAndLook response;

        response = new PlayerPositionAndLook(this.handler);

        /*
         * Copy the packet.
         */
        response.x = this.x;
        response.stance = this.stance;
        response.y = this.y;
        response.z = this.z;
        response.yaw = this.yaw;
        response.pitch = this.pitch;
        response.onGround = this.onGround;

        return response;
    }

    @Override
    public Object getData() {
        return this.instance;
    }
}
