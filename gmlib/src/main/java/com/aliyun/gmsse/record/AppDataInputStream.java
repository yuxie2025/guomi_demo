package com.aliyun.gmsse.record;

import com.aliyun.gmsse.Record;
import com.aliyun.gmsse.RecordStream;

import java.io.IOException;
import java.io.InputStream;

public class AppDataInputStream extends InputStream {

    private RecordStream recordStream;

    public AppDataInputStream(RecordStream recordStream) {
        this.recordStream = recordStream;
    }

    @Override
    public int read() throws IOException {
        byte[] buf = new byte[1];
        int ret = read(buf, 0, 1);
        return ret < 0 ? -1 : buf[0] & 0xFF;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        Record record = recordStream.read(true);
        int length = Math.min(record.fragment.length, len);
        System.arraycopy(record.fragment, 0, b, off, length);
        return length;
    }
}
