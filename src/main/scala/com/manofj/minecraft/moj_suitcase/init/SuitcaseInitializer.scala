package com.manofj.minecraft.moj_suitcase.init

import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry

import com.manofj.minecraft.moj_suitcase.Suitcase
import com.manofj.minecraft.moj_suitcase.SuitcaseGuiHandler
import com.manofj.minecraft.moj_suitcase.inventory.DummyInventorySuitcase
import com.manofj.minecraft.moj_suitcase.inventory.InventorySuitcase
import com.manofj.minecraft.moj_suitcase.inventory.InventorySuitcaseStorage


sealed trait SuitcaseInitializer {
  def preInit( event: FMLPreInitializationEvent ): Unit
  def init( event: FMLInitializationEvent ): Unit
}

class SuitcaseCommonInitializer
  extends SuitcaseInitializer
{
  override def preInit( event: FMLPreInitializationEvent ): Unit = {
    SuitcaseItems.preInit()

    CapabilityManager.INSTANCE.register(
      classOf[ InventorySuitcase ],
      InventorySuitcaseStorage,
      classOf[ DummyInventorySuitcase ] )

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
