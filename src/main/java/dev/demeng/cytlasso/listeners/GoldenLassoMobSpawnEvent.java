package dev.demeng.cytlasso.listeners;

import dev.demeng.cytlasso.CYTLasso;
import dev.demeng.demlib.api.messages.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GoldenLassoMobSpawnEvent implements Listener {

  private final CYTLasso i;

  public GoldenLassoMobSpawnEvent(CYTLasso i) {
    this.i = i;
  }

  @EventHandler
  public void onGoldenLassoMobSpawnEvent(PlayerInteractEvent e) {

    if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
    if (e.getHand() == EquipmentSlot.OFF_HAND) return;

    if (!e.getPlayer().getInventory().getItemInMainHand().getType().name().endsWith("_SPAWN_EGG"))
      return;

    ItemStack hand = e.getPlayer().getInventory().getItemInMainHand();
    ItemMeta meta = hand.getItemMeta();

    if (!meta.hasDisplayName()) return;

    if (!meta.getDisplayName()
        .contains(
            MessageUtils.stripColors(
                i.getSettings().getString("egg-name").replace("%entity%", "")))) return;

    e.setCancelled(true);

    String name = null;
    String owner = null;
    String leash = null;

    if (hasName(hand)) name = meta.getLore().get(0).split(": ")[1];
    if (hasOwner(hand)) owner = MessageUtils.stripColors(meta.getLore().get(1)).split(": ")[1];
    if (hasLeash(hand)) leash = MessageUtils.stripColors(meta.getLore().get(2)).split(": ")[1];

    Location loc =
        new Location(
            e.getClickedBlock().getWorld(),
            e.getClickedBlock().getX(),
            e.getClickedBlock().getY() + 1,
            e.getClickedBlock().getZ());

    Entity i =
        e.getPlayer()
            .getWorld()
            .spawnEntity(loc, EntityType.valueOf(hand.getType().name().replace("_SPAWN_EGG", "")));

    if (name != null) i.setCustomName(name);
    else i.setCustomNameVisible(false);

    if (i instanceof Tameable) {

      Tameable tameable = (Tameable) i;
      if (owner != null) tameable.setOwner(Bukkit.getServer().getPlayer(owner));

      if (tameable.getType() == EntityType.WOLF) {
        Wolf wolf = (Wolf) tameable;
        if (leash != null) wolf.setCollarColor(DyeColor.valueOf(leash));
      }
    }

    ItemStack toRemove = new ItemStack(hand);
    toRemove.setAmount(1);

    e.getPlayer().getInventory().removeItem(toRemove);
  }

  private boolean hasName(ItemStack item) {

    ItemMeta meta = item.getItemMeta();

    if (!meta.hasLore()) return false;

    String s;

    try {
      s = meta.getLore().get(0).split(": ")[1];
    } catch (NullPointerException | IndexOutOfBoundsException e) {
      return false;
    }

    return s != null;
  }

  private boolean hasOwner(ItemStack item) {

    ItemMeta meta = item.getItemMeta();

    if (!meta.hasLore()) return false;

    String s;

    try {
      s = meta.getLore().get(1).split(": ")[1];
    } catch (NullPointerException | IndexOutOfBoundsException e) {
      return false;
    }

    return s != null;
  }

  private boolean hasLeash(ItemStack item) {

    ItemMeta meta = item.getItemMeta();

    if (!meta.hasLore()) return false;

    String s;

    try {
      s = meta.getLore().get(2).split(": ")[1];
    } catch (NullPointerException | IndexOutOfBoundsException e) {
      return false;
    }

    return s != null;
  }
}
