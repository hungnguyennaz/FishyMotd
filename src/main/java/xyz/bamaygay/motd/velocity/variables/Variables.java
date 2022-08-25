package xyz.bamaygay.motd.velocity.variables;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.velocitypowered.api.proxy.server.ServerPing;

import xyz.bamaygay.motd.velocity.FishyMotd;
import xyz.bamaygay.motd.velocity.utils.Components;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

@ConfigSerializable
public class Variables {
    private static final ObjectMapper<Variables> MAPPER;
    private static final Random rm = new Random();

    static {
        try {
            MAPPER = ObjectMapper.forClass(Variables.class);
        } catch (ObjectMappingException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static Variables loadFrom(ConfigurationNode node) throws ObjectMappingException {
        return MAPPER.bindToNew().populate(node);
    }

    public static Variables loadConfig(Path path, FishyMotd plugin) throws IOException, ObjectMappingException {
        path = path.resolve("config.yml");
        if (Files.notExists(path)) {
            try (InputStream in = plugin.getClass().getClassLoader().getResourceAsStream("config.yml")) {
                Files.copy(in, path);
            }
        }

        final YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder()
            .setPath(path)
            .build();

        final ConfigurationNode node = loader.load();
        return Variables.loadFrom(node);
    }

    @Setting
    private Motd motd;
    public Motd motd() {
        return this.motd;
    }
    @Setting
    private Sample sample;
    public Sample sample() {
        return this.sample;
    }
    @Setting
    private Protocol protocol;
    public Protocol protocol() {
        return this.protocol;
    }
    @Setting
    private MaxPlayers maxplayers;
    public MaxPlayers maxPlayers() {
        return this.maxplayers;
    }
    @Setting
    private FakePlayers fakeplayers;
    public FakePlayers fakePlayers() {
        return this.fakeplayers;
    }

    @ConfigSerializable
    public static class Motd {
        @Setting
        private boolean enabled;
        public boolean enabled() {
            return this.enabled;
        }

        @Setting
        private List<String> motds;

        private static final Component DEFAULT_MOTD = Component.text()
            .append(Component.text("FishyMotd default generated MOTD"))
            .append(Component.newline())
            .append(Component.text("Whoops... No MOTD has been specified!"))
            .build();

        public Component getMOTD(final int maxPlayers, final int onlinePlayers) {
            if (motds.isEmpty()) {
                return DEFAULT_MOTD;
            }
    
            final int randomIndex = rm.nextInt(motds.size());
    
            return Components.SERIALIZER.deserialize(
                    motds.get(randomIndex)
                        .replace("%maxplayers%", Integer.toString(maxPlayers))
                        .replace("%onlineplayers%", Integer.toString(onlinePlayers))
                    );
        }
    }

    @ConfigSerializable
    public static class Sample {
        @Setting
        private boolean enabled;
        public boolean enabled() {
            return this.enabled;
        }

        @Setting
        private List<String> samples;

        private final UUID uuid = UUID.randomUUID();
        public ServerPing.SamplePlayer[] getSample(final int maxPlayers, final int onlinePlayers) {
            String[] string = samples.get(rm.nextInt(samples.size()))
                .replace("%maxplayers%", String.valueOf(maxPlayers))
                .replace("%onlineplayers%", String.valueOf(onlinePlayers))
                .replace(LegacyComponentSerializer.AMPERSAND_CHAR, LegacyComponentSerializer.SECTION_CHAR)
                .split("\n");
            
            ServerPing.SamplePlayer[] players = new ServerPing.SamplePlayer[string.length];

            for (int i = 0; i < string.length; i++) {
                players[i] = new ServerPing.SamplePlayer(string[i], uuid);
            }
            return players;
        }
    }

    @ConfigSerializable
    public static class Protocol {
        @Setting
        private boolean enabled;
        public boolean enabled() {
            return this.enabled;
        }

        @Setting
        private String name;
        public String name() {
            return this.name;
        }
    }

    @ConfigSerializable
    public static class MaxPlayers {
        @Setting
        private boolean enabled;
        public boolean enabled() {
            return this.enabled;
        }

        @Setting
        private int maxplayers;
        public int maxPlayers() {
            return this.maxplayers;
        }

        @Setting
        private boolean justonemore;
        public boolean justOneMore() {
            return this.justonemore;
        }
    }

    @ConfigSerializable
    public static class FakePlayers {
        @Setting
        private boolean enabled;
        public boolean enabled() {
            return this.enabled;
        }

        @Setting
        private int amount;

        @Setting
        private Mode mode;

        public int players(int amount) {
            switch (mode) {
                case STATIC:
                    return this.amount;
                case RANDOM:
                    return rm.nextInt(this.amount);
                case DIVISION:
                    return amount / this.amount;
            }
            return 0;
        }
    }

    public enum Mode {
        STATIC,
        RANDOM,
        DIVISION;
    }
}
