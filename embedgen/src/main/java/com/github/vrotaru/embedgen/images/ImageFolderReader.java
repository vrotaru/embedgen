package com.github.vrotaru.embedgen.images;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;

@Data
public class ImageFolderReader {

    public final File   workingDir;
    public final String srcPath;
    public final String imagePath;

    @SneakyThrows
    public ImageDescList read() {
        val images = new ImageDescList();

        val srcFolder = new File(workingDir, srcPath);
        val imageFolder = new File(srcFolder, imagePath);

        String[] filenames = imageFolder.list();
        if (filenames != null) {
            Arrays.sort(filenames);

            for (String filename : filenames) {
                if (filename.endsWith(".png")) {
                    val imageFile = new File(imageFolder, filename);
                    val bufferedImage = ImageIO.read(new FileInputStream(imageFile));

                    int width = bufferedImage.getWidth();
                    int height = bufferedImage.getHeight();

                    val item = new ImageDesc();
                    {
                        item.setSource("/" + imagePath + "/" + filename);
                        item.setWidth(width);
                        item.setHeight(height);
                    }
                    images.add(item);
                }
            }
        }

        return images;
    }
}
