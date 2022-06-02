package net.fabricmc.minja.events;

import net.fabricmc.minja.clocks.Clock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Date;

public abstract class MinjaItems extends Item {

    private long lastEvent;
    private boolean firstTime = true;

    private final int TIMER = 200;

    public MinjaItems(Settings settings) {
        super(settings);
    }

    @Override
    //Used when the player use right click with the Wand
    final public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

        TypedActionResult state = null;

        //Mettre ici l'ouverture de l'HUD
        lastEvent = new Date().getTime();

        if(firstTime) {
            firstTime = false;
            state = onRightClickPressed(world, playerEntity, hand);
            Clock clock = new Clock(TIMER) {

                @Override
                public void execute() {
                    if(new Date().getTime() - lastEvent > TIMER-10) {
                        onRightClickReleased(world, playerEntity, hand);
                        firstTime = true;
                    } else {
                        this.run();
                    }
                }
            };
            clock.start();
        }

        else {
            state = onRightClickMaintained(world, playerEntity, hand);
        }

        if(state == null) state = TypedActionResult.success(playerEntity.getStackInHand(hand));

        return state;
    }

    public abstract TypedActionResult<ItemStack> onRightClickPressed(World world, PlayerEntity playerEntity, Hand hand);

    public abstract TypedActionResult<ItemStack> onRightClickReleased(World world, PlayerEntity playerEntity, Hand hand);

    public abstract TypedActionResult<ItemStack> onRightClickMaintained(World world, PlayerEntity playerEntity, Hand hand);

}
