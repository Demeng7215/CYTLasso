package com.demeng7215.cytlasso.listeners;

import com.demeng7215.cytlasso.CYTLasso;
import com.demeng7215.cytlasso.utils.GoldenLassoItem;
import com.demeng7215.cytlasso.utils.XEnchantment;
import com.demeng7215.demlib.api.messages.MessageUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GoldenLassoCraftEvent implements Listener {

	private CYTLasso i;

	public GoldenLassoCraftEvent(CYTLasso i) {
		this.i = i;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onGoldenLassoCraft(CraftItemEvent e) {

		Inventory inv = e.getInventory();
		ItemStack[] items = inv.getContents();
		Player p = (Player) e.getWhoClicked();

		if (!e.getRecipe().getResult().equals(GoldenLassoItem.getGoldenLasso())) {
			return;
		}

		for (int x = 1; x < items.length; x++) {

			if (items[x] != null && items[x].getType() != Material.AIR) {

				ItemMeta meta = items[x].getItemMeta();

				if (i.getSettings().getString("recipe." + x + ".name")
						.equalsIgnoreCase("%default%") && meta.hasDisplayName()) {
					craftFailed(p);
					e.setCancelled(true);
					return;
				}

				if (!i.getSettings().getString("recipe." + x + ".name")
						.equalsIgnoreCase("%default%")) {
					if (!MessageUtils.colorize(i.getSettings().getString("recipe." + x + ".name"))
							.equals(meta.getDisplayName())) {
						craftFailed(p);
						e.setCancelled(true);
						return;
					}
				}

				if (i.getSettings().getStringList("recipe." + x + ".lore")
						.get(0).equalsIgnoreCase("%default%") && meta.hasLore()) {
					craftFailed(p);
					e.setCancelled(true);
					return;
				}

				if (!i.getSettings().getStringList("recipe." + x + ".lore")
						.get(0).equalsIgnoreCase("%default%")) {

					List<String> lore = new ArrayList<>();

					for (String s : i.getSettings().getStringList("recipe." + x + ".lore"))
						lore.add(MessageUtils.colorize(s));

					if (meta.getLore().equals(lore)) {
						craftFailed(p);
						e.setCancelled(true);
						return;
					}
				}

				if (i.getSettings().getStringList("recipe." + x + ".enchants").get(0)
						.equalsIgnoreCase("%default%") && meta.hasEnchants()) {
					craftFailed(p);
					e.setCancelled(true);
					return;
				}

				if (!i.getSettings().getStringList("recipe." + x + ".enchants").get(0)
						.equalsIgnoreCase("%default%")) {

					List<Enchantment> enchants = new ArrayList<>();

					for (String s : i.getSettings().getStringList("recipe." + x + ".enchants"))
						enchants.add(XEnchantment.valueOf(s).parseEnchantment());

					if (!enchants.containsAll(meta.getEnchants().keySet())) {
						craftFailed(p);
						e.setCancelled(true);
						return;
					}
				}
			}
		}
	}

	private void craftFailed(Player p) {
		p.closeInventory();
		p.sendTitle(MessageUtils.colorize(i.getSettings().getString("failed-title")),
				MessageUtils.colorize(i.getSettings().getString("failed-subtitle")),
				20, 60, 20);
	}
}
