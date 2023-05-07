package com.tesseraemc.stitch;

import com.tesseraemc.stitch.schematic.Schematic;
import com.tesseraemc.stitch.schematic.impl.MCEditSchematic;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jglrxavpok.hephaistos.nbt.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class Stitch {
    public static Schematic fromStream(InputStream inputStream) throws IOException, NBTException {
        NBTReader reader = new NBTReader(inputStream, CompressedProcesser.GZIP);
        Pair<String, NBT> pair = reader.readNamed();
        NBTCompound nbtTag = (NBTCompound) pair.getSecond();
        Schematic schematic = null;

        if (nbtTag.contains("Blocks")) schematic = new MCEditSchematic();

        if (schematic != null) schematic.read(nbtTag);
        return schematic;
    }

    public static @Nullable Schematic fromPath(@NotNull Path path) throws IOException, NBTException {
        if (!Files.exists(path)) throw new FileNotFoundException("[Stitch] Schematic does not exist.");
        return fromStream(Files.newInputStream(path));
    }

    public static @Nullable Schematic fromFile(@NotNull File file) throws IOException, NBTException {
        if (!file.exists()) throw new FileNotFoundException("[Stitch] Schematic does not exist.");
        return fromStream(new FileInputStream(file));
    }
}