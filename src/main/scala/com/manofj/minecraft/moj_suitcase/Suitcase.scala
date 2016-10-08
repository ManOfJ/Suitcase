package com.manofj.minecraft.moj_suitcase

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry

import com.manofj.commons.minecraftforge.base.MinecraftForgeMod
import com.manofj.commons.minecraftforge.i18n.I18nSupportMod
import com.manofj.commons.minecraftforge.resource.ResourceLocationMakerMod

import com.manofj.minecraft.moj_suitcase.init.SuitcaseItems


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


  @Mod.EventHandler
  def preInit( event: FMLPreInitializationEvent ): Unit = {
    SuitcaseItems.init()
    NetworkRegistry.INSTANCE.registerGuiHandler( this, SuitcaseGuiHandler )
  }

}
