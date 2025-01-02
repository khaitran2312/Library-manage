package Polyfill;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class PFFileReader extends PFFile {
    private static final Logger LOGGER = Logger.getLogger(PFFile.class.getName());

    public PFFileReader(String csvPath0, String... csvPath) {
        super(csvPath0, csvPath);
    }

    public PFFileReader(Path csvPath) {
        super(csvPath);
    }

    public PFArray<String[]> read() {
        PFArray<String[]> lines = new PFArray<>();
        LOGGER.info("Opening file " + path.toAbsolutePath());
        try (Reader r = Files.newBufferedReader(path, charset)) {
            try (CSVReader cr = new CSVReader(r)) {
                // As List usage is not allowed we cannot use CSV.readAll() as it returns a
                // List<String[]>
                String[] record;
                while ((record = cr.readNext()) != null) {
                    lines.push_back(record);
                }
            }
        } catch (CsvValidationException e) {
            LOGGER.log(Level.WARNING, "Error during read " + path.toAbsolutePath(), e);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error opening " + path.toAbsolutePath(), e);
        } finally {
            if (lines.size() > 0) {
                LOGGER.info(String.format("Read %d records, %d columns each", lines.size(), lines.at(0).length));
            } else {
                LOGGER.warning("No data");
            }
        }
        return lines;
    }
}
