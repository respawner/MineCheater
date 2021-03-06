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

import fr.respawner.minecheater.packet.Packet;
import fr.respawner.minecheater.structure.entity.MCCharacter;
import fr.respawner.minecheater.structure.inventory.MCEquipment;
import fr.respawner.minecheater.worker.IHandler;

public final class EntityEquipment extends Packet {
    private int entityID;
    private short slot;
    private short itemID;
    private short damage;

    public EntityEquipment(IHandler handler) {
        super(handler, ENTITY_EQUIPMENT);
    }

    @Override
    public void read() throws IOException {
        this.entityID = this.readInt();
        this.slot = this.readShort();
        this.itemID = this.readShort();
        this.damage = this.readShort();
    }

    @Override
    public void write() throws IOException {
        /*
         * We don't write this packet.
         */
    }

    @Override
    public void process() {
        final MCCharacter character;
        final MCEquipment equipment;

        /*
         * Find the character to set its equipment.
         */
        character = (MCCharacter) this.getWorld().findObjectByID(this.entityID);
        equipment = new MCEquipment(this.slot, this.itemID, this.damage);

        if (character != null) {
            character.setEquipment(equipment);
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
        builder.append(" | Slot = ");
        builder.append(this.slot == 0 ? "held" : ((this.slot) >= 1)
                && (this.slot <= 4) ? "armor" : this.slot);
        builder.append(" | Item ID = ");
        builder.append(this.itemID == -1 ? "no item" : this.itemID);
        builder.append(" | Damage = ");
        builder.append(this.damage);

        return builder.toString();
    }
}
