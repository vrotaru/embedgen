package com.github.vrotaru.embedgen;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lombok.SneakyThrows;
import lombok.val;

import com.github.vrotaru.embedgen.source.EmbedClass;

public enum EmbedConf {

    IMPL("embed.properties");
    //
    // Implementation
    //
    private static final Map<String, Boolean> truthValues = new HashMap<String, Boolean>();
    static {
        truthValues.put("true", true);
        truthValues.put("false", false);

        truthValues.put("yes", true);
        truthValues.put("no", false);
    }

    private final Properties                  properties;

    @SneakyThrows
    private EmbedConf(String filename) {
        properties = new Properties();
        FileInputStream inputStream = new FileInputStream(filename);

        properties.load(inputStream);
    }

    public JobDesc getJob() {
        val result = new JobDesc();
        {
            result.setSourcePath(properties.getProperty("src.path"));
            result.setBitmapsPkg(properties.getProperty("bitmaps.package"));

            String[] clsNames = properties.getProperty("bitmaps.classes").split(",");

            readClassesInto(result, clsNames);
        }

        return result;
    }

    private void readClassesInto(final JobDesc result, String[] clsNames) {
        for (String clsName : clsNames) {
            val cleanName = clsName.trim();
            val imagePath = properties.getProperty(cleanName + ".path");
            val scale9Repr = properties.getProperty(cleanName + ".scale9")
                    .trim()
                    .toLowerCase();
            val scale9 = truthValues.get(scale9Repr);

            val embed = new EmbedClass();
            {
                embed.setClsName(cleanName);
                embed.setImagePath(imagePath);
                embed.setScale9(scale9);
            }

            result.add(embed);
        }
    }
}
