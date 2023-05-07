# stitch
An implementation of schematics for Minestom, written in Java. Forked from [Scaffolding](https://github.com/HyperaDev/Scaffolding).

## Usage

```java

// Load a schematic from a file path.
public void load() {
    Schematic schematic = Stitch.fromFile(new File("schems/awesome_schem.schematic"));
}

// Place a schematic at a location.
public void place(Instance instance, Pos pos) {
    schematic.build(instance, pos);
}
```