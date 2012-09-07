package com.github.vrotaru.embedgen;

import java.util.ArrayList;
import java.util.List;

import com.github.vrotaru.embedgen.source.EmbedClass;

import lombok.Data;
import lombok.Delegate;

@Data
public class JobDesc {
    public interface IList {
        void add(EmbedClass decl);

        int size();

        EmbedClass get(int n);
    }

    private String                 sourcePath;
    private String                 bitmapsPkg;

    @Delegate(types = IList.class)
    private final List<EmbedClass> items = new ArrayList<EmbedClass>();

}
