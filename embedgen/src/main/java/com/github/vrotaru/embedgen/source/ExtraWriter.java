package com.github.vrotaru.embedgen.source;

import java.io.File;
import java.io.FileWriter;
import java.util.Properties;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

@Data
public class ExtraWriter {

    private final File            workingDir;
    private final VelocityContext context;

    private VelocityEngine        engine;

    private void initVelocityEngine() {
        if (engine == null) {
            engine = new VelocityEngine();

            Properties properties = new Properties();
            {
                properties.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
                properties.setProperty("file.resource.loader.class", FileResourceLoader.class.getName());

                properties.setProperty("file.resource.loader.path", workingDir.getAbsolutePath());

                engine.init(properties);
            }
        }
    }

    @SneakyThrows
    public void write(File workingDir, String fileName, String templateName) {
        initVelocityEngine();

        val file = new File(workingDir, fileName);
        val writer = new FileWriter(file);

        val template = engine.getTemplate(templateName);

        template.merge(context, writer);

        writer.flush();
        writer.close();

    }
}
