package xyz.bamaygay.motd.bungee.handler;

import xyz.bamaygay.motd.bungee.BungeePlugin;
import xyz.bamaygay.motd.bungee.variables.Messages;
import xyz.bamaygay.motd.bungee.variables.Variables;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class CommandHandler extends Command {
	private final Variables variables;
	private final Messages messages;
	private final BungeePlugin plugin;

	public CommandHandler(final String string, String permission, final Variables variables, final Messages messages, BungeePlugin plugin) {
		super(string, permission);
		this.variables = variables;
		this.messages = messages;
		this.plugin = plugin;
	}

	public void execute(final CommandSender commandSender, final String[] args) {
			if (args.length < 1 || args[0].equalsIgnoreCase("help"))
				commandSender.sendMessage(new TextComponent(Messages.USAGE));
			else if (args[0].equalsIgnoreCase("reload")) {
				if (commandSender.hasPermission("fishymotd.admin")) {
				variables.reloadConfig();
				messages.reload();
				commandSender.sendMessage(new TextComponent(Messages.RELOAD));
			} else
				commandSender.sendMessage(new TextComponent(Messages.UNKNOWNCOMMAND));
		} else
			commandSender.sendMessage(new TextComponent(Messages.NOPERMISSION));
			}
}
