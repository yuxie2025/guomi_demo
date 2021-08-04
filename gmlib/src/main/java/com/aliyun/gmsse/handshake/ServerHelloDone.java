package com.aliyun.gmsse.handshake;

import com.aliyun.gmsse.record.Handshake.Body;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ServerHelloDone extends Body {

    @Override
    public byte[] getBytes() throws IOException {
        return new byte[0];
    }

    public static Body read(InputStream input) {
        return new ServerHelloDone();
    }

    @Override
    public String toString() {
        StringWriter str = new StringWriter();
        PrintWriter out = new PrintWriter(str);
        out.println("struct {");
        out.println("} ServerHelloDone;");
        return str.toString();
    }

}
