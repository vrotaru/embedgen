package com.github.vrotaru.embedgen.source;

import lombok.Data;

@Data
public class EmbedDecl {

    private String source;
    private String name;
    private int    left;
    private int    top;
    private int    right;
    private int    bottom;

}
