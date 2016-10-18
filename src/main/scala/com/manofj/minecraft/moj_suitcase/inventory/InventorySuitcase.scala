package com.manofj.minecraft.moj_suitcase.inventory

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.InventoryBasic
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.EnumFacing
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString

import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.Capability.IStorage
import net.minecraftforge.common.capabilities.ICapabilitySerializable

import com.manofj.commons.scala.util.conversions.Any$
import com.manofj.commons.scala.util.conversions.Boolean$

import com.manofj.commons.minecraft.inventory.IInventoryUtils

import com.manofj.minecraft.moj_suitcase.item.ItemSuitcase
import com.manofj.minecraft.moj_suitcase.item.SuitcaseMaterial


trait InventorySuitcase extends IInventory

class DummyInventorySuitcase extends InventoryBasic( "", true, 0 ) with InventorySuitcase

class InventorySuitcaseImpl( suitcase: ItemStack )
  extends InventorySuitcase
{
  private[ this ] val items = Array.fill[ ItemStack ]( SuitcaseMaterial.DIAMOND.capacity )( null )


  @deprecated( message = "IInventoryUtilsにアイテム挿入用関数を作ってそちらを使用する", since = "1.10.2-2" )
  def addItem( stack: ItemStack ): ItemStack = {
    val item = stack.copy

    ( 0 until getSizeInventory ).foreach { i =>
      getStackInSlot( i ) match {
        case null =>
          setInventorySlotContents( i, item )
          markDirty()

          return null

        case slot if ItemStack.areItemsEqual( item, slot ) =>
          val limit = getInventoryStackLimit min slot.getMaxStackSize
          val size = item.stackSize min ( limit - slot.stackSize )

          if ( size > 0 ) {
            slot.stackSize += size
            item.stackSize -= size
            if ( item.stackSize <= 0 ) {
              markDirty()

              return null
            }
          }

        case _ =>
    } }

    if ( item.stackSize != stack.stackSize ) {
      markDirty()
    }

    item
  }

  // 1.10.2-1のデータ引き継ぎ専用メソッド
  // 1.10.2-1を公開停止し､Minecraft 1.11ではこのメソッドを削除する
  @deprecated( message = "Minecraft 1.11で削除する", since = "1.10.2-2" )
  def loadOldVersionItems(): Unit = {
    val tag = suitcase.getTagCompound
    if ( tag.nonNull ) {
      tag.hasKey( "SuitcaseInventory", 10 ) >> {
        IInventoryUtils.itemsFromNBTTagCompound( this, tag.getCompoundTag( "SuitcaseInventory" ) )
        tag.removeTag( "SuitcaseInventory" )
    } }
  }


  override def getInventoryStackLimit: Int = 64
  override def getSizeInventory: Int = ItemSuitcase.getMaterial( suitcase ).capacity

  override def getDisplayName: ITextComponent = new TextComponentString( getName )
  override def getName: String = suitcase.getDisplayName
  override def hasCustomName: Boolean = true


  override def clear(): Unit =
    ( 0 until getSizeInventory ).foreach( items.update( _, null ) )

  override def decrStackSize( index: Int, count: Int ): ItemStack =
    ItemStackHelper.getAndSplit( items, index, count ) << { x => if ( x.nonNull ) markDirty() }

  override def getStackInSlot( index: Int ): ItemStack =
    ( 0 until getSizeInventory ).contains( index ).map( items( index ) ).orNull

  override def removeStackFromSlot( index: Int ): ItemStack =
    getStackInSlot( index ) << { _ >> { _ => items.update( index, null ) } }

  override def setInventorySlotContents( index: Int, stack: ItemStack ): Unit =
    ( 0 until getSizeInventory ).contains( index ).map {
      items.update( index, stack )
      stack >> { x => if ( x.stackSize > getInventoryStackLimit ) x.stackSize = getInventoryStackLimit }
      markDirty()
    }


  override def isItemValidForSlot( index: Int, stack: ItemStack ): Boolean = !ItemSuitcase.isSuitcaseItem( stack )
  override def isUseableByPlayer( player: EntityPlayer ): Boolean = true
  override def openInventory( player: EntityPlayer ): Unit = {}
  override def closeInventory( player: EntityPlayer ): Unit = {}
  override def markDirty(): Unit = {}

  override def getField( id: Int ): Int = 0
  override def getFieldCount: Int = 0
  override def setField( id: Int, value: Int ): Unit = {}

}

object InventorySuitcaseStorage
  extends IStorage[ InventorySuitcase ]
{

  override def readNBT( capability: Capability[ InventorySuitcase ],
                        instance: InventorySuitcase,
                        side: EnumFacing,
                        nbt: NBTBase ): Unit =
    nbt match {
      case list: NBTTagList => IInventoryUtils.itemsFromNBTTagList( instance, list )
      case _ =>
    }

  override def writeNBT( capability: Capability[ InventorySuitcase ],
                         instance: InventorySuitcase,
                         side: EnumFacing ): NBTBase =
    IInventoryUtils.itemsToNBTTagList( instance )

}

class InventorySuitcaseProvider( stack: ItemStack )
  extends ICapabilitySerializable[ NBTTagList ]
{
  import com.manofj.minecraft.moj_suitcase.Suitcase.{ capability => CAPABILITY }


  private[ this ] val inventory = new InventorySuitcaseImpl( stack )


  override def deserializeNBT( nbt: NBTTagList ): Unit =
    CAPABILITY.getStorage.readNBT( CAPABILITY, inventory, null, nbt )

  override def serializeNBT(): NBTTagList =
    CAPABILITY.getStorage.writeNBT( CAPABILITY, inventory, null ).asInstanceOf[ NBTTagList ]

  override def getCapability[ T ]( capability: Capability[ T ], facing: EnumFacing ): T =
    if ( hasCapability( capability, facing ) ) CAPABILITY.cast[ T ]( inventory ) else null.asInstanceOf[ T ]

  override def hasCapability( capability: Capability[ _ ], facing: EnumFacing ): Boolean =
    capability == CAPABILITY

}