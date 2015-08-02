/** Copyright (c) 2011-2015, SpaceToad and the BuildCraft Team http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL. Please check the contents
 * of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt */
package buildcraft.silicon;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import buildcraft.core.BCCreativeTab;
import buildcraft.core.lib.block.BlockBuildCraft;
import buildcraft.core.lib.render.ICustomHighlight;
import buildcraft.silicon.SiliconProxy;
import buildcraft.silicon.tile.TileLaser;

public class BlockLaser extends BlockBuildCraft implements ICustomHighlight {

    private static final AxisAlignedBB[][] boxes = { { new AxisAlignedBB(0.0, 0.75, 0.0, 1.0, 1.0, 1.0), new AxisAlignedBB(0.3125, 0.1875, 0.3125,
            0.6875, 0.75, 0.6875) },   // -Y
        { new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.25, 1.0), new AxisAlignedBB(0.3125, 0.25, 0.3125, 0.6875, 0.8125, 0.6875) },   // +Y
        { new AxisAlignedBB(0.0, 0.0, 0.75, 1.0, 1.0, 1.0), new AxisAlignedBB(0.3125, 0.3125, 0.1875, 0.6875, 0.6875, 0.75) },   // -Z
        { new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.25), new AxisAlignedBB(0.3125, 0.3125, 0.25, 0.6875, 0.6875, 0.8125) },   // +Z
        { new AxisAlignedBB(0.75, 0.0, 0.0, 1.0, 1.0, 1.0), new AxisAlignedBB(0.1875, 0.3125, 0.3125, 0.75, 0.6875, 0.6875) },   // -X
        { new AxisAlignedBB(0.0, 0.0, 0.0, 0.25, 1.0, 1.0), new AxisAlignedBB(0.25, 0.3125, 0.3125, 0.8125, 0.6875, 0.6875) } // +X
    };

    public BlockLaser() {
        super(Material.iron);
        setHardness(10F);
        setCreativeTab(BCCreativeTab.get("main"));
    }

    @Override
    public AxisAlignedBB[] getBoxes(World wrd, BlockPos pos, EntityPlayer player) {
        return boxes[wrd.getBlockMetadata(pos)];
    }

    @Override
    public double getExpansion() {
        return 0.0075;
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World wrd, BlockPos pos, Vec3 origin, Vec3 direction) {
        AxisAlignedBB[] aabbs = boxes[wrd.getBlockMetadata(pos)];
        MovingObjectPosition closest = null;
        for (AxisAlignedBB aabb : aabbs) {
            MovingObjectPosition mop = aabb.getOffsetBoundingBox(pos).calculateIntercept(origin, direction);
            if (mop != null) {
                if (closest != null && mop.hitVec.distanceTo(origin) < closest.hitVec.distanceTo(origin)) {
                    closest = mop;
                } else {
                    closest = mop;
                }
            }
        }
        if (closest != null) {
            closest.blockX = x;
            closest.blockY = y;
            closest.blockZ = z;
        }
        return closest;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addCollisionBoxesToList(World wrd, BlockPos pos, AxisAlignedBB mask, List list, Entity ent) {
        AxisAlignedBB[] aabbs = boxes[wrd.getBlockMetadata(pos)];
        for (AxisAlignedBB aabb : aabbs) {
            AxisAlignedBB aabbTmp = aabb.getOffsetBoundingBox(pos);
            if (mask.intersectsWith(aabbTmp)) {
                list.add(aabbTmp);
            }
        }
    }

    @Override
    public int getRenderType() {
        return SiliconProxy.laserBlockModel;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileLaser();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(IBlockAccess access, BlockPos pos, int side) {
        return getIcon(side, access.getBlockMetadata(pos));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(int i, int j) {
        if (i == (j ^ 1)) {
            return icons[0][0];
        } else if (i == j) {
            return icons[0][1];
        } else {
            return icons[0][2];
        }
    }

    @Override
    public int onBlockPlaced(World world, BlockPos pos, int side, float par6, float par7, float par8, int meta) {
        super.onBlockPlaced(world, pos, side, par6, par7, par8, meta);

        int retMeta = meta;

        if (side <= 6) {
            retMeta = side;
        }

        return retMeta;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, int side) {
        return true;
    }
}