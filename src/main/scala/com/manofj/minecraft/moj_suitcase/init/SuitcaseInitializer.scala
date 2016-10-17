package com.manofj.minecraft.moj_suitcase.init

import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry

import com.manofj.minecraft.moj_suitcase.Suitcase
import com.manofj.minecraft.moj_suitcase.SuitcaseGuiHandler


sealed trait SuitcaseInitializer {
  def preInit( event: FMLPreInitializationEvent ): Unit
  def init( event: FMLInitializationEvent ): Unit
}

class SuitcaseCommonInitializer
  extends SuitcaseInitializer
{
  override def preInit( event: FMLPreInitializationEvent ): Unit = {
    SuitcaseItems.preInit()
    NetworkRegistry.INSTANCE.registerGuiHandler( Suitcase, SuitcaseGuiHandler )
  }

  override def init( event: FMLInitializationEvent ): Unit = SuitcaseItems.init()
}

class SuitcaseClientInitializer
  extends SuitcaseCommonInitializer
{
  override def preInit( event: FMLPreInitializationEvent ): Unit = {
    super.preInit( event )
    SuitcaseItems.preInitClient()
  }
}
