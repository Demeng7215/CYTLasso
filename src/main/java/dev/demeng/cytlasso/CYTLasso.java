package dev.demeng.cytlasso;

import dev.demeng.cytlasso.commands.CYTLassoCmd;
import dev.demeng.cytlasso.commands.GiveCmd;
import dev.demeng.cytlasso.listeners.GoldenLassoCraftEvent;
import dev.demeng.cytlasso.listeners.GoldenLassoMobSpawnEvent;
import dev.demeng.cytlasso.listeners.GoldenLassoUseEvent;
import dev.demeng.cytlasso.utils.GoldenLassoItem;
import dev.demeng.demlib.DemLib;
import dev.demeng.demlib.api.Common;
import dev.demeng.demlib.api.Registerer;
import dev.demeng.demlib.api.commands.CommandSettings;
import dev.demeng.demlib.api.files.CustomConfig;
import dev.demeng.demlib.api.messages.MessageUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class CYTLasso extends JavaPlugin {

  private static CYTLasso instance;

  public CustomConfig settingsFile;

  private Economy econ = null;

  private CommandSettings commandSettings;

  @Override
  public void onEnable() {

    instance = this;

    DemLib.setPlugin(this);
    MessageUtils.setPrefix("&8[&6CYTLasso&8] ");

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

    this.commandSettings = new CommandSettings();
    commandSettings.setIncorrectUsageMessage("");
    commandSettings.setNoPermissionMessage(getSettings().getString("no-perms"));
    commandSettings.setNotPlayerMessage("");

    Registerer.registerCommand(new CYTLassoCmd(this));
    Registerer.registerCommand(new GiveCmd(this));

    getLogger().info("Registering listeners...");
    Registerer.registerListener(new GoldenLassoCraftEvent(this));
    Registerer.registerListener(new GoldenLassoUseEvent(this));
    Registerer.registerListener(new GoldenLassoMobSpawnEvent(this));

    getLogger().info("Hooking into Vault...");
    if (!setupEconomy()) {
      MessageUtils.error(null, 3, "Failed to hook into Vault.", true);
      return;
    }

    MessageUtils.console("&aCYTLasso v" + Common.getVersion() + " has been successfully enabled.");
  }

  @Override
  public void onDisable() {
    MessageUtils.console("&cCYTLasso v" + Common.getVersion() + " has been successfully disabled.");
  }

  public FileConfiguration getSettings() {
    return settingsFile.getConfig();
  }

  private boolean setupEconomy() {
    if (getServer().getPluginManager().getPlugin("Vault") == null) return false;

    RegisteredServiceProvider<Economy> rsp =
        getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null) return false;

    econ = rsp.getProvider();

    return econ != null;
  }

  public Economy getEconomy() {
    return econ;
  }

  public CommandSettings getCommandSettings() {
    return commandSettings;
  }

  public static CYTLasso getInstance() {
    return instance;
  }
}
