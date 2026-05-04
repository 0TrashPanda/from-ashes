package dev.trashpanda.fromashes.worldgen.rock;

import dev.trashpanda.fromashes.block.ModBlocks;
import dev.trashpanda.fromashes.block.Rock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class RockScatterFeature extends Feature<RockScatterFeatureConfig> {
    public RockScatterFeature() {
        super(RockScatterFeatureConfig.CODEC);
    }

    @Override
    public boolean place(final FeaturePlaceContext<RockScatterFeatureConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        RockScatterFeatureConfig config = context.config();

        BlockPos center = surfacePos(level, context.origin().getX(), context.origin().getZ());
        if (center == null) {
            return false;
        }

        int spread = randomRange(random, config.minSpread(), config.maxSpread());
        int targetCount = randomRange(random, config.minRocks(), config.maxRocks());
        int attempts = Math.max(targetCount * 4, targetCount + 6);
        int placed = 0;

        for (int i = 0; i < attempts && placed < targetCount; i++) {
            int offsetX = randomOffset(random, spread);
            int offsetZ = randomOffset(random, spread);

            if (offsetX == 0 && offsetZ == 0 && i > 0 && random.nextBoolean()) {
                continue;
            }

            BlockPos candidate = surfacePos(level, center.getX() + offsetX, center.getZ() + offsetZ);
            if (candidate == null || !isValidPlacement(level, candidate, config)) {
                continue;
            }

            if (placed > 0 && random.nextFloat() > config.extraPlacementChance()) {
                continue;
            }

            BlockState rockState = ModBlocks.ROCK.defaultBlockState()
                    .setValue(Rock.FACING, Direction.from2DDataValue(random.nextInt(4)));
            level.setBlock(candidate, rockState, Block.UPDATE_CLIENTS);
            placed++;
        }

        return placed > 0;
    }

    private static BlockPos surfacePos(final LevelAccessor level, final int x, final int z) {
        int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        BlockPos pos = new BlockPos(x, y, z);
        return pos.getY() > level.getMinY() ? pos : null;
    }

    private static boolean isValidPlacement(final WorldGenLevel level, final BlockPos pos, final RockScatterFeatureConfig config) {
        BlockPos belowPos = pos.below();
        BlockState state = level.getBlockState(pos);
        BlockState below = level.getBlockState(belowPos);

        if (!state.canBeReplaced() || !level.getFluidState(pos).isEmpty()) {
            return false;
        }

        if (!below.isFaceSturdy(level, belowPos, Direction.UP) || level.getFluidState(belowPos).is(FluidTags.WATER)) {
            return false;
        }

        if (!isNaturalSurface(below.getBlock(), config.profile())) {
            return false;
        }

        int slope = slopeAt(level, belowPos);
        if (slope < config.minSlope()) {
            return false;
        }

        if (config.profile() == RockPlacementProfile.RIVERBANK) {
            int waterRise = nearestWaterRise(level, pos, config.maxWaterDistance());
            return waterRise >= config.minWaterRise() && waterRise <= config.maxWaterRise();
        }

        if (config.profile() == RockPlacementProfile.FOREST && below.is(Blocks.GRASS_BLOCK) && slope < config.minSlope() + 1) {
            return false;
        }

        return true;
    }

    private static boolean isNaturalSurface(final Block block, final RockPlacementProfile profile) {
        return switch (profile) {
            case RIVERBANK -> block == Blocks.DIRT
                    || block == Blocks.GRASS_BLOCK
                    || block == Blocks.SAND
                    || block == Blocks.GRAVEL
                    || block == Blocks.STONE
                    || block == Blocks.COARSE_DIRT
                    || block == Blocks.CLAY;
            case ROUGH -> block == Blocks.STONE
                    || block == Blocks.GRAVEL
                    || block == Blocks.COARSE_DIRT
                    || block == Blocks.PODZOL
                    || block == Blocks.ANDESITE
                    || block == Blocks.DIORITE
                    || block == Blocks.GRASS_BLOCK;
            case FOREST -> block == Blocks.PODZOL
                    || block == Blocks.COARSE_DIRT
                    || block == Blocks.STONE
                    || block == Blocks.MOSS_BLOCK
                    || block == Blocks.GRASS_BLOCK
                    || block == Blocks.ROOTED_DIRT;
            case GLOBAL -> block == Blocks.STONE
                    || block == Blocks.GRAVEL
                    || block == Blocks.COARSE_DIRT
                    || block == Blocks.DIRT
                    || block == Blocks.GRASS_BLOCK
                    || block == Blocks.SAND;
        };
    }

    private static int slopeAt(final LevelAccessor level, final BlockPos pos) {
        int centerY = surfaceY(level, pos.getX(), pos.getZ());
        int north = surfaceY(level, pos.getX(), pos.getZ() - 1);
        int south = surfaceY(level, pos.getX(), pos.getZ() + 1);
        int east = surfaceY(level, pos.getX() + 1, pos.getZ());
        int west = surfaceY(level, pos.getX() - 1, pos.getZ());

        int min = Math.min(Math.min(centerY, north), Math.min(Math.min(south, east), west));
        int max = Math.max(Math.max(centerY, north), Math.max(Math.max(south, east), west));
        return max - min;
    }

    private static int surfaceY(final LevelAccessor level, final int x, final int z) {
        return level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z) - 1;
    }

    private static int nearestWaterRise(final WorldGenLevel level, final BlockPos pos, final int maxDistance) {
        int bestDistance = Integer.MAX_VALUE;
        int bestRise = Integer.MAX_VALUE;

        for (int dx = -maxDistance; dx <= maxDistance; dx++) {
            for (int dz = -maxDistance; dz <= maxDistance; dz++) {
                int horizontalDistance = Math.abs(dx) + Math.abs(dz);
                if (horizontalDistance == 0 || horizontalDistance > maxDistance) {
                    continue;
                }

                int waterY = exposedWaterY(level, pos.getX() + dx, pos.getZ() + dz, pos.getY());
                if (waterY == Integer.MIN_VALUE) {
                    continue;
                }

                int rise = pos.getY() - waterY;
                if (horizontalDistance < bestDistance || (horizontalDistance == bestDistance && rise < bestRise)) {
                    bestDistance = horizontalDistance;
                    bestRise = rise;
                }
            }
        }

        return bestRise;
    }

    private static int exposedWaterY(final WorldGenLevel level, final int x, final int z, final int aroundY) {
        int minY = Math.max(level.getMinY(), aroundY - 3);
        int maxY = Math.min(level.getMaxY(), aroundY + 1);

        for (int y = maxY; y >= minY; y--) {
            BlockPos scanPos = new BlockPos(x, y, z);
            if (!level.getFluidState(scanPos).is(FluidTags.WATER)) {
                continue;
            }

            if (level.getFluidState(scanPos.above()).isEmpty()) {
                return y;
            }
        }

        return Integer.MIN_VALUE;
    }

    private static int randomOffset(final RandomSource random, final int spread) {
        return random.nextInt(spread + 1) - random.nextInt(spread + 1);
    }

    private static int randomRange(final RandomSource random, final int min, final int max) {
        return min >= max ? min : min + random.nextInt(max - min + 1);
    }
}
