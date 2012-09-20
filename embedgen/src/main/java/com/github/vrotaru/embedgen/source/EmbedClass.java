package com.github.vrotaru.embedgen.source;

import lombok.Data;

@Data
public class EmbedClass {
    private String     imagePath;
    private String     clsName;
    private boolean    scale9;
    private EmbedExtra extra;
}
