/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 17, 2015, 4:08:58 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.common.block.tile.TileLightRelay;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class BlockLightLauncher extends BlockMod {

	public BlockLightLauncher() {
		super(Material.wood);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
		setBlockName(LibBlockNames.LIGHT_LAUNCHER);
		setBlockBounds(0F, 0F, 0F, 1F, 0.25F, 1F);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		boolean power = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
		int meta = world.getBlockMetadata(x, y, z);
		boolean powered = (meta & 8) != 0;

		if(power && !powered) {
			pickUpEntities(world, x, y, z);
			world.setBlockMetadataWithNotify(x, y, z, meta | 8, 4);
		} else if(!power && powered)
			world.setBlockMetadataWithNotify(x, y, z, meta & -9, 4);
	}
	
	public void pickUpEntities(World world, int x, int y, int z) {
		List<TileLightRelay> relays = new ArrayList();
		for(ForgeDirection dir : LibMisc.CARDINAL_DIRECTIONS) {
			TileEntity tile = world.getTileEntity(x + dir.offsetX, y, z + dir.offsetZ);
			if(tile instanceof TileLightRelay) {
				TileLightRelay relay = (TileLightRelay) tile;
				if(relay.getBinding() != null)
					relays.add(relay);
			}
		}
		
		if(!relays.isEmpty()) {
			List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1));
			if(!entities.isEmpty()) {
				for(EntityLivingBase entity : entities) {
					TileLightRelay relay = relays.get(world.rand.nextInt(relays.size()));
					relay.mountEntity(entity);
				}
			}
		}
	}
	
}
