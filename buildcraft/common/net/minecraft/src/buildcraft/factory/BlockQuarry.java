package net.minecraft.src.buildcraft.factory;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.ModLoader;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.mod_BuildCraftFactory;
import net.minecraft.src.buildcraft.core.BluePrint;
import net.minecraft.src.buildcraft.core.Orientations;
import net.minecraft.src.buildcraft.core.Position;
import net.minecraft.src.buildcraft.core.Utils;

public class BlockQuarry extends BlockMachineRoot {
	
	public static final BluePrint bluePrint;
	
	public static final int MINING_FIELD_SIZE = 9; 
	
	int textureTop;
	int textureFront;
	int textureSide;
	
	public BlockQuarry(int i) {
		super(i, Material.iron);
		
		setHardness(1.5F);
		setResistance(10F);
		setStepSound(soundStoneFootstep);
		
		textureSide = ModLoader.addOverride("/terrain.png",
		"/net/minecraft/src/buildcraft/factory/gui/quarry_side.png");
		textureFront = ModLoader.addOverride("/terrain.png",
		"/net/minecraft/src/buildcraft/factory/gui/quarry_front.png");
		textureTop = ModLoader.addOverride("/terrain.png",
		"/net/minecraft/src/buildcraft/factory/gui/quarry_top.png");	
		
	}
    
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving)
    {
    	super.onBlockPlacedBy(world, i, j, k, entityliving);
    	
		Orientations orientation = Utils.get2dOrientation(new Position(entityliving.posX,
				entityliving.posY, entityliving.posZ), new Position(i, j, k));    	
    	
    	world.setBlockMetadataWithNotify(i, j, k, orientation.reverse().ordinal());    	  	
    	
		Position p = new Position (i, j, k, orientation);
				
		p.moveForwards(1);
		p.moveLeft((MINING_FIELD_SIZE - 1) / 2 + 1);
		
		double xMin = Integer.MAX_VALUE, zMin = Integer.MAX_VALUE;

		for (int s = 0; s < MINING_FIELD_SIZE + 1; ++s) {
			p.moveRight(1);

			if (p.i < xMin) {
				xMin = p.i;
			}

			if (p.k < zMin) {
				zMin = p.k;
			}
		}

		for (int s = 0; s < MINING_FIELD_SIZE + 1; ++s) {
			p.moveForwards(1);

			if (p.i < xMin) {
				xMin = p.i;
			}

			if (p.k < zMin) {
				zMin = p.k;
			}
		}

		for (int s = 0; s < MINING_FIELD_SIZE + 1; ++s) {
			p.moveLeft(1);

			if (p.i < xMin) {
				xMin = p.i;
			}

			if (p.k < zMin) {
				zMin = p.k;
			}
		}

		for (int s = 0; s < MINING_FIELD_SIZE + 1; ++s) {
			p.moveBackwards(1);

			if (p.i < xMin) {
				xMin = p.i;
			}

			if (p.k < zMin) {
				zMin = p.k;
			}
		}
		
		((TileQuarry) world.getBlockTileEntity(i, j, k)).setMinPos((int) xMin,
				(int) zMin);
    }
    
    public void onNeighborBlockChange(World world, int i, int j, int k, int l) {    	    	    	
    	TileQuarry tile = (TileQuarry) world.getBlockTileEntity(i, j, k);
    	    	
		tile.checkPower();    	        
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
    	TileQuarry tile = (TileQuarry) world.getBlockTileEntity(i, j, k);
    	
    	if (tile == null) {
    		tile = new TileQuarry();
    		world.setBlockTileEntity(i, j, k, tile);
    	}
    	
		tile.work();
    	
        return false;
    }

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		// If no metadata is set, then this is an icon.
		if (j == 0 && i == 3) {
			return textureFront;
		}
		
		if (i == j) {
			return textureFront;
		}

		switch (i) {
		case 1:
			return textureTop;
		default:
			return textureSide;
		}
	}
    
	@Override
	protected TileEntity getBlockEntity() {		
		return new TileQuarry();
	}
	

	public void onBlockRemoval(World world, int i, int j, int k) {
		((TileQuarry) world.getBlockTileEntity(i, j, k)).delete();
		
		super.onBlockRemoval(world, i, j, k);
	}
	
	static {
		bluePrint = new BluePrint (MINING_FIELD_SIZE + 2, 5, MINING_FIELD_SIZE + 2);
		
		for (int i = 0; i < MINING_FIELD_SIZE + 2; ++i) {
			for (int j = 0; j < 5; ++j) {
				for (int k = 0; k < MINING_FIELD_SIZE + 2; ++k) {
					bluePrint.setBlockId(i, j, k, 0);
				}
			}
		}
		
		for (int j = 0; j < 5; j += 4) {
			for (int i = 0; i < MINING_FIELD_SIZE + 2; ++i) {
				bluePrint.setBlockId(i, j, 0,
						mod_BuildCraftFactory.frameBlock.blockID);
				bluePrint.setBlockId(i, j, MINING_FIELD_SIZE + 1,
						mod_BuildCraftFactory.frameBlock.blockID);
			}
			
			for (int k = 0; k < MINING_FIELD_SIZE + 2; ++k) {
				bluePrint.setBlockId(0, j, k,
						mod_BuildCraftFactory.frameBlock.blockID);
				bluePrint.setBlockId(MINING_FIELD_SIZE + 1, j, k,
						mod_BuildCraftFactory.frameBlock.blockID);

			}
		}
		
		for (int h = 1; h < 4; ++h) {
			bluePrint.setBlockId(0, h, 0,
					mod_BuildCraftFactory.frameBlock.blockID);
			bluePrint.setBlockId(0, h, MINING_FIELD_SIZE + 1,
					mod_BuildCraftFactory.frameBlock.blockID);
			bluePrint.setBlockId(MINING_FIELD_SIZE + 1, h, 0,
					mod_BuildCraftFactory.frameBlock.blockID);
			bluePrint.setBlockId(MINING_FIELD_SIZE + 1, h, MINING_FIELD_SIZE + 1,
					mod_BuildCraftFactory.frameBlock.blockID);
		}
	}		
}
