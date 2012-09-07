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

    private void run(JobDesc job) {
        val srcPath = job.getSourcePath();
        val bitmapsPkg = job.getBitmapsPkg();

        int current = 0;
        val size = job.size();
        while (current < size) {
            val item = job.get(current);
            val imagePath = item.getImagePath();
            val clsName = item.getClsName();

            val sourceReader = new SourceReader(srcPath);
            val decls = sourceReader.read(bitmapsPkg, clsName);

            val folderReader = new ImageFolderReader(srcPath, imagePath);
            val images = folderReader.read();

            val sourceWriter = new SourceWriter(images, decls);
            sourceWriter.write(new File(srcPath), bitmapsPkg, clsName);

            current += 1;
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        EmbedConf conf = EmbedConf.IMPL;

        new EmbedGen().run(conf.getJob());
    }

}
