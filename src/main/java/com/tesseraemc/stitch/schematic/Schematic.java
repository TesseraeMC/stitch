package com.tesseraemc.stitch.schematic;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.jglrxavpok.hephaistos.nbt.NBTReader;
import com.tesseraemc.stitch.region.Region;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CompletableFuture;

public interface Schematic {
    default void read(InputStream inputStream) throws IOException, NBTException {
        NBTReader nbtReader = new NBTReader(inputStream);
        read((NBTCompound) nbtReader.readNamed().getSecond());
    }

    void read(NBTCompound nbtTag) throws NBTException;
    void write(OutputStream outputStream, Region region);

    CompletableFuture<Region> build(Instance instance, Point pos);

    short getWidth();
    short getHeight();
    short getLength();

    int getOffsetX();
    int getOffsetY();
    int getOffsetZ();

    void apply(Block.Setter blockSetter);
}
