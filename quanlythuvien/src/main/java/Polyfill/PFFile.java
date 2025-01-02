package Polyfill;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class PFFile {
    public PFFile(String csvPath0, String... csvPath) {
        path = Paths.get(csvPath0, csvPath);
    }

    public PFFile(Path csvPath) {
        path = csvPath;
    }

    public Path getPath() {
        return path;
    }

    protected Path path;
    protected static final Charset charset = StandardCharsets.UTF_8;
}
