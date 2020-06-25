package dev.demeng.cytlasso.commands;

import dev.demeng.cytlasso.CYTLasso;
import dev.demeng.demlib.api.Common;
import dev.demeng.demlib.api.commands.CommandSettings;
import dev.demeng.demlib.api.commands.types.BaseCommand;
import dev.demeng.demlib.api.messages.MessageUtils;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class CYTLassoCmd implements BaseCommand {

  private final CYTLasso i;

  public CYTLassoCmd(CYTLasso i) {
    this.i = i;
  }

  @Override
  public CommandSettings getSettings() {
    return i.getCommandSettings();
  }

  @Override
  public String getName() {
    return "cytlasso";
  }

  @Override
  public List<String> getAliases() {
    return Collections.singletonList("goldenlasso");
  }

  @Override
  public boolean isPlayerCommand() {
    return false;
  }

  @Override
  public String getPermission() {
    return null;
  }

  @Override
  public String getUsage() {
    return "";
  }

  @Override
  public int getArgs() {
    return 0;
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    MessageUtils.tellWithoutPrefix(sender, "&6Running CYTLasso v" + Common.getVersion() + ".");
  }
}
