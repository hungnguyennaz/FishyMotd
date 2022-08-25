package xyz.bamaygay.motd.velocity.utils;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class Components {
    private Components(){}
    public static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder()
        .character(LegacyComponentSerializer.AMPERSAND_CHAR)
        .hexColors()
        .build();
}
