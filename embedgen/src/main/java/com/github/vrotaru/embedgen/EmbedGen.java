package com.github.vrotaru.embedgen;

import java.io.File;
import java.util.List;

import lombok.val;

import org.apache.velocity.VelocityContext;

import com.github.vrotaru.embedgen.images.ImageDescList;
import com.github.vrotaru.embedgen.images.ImageFolderReader;
import com.github.vrotaru.embedgen.source.EmbedClass;
import com.github.vrotaru.embedgen.source.EmbedDeclList;
import com.github.vrotaru.embedgen.source.EmbedExtra;
import com.github.vrotaru.embedgen.source.ExtraWriter;
import com.github.vrotaru.embedgen.source.SourceReader;
import com.github.vrotaru.embedgen.source.SourceWriter;

public class EmbedGen implements IGenerate {

    private static final String CONFIG = "embed.properties";

    @Override
    public void generate() {
        EmbedConf conf = EmbedConf.IMPL;
        JobDesc job = conf.getJob(CONFIG);

        String srcPath = job.getSourcePath();
        String bitmapsPkg = job.getBitmapsPkg();

        List<EmbedClass> items = job.getItems();
        for (EmbedClass item : items) {
            String imagePath = item.getImagePath();
            String clsName = item.getClsName();
            boolean scale9 = item.isScale9();
            EmbedExtra extra = item.getExtra();

            val sourceReader = new SourceReader(srcPath);
            EmbedDeclList decls = sourceReader.read(bitmapsPkg, clsName);

            val folderReader = new ImageFolderReader(srcPath, imagePath);
            ImageDescList images = folderReader.read();

            val sourceWriter = new SourceWriter(images, decls, scale9);
            sourceWriter.write(new File(srcPath), bitmapsPkg, clsName);

            if (extra != null) {
                VelocityContext context = sourceWriter.getContext();
                val extraWriter = new ExtraWriter(context);

                extraWriter.write(extra.getOutputFile(), extra.getVelocityTemplate());

            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new EmbedGen().generate();
    }

}
