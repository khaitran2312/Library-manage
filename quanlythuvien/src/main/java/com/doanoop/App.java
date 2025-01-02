package com.doanoop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import ThuVien.Main;
import ThuVien.Main.Settings;

/**
 * Hello world!
 *
 */
public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        System.out.println("Hello World!");
        LOGGER.setLevel(Level.ALL);
        System.out.println(Paths.get(".").toAbsolutePath());
        Properties def_props = new Properties();
        def_props.setProperty("loadfromdata", String.valueOf(Settings.loadFromData));
        def_props.setProperty("savetodata", String.valueOf(Settings.saveToData));
        def_props.setProperty("skipfirstrow", String.valueOf(Settings.skipFirstRow));
        def_props.setProperty("loglevel", String.valueOf(Settings.logLevel));
        Properties props = new Properties(def_props);
        try {
            props.load(Files.newBufferedReader(Paths.get("quanlythuvien", "data", "config.properties")));
            Settings.loadFromData = Boolean.parseBoolean(props.getProperty("loadfromdata"));
            Settings.saveToData = Boolean.parseBoolean(props.getProperty("savetodata"));
            Settings.skipFirstRow = Boolean.parseBoolean(props.getProperty("skipfirstrow"));
            Settings.logLevel = Integer.parseInt(props.getProperty("loglevel"));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "The config file could not be read", e);
        }
        Main.runApp();
        try {
            props.store(Files.newBufferedWriter(Paths.get("quanlythuvien", "data", "config.properties")),
                    "Quan ly thu vien's configuration file");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "The config file could not be written", e);
        }
    }
}
