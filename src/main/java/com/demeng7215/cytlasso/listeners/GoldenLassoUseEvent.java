package com.demeng7215.cytlasso.listeners;

import com.demeng7215.cytlasso.CYTLasso;
import com.demeng7215.cytlasso.utils.GoldenLassoItem;
import com.demeng7215.cytlasso.utils.XMaterial;
import com.demeng7215.demlib.api.messages.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GoldenLassoUseEvent implements Listener {

	private CYTLasso i;

	public GoldenLassoUseEvent(CYTLasso i) {
		this.i = i;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onGoldenLassoUse(PlayerInteractEntityEvent e) {

		if (!e.getPlayer().getInventory().getItemInMainHand().isSimilar(GoldenLassoItem.getGoldenLasso())) return;

		if (!e.getEventName().equals("PlayerInteractEntityEvent")) return;

		if (e.getHand() == EquipmentSlot.OFF_HAND) return;

		if (!(e.getRightClicked() instanceof LivingEntity)) return;

		if (e.getRightClicked().getType() == EntityType.WITHER ||
				e.getRightClicked().getType() == EntityType.ENDER_DRAGON) {
			return;
		}

		e.setCancelled(true);

		if (!e.getPlayer().hasPermission("cytlasso.use")) {
			MessageUtils.tell(e.getPlayer(), i.getSettings().getString("no-perms"));
			return;
		}

		if (!CYTLasso.getEconomy().has(e.getPlayer(), i.getSettings().getDouble("cost"))) {
			MessageUtils.tell(e.getPlayer(), i.getSettings().getString("insufficient-balance"));
			return;
		}

		ItemStack spawnEgg = XMaterial.valueOf(e.getRightClicked().getType().name() + "_SPAWN_EGG").parseItem();
		ItemMeta spawnEggMeta = spawnEgg.getItemMeta();

		List<String> dataLore = new ArrayList<>();
		String name = null;
		String owner = null;
		String leash = null;

		if (e.getRightClicked().getCustomName() != null) name = e.getRightClicked().getCustomName();

		spawnEggMeta.setDisplayName(MessageUtils.colorize(i.getSettings().getString("egg-name")
				.replace("%entity%", e.getRightClicked().getType().name())));

		if (e.getRightClicked() instanceof Tameable) {

			Tameable tameable = (Tameable) e.getRightClicked();
			if (tameable.isTamed()) {
				owner = tameable.getOwner().getUniqueId().toString();

				if (tameable.getType() == EntityType.WOLF) {
					Wolf wolf = (Wolf) tameable;
					leash = wolf.getCollarColor().name();
				}
			}
		}

		if (name != null) dataLore.add(parseData("Name", name));
		if (owner != null) dataLore.add(parseData("Owner", Bukkit.getServer().getOfflinePlayer(
				UUID.fromString(owner)).getName()));
		if (leash != null) dataLore.add(parseData("Leash", leash));

		spawnEggMeta.setLore(dataLore);

		spawnEgg.setItemMeta(spawnEggMeta);

		CYTLasso.getEconomy().withdrawPlayer(e.getPlayer(), i.getSettings().getDouble("cost"));

		Bukkit.getScheduler().scheduleSyncDelayedTask(i, () -> {

			if (!e.getPlayer().getInventory().getItemInMainHand().isSimilar(GoldenLassoItem.getGoldenLasso())) {
				e.getPlayer().sendTitle(MessageUtils.colorize(i.getSettings().getString("exploit-detected-title")),
						MessageUtils.colorize(i.getSettings().getString("exploit-detected-subtitle")),
						20, 60, 20);
				;
				return;
			}

			e.getPlayer().getInventory().removeItem(GoldenLassoItem.getGoldenLasso());
			e.getRightClicked().remove();

			e.getPlayer().getInventory().addItem(spawnEgg);
		}, 5L);
	}

	private String parseData(String key, String data) {
		return MessageUtils.colorize(i.getSettings().getString("key-color") + key + ": "
				+ i.getSettings().getString("value-color") + data);
	}
}
