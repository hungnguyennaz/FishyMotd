package xyz.bamaygay.motd.velocity.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;

import xyz.bamaygay.motd.velocity.FishyMotd;
import xyz.bamaygay.motd.velocity.variables.Variables;

public final class ProxyPingListener {
    private final FishyMotd plugin;
    public ProxyPingListener(FishyMotd plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPing(ProxyPingEvent event) {
        final ServerPing.Builder builder = event.getPing().asBuilder();
		final Variables variables = plugin.getVariables();
		int onlinePlayers = builder.getOnlinePlayers();
		int maxPlayers = 0;

		if (variables.fakePlayers().enabled()) {
			onlinePlayers = onlinePlayers + variables.fakePlayers().players(onlinePlayers);
			builder.onlinePlayers(onlinePlayers);
		}

		if (variables.maxPlayers().enabled()) {
			maxPlayers = variables.maxPlayers().justOneMore()
				? onlinePlayers + 1
				: variables.maxPlayers().maxPlayers();
			builder.maximumPlayers(maxPlayers);
		}

		if (variables.motd().enabled()) {
			builder.description(variables.motd().getMOTD(maxPlayers, onlinePlayers));
		}

		if (variables.protocol().enabled()) {
			builder.version(
				new ServerPing.Version(
					builder.getVersion().getProtocol(),
					variables.protocol().name())
				);
		}

		if (variables.sample().enabled()) {
			builder.samplePlayers(variables.sample().getSample(maxPlayers, onlinePlayers));
		}

		event.setPing(builder.build());
    }
}
