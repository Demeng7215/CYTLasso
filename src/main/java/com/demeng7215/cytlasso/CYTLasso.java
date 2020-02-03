package com.demeng7215.cytlasso;

import com.demeng7215.cytlasso.commands.CYTLassoCmd;
import com.demeng7215.cytlasso.listeners.GoldenLassoCraftEvent;
import com.demeng7215.cytlasso.listeners.GoldenLassoMobSpawnEvent;
import com.demeng7215.cytlasso.listeners.GoldenLassoUseEvent;
import com.demeng7215.cytlasso.utils.GoldenLassoItem;
import com.demeng7215.demlib.DemLib;
import com.demeng7215.demlib.api.BlacklistSystem;
import com.demeng7215.demlib.api.Common;
import com.demeng7215.demlib.api.Registerer;
import com.demeng7215.demlib.api.files.CustomConfig;
import com.demeng7215.demlib.api.messages.MessageUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class CYTLasso extends JavaPlugin {

	private static CYTLasso instance;

	public CustomConfig settingsFile;

	private static Economy econ = null;

	@Override
	public void onEnable() {

		instance = this;

		DemLib.setPlugin(this, "WQzUTKqTC5Zk7Ufm");
		MessageUtils.setPrefix("&8[&6CYTLasso&8] ");

		try {
			if (BlacklistSystem.checkBlacklist()) {
				MessageUtils.error(null, 0, "Plugin has been forcibly disabled.", true);
				return;
			}
		} catch (final IOException ex) {
			MessageUtils.error(ex, 2, "Failed to connect to auth server.", false);
		}

		getLogger().info("Loading configuration files...");

		try {
			this.settingsFile = new CustomConfig("settings.yml");
		} catch (final Exception ex) {
			MessageUtils.error(ex, 1, "Failed to load settings.yml.", true);
			return;
		}

		MessageUtils.setPrefix(getSettings().getString("prefix"));

		getLogger().info("Adding crafting recipes...");
		Bukkit.addRecipe(GoldenLassoItem.getRecipe());

		getLogger().info("Registering commands...");
		Registerer.registerCommand(new CYTLassoCmd(this));

		getLogger().info("Registering listeners...");
		Registerer.registerListeners(new GoldenLassoCraftEvent(this));
		Registerer.registerListeners(new GoldenLassoUseEvent(this));
		Registerer.registerListeners(new GoldenLassoMobSpawnEvent(this));

		getLogger().info("Hooking into Vault...");
		if (!setupEconomy()) {
			MessageUtils.error(null, 3, "Failed to hook into Vault.", true);
			return;
		}

		MessageUtils.console("&aCYTLasso v" + Common.getVersion() +
				" has been successfully enabled.");
	}

	@Override
	public void onDisable() {
		MessageUtils.console("&cCYTLasso v" + Common.getVersion() +
				" has been successfully disabled.");
	}

	public FileConfiguration getSettings() {
		return settingsFile.getConfig();
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) return false;

		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) return false;

		econ = rsp.getProvider();

		return econ != null;
	}

	public static Economy getEconomy() {
		return econ;
	}

	public static CYTLasso getInstance() {
		return instance;
	}
}
