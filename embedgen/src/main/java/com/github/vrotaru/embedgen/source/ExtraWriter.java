package com.github.vrotaru.embedgen.source;

import java.io.FileWriter;
import java.util.Properties;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

@Data
public class ExtraWriter {

    private final VelocityContext context;

    private VelocityEngine        engine = new VelocityEngine();
    {
        Properties properties = new Properties();
        {
            engine.init(properties);
        }
    }

    @SneakyThrows
    public void write(String fileName, String templateName) {
        val writer = new FileWriter(fileName);
        val template = engine.getTemplate(templateName);

        template.merge(context, writer);

        writer.flush();
        writer.close();

    }
}
