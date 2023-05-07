package com.tesseraemc.stitch.instance;

import com.tesseraemc.stitch.builder.Builder;
import com.tesseraemc.stitch.schematic.Schematic;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.DynamicChunk;
import net.minestom.server.instance.IChunkLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.chunk.ChunkUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import space.vectrix.flare.fastutil.Long2ObjectSyncMap;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@SuppressWarnings("UnstableApiUsage")
public class SchematicChunkLoader implements IChunkLoader {
    private final @NotNull Function<@NotNull Chunk, @NotNull CompletableFuture<Void>> saveHandler;
    private final Long2ObjectMap<ChunkBatch> batches = Long2ObjectSyncMap.hashmap();

    public SchematicChunkLoader(
            @NotNull Function<Chunk, CompletableFuture<Void>> saveHandler,
            Collection<Schematic> schematics,
            int offsetX,
            int offsetY,
            int offsetZ) {
        this.saveHandler = saveHandler;

        Block.Setter setter = (x, y, z, block) -> {
            int chunkX = ChunkUtils.getChunkCoordinate(x);
            int chunkZ = ChunkUtils.getChunkCoordinate(z);
            long index = ChunkUtils.getChunkIndex(chunkX, chunkZ);

            // Get the batch, create it if it doesn't exist
            ChunkBatch batch = batches.computeIfAbsent(index, key -> new ChunkBatch());

            // Add the block to the batch
            batch.setBlock(x + offsetX, y + offsetY, z + offsetZ, block);
        };

        // Apply the schematics
        for (Schematic schematic : schematics) {
            schematic.apply(setter);
        }
    }

    @Override
    public @NotNull CompletableFuture<@Nullable Chunk> loadChunk(@NotNull Instance instance, int chunkX, int chunkZ) {
        long index = ChunkUtils.getChunkIndex(chunkX, chunkZ);
        ChunkBatch batch = batches.get(index);

        if (batch == null) {
            return CompletableFuture.completedFuture(null);
        }

        DynamicChunk chunk = new DynamicChunk(instance, chunkX, chunkZ);
        CompletableFuture<Chunk> future = new CompletableFuture<>();
        batch.apply(instance, chunk, future::complete);

        return future;
    }

    @Override
    public @NotNull CompletableFuture<Void> saveChunk(@NotNull Chunk chunk) {
        return saveHandler.apply(chunk);
    }

    public static Builder builder() {
        return new Builder();
    }
}
