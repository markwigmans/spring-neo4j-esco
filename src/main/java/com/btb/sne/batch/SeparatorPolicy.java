package com.btb.sne.batch;

import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;

import java.util.StringTokenizer;

public class SeparatorPolicy extends DefaultRecordSeparatorPolicy {

    private final int fields;

    public SeparatorPolicy(int fields) {
        this.fields = fields;
    }

    @Override
    public boolean isEndOfRecord(String record) {
        if (super.isEndOfRecord(record)) {
            int length = record.split(",", -1).length;
            return length >= fields;
        }
        return false;
    }
}

