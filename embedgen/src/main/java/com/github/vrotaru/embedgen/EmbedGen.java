package com.github.vrotaru.embedgen;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import lombok.SneakyThrows;
import lombok.val;

import com.github.vrotaru.embedgen.images.ImageFolderReader;
import com.github.vrotaru.embedgen.source.SourceReader;
import com.github.vrotaru.embedgen.source.SourceWriter;

public class EmbedGen {

    private static final String CONFIG = "embed.properties";

    private Properties          config;

    public EmbedGen() {
        init();
    }

    @SneakyThrows
    private void init() {
        config = new Properties();
        config.load(new FileInputStream(CONFIG));
    }

    private void run() {
        val srcPath = config.getProperty("src.path");
        val imagePath = config.getProperty("image.path");
        val bitmapsPkg = config.getProperty("bitmaps.package");
        val bitmapsCls = config.getProperty("bitmaps.class");

        val sourceReader = new SourceReader(srcPath);
        val decls = sourceReader.read(bitmapsPkg, bitmapsCls);

        val folderReader = new ImageFolderReader(srcPath, imagePath);
        val images = folderReader.read();

        val sourceWriter = new SourceWriter(images, decls);
        sourceWriter.write(new File(srcPath), bitmapsPkg, bitmapsCls);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new EmbedGen().run();

    }

}
