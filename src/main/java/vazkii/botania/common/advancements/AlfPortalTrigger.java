/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlfPortalTrigger implements ICriterionTrigger<AlfPortalTrigger.Instance> {
	public static final ResourceLocation ID = new ResourceLocation(LibMisc.MOD_ID, "open_elf_portal");
	public static final AlfPortalTrigger INSTANCE = new AlfPortalTrigger();
	private final Map<PlayerAdvancements, PlayerTracker> playerTrackers = new HashMap<>();

	private AlfPortalTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<AlfPortalTrigger.Instance> listener) {
		this.playerTrackers.computeIfAbsent(player, PlayerTracker::new).listeners.add(listener);
	}

	@Override
	public void removeListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<AlfPortalTrigger.Instance> listener) {
		PlayerTracker tracker = this.playerTrackers.get(player);

		if (tracker != null) {
			tracker.listeners.remove(listener);

			if (tracker.listeners.isEmpty()) {
				this.playerTrackers.remove(player);
			}
		}
	}

	@Override
	public void removeAllListeners(@Nonnull PlayerAdvancements player) {
		playerTrackers.remove(player);
	}

	@Nonnull
	@Override
	public Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
		return new Instance(ItemPredicate.deserialize(json.get("wand")), LocationPredicate.deserialize(json.get("location")));
	}

	static class PlayerTracker {
		private final PlayerAdvancements playerAdvancements;
		final Set<ICriterionTrigger.Listener<Instance>> listeners = new HashSet<>();

		PlayerTracker(PlayerAdvancements playerAdvancementsIn) {
			this.playerAdvancements = playerAdvancementsIn;
		}

		public void trigger(ServerWorld world, BlockPos pos, ItemStack wand) {
			List<ICriterionTrigger.Listener<Instance>> list = new ArrayList<>();

			for (ICriterionTrigger.Listener<AlfPortalTrigger.Instance> listener : this.listeners) {
				if (listener.getCriterionInstance().test(world, pos, wand)) {
					list.add(listener);
				}
			}

			for (ICriterionTrigger.Listener<AlfPortalTrigger.Instance> listener : list) {
				listener.grantCriterion(this.playerAdvancements);
			}
		}
	}

	public void trigger(ServerPlayerEntity player, ServerWorld world, BlockPos pos, ItemStack wand) {
		PlayerTracker tracker = playerTrackers.get(player.getAdvancements());
		if (tracker != null) {
			tracker.trigger(world, pos, wand);
		}
	}

	static class Instance implements ICriterionInstance {
		private final ItemPredicate wand;
		private final LocationPredicate pos;

		Instance(ItemPredicate predicate, LocationPredicate pos) {
			this.wand = predicate;
			this.pos = pos;
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return ID;
		}

		boolean test(ServerWorld world, BlockPos pos, ItemStack wand) {
			return this.wand.test(wand) && this.pos.test(world, pos.getX(), pos.getY(), pos.getZ());
		}
	}
}
