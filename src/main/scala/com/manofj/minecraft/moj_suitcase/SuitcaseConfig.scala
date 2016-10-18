package com.manofj.minecraft.moj_suitcase

import net.minecraft.client.gui.GuiScreen

import net.minecraftforge.common.config.Property
import net.minecraftforge.fml.common.Mod

import com.manofj.commons.scala.util.conversions.Any$

import com.manofj.commons.minecraftforge.config.ForgeConfig
import com.manofj.commons.minecraftforge.config.SimpleConfig
import com.manofj.commons.minecraftforge.config.gui.ConfigGui
import com.manofj.commons.minecraftforge.config.gui.SimpleGuiFactory
import com.manofj.commons.minecraftforge.config.gui.SimpleGuiParams


@Mod.EventBusSubscriber
object SuitcaseConfig
  extends SimpleConfig
{
  private[ this ] final val SEARCH_FROM_ALL = "search_from_all"


  override protected final val modId: String = Suitcase.modId


  private[ this ] var searchFromAllOpt = Option.empty[ Boolean ]
  def searchFromAll: Boolean = searchFromAllOpt.getOrElse( false )


  override def sync(): Unit = {
    def languageKey( k: String, s: String = "" ) = Suitcase.languageKey( k, "config", s )
    def comment( k: String ) = Suitcase.message( languageKey( k, "tooltip" ) )

    val cfg  = theConfig
    var prop = null: Property

    prop = cfg.get( configId, SEARCH_FROM_ALL, false )
    prop.setComment( comment( SEARCH_FROM_ALL ) )
    prop.setLanguageKey( languageKey( SEARCH_FROM_ALL ) )
    searchFromAllOpt = prop.getBoolean.?

  }
}

object SuitcaseConfigGuiParams
  extends SimpleGuiParams
{
  override protected def config: ForgeConfig = SuitcaseConfig.theConfig
  override final val modId: String = Suitcase.modId
  override def title: String = Suitcase.format( "gui.config.title" )
}

class SuitcaseConfigGui( parent: GuiScreen ) extends ConfigGui( parent, SuitcaseConfigGuiParams )
class SuitcaseConfigGuiFactory extends SimpleGuiFactory[ SuitcaseConfigGui ]
