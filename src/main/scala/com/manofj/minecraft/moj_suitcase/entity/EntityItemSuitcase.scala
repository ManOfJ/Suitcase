package com.manofj.minecraft.moj_suitcase.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItem
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.world.World

import com.manofj.commons.scala.util.conversions.Any$

import com.manofj.minecraft.moj_suitcase.Suitcase


class EntityItemSuitcase( world: World, location: Entity, stack: ItemStack )
  extends EntityItem( world )
{

  {
    this.readFromNBT( location.serializeNBT )
  }


  override def attackEntityFrom( source: DamageSource, amount: Float ): Boolean =
    super.attackEntityFrom( source, amount ) << { _ =>
      // ダメージを受けて消滅する際にインベントリ内のアイテムを周囲にぶちまける
      if ( !worldObj.isRemote && !isEntityAlive ) {
        getEntityItem.getCapability( Suitcase.capability, null ) >> { inventory =>
          InventoryHelper.dropInventoryItems( world, this, inventory )
      } }
    }

}
