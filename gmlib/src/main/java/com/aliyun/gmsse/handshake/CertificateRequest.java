package com.aliyun.gmsse.handshake;

import com.aliyun.gmsse.record.Handshake.Body;

import java.io.IOException;
import java.io.InputStream;


public class CertificateRequest extends Body {

    @Override
    public byte[] getBytes() throws IOException {
        return null;
    }

    public static Body read(InputStream input) {
        return null;
    }

}
