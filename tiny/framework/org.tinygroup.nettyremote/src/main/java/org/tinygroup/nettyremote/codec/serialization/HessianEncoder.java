package org.tinygroup.nettyremote.codec.serialization;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.Serializable;

import com.caucho.hessian.io.HessianOutput;

public class HessianEncoder extends MessageToByteEncoder<Serializable> {
    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        int startIdx = out.writerIndex();

        ByteBufOutputStream bout = new ByteBufOutputStream(out);
        bout.write(LENGTH_PLACEHOLDER);
//        ObjectOutputStream oout = new CompactObjectOutputStream(bout);
//        oout.writeObject(msg);
//        oout.flush();
//        oout.close();
        HessianOutput hout1 = new HessianOutput(bout);
        hout1.writeObject(msg);
        int endIdx = out.writerIndex();

        out.setInt(startIdx, endIdx - startIdx - 4);
    }
}
