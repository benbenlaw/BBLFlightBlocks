package com.benbenlaw.flightblocks.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class StartupConfig {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> flightBlockRange;

    static {

        // Ender Scrambler Configs
        BUILDER.comment("Flight Block Config")
                .push("Flight Block");

        flightBlockRange = BUILDER.comment("Range on each side on the Flight Block, default = 32")
                .define("Max Flight Block Range", 32);

        BUILDER.pop();


        //LAST

        SPEC = BUILDER.build();

    }

}
