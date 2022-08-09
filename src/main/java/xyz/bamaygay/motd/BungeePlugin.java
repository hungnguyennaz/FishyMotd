package xyz.bamaygay.motd;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import xyz.bamaygay.motd.handler.CommandHandler;
import xyz.bamaygay.motd.listeners.ProxyPingListener;
import xyz.bamaygay.motd.utils.ConfigUtil;
import xyz.bamaygay.motd.variables.Messages;
import xyz.bamaygay.motd.variables.Variables;

import java.io.File;

public class BungeePlugin extends Plugin {
	public static BungeePlugin INSTANCE;

	@Override
	public void onEnable() {
		INSTANCE = this;

		final ConfigUtil configUtil = new ConfigUtil(this);

		configUtil.createConfiguration(new File(getDataFolder(), "config.yml"));
		configUtil.createConfiguration(new File(getDataFolder(), "messages.yml"));

		final ProxyServer proxy = getProxy();
		final Variables variables = new Variables(configUtil);
		final Messages messages = new Messages(configUtil);
		final PluginManager pluginManager = proxy.getPluginManager();

		pluginManager.registerListener(this, new ProxyPingListener(variables));
		pluginManager.registerCommand(this, new CommandHandler("motd", "fishymotd.command.motd", variables, messages, this));
	}
}