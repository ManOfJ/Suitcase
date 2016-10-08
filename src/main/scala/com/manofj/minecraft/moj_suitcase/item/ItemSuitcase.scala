package com.manofj.minecraft.moj_suitcase.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World

import com.manofj.commons.scala.alias.java.Collection.JavaList

import com.manofj.minecraft.moj_suitcase.Suitcase
import com.manofj.minecraft.moj_suitcase.entity.EntityItemSuitcase
import com.manofj.minecraft.moj_suitcase.inventory.InventorySuitcase
import com.manofj.commons.scala.util.conversions.Boolean$

object ItemSuitcase {

  def isSuitcaseItem( stack: ItemStack ): Boolean =
    Option( stack ).map( _.getItem ) match {
      case Some( _: ItemSuitcase ) => true
      case _ => false
    }

  def createSuitcaseInventory( stack: ItemStack ): Option[ InventorySuitcase ] =
    isSuitcaseItem( stack ) ? Some( new InventorySuitcase( stack, getMaterial( stack ) ) ) ! None

  def getHeldSuitcase( player: EntityPlayer ): Seq[ ItemStack ] = {
    import scala.collection.convert.WrapAsScala.iterableAsScalaIterable

    player.getHeldEquipment.toIndexedSeq.filter( isSuitcaseItem )
  }

  def getMaterial( stack: ItemStack ): SuitcaseMaterial = SuitcaseMaterial.byMetadata( stack.getMetadata )

}

class ItemSuitcase
  extends Item
{

  {
    setMaxStackSize( 1 )
    setHasSubtypes( true )
    setMaxDamage( 0 )
    setUnlocalizedName( "[ THIS IS ILLEGAL ITEM ]" )
    setCreativeTab( CreativeTabs.MISC )
  }


  override def hasCustomEntity( stack: ItemStack ): Boolean = true

  override def createEntity( world: World, location: Entity, stack: ItemStack ): Entity =
    new EntityItemSuitcase( world, location, this, stack )

  override def onItemRightClick( itemStackIn: ItemStack,
                                 worldIn:     World,
                                 playerIn:    EntityPlayer,
                                 hand:        EnumHand ): ActionResult[ ItemStack ] =
  {
    if ( !worldIn.isRemote ) playerIn.openGui( Suitcase, 0, worldIn, 0, 0, 0 )
    super.onItemRightClick( itemStackIn, worldIn, playerIn, hand )
  }

  override def getSubItems( itemIn: Item, tab: CreativeTabs, subItems: JavaList[ ItemStack ] ): Unit = {
    import scala.collection.convert.WrapAsScala.asScalaBuffer

    SuitcaseMaterial.ALL.foreach { material =>
      subItems.add( new ItemStack( itemIn, 1, material.metadata ) )
    }
  }

  override def getUnlocalizedName( stack: ItemStack ): String =
    "item." + Suitcase.languageKey( ItemSuitcase.getMaterial( stack ).suitcaseId )

}
