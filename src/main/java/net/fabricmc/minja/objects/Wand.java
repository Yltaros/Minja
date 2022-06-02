package net.fabricmc.minja.objects;

import net.fabricmc.minja.events.PlayerEvent;
import net.fabricmc.minja.exceptions.NotEnoughtManaException;
import net.fabricmc.minja.PlayerMinja;
import net.fabricmc.minja.spells.LightningBall;
import net.fabricmc.minja.spells.Spell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class Wand extends Item {
	private static Wand WAND;

	public Wand(Settings settings) {
		super(settings);
		WAND = this;
	}

	public static Item getWand(){
		return WAND;
	}

	@Override
	//Used when the player use right click with the Wand
	public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {

		// Get the current player
		PlayerMinja player = (PlayerMinja) playerEntity;

		try {
			// Remove the current spell's mana cost to the mana of the player
			((PlayerMinja) playerEntity).removeMana(player.getActiveSpell().getManaCost());

			// Cast the spell associated
			Spell spell = player.getActiveSpell();
			spell.cast(playerEntity);

		} catch (NotEnoughtManaException e) {
			// throw new RuntimeException(e);
			// TODO : A UPGRADE
		}
		return TypedActionResult.success(playerEntity.getStackInHand(hand));
	}

	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		((PlayerEntity)user).sendMessage(new LiteralText("Event de fin !!"), false);
		return stack;
	}

	/*@Override
	//Click gauche
	public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {

		if (clickType == ClickType.LEFT) {
			player.playSound(SoundEvents.ENTITY_COW_AMBIENT, 1.0F, 1.0F);

			((PlayerMinja) player).addMana(20);
			try {
				((PlayerMinja) player).removeMana(10);
			} catch (NotEnoughtManaException e) {
				System.out.println(e.getMessage());
			}
			return true;
		} else {
			return false;
		}
	}*/
}
