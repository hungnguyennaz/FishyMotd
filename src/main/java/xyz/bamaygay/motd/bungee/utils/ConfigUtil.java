package xyz.bamaygay.motd.bungee.utils;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigUtil {
	final private Plugin plugin;

	public ConfigUtil(final Plugin plugin) {
		this.plugin = plugin;
	}

	public Configuration getConfiguration(File file) {
		try {
			return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void createConfiguration(File file) {
		try {
			if (!file.exists()) {
				final InputStream inputStream = plugin.getClass().getClassLoader().getResourceAsStream(file.getName());
				final File parentFile = file.getParentFile();

				if (parentFile != null) parentFile.mkdirs();

				if (inputStream != null) {
					Files.copy(inputStream, file.toPath());
				} else file.createNewFile();
			}
		} catch (final IOException e) {
			plugin.getLogger().severe("Unable to create configuration file!");
		}
	}
}