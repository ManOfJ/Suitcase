package com.manofj.minecraft.moj_suitcase

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

import net.minecraftforge.fml.common.network.IGuiHandler

import com.manofj.minecraft.moj_suitcase.gui.GuiSuitcase
import com.manofj.minecraft.moj_suitcase.inventory.ContainerSuitcase
import com.manofj.minecraft.moj_suitcase.inventory.InventorySuitcase
import com.manofj.minecraft.moj_suitcase.item.ItemSuitcase


object SuitcaseGuiHandler
  extends IGuiHandler
{

  private[ this ] def suitcaseInventory( player: EntityPlayer ): InventorySuitcase =
    ItemSuitcase.getHeldSuitcase( player ).flatMap( ItemSuitcase.createSuitcaseInventory ).head

  override def getClientGuiElement( ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int ): AnyRef = {
    ID match {
      case 0 => new GuiSuitcase( player.inventory, suitcaseInventory( player ), player )
    }
  }

  override def getServerGuiElement( ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int ): AnyRef = {
    ID match {
      case 0 => new ContainerSuitcase( player.inventory, suitcaseInventory( player ), player )
    }
  }

}
