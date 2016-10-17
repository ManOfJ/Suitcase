package com.manofj.minecraft.moj_suitcase

import scala.annotation.meta.setter

import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.CapabilityInject
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

import com.manofj.commons.minecraftforge.base.MinecraftForgeMod
import com.manofj.commons.minecraftforge.i18n.I18nSupportMod
import com.manofj.commons.minecraftforge.resource.ResourceLocationMakerMod

import com.manofj.minecraft.moj_suitcase.init.SuitcaseInitializer
import com.manofj.minecraft.moj_suitcase.inventory.InventorySuitcase


@Mod( modid       = Suitcase.modId,
      name        = Suitcase.modName,
      version     = Suitcase.modVersion,
//      guiFactory  = Suitcase.modGuiFactory,
      modLanguage = Suitcase.modLanguage )
object Suitcase
  extends MinecraftForgeMod
  with    ResourceLocationMakerMod
  with    I18nSupportMod
{

  override final val modId      = "moj_suitcase"
  override final val modName    = "Suitcase"
  override final val modVersion = "@version@"

//  final val modGuiFactory: String = ""

  @SidedProxy( modId      = Suitcase.modId,
               serverSide = "com.manofj.minecraft.moj_suitcase.init.SuitcaseCommonInitializer",
               clientSide = "com.manofj.minecraft.moj_suitcase.init.SuitcaseClientInitializer" )
  var initializer: SuitcaseInitializer = null


  @( CapabilityInject @ setter )( classOf[ InventorySuitcase ] )
  var capability: Capability[ InventorySuitcase ] = null


  @Mod.EventHandler def preInit( event: FMLPreInitializationEvent ): Unit = initializer.preInit( event )
  @Mod.EventHandler def init( event: FMLInitializationEvent ): Unit = initializer.init( event )

}
