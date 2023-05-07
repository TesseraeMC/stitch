package com.tesseraemc.stitch.builder;

import com.tesseraemc.stitch.instance.SchematicChunkLoader;
import com.tesseraemc.stitch.schematic.Schematic;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Builder {

    private final List<Schematic> schematics = new ArrayList<>();
    private Function<Chunk, CompletableFuture<Void>> handler = chunk ->
            CompletableFuture.completedFuture(null);
    private int xOffset;
    private int yOffset;
    private int zOffset;

    public Builder() {}

    /**
     * Adds a schematic to this chunk loader.
     * <br><br>
     * Note that schematics are loaded in the order they are added.
     * <br>
     * This means that the last added schematic is the only schematic that is guaranteed to have all its data.
     * @param schematic The schematic to add.
     * @return This builder.
     */
    // TODO: Add a way to position schematics within the instance.
    public @NotNull Builder addSchematic(@NotNull Schematic schematic) {
        schematics.add(schematic);
        return this;
    }

    /**
     * Specifies the offset that applies to all schematics added to this chunk loader.
     * @param x The x offset.
     * @param y The y offset.
     * @param z The z offset.
     * @return This builder.
     */
    public @NotNull Builder offset(int x, int y, int z) {
        this.xOffset = x;
        this.yOffset = y;
        this.zOffset = z;
        return this;
    }

    /**
     * Specifies the handler to use to save the chunks.
     * @param handler The handler.
     * @return This builder.
     */
    public @NotNull Builder saveChunkHandler(@NotNull Function<@NotNull Chunk, @NotNull CompletableFuture<Void>> handler) {
        this.handler = handler;
        return this;
    }

    public @NotNull SchematicChunkLoader build() {
        return new SchematicChunkLoader(handler, List.copyOf(schematics), xOffset, yOffset, zOffset);
    }
}
