package Polyfill;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opencsv.CSVWriter;

public class PFFileWriter extends PFFile {
    private static final Logger LOGGER = Logger.getLogger(PFFile.class.getName());

    public PFFileWriter(String csvPath0, String... csvPath) {
        super(csvPath0, csvPath);
    }

    public PFFileWriter(Path csvPath) {
        super(csvPath);
    }

    public boolean write(PFArray<String[]> lines) {
        LOGGER.info("Opening file " + path.toAbsolutePath());
        // no OpenOptions: combination of CREATE | TRUNCATE_EXISTING | WRITE
        try (Writer w = Files.newBufferedWriter(path, charset)) {
            try (CSVWriter cw = new CSVWriter(w)) {
                // don't worry PFArray is iterable
                cw.writeAll(lines);
            }
            return true; // success
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error writing " + path.toAbsolutePath(), e);
        } finally {
            if (lines.size() > 0) {
                LOGGER.info(String.format("Wrote %d records, %d columns each", lines.size(), lines.at(0).length));
            } else {
                LOGGER.warning("No data");
            }
        }
        return false; // failure
    }
}
