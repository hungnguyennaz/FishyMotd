package xyz.bamaygay.motd.variables;

import xyz.bamaygay.motd.BungeePlugin;
import xyz.bamaygay.motd.utils.ConfigUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;

import java.io.File;

public class Messages {
	private final ConfigUtil configUtil;
	public static String RELOAD;
	public static String USAGE;
	public static String UNKNOWNCOMMAND;
	public static String NOPERMISSION;

	public Messages(final ConfigUtil configUtil) {
		this.configUtil = configUtil;
		reload();
	}

	public void reload() {
		final Configuration messages = configUtil.getConfiguration(new File(BungeePlugin.INSTANCE.getDataFolder(),"messages.yml"));

		RELOAD = ChatColor.translateAlternateColorCodes('&', messages.getString("messages.reload"));
		USAGE = ChatColor.translateAlternateColorCodes('&', messages.getString("messages.usage"));
		UNKNOWNCOMMAND = ChatColor.translateAlternateColorCodes('&', messages.getString("messages.unknown-command"));
		NOPERMISSION = ChatColor.translateAlternateColorCodes('&', messages.getString("messages.no-permission"));
	}
}
