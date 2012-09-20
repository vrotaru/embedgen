package com.github.vrotaru.embedgen;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lombok.SneakyThrows;
import lombok.val;

import com.github.vrotaru.embedgen.source.EmbedClass;
import com.github.vrotaru.embedgen.source.EmbedExtra;

public enum EmbedConf {

    IMPL;
    //
    // Implementation
    //
    private static final Map<String, Boolean> truthValues = new HashMap<String, Boolean>();
    static {
        truthValues.put("true", true);
        truthValues.put("false", false);

        truthValues.put("yes", true);
        truthValues.put("no", false);

        truthValues.put("on", true);
        truthValues.put("off", false);
    }

    @SneakyThrows
    public JobDesc getJob(String filename) {
        val properties = readProperties(filename);

        val result = new JobDesc();
        {
            result.setSourcePath(properties.getProperty("src.path"));
            result.setBitmapsPkg(properties.getProperty("bitmaps.package"));

            String[] clsNames = properties.getProperty("bitmaps.classes").split(",");

            readClassesInto(result, clsNames, properties);
        }

        return result;
    }

    private Properties readProperties(String filename) throws FileNotFoundException, IOException {
        val properties = new Properties();
        val inputStream = new FileInputStream(filename);

        properties.load(inputStream);
        return properties;
    }

    private void readClassesInto(final JobDesc result, String[] clsNames, Properties properties) {

        for (String clsName : clsNames) {
            val cleanName = clsName.trim();
            val imagePath = properties.getProperty(cleanName + ".path")
                    .trim();
            val scale9Repr = properties.getProperty(cleanName + ".scale9")
                    .trim()
                    .toLowerCase();
            val scale9 = truthValues.get(scale9Repr);
            val extraRepr = properties.getProperty(cleanName + ".extra");
            val extra = readExtra(extraRepr);

            val embed = new EmbedClass();
            {
                embed.setClsName(cleanName);
                embed.setImagePath(imagePath);
                embed.setScale9(scale9);
                embed.setExtra(extra);
            }

            result.add(embed);
        }
    }

    private EmbedExtra readExtra(String repr) {
        if (repr == null) {
            return null;
        }
        else {
            String[] parts = repr.split("->");
            String template = parts[0].trim();
            String output = parts[1].trim();

            val extra = new EmbedExtra();
            {
                extra.setOutputFile(output);
                extra.setVelocityTemplate(template);
            }

            return extra;
        }
    }
}
