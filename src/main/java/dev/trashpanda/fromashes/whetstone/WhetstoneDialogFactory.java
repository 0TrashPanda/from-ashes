package dev.trashpanda.fromashes.whetstone;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.dialog.ActionButton;
import net.minecraft.server.dialog.CommonButtonData;
import net.minecraft.server.dialog.CommonDialogData;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.dialog.DialogAction;
import net.minecraft.server.dialog.MultiActionDialog;
import net.minecraft.server.dialog.action.StaticAction;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.ItemBody;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class WhetstoneDialogFactory {
    private static final int BUTTON_WIDTH = 220;

    private WhetstoneDialogFactory() {
    }

    public static Holder<Dialog> createShapeSelectionDialog(
            final int x,
            final int y,
            final int z,
            final ResourceKey<Level> dimension,
            final ItemStack sourceStack,
            final List<WhetstoneRecipeSelection> selections
    ) {
        Component sourceName = sourceStack.getHoverName();
        int optionCount = selections.size();
        Component prompt = Component.translatable(
                optionCount == 1
                        ? "dialog.from-ashes.whetstone.prompt.single"
                        : "dialog.from-ashes.whetstone.prompt.multiple",
                optionCount,
                sourceName
        );
        List<DialogBody> body = new ArrayList<>();
        body.add(new PlainMessage(prompt, 260));

        List<ActionButton> actions = new ArrayList<>();
        for (WhetstoneRecipeSelection selection : selections) {
            body.add(new ItemBody(selection.recipe().resultTemplate(), Optional.of(new PlainMessage(selection.recipe().description(), 220)), true, true, 16, 16));
            CompoundTag payload = new CompoundTag();
            payload.putInt("x", x);
            payload.putInt("y", y);
            payload.putInt("z", z);
            payload.putString("dimension", dimension.identifier().toString());
            payload.putString("recipe", selection.holder().id().identifier().toString());
            actions.add(
                    new ActionButton(
                            new CommonButtonData(selection.recipe().displayName(), Optional.of(selection.recipe().description()), BUTTON_WIDTH),
                            Optional.of(
                                    new StaticAction(
                                            new ClickEvent.Custom(
                                                    WhetstoneDialogActionHandler.ACTION_ID,
                                                    Optional.of(payload)
                                            )
                                    )
                            )
                    )
            );
        }

        Dialog dialog = new MultiActionDialog(
                new CommonDialogData(
                        Component.translatable("dialog.from-ashes.whetstone.title", sourceName),
                        Optional.empty(),
                        true,
                        false,
                        DialogAction.CLOSE,
                        body,
                        List.of()
                ),
                actions,
                Optional.empty(),
                1
        );
        return Holder.direct(dialog);
    }
}
