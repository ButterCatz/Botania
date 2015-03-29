/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 29, 2015, 10:13:32 PM (GMT)]
 */
package vazkii.botania.common.item.relic;

import net.minecraft.item.ItemStack;
import baubles.api.BaubleType;
import vazkii.botania.common.lib.LibItemNames;

public class ItemLokiRing extends ItemRelicBauble {

	public ItemLokiRing() {
		super(LibItemNames.LOKI_RING);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.RING;
	}

}
