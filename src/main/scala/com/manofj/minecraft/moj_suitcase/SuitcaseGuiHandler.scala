package com.manofj.minecraft.moj_suitcase

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

import net.minecraftforge.fml.common.network.IGuiHandler

import com.manofj.minecraft.moj_suitcase.gui.GuiSuitcase
import com.manofj.minecraft.moj_suitcase.inventory.ContainerSuitcase
import com.manofj.minecraft.moj_suitcase.inventory.InventorySuitcaseImpl
import com.manofj.minecraft.moj_suitcase.item.ItemSuitcase


object SuitcaseGuiHandler
  extends IGuiHandler
{

  private[ this ] def suitcaseInventory( player: EntityPlayer ) =
    ItemSuitcase.getHeldSuitcase( player )
      .headOption
      .flatMap( x => Option( x.getCapability( Suitcase.capability, null ) ) )
      //TODO: 旧データ引き継ぎ処理 - Minecraft 1.11で削除
      .map { case x: InventorySuitcaseImpl => x.loadOldVersionItems(); x }


  override def getClientGuiElement( ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int ): AnyRef = {
    ID match {
      case 0 => suitcaseInventory( player ).map( new GuiSuitcase( player.inventory, _, player ) ).orNull
    }
  }

  override def getServerGuiElement( ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int ): AnyRef = {
    ID match {
      case 0 => suitcaseInventory( player ).map( new ContainerSuitcase( player.inventory, _, player ) ).orNull
    }
  }

}
