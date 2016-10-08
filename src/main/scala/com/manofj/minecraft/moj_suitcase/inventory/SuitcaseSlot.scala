package com.manofj.minecraft.moj_suitcase.inventory

import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack

import com.manofj.minecraft.moj_suitcase.item.ItemSuitcase


class SuitcaseSlot( base: Slot )
  extends Slot( base.inventory, base.getSlotIndex, base.xDisplayPosition, base.yDisplayPosition )
{

  {
    slotNumber = base.slotNumber
  }

  override def isItemValid( stack: ItemStack ): Boolean = !ItemSuitcase.isSuitcaseItem( stack )

}
