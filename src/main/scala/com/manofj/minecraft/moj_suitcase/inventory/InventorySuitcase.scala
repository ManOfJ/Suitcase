package com.manofj.minecraft.moj_suitcase.inventory

import com.google.common.cache.CacheBuilder

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.InventoryBasic
import net.minecraft.item.ItemStack

import com.manofj.commons.minecraft.inventory.IInventoryUtils

import com.manofj.minecraft.moj_suitcase.item.SuitcaseMaterial


object InventorySuitcase {

  final val active = CacheBuilder.newBuilder.weakKeys.build[ EntityPlayer, InventorySuitcase ]

}

class InventorySuitcase( suitcase: ItemStack, material: SuitcaseMaterial )
  extends InventoryBasic( "", false, material.capacity )
{

  def readInventoryItems(): Unit = {
    val tag = suitcase.getSubCompound( "SuitcaseInventory", true )
    IInventoryUtils.itemsFromNBTTagCompound( this, tag )
  }

  def writeInventoryItems(): Unit = {
    val tag = IInventoryUtils.itemsToNBTTagCompound( this )
    suitcase.setTagInfo( "SuitcaseInventory", tag )
  }


  override def getName: String = suitcase.getDisplayName

  override def openInventory( player: EntityPlayer ): Unit = {
    InventorySuitcase.active.put( player, this )
    readInventoryItems()
  }

  override def closeInventory( player: EntityPlayer ): Unit = {
    writeInventoryItems()
    InventorySuitcase.active.invalidate( player )
  }

}
