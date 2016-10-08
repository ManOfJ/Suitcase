package com.manofj.minecraft.moj_suitcase.init

import scala.{ Array => * }

import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side


@Mod.EventBusSubscriber( *( Side.CLIENT ) )
object SuitcaseModelRegistry {

  @SubscribeEvent
  def registerModels( event: ModelRegistryEvent ): Unit = {
    SuitcaseItems.registerModels()
  }

}
