package com.manofj.minecraft.moj_suitcase.init

import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.init.Items
import net.minecraft.item.ItemStack

import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.GameRegistry

import com.manofj.minecraft.moj_suitcase.Suitcase
import com.manofj.minecraft.moj_suitcase.item.ItemSuitcase
import com.manofj.minecraft.moj_suitcase.item.SuitcaseMaterial


object SuitcaseItems {
  import scala.collection.convert.WrapAsScala.asScalaBuffer


  final val SUITCASE = new ItemSuitcase


  private[ init ] def preInit(): Unit = {
    GameRegistry.register( SUITCASE, Suitcase.resourceLocation( "suitcase" ) )
  }

  private[ init ] def preInitClient(): Unit = {
    SuitcaseMaterial.ALL.foreach { material =>
      val name = Suitcase.resourceLocation( material.suitcaseId )
      val location = new ModelResourceLocation( name, "inventory" )

      ModelLoader.setCustomModelResourceLocation( SUITCASE, material.metadata, location )
    }
  }

  private[ init ] def init(): Unit = {

    SuitcaseMaterial.ALL.foreach { material =>
      GameRegistry.addRecipe( new ItemStack( SUITCASE, 1, material.metadata ), Seq(
        "LLL",
        "MMM",
        "MMM",
        'L': Character, Items.LEATHER,
        'M': Character, material.item
      ): _* )
    }

  }

}
