/*
 * Copyright (c) 2012 Guillaume Mazoyer
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.respawner.minecheater.packet.serverpacket;

import java.io.IOException;

import fr.respawner.minecheater.math.Rotation;
import fr.respawner.minecheater.packet.Packet;
import fr.respawner.minecheater.structure.entity.MCEntity;
import fr.respawner.minecheater.worker.IHandler;

public final class EntityLookAndRelativeMove extends Packet {
    private int entityID;
    private byte dX;
    private byte dY;
    private byte dZ;
    private byte yaw;
    private byte pitch;

    public EntityLookAndRelativeMove(IHandler handler) {
        super(handler, ENTITY_LOOK_AND_RELATIVE_MOVE);
    }

    @Override
    public void read() throws IOException {
        this.entityID = this.readInt();
        this.dX = this.readByte();
        this.dY = this.readByte();
        this.dZ = this.readByte();
        this.yaw = this.readByte();
        this.pitch = this.readByte();
    }

    @Override
    public void write() throws IOException {
        /*
         * We don't write this packet.
         */
    }

    @Override
    public void process() {
        final MCEntity entity;
        Rotation rotation;

        /*
         * Find the entity to set its look and move.
         */
        entity = (MCEntity) this.getWorld().findObjectByID(this.entityID);

        if (entity != null) {
            rotation = entity.getRotation();

            entity.setMove(this.dX, this.dY, this.dZ);

            if (rotation != null) {
                rotation.setRotationFromPacket(this.yaw, this.pitch);
            } else {
                rotation = new Rotation();
                rotation.setRotationFromPacket(this.yaw, this.pitch);
                entity.setRotation(rotation);
            }
        }
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

        builder.append("Entity ID = ");
        builder.append(this.entityID);
        builder.append(" | Move : x = ");
        builder.append(this.dX);
        builder.append(", y = ");
        builder.append(this.dY);
        builder.append(", z = ");
        builder.append(this.dZ);
        builder.append(" | Look: yaw = ");
        builder.append(this.yaw);
        builder.append(", pitch = ");
        builder.append(this.pitch);

        return builder.toString();
    }
}
