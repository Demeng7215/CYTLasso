package com.demeng7215.cytlasso.utils;

import com.demeng7215.cytlasso.CYTLasso;
import com.demeng7215.demlib.api.messages.MessageUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class GoldenLassoItem {

	public static Recipe getRecipe() {

		final NamespacedKey namespacedKey = new NamespacedKey(CYTLasso.getInstance(), "golden_lasso");

		ShapedRecipe recipe = new ShapedRecipe(namespacedKey, getGoldenLasso());

		final String[] matrix = CYTLasso.getInstance().getSettings()
				.getString("recipe.matrix.form").split(";");

		if (matrix.length != 3 || matrix[0].length() != 3 || matrix[1].length() != 3 || matrix[2].length() != 3) {
			MessageUtils.console("&4ERROR: &cAll 3 rows in the crafting recipe matrix must have " +
					"exactly 3 characters.");
			CYTLasso.getInstance().getPluginLoader().disablePlugin(CYTLasso.getInstance());
			return null;
		}

		recipe.shape(matrix[0], matrix[1], matrix[2]);

		for (String key : CYTLasso.getInstance().getSettings()
				.getConfigurationSection("recipe.matrix.definitions").getKeys(false)) {

			if (key.length() > 1) {
				MessageUtils.console("&4ERROR: &cAll definitions (keys) must be exactly one letter.");
				CYTLasso.getInstance().getPluginLoader().disablePlugin(CYTLasso.getInstance());
				return null;
			}

			recipe.setIngredient(key.charAt(0), XMaterial.valueOf(CYTLasso.getInstance().getSettings()
					.getString("recipe.matrix.definitions." + key)).parseMaterial());
		}

		return recipe;
	}

	public static ItemStack getGoldenLasso() {

		ItemStack stack = new ItemStack(XMaterial.LEAD.parseMaterial());
		ItemMeta meta = stack.getItemMeta();

		List<String> lore = new ArrayList<>();
		for (String s : CYTLasso.getInstance().getSettings().getStringList("item.lore")) {
			lore.add(MessageUtils.colorize(s));
		}

		meta.setDisplayName(MessageUtils.colorize(
				CYTLasso.getInstance().getSettings().getString("item.display-name")));
		meta.setLore(lore);
		stack.setItemMeta(meta);

		return stack;
	}
}
