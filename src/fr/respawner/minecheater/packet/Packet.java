package fr.respawner.minecheater.packet;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import fr.respawner.minecheater.World;
import fr.respawner.minecheater.worker.PacketsHandler;

public abstract class Packet {
    public static final String STRING_DELIMITER = new String(
            new char[] { 0xA7 });

    private final List<Byte> rawPacket;
    private final List<Byte> packet;

    protected final PacketsHandler handler;
    protected final byte id;

    public Packet(PacketsHandler handler, byte id) {
        this.handler = handler;
        this.rawPacket = new ArrayList<>();
        this.packet = new ArrayList<>();
        this.id = id;
    }

    /**
     * Read and return a byte.
     */
    protected final byte readByte() throws IOException {
        final byte read;

        read = this.handler.getInput().readByte();
        this.rawPacket.add(read);

        return read;
    }

    /**
     * Read and return an unsigned byte as integer.
     */
    protected final int readUnsignedByte() throws IOException {
        final int read;

        read = this.handler.getInput().readUnsignedByte();

        for (byte b : ByteBuffer.allocate(4).putInt(read).array()) {
            this.rawPacket.add(b);
        }

        return read;
    }

    /**
     * Read and return a short.
     */
    protected final short readShort() throws IOException {
        final short read;

        read = this.handler.getInput().readShort();

        for (byte b : ByteBuffer.allocate(2).putShort(read).array()) {
            this.rawPacket.add(b);
        }

        return read;
    }

    /**
     * Read and return a integer.
     */
    protected final int readInt() throws IOException {
        final int read;

        read = this.handler.getInput().readInt();

        for (byte b : ByteBuffer.allocate(4).putInt(read).array()) {
            this.rawPacket.add(b);
        }

        return read;
    }

    /**
     * Read and return a long.
     */
    protected final long readLong() throws IOException {
        final long read;

        read = this.handler.getInput().readLong();

        for (byte b : ByteBuffer.allocate(8).putLong(read).array()) {
            this.rawPacket.add(b);
        }

        return read;
    }

    /**
     * Read and return a float.
     */
    protected final float readFloat() throws IOException {
        final float read;

        read = this.handler.getInput().readFloat();

        for (byte b : ByteBuffer.allocate(4).putFloat(read).array()) {
            this.rawPacket.add(b);
        }

        return read;
    }

    /**
     * Read and return a double.
     */
    protected final double readDouble() throws IOException {
        final double read;

        read = this.handler.getInput().readDouble();

        for (byte b : ByteBuffer.allocate(8).putDouble(read).array()) {
            this.rawPacket.add(b);
        }

        return read;
    }

    /**
     * Read and return a boolean.
     */
    protected final boolean readBoolean() throws IOException {
        final boolean read;

        read = this.handler.getInput().readBoolean();

        rawPacket.add((byte) (read ? 0x01 : 0x00));

        return read;
    }

    /**
     * Read and return a UTF-16 string.
     */
    protected final String readUnicodeString() throws IOException {
        final short length;
        final byte[] buffer;

        /*
         * Read the length of the string.
         */
        length = this.readShort();

        if (length < 0) {
            System.out.println("Length of string = " + length);
        }

        /*
         * UTF-16 uses 16-bit word so since we read bytes which have 8 bits
         * each, it doubles the length of the string to read.
         */
        buffer = new byte[length * 2];

        for (short s = 0; s < buffer.length; s++) {
            buffer[s] = this.readByte();
        }

        return new String(buffer, "UTF-16BE");
    }

    /**
     * Read and return an array of bytes.
     */
    protected final byte[] readByteArray(int length) throws IOException {
        final byte[] array;

        /*
         * Initialize the array.
         */
        array = new byte[length];

        /*
         * Fill it up.
         */
        for (int i = 0; i < length; i++) {
            array[i] = this.readByte();
        }

        return array;
    }

    /**
     * Read and return an array of bytes.
     */
    protected final short[] readShortArray(int length) throws IOException {
        final short[] array;

        /*
         * Initialize the array.
         */
        array = new short[length];

        /*
         * Fill it up.
         */
        for (int i = 0; i < length; i++) {
            array[i] = this.readShort();
        }

        return array;
    }

