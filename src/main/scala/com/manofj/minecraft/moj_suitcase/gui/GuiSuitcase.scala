package com.manofj.minecraft.moj_suitcase.gui

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.util.ResourceLocation

import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

import com.manofj.minecraft.moj_suitcase.inventory.ContainerSuitcase


@SideOnly( Side.CLIENT )
class GuiSuitcase( playerInventory: IInventory, suitcaseInventory: IInventory, player: EntityPlayer )
  extends GuiContainer( new ContainerSuitcase( playerInventory, suitcaseInventory, player ) )
{

  private[ this ] final val CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png")

  private[ this ] final val inventoryRows = suitcaseInventory.getSizeInventory / 9


  {
    allowUserInput = false
    ySize = 114 + inventoryRows * 18
  }


  override def drawGuiContainerBackgroundLayer( partialTicks: Float, mouseX: Int, mouseY: Int ): Unit = {
    GlStateManager.color( 1.0F, 1.0F, 1.0F, 1.0F )
    mc.getTextureManager.bindTexture( CHEST_GUI_TEXTURE )
    val i = ( width - xSize ) / 2
    val j = ( height - ySize ) / 2
    drawTexturedModalRect( i, j, 0, 0, xSize, inventoryRows * 18 + 17 )
    drawTexturedModalRect( i, j + inventoryRows * 18 + 17, 0, 126, xSize, 96 )
  }

  override def drawGuiContainerForegroundLayer( mouseX: Int, mouseY: Int ): Unit = {
    fontRendererObj.drawString( suitcaseInventory.getDisplayName.getUnformattedText, 8, 6, 4210752 )
    fontRendererObj.drawString( playerInventory.getDisplayName.getUnformattedText, 8, ySize - 96 + 2, 4210752 )
  }

}
