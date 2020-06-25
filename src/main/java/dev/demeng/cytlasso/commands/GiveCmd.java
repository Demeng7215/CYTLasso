package dev.demeng.cytlasso.commands;

import dev.demeng.cytlasso.CYTLasso;
import dev.demeng.cytlasso.utils.GoldenLassoItem;
import dev.demeng.demlib.api.commands.CommandSettings;
import dev.demeng.demlib.api.commands.types.SubCommand;
import dev.demeng.demlib.api.messages.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class GiveCmd implements SubCommand {

  private final CYTLasso i;

  public GiveCmd(CYTLasso i) {
    this.i = i;
  }

  @Override
  public String getBaseCommand() {
    return "cytlasso";
  }

  @Override
  public CommandSettings getSettings() {
    return i.getCommandSettings();
  }

  @Override
  public String getName() {
    return "give";
  }

  @Override
  public List<String> getAliases() {
    return Collections.emptyList();
  }

  @Override
  public boolean isPlayerCommand() {
    return false;
  }

  @Override
  public String getPermission() {
    return "cytlasso.admin";
  }

  @Override
  public String getUsage() {
    return "<player>";
  }

  @Override
  public int getArgs() {
    return 1;
  }

  @Override
  public void execute(CommandSender sender, String[] args) {

    final Player target = Bukkit.getServer().getPlayerExact(args[1]);

    if (target == null) {
      MessageUtils.tell(sender, i.getSettings().getString("player-offline"));
      return;
    }

    target.getInventory().addItem(GoldenLassoItem.getGoldenLasso());

    MessageUtils.tell(
        sender,
        i.getSettings().getString("successfully-given").replace("%target%", target.getName()));
  }
}
