package ThuVien;

import java.util.logging.Level;
import java.util.logging.Logger;

import Polyfill.PFArray;
import Polyfill.PFFileReader;
import Polyfill.PFFileWriter;
import Polyfill.StringHelper;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static int runApp() {
        switch (Settings.logLevel) {
            case 1 -> LOGGER.setLevel(Level.ALL);
            case 2 -> LOGGER.setLevel(Level.WARNING);
            case 3 -> LOGGER.setLevel(Level.FINE);
            case 4 -> LOGGER.setLevel(Level.OFF);
            default -> LOGGER.setLevel(Level.SEVERE);
        }
        Global.trandan = new Owner();
        if (Settings.loadFromData) {
            LOGGER.info("Loading from _data");
            load("_data");
        } else {
            LOGGER.info("Loading from default");
            load("");
        }
        int ret = mainMenu();
        LOGGER.info("Exiting application with menu code " + ret);
        return 0;
    }

    public static int mainMenu() {
        while (true) {
            System.out.println("Select an entry to log in with");
            int n = StringHelper.acceptInput("Doc gia", "Nhan vien", "Quan li", "CEO", "Save data", "Quit");
            if (n == 6) {
                return 0;
            }
            if (n == -1) {
                break;
            }
            switch (n) {
                case 5 -> {
                    if (Settings.saveToData) {
                        LOGGER.info("Saving to _data");
                        save("_data");
                    } else {
                        LOGGER.info("Saving to default");
                        save("");
                    }
                }
                default -> loginList[n - 1].login();
            }
        }
        return 1;
    }

    public static int load(String annex) {
        try {
            Global.cards = Cards
                    .fromBatchBlob(
                            dataTrim(new PFFileReader("quanlythuvien", "data", "List_The" + annex + ".csv").read()));
            Global.authors = Authors
                    .fromBatchBlob(
                            dataTrim(new PFFileReader("quanlythuvien", "data", "List_TacGia" + annex + ".csv").read()));
            Global.documents = Documents
                    .fromBatchBlob(
                            dataTrim(
                                    new PFFileReader("quanlythuvien", "data", "List_TaiLieu" + annex + ".csv").read()));
            Global.readers = Readers
                    .fromBatchBlob(
                            dataTrim(new PFFileReader("quanlythuvien", "data", "List_DocGia" + annex + ".csv").read()));
            Global.cashiers = Cashiers
                    .fromBatchBlob(
                            dataTrim(new PFFileReader("quanlythuvien", "data", "List_NhanVien" + annex + ".csv")
                                    .read()));
            Global.managers = Managers
                    .fromBatchBlob(
                            dataTrim(new PFFileReader("quanlythuvien", "data", "List_QuanLi" + annex + ".csv").read()));
            Global.hoadons = HoaDons
                    .fromBatchBlob(
                            dataTrim(new PFFileReader("quanlythuvien", "data", "List_HoaDon" + annex + ".csv").read()));
            LOGGER.info("Loaded without errors");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Load data error", e);
            throw e;
        } finally {
            loginList = new ILogin[] { Global.readers, Global.cashiers, Global.managers, Global.trandan };
        }
        return 0;
    }

    public static int save(String annex) {
        try {
            new PFFileWriter("quanlythuvien", "data", "List_The" + annex + ".csv").write(Global.cards.toBatchBlob());
            new PFFileWriter("quanlythuvien", "data", "List_TacGia" + annex + ".csv")
                    .write(Global.authors.toBatchBlob());
            new PFFileWriter("quanlythuvien", "data", "List_TaiLieu" + annex + ".csv")
                    .write(Global.documents.toBatchBlob());
            new PFFileWriter("quanlythuvien", "data", "List_DocGia" + annex + ".csv")
                    .write(Global.readers.toBatchBlob());
            new PFFileWriter("quanlythuvien", "data", "List_NhanVien" + annex + ".csv")
                    .write(Global.cashiers.toBatchBlob());
            new PFFileWriter("quanlythuvien", "data", "List_QuanLi" + annex + ".csv")
                    .write(Global.managers.toBatchBlob());
            new PFFileWriter("quanlythuvien", "data", "List_HoaDon" + annex + ".csv")
                    .write(Global.hoadons.toBatchBlob());
            LOGGER.info("Saved without errors");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Save data error", e);
            // throw e;
            return 1;
        }
        return 0;
    }

    public static PFArray<String[]> dataTrim(PFArray<String[]> inp) {
        while (StringHelper.isNullOrBlank(inp.back()[0])) {
            inp.pop_back();
        }
        if (Settings.skipFirstRow) {
            inp.pop_front();
        }
        return inp;
    }

    public static ILogin[] loginList;

    public static final class Settings {
        public static boolean loadFromData = false, saveToData = true, skipFirstRow = true;
        public static int logLevel = 1;
    }
}
