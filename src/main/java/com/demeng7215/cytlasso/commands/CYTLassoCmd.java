package com.demeng7215.cytlasso.commands;

import com.demeng7215.cytlasso.CYTLasso;
import com.demeng7215.cytlasso.utils.GoldenLassoItem;
import com.demeng7215.demlib.api.Common;
import com.demeng7215.demlib.api.CustomCommand;
import com.demeng7215.demlib.api.messages.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class CYTLassoCmd extends CustomCommand {

	private CYTLasso i;

	public CYTLassoCmd(CYTLasso i) {
		super("cytlasso");

		this.i = i;

		setDescription("Main command for CYTLasso.");
		setAliases(Collections.singletonList("goldenlasso"));
	}

	@Override
	protected void run(CommandSender sender, String[] args) {

		if (args.length < 1 || !args[0].equalsIgnoreCase("give")) {
			MessageUtils.tellWithoutPrefix(sender, "&6Running CYTLasso v" + Common.getVersion() + ".");
			return;
		}

		if (!checkHasPerm("cytlasso.admin", sender, i.getSettings().getString("no-perms"))) return;

		if (Bukkit.getServer().getPlayerExact(args[1]) == null) {
			MessageUtils.tell(sender, i.getSettings().getString("player-offline"));
			return;
		}

		final Player target = Bukkit.getServer().getPlayerExact(args[1]);

		target.getInventory().addItem(GoldenLassoItem.getGoldenLasso());

		MessageUtils.tell(sender, i.getSettings().getString("successfully-given")
				.replace("%target%", target.getName()));
	}
}