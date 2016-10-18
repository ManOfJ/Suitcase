package com.manofj.minecraft.moj_suitcase.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.ContainerChest
import net.minecraft.inventory.IInventory


class ContainerSuitcase( playerInventory: IInventory, suitcaseInventory: IInventory, player: EntityPlayer )
  extends ContainerChest( playerInventory, suitcaseInventory, player )
{

  {
    import scala.collection.convert.WrapAsJava.seqAsJavaList
    import scala.collection.convert.WrapAsScala.iterableAsScalaIterable


    val newInventorySlots = inventorySlots.toIndexedSeq.map {
      case slot if slot.inventory == suitcaseInventory => new SuitcaseSlot( slot )
      case slot => slot
    }

    inventorySlots.clear()
    inventorySlots.addAll( newInventorySlots )
  }

}
