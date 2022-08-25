package xyz.bamaygay.motd.bungee.variables;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

import xyz.bamaygay.motd.bungee.BungeePlugin;
import xyz.bamaygay.motd.bungee.utils.ConfigUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

public class Variables {
	private static final String DEFAULT_MOTD = "Fishy Motd\nDefault server Motd.";

	private final ConfigUtil configUtil;
	private final Collection<String> pinged = new HashSet<>();
	private String[] motds;
	private String[] sampleSamples;
	private int maxPlayers, fakePlayersAmount;
	private boolean motdEnabled, sampleEnabled, protocolEnabled, maxPlayersJustOneMore, maxPlayersEnabled, fakePlayersEnabled;
	private String protocolName, fakePlayersMode;

	public Variables(ConfigUtil configUtil) {
		this.configUtil = configUtil;
		reloadConfig();
	}

	public void reloadConfig() {
		final Configuration config = configUtil.getConfiguration(new File(BungeePlugin.INSTANCE.getDataFolder(), "config.yml"));

		motdEnabled = config.getBoolean("motd.enabled");
		motds = config.getStringList("motd.motds").toArray(new String[0]);
		sampleEnabled = config.getBoolean("sample.enabled");
		sampleSamples = config.getStringList("sample.samples").toArray(new String[0]);
		protocolEnabled = config.getBoolean("protocol.enabled");
		protocolName = config.getString("protocol.name");
		maxPlayersEnabled = config.getBoolean("maxplayers.enabled");
		maxPlayers = config.getInt("maxplayers.maxplayers");
		maxPlayersJustOneMore = config.getBoolean("maxplayers.justonemore");
		fakePlayersEnabled = config.getBoolean("fakeplayers.enabled");
		fakePlayersAmount = config.getInt("fakeplayers.amount");
		fakePlayersMode = config.getString("fakeplayers.mode");
	}

	public boolean isMotdEnabled() {
		return motdEnabled;
	}

	public String getMOTD(final int maxPlayers, final int onlinePlayers) {
		if (motds.length < 1) {
			return DEFAULT_MOTD;
		}

		final int randomIndex = (int) (Math.floor(Math.random() * motds.length));

		return ChatColor.translateAlternateColorCodes('&',
				motds[randomIndex].replace("%maxplayers%", String.valueOf(maxPlayers)).replace("%onlineplayers%",
						String.valueOf(onlinePlayers)));
	}

	public boolean isSampleEnabled() {
		return sampleEnabled;
	}

	public String[] getSample(final int maxPlayers, final int onlinePlayers) {
		return ChatColor.translateAlternateColorCodes('&',
				sampleSamples[(int) (Math.floor(Math.random() * sampleSamples.length))]
						.replace("%maxplayers%", String.valueOf(maxPlayers))
						.replace("%onlineplayers%", String.valueOf(onlinePlayers)))
				.split("\n");
	}

	public boolean isProtocolEnabled() {
		return protocolEnabled;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public boolean isMaxPlayersEnabled() {
		return maxPlayersEnabled;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public boolean isMaxPlayersJustOneMore() {
		return maxPlayersJustOneMore;
	}

	public boolean isFakePlayersEnabled() {
		return fakePlayersEnabled;
	}

	public int getFakePlayersAmount(int players) {
		switch (fakePlayersMode) {
			case "STATIC":
				return fakePlayersAmount;
			case "RANDOM":
				return (int) (Math.floor(Math.random() * fakePlayersAmount) + 1);
			case "DIVISION":
				return players / fakePlayersAmount;
			default:
				return 0;
		}
	}

	public boolean hasPinged(final String name) {
		return pinged.contains(name);
	}

	public void addPinged(final String name) {
		pinged.add(name);
	}

	public void clearPinged() {
		pinged.clear();
	}
}
