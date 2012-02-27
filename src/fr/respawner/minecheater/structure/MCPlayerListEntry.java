package fr.respawner.minecheater.structure;

public final class MCPlayerListEntry {
    private String playerName;
    private boolean online;
    private short ping;

    public MCPlayerListEntry(String playerName, boolean online, short ping) {
        this.playerName = playerName;
        this.online = online;
        this.ping = ping;
    }

    public final String getPlayerName() {
        return this.playerName;
    }

    public final boolean isOnline() {
        return this.online;
    }

    public final void setOnline(boolean online) {
        this.online = online;
    }

    public final short getPing() {
        return this.ping;
    }

    public final void setPing(short ping) {
        this.ping = ping;
    }

    @Override
    public String toString() {
        final StringBuilder builder;

        builder = new StringBuilder();

        builder.append("Player name = ");
        builder.append(this.playerName);
        builder.append(" | ");
        builder.append(this.online ? "Online" : "Not online");
        builder.append(" | Ping = ");
        builder.append(this.ping);

        return builder.toString();
    }
}
