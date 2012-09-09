package com.github.vrotaru.embedgen;

import java.io.File;

import lombok.val;

import com.github.vrotaru.embedgen.images.ImageFolderReader;
import com.github.vrotaru.embedgen.source.SourceReader;
import com.github.vrotaru.embedgen.source.SourceWriter;

public class EmbedGen {

    private static final String CONFIG = "embed.properties";

    private void run() {
        val conf = EmbedConf.IMPL;
        val job = conf.getJob(CONFIG);

        val srcPath = job.getSourcePath();
        val bitmapsPkg = job.getBitmapsPkg();

        val items = job.getItems();
        for (val item : items) {
            val imagePath = item.getImagePath();
            val clsName = item.getClsName();
            val scale9 = item.isScale9();

            val sourceReader = new SourceReader(srcPath);
            val decls = sourceReader.read(bitmapsPkg, clsName);

            val folderReader = new ImageFolderReader(srcPath, imagePath);
            val images = folderReader.read();

            val sourceWriter = new SourceWriter(images, decls, scale9);
            sourceWriter.write(new File(srcPath), bitmapsPkg, clsName);
        }

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new EmbedGen().run();
    }

}
