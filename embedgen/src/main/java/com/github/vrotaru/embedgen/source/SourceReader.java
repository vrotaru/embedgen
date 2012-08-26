package com.github.vrotaru.embedgen.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import lombok.Cleanup;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;

@Data
public class SourceReader {

    private final String srcPath;

    @SneakyThrows
    public EmbedDeclList read(String pkg, String cls) {
        val list = new EmbedDeclList();

        val srcFolder = new File(srcPath);
        val clsFolder = new File(srcFolder, pkg.replace('.', '/'));
        val clsFile = new File(clsFolder, cls + ".as");

        if (clsFile.exists()) {
            @Cleanup
            val reader = new BufferedReader(new FileReader(clsFile));

            String line = reader.readLine();
            while (line != null) {
                line = line.trim();
                if (line.startsWith("[Embed")) {
                    int start = line.indexOf('(');
                    int end = line.indexOf(')');
                    val embedLine = line.substring(start + 1, end);
                    val nextLine = reader.readLine();

                    list.add(readEmdebDecl(embedLine, nextLine));
                }

                line = reader.readLine();
            }
        }

        return list;
    }

    private EmbedDecl readEmdebDecl(String embedLine, String nextLine) {
        val data = embedLine.trim();

        String source = getSource(data);
        int left = getOffset(data, "scaleGridLeft");
        int top = getOffset(data, "scaleGridTop");
        int right = getOffset(data, "scaleGridRight");
        int bottom = getOffset(data, "scaleGridBottom");

        String name = getName(nextLine);

        val item = new EmbedDecl();
        {
            item.setSource(source);
            item.setName(name);
            item.setLeft(left);
            item.setTop(top);
            item.setRight(right);
            item.setBottom(bottom);
        }

        return item;
    }

    private String getSource(String data) {
        int start = data.indexOf('"');
        int end = data.indexOf('"', start + 1);
        String source = data.substring(start + 1, end);
        return source;
    }

    private String getName(String nextLine) {
        int start = nextLine.indexOf("const ") + "const ".length();
        int end = nextLine.indexOf(":", start + 1);
        String value = nextLine.substring(start, end);
        return value.trim();
    }

    private int getOffset(String data, String param) {
        int start0 = data.indexOf(param);
        if (start0 >= 0) {
            int start = data.indexOf("=", start0 + 1);
            if (start >= 0) {
                int end = data.indexOf(",", start + 1);

                String value;
                if (end >= 0) {
                    value = data.substring(start + 1, end);
                }
                else {
                    value = data.substring(start + 1);
                }

                return Integer.parseInt(value.trim());
            }
        }
        return -1;
    }

}