    /**
     * Write a byte.
     */
    protected final void writeByte(byte v) throws IOException {
        packet.add(v);
    }

    /**
     * Write an unsigned byte.
     */
    protected final void writeUnsignedByte(int v) throws IOException {
        final byte unsigned;

        unsigned = (byte) ((v < 128) ? v : (v - 256));
        packet.add(unsigned);
    }

    /**
     * Write a short.
     */
    protected final void writeShort(short v) throws IOException {
        final byte[] bytes;

        bytes = ByteBuffer.allocate(2).putShort(v).array();
        for (byte b : bytes) {
            packet.add(b);
        }
    }

    /**
     * Write an integer.
     */
    protected final void writeInt(int v) throws IOException {
        final byte[] bytes;

        bytes = ByteBuffer.allocate(4).putInt(v).array();
        for (byte b : bytes) {
            packet.add(b);
        }
    }

    /**
     * Write a long.
     */
    protected final void writeLong(long v) throws IOException {
        final byte[] bytes;

        bytes = ByteBuffer.allocate(8).putLong(v).array();
        for (byte b : bytes) {
            packet.add(b);
        }
    }

    /**
     * Write a float.
     */
    protected final void writeFloat(float v) throws IOException {
        final byte[] bytes;

        bytes = ByteBuffer.allocate(4).putFloat(v).array();
        for (byte b : bytes) {
            packet.add(b);
        }
    }

    /**
     * Write a double.
     */
    protected final void writeDouble(double v) throws IOException {
        final byte[] bytes;

        bytes = ByteBuffer.allocate(8).putDouble(v).array();
        for (byte b : bytes) {
            packet.add(b);
        }
    }

    /**
     * Write a boolean.
     */
    protected final void writeBoolean(boolean v) throws IOException {
        packet.add((byte) (v ? 0x01 : 0x00));
    }

    /**
     * Write a UTF-16 string.
     */
    protected final void writeUnicodeString(String string) throws IOException {
        final byte[] unicode;

        /*
         * We first send the size of the string.
         */
        this.writeShort((short) string.length());

        /*
         * Convert the string to unicode and send it byte per byte.
         */
        unicode = string.getBytes("UTF-16BE");
        for (short s = 0; s < unicode.length; s++) {
            packet.add(unicode[s]);
        }
    }

    /**
     * Actually send the packet (the buffer of bytes).
     */
    protected final void send() throws IOException {
        final byte[] bytes;

        bytes = new byte[packet.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = packet.get(i);
        }

        this.handler.getOutput().write(bytes);
    }

    /**
     * Get the world used by the server.
     */
    protected final World getWorld() {
        return this.handler.getWorld();
    }

    /**
     * The ID of the packet.
     */
    public final int getID() {
        return this.id;
    }

    /**
     * Read the packet from the network.
     */
    public abstract void read() throws IOException;

    /**
     * Write the packet on the network.
     */
    public abstract void write() throws IOException;

    /**
     * Process the packet.
     * 
     * <p>
     * This method is not used to send a response or get the data of the packet.
     * It is used to make a link between what's happening on the network and
     * what should be done in our code.
     */
    public abstract void process();

    /**
     * Get the packet that should be sent as a response to this current packet.
     */
    public abstract Packet response();

    /**
     * Get the data contained by the packet.
     */
    public abstract Object getData();

    @Override
    public final String toString() {
        final StringBuilder builder;
        final Field[] fields;

        builder = new StringBuilder();
        fields = this.getClass().getDeclaredFields();

        builder.append("Packet ");
        builder.append(String.format("%x", this.id));
        builder.append(" : ");
        builder.append(this.getClass().getName());
        builder.append('\n');

        for (Field field : fields) {
            builder.append("  * field '");
            builder.append(field.getName());
            builder.append("' of type '");
            builder.append(field.getType().getName());
            builder.append("'\n");
        }

        builder.append("  * Parsed data -> '");
        builder.append(this.getData());
        builder.append("'\n");
        builder.append("  * Raw packet  -> '");
        builder.append(String.format("%x", this.id));

        for (Byte b : this.rawPacket) {
            builder.append(String.format("%x", b));
        }

        builder.append("'\n");

        return builder.toString();
    }
}
