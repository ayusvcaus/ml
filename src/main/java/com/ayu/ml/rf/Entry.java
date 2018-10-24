package com.ayu.ml.rf;

import java.util.ArrayList;
import java.util.List;

public class Entry {
    List<CellData> attributes;
    String label;

    Entry() {
        this.attributes = new ArrayList<>();
        this.label = null;
    }
}
