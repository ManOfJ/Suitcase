package com.manofj.minecraft.moj_suitcase.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

import net.minecraftforge.common.capabilities.ICapabilityProvider

import com.manofj.commons.scala.alias.java.Collection.JavaList

import com.manofj.minecraft.moj_suitcase.Suitcase
import com.manofj.minecraft.moj_suitcase.entity.EntityItemSuitcase
import com.manofj.minecraft.moj_suitcase.init.SuitcaseItems
import com.manofj.minecraft.moj_suitcase.inventory.InventorySuitcaseProvider


object ItemSuitcase {

  def isSuitcaseItem( item: Item ): Boolean =
    item == SuitcaseItems.SUITCASE

  def isSuitcaseItem( stack: ItemStack ): Boolean =
    Option( stack ).map( _.getItem ) match {
      case Some( _: ItemSuitcase ) => true
      case _                       => false
    }

  def getHeldSuitcase( player: EntityPlayer ): Seq[ ItemStack ] = {
    import scala.collection.convert.WrapAsScala.iterableAsScalaIterable


    player.getHeldEquipment.toIndexedSeq.filter( isSuitcaseItem )
  }

  def getMaterial( stack: ItemStack ): SuitcaseMaterial =
    if ( stack.getItem eq null ) SuitcaseMaterial.DIAMOND else SuitcaseMaterial.byMetadata( stack.getMetadata )

}

class ItemSuitcase
  extends Item
{
  import com.manofj.minecraft.moj_suitcase.item.ItemSuitcase._


  {
    setMaxStackSize( 1 )
    setMaxDamage( 0 )
    setHasSubtypes( true )
    setUnlocalizedName( "[ THIS IS ILLEGAL ITEM ]" )
    setCreativeTab( CreativeTabs.MISC )
  }

  override def initCapabilities( stack: ItemStack, nbt: NBTTagCompound ): ICapabilityProvider =
    new InventorySuitcaseProvider( stack )

  override def hasCustomEntity( stack: ItemStack ): Boolean =
    isSuitcaseItem( stack )

  override def createEntity( world: World, location: Entity, stack: ItemStack ): Entity =
    new EntityItemSuitcase( world, location, stack )

  override def onItemRightClick( itemStackIn: ItemStack,
                                 worldIn:     World,
                                 playerIn:    EntityPlayer,
                                 hand:        EnumHand ): ActionResult[ ItemStack ] =
  {
    if ( !worldIn.isRemote ) {
      playerIn.openGui( Suitcase, 0, worldIn, 0, 0, 0 )
    }

    super.onItemRightClick( itemStackIn, worldIn, playerIn, hand )
  }

  override def getSubItems( itemIn: Item, tab: CreativeTabs, subItems: JavaList[ ItemStack ] ): Unit = {
    import scala.collection.convert.WrapAsScala.asScalaBuffer


    SuitcaseMaterial.ALL.foreach { material =>
      subItems.add( new ItemStack( itemIn, 1, material.metadata ) )
    }
  }

  override def getUnlocalizedName( stack: ItemStack ): String =
    "item." + Suitcase.languageKey( getMaterial( stack ).suitcaseId )

}
