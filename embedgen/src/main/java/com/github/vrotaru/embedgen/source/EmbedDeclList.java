package com.github.vrotaru.embedgen.source;

import java.util.ArrayList;
import java.util.List;

import lombok.Delegate;

public class EmbedDeclList {

    public interface IList {
        void add(EmbedDecl decl);

        int size();

        EmbedDecl get(int n);

        boolean contains(EmbedDecl decl);
    }

    @Delegate
    private List<EmbedDecl> items = new ArrayList<EmbedDecl>();

    public EmbedDecl find(String source) {
        for (EmbedDecl item : items) {
            if (item.getSource().equals(source)) { return item; }
        }
        return null;
    }

}
