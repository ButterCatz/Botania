/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [25/11/2015, 19:46:11 (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import vazkii.botania.client.gui.box.ContainerBaubleBox;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBaubleBox extends ItemMod {

	public ItemBaubleBox(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
		return new InvProvider();
	}

	private static class InvProvider implements ICapabilitySerializable<INBT> {

		private final IItemHandler inv = new ItemStackHandler(24) {
			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack toInsert, boolean simulate) {
				/* todo 1.13
				if(!toInsert.isEmpty()) {
					boolean isBauble = toInsert.getCapability(BaublesCapabilities.CAPABILITY_ITEM_BAUBLE).isPresent();
					if(toInsert.getItem() instanceof IManaItem || isBauble)
						return super.insertItem(slot, toInsert, simulate);
				}
				*/
				return toInsert;
			}
		};
		private final LazyOptional<IItemHandler> opt = LazyOptional.of(() -> inv);

		@Nonnull
		@Override
		public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(capability, opt);
		}

		@Override
		public INBT serializeNBT() {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inv, null);
		}

		@Override
		public void deserializeNBT(INBT nbt) {
			CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inv, null, nbt);
		}
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		if(!world.isRemote) {
			NetworkHooks.openGui((ServerPlayerEntity) player, new ContainerProvider(player.getHeldItem(hand)), b -> {
				b.writeBoolean(hand == Hand.MAIN_HAND);
			});
		}
		return ActionResult.newResult(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	private static class ContainerProvider implements INamedContainerProvider {
		private final ItemStack stack;

		private ContainerProvider(ItemStack stack) {
			this.stack = stack;
		}

		@Nonnull
		@Override
		public ITextComponent getDisplayName() {
			return stack.getDisplayName();
		}

		@Nullable
		@Override
		public Container createMenu(int windowId, PlayerInventory playerInv, PlayerEntity player) {
			return new ContainerBaubleBox(windowId, playerInv, stack);
		}
	}

}
