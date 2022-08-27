package xyz.bamaygay.motd.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import xyz.bamaygay.motd.bungee.handler.CommandHandler;
import xyz.bamaygay.motd.bungee.listeners.ProxyPingListener;
import xyz.bamaygay.motd.bungee.utils.ConfigUtil;
import xyz.bamaygay.motd.bungee.variables.Messages;
import xyz.bamaygay.motd.bungee.variables.Variables;

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
		pluginManager.registerCommand(this, new CommandHandler("fishymotd", "fishymotd.admin", variables, messages, this));
	}
}
