package com.github.vrotaru.embedgen.source;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Properties;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.github.vrotaru.embedgen.images.ImageDesc;
import com.github.vrotaru.embedgen.images.ImageDescList;

@Data
public class SourceWriter {
    private static final String VELOCITY_TEMPLATE = "as3-template.vm";

    private final ImageDescList images;
    private final EmbedDeclList decls;
    private final boolean       scale9;

    private VelocityEngine      engine            = new VelocityEngine();
    {
        Properties properties = new Properties();
        {
            properties.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            properties.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

            engine.init(properties);
        }
    }

    public void write(File folder, String pkg, String clsName) {
        val items = new ArrayList<EmbedDecl>();
        val context = new VelocityContext();
        {
            context.put("pkg", pkg);
            context.put("class", clsName);
            context.put("scale9", scale9);

            context.put("items", items);

        }

        int size = images.size();
        for (int i = 0; i < size; ++i) {
            ImageDesc desc = images.get(i);
            EmbedDecl decl = decls.find(desc.getSource());
            if (decl == null) {
                decl = makeEmded(desc);
            }
            items.add(decl);
        }

        val template = engine.getTemplate(VELOCITY_TEMPLATE);
        val writer = makeWriter(folder, pkg, clsName);
        template.merge(context, writer);

        writer.flush();
        writer.close();
    }

    @SneakyThrows
    private PrintWriter makeWriter(File folder, String pkg, String clsName) {
        val pkgFolder = new File(folder, pkg.replace('.', '/'));
        pkgFolder.mkdirs();

        val clsFile = new File(pkgFolder, clsName + ".as");

        return new PrintWriter(clsFile);
    }

    private EmbedDecl makeEmded(ImageDesc desc) {
        String name = extractName(desc.getSource());
        int offset = extractOffset(desc.getWidth(), desc.getHeight());

        val em = new EmbedDecl();
        {
            em.setSource(desc.getSource());
            em.setName(name);
            em.setLeft(offset);
            em.setTop(offset);
            em.setRight(desc.getWidth() - 2 * offset);
            em.setBottom(desc.getHeight() - 2 * offset);
        }
        return em;
    }

    private int extractOffset(int width, int height) {
        int minSize = width < height ? width : height;
        if (minSize < 15) {
            return -1;
        }
        else if (minSize < 30) {
            return 5;
        }
        else if (minSize < 60) {
            return 10;
        }
        else {
            return 15;
        }
    }

    private String extractName(String source) {
        int start = source.lastIndexOf('/');
        int end = source.lastIndexOf(".png");

        String name = source.substring(start + 1, end);
        {
            name = name.toUpperCase();
            name = name.replace('-', '_');

            return name;
        }
    }

}
