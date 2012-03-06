package fr.respawner.minecheater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.respawner.minecheater.structure.MCPlayerListEntry;
import fr.respawner.minecheater.structure.entity.MCCharacter;
import fr.respawner.minecheater.structure.entity.MCMob;
import fr.respawner.minecheater.structure.entity.MCObject;
import fr.respawner.minecheater.worker.PacketsHandler;
import fr.respawner.minecheater.worker.PingHandler;

public final class MinecraftClient extends Thread {
    private static final Logger log;
    private static final InputStream stdin;
    private static final PrintStream stdout;

    private String ip;
    private int port;
    private boolean running;

    static {
        log = Logger.getLogger(MinecraftClient.class);
        stdin = System.in;
        stdout = System.out;
    }

    public MinecraftClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.running = true;
    }

    public String getIP() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public boolean isRunning() {
        return this.running;
    }

    public void stopClient() {
        this.running = false;
    }

    @Override
    public void run() {
        final BufferedReader userInput;

        PingHandler ping;
        PacketsHandler handler;

        String command;
        String[] args;

        List<MCObject> objects;

        ping = null;
        handler = null;

        /*
         * Handle user inputs.
         */
        userInput = new BufferedReader(new InputStreamReader(stdin));
        while (this.running) {
            try {
                command = userInput.readLine();

                /*
                 * No input?
                 */
                if (command == null) {
                    continue;
                }

                /*
                 * The command is only the first element of the array.
                 */
                args = command.split(" ", 2);

                switch (args[0].toLowerCase()) {
                case "connect":
                    /*
                     * Handle incoming packet.
                     */
                    if (handler != null) {
                        stdout.println("Already connected to the server.");
                    } else {
                        handler = new PacketsHandler(this);
                        handler.start();
                    }
                    break;

                case "help":
                case "?":
                    stdout.println("Available commands:");
                    stdout.println("  * connect        - connect to the server and login");
                    stdout.println("  * help|?         - print this help");
                    stdout.println("  * message <text> - send a message");
                    stdout.println("  * mobs           - show all the nearest mobs");
                    stdout.println("  * objects        - list all objects of the world");
                    stdout.println("  * online         - show who is online");
                    stdout.println("  * ping           - try to ping the server");
                    stdout.println("  * player         - show information about the player");
                    stdout.println("  * players         - show all the nearest players and NPC");
                    stdout.println("  * quit|exit      - stop this program");
                    stdout.println("  * respawn        - respawn the player");
                    stdout.println("  * time           - display the time of the world");
                    stdout.println("  * system         - show informations about the system");
                    break;

                case "message":
                    if (args.length < 2) {
                        stdout.println("The 'message' command needs an argument.");
                    } else {
                        handler.sendPacket((byte) 0x03, args[1]);
                    }
                    break;

                case "mobs":
                    objects = new ArrayList<>();

                    for (MCObject object : handler.getWorld().getAllObjects()) {
                        if (object instanceof MCMob) {
                            objects.add(object);
                        }
                    }

                    for (MCObject object : objects) {
                        stdout.println(object);
                    }

                    break;

                case "move":
                    final double[] move;
                    final String[] numb;

                    move = new double[3];
                    numb = args[1].split(" ");

                    for (byte b = 0; b < move.length; b++) {
                        move[b] = Double.parseDouble(numb[b]);
                    }

                    handler.getWorld().getPlayer().getLocation()
                            .setOnGround(true);
                    handler.getWorld().getPlayer()
                            .move(move[0], move[1], move[2]);
                    handler.sendPacket((byte) 0x0D, false);
                    break;

                case "objects":
                    objects = handler.getWorld().getAllObjects();

                    for (MCObject object : objects) {
                        stdout.println(object);
                    }

                    break;

                case "online":
                    final List<MCPlayerListEntry> people;

                    people = handler.getWorld().getOnlinePeople();

                    for (MCPlayerListEntry entry : people) {
                        stdout.println(entry);
                    }

                    break;

                case "ping":
                    /*
                     * Just a simple handler that ping the server.
                     */
                    if (handler != null) {
                        stdout.println("Already connected to the server.");
                    } else {
                        ping = new PingHandler(this);
                        ping.start();

                        try {
                            /*
                             * Wait for the ping to terminate.
                             */
                            ping.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;

                case "player":
                    stdout.println(handler.getWorld().getPlayer());
                    break;

                case "players":
                    objects = new ArrayList<>();

                    for (MCObject object : handler.getWorld().getAllObjects()) {
                        if (object instanceof MCCharacter) {
                            objects.add(object);
                        }
                    }

                    for (MCObject object : objects) {
                        stdout.println(object);
                    }

                    break;

                case "quit":
                case "exit":
                    if (handler != null) {
                        handler.sendPacket((byte) 0xFF);
                    }
                    this.running = false;
                    break;

                case "respawn":
                    handler.sendPacket((byte) 0x09);
                    break;

                case "time":
                    stdout.println(handler.getWorld().getTime());
                    break;

                case "system":
                    Config.showSystemInfo();
                    break;

                default:
                    stdout.println("Unknown command '" + args[0] + "'.");
                    break;
                }
            } catch (IOException e) {
                log.error("Can't read user input.");
            }
        }

        /*
         * Stop the packets handler.
         */
        if (handler.isRunning()) {
            handler.stopHandler();
        }

        try {
            /*
             * Wait for the packet handler to stop.
             */
            handler.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            userInput.close();
        } catch (IOException e) {
            log.error("User input already closed.");
        }

        log.info("Shutting down client worker.");
    }
}
