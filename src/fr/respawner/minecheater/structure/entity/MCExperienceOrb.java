package fr.respawner.minecheater.structure.entity;

public final class MCExperienceOrb extends MCEntity {
    private short count;

    public MCExperienceOrb(int entityID, int x, int y, int z, short count) {
        super(entityID, x, y, z);

        this.count = count;
    }

    public short getCount() {
        return this.count;
    }

    @Override
    public String toString() {
        final StringBuilder builder;

        builder = new StringBuilder();

        builder.append("Entity ID = ");
        builder.append(this.getEntityID());
        builder.append(" | ");
        builder.append(this.getLocation());
        builder.append(" | Count = ");
        builder.append(this.count);

        return builder.toString();
    }
}
