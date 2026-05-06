package dev.trashpanda.fromashes.block.entity.custom;

import dev.trashpanda.fromashes.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class WhetStoneBlockEntity extends BlockEntity {
    private String selectedRecipeId = "";

    public WhetStoneBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.WHET_STONE_BLOCK_ENTITY, pos, state);
    }

    public String getSelectedRecipeId() {
        return this.selectedRecipeId;
    }

    public void setSelectedRecipeId(final String selectedRecipeId) {
        this.selectedRecipeId = selectedRecipeId;
        this.setChanged();
    }

    @Override
    protected void saveAdditional(final ValueOutput output) {
        super.saveAdditional(output);
        output.putString("SelectedRecipeId", this.selectedRecipeId);
    }

    @Override
    protected void loadAdditional(final ValueInput input) {
        super.loadAdditional(input);
        this.selectedRecipeId = input.getStringOr("SelectedRecipeId", "");
    }
}
