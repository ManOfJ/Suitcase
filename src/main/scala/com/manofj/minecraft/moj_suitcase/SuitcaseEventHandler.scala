package com.manofj.minecraft.moj_suitcase

import net.minecraft.item.ItemStack

import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

import com.manofj.commons.scala.util.conversions.Any$

import com.manofj.commons.minecraft.inventory.IInventoryUtils
import com.manofj.commons.minecraft.item.conversions.ItemStack$

import com.manofj.minecraft.moj_suitcase.inventory.InventorySuitcaseImpl
import com.manofj.minecraft.moj_suitcase.inventory.InventorySuitcaseProvider
import com.manofj.minecraft.moj_suitcase.item.ItemSuitcase


@Mod.EventBusSubscriber
object SuitcaseEventHandler {

  @SubscribeEvent
  def attachCapability( event: AttachCapabilitiesEvent.Item ): Unit = {
    val item  = event.getObject
    val stack = event.getItemStack

    if ( ItemSuitcase.isSuitcaseItem( item ) &&
         !stack.hasCapability( Suitcase.capability, null ) )
    {
      event.addCapability( Suitcase.resourceLocation( "SuitcaseInventory" ), new InventorySuitcaseProvider( stack ) )
    }
  }

  // ドロップアイテムを取得した際､装備中のスーツケース内に
  // 同一のアイテムが存在すればスーツケースインベントリに収納する
  @SubscribeEvent
  def itemPickup( event: EntityItemPickupEvent ): Unit = {
    val player     = event.getEntityPlayer
    val itemEntity = event.getItem
    val pickupItem = itemEntity.getEntityItem


    def store( inv: InventorySuitcaseImpl ) =
      if ( IInventoryUtils.toIterator( inv )
        .exists( pickupItem.isItemAndTagsEqualIgnoreDurability ) )
      {
        val result = inv.addItem( pickupItem )

        if ( result.nonNull ) {
          pickupItem.stackSize = result.stackSize
        }
        else {
          player.onItemPickup( itemEntity, 0 )
          itemEntity.setDead()
        }

        event.setCanceled( itemEntity.isDead )
      }

    val suitcases = IndexedSeq.newBuilder[ ItemStack ]

    suitcases ++= ItemSuitcase.getHeldSuitcase( player )
    if ( SuitcaseConfig.searchFromAll ) {
      suitcases ++= wrapRefArray( player.inventory.mainInventory ).filter( ItemSuitcase.isSuitcaseItem )
    }

    suitcases.result.distinct
      .withFilter( _ => !event.isCanceled )
      .foreach { _
        .getCapability( Suitcase.capability, null ) match {
          case inventory: InventorySuitcaseImpl =>
            //TODO: 旧データ引き継ぎ処理 - Minecraft 1.11で削除
            inventory.loadOldVersionItems()
            store( inventory )
          case _ =>
      } }

  }

}
