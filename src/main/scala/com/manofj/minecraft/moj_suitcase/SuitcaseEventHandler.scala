package com.manofj.minecraft.moj_suitcase

import net.minecraftforge.event.entity.player.EntityItemPickupEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

import com.manofj.commons.scala.util.conversions.Any$

import com.manofj.commons.minecraft.inventory.IInventoryUtils
import com.manofj.commons.minecraft.item.conversions.ItemStack$

import com.manofj.minecraft.moj_suitcase.inventory.InventorySuitcase
import com.manofj.minecraft.moj_suitcase.item.ItemSuitcase


@Mod.EventBusSubscriber
object SuitcaseEventHandler {

  // ドロップアイテムを取得した際､装備中のスーツケース内に
  // 同一のアイテムが存在すればスーツケースインベントリに収納する
  @SubscribeEvent
  def itemPickup( event: EntityItemPickupEvent ): Unit = {
    val player     = event.getEntityPlayer
    val itemEntity = event.getItem
    val pickupItem = itemEntity.getEntityItem

    def store( inv: InventorySuitcase ) =
      if ( IInventoryUtils.toIterator( inv ).exists( pickupItem.isItemAndTagsEqualIgnoreDurability ) ) {
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

    ItemSuitcase.getHeldSuitcase( player )
      .takeWhile( _ => !event.isCanceled )
      .foreach { suitcase =>

        Option( InventorySuitcase.active.getIfPresent( player ) ) match {
          case Some( inventory ) => store( inventory )
          case None =>
            InventorySuitcase.create( suitcase ).foreach { inventory =>
              inventory.readInventoryItems()
              store( inventory )
              inventory.writeInventoryItems()
        } }

      }
  }

}
