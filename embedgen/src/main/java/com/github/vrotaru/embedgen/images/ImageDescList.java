package com.github.vrotaru.embedgen.images;

import java.util.ArrayList;
import java.util.List;

import lombok.Delegate;

public class ImageDescList {

    public interface IList {

        void add(ImageDesc desc);

        int size();

        ImageDesc get(int n);
    }

    @Delegate(types = IList.class)
    private List<ImageDesc> items = new ArrayList<ImageDesc>();

}
