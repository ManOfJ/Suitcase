package com.manofj.minecraft.moj_suitcase.item;

import java.util.List;
import java.util.NoSuchElementException;

import com.google.common.collect.ImmutableList;
import net.minecraft.init.Items;
import net.minecraft.item.Item;


public enum SuitcaseMaterial {
    IRON( 0, "iron", 9, Items.IRON_INGOT ),
    GOLD( 1, "gold", 18, Items.GOLD_INGOT ),
    DIAMOND( 2, "diamond", 27, Items.DIAMOND );

    public static final List< SuitcaseMaterial > ALL = ImmutableList.of(
            IRON, GOLD, DIAMOND
    );

    public final int metadata;
    public final String name;
    public final int capacity;
    public final Item item;

    SuitcaseMaterial( int metadata, String name, int capacity, Item item ) {
        this.metadata = metadata;
        this.item = item;
        this.capacity = capacity;
        this.name = name;
    }

    public static SuitcaseMaterial byMetadata( int metadata ) {
        for ( SuitcaseMaterial m : values() ) {
            if ( m.metadata == metadata ) return m;
        }
        throw new NoSuchElementException( "metadata: " + metadata );
    }

    public String suitcaseId() {
        return "suitcase_" + name;
    }

}
