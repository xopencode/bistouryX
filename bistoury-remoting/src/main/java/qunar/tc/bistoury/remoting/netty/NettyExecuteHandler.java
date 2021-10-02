/*
 * Copyright (C) 2019 Qunar, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package qunar.tc.bistoury.remoting.netty;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.bistoury.agent.common.ResponseHandler;
import qunar.tc.bistoury.remoting.protocol.RemotingBuilder;
import qunar.tc.bistoury.remoting.protocol.RemotingHeader;

import java.util.Map;

/**
 * @author 肖哥弹架构
 * @date 2022-09-16
 * @desc 响应结果处理
 */
public class NettyExecuteHandler implements ResponseHandler {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(NettyExecuteHandler.class);
    /**
     * 响应结果输出
     */
    private ResponseWriter responseWriter = ResponseWriter.getInstance();
    /**
     * 远程协议头
     */
    private RemotingHeader header;
    /**
     * Netty通道处理上下文
     */
    private ChannelHandlerContext ctx;

    /**
     * 构造方法
     * @param header 远程协议头
     * @param ctx netty处理上下文
     */
    public NettyExecuteHandler(RemotingHeader header, ChannelHandlerContext ctx) {
        this.header = header;
        this.ctx = ctx;
    }

    /**
     * 创建NettyExecuteHandler
     * @param remotingHeader
     * @param ctx
     * @return
     */
    public static NettyExecuteHandler of(RemotingHeader remotingHeader, ChannelHandlerContext ctx) {
        return new NettyExecuteHandler(remotingHeader, ctx);
    }

    @Override
    public boolean isWritable() {
        return ctx.channel().isWritable();
    }

    @Override
    public boolean isActive() {
        return ctx.channel().isActive();
    }

    @Override
    public void handle(String line) {
        responseWriter.writeNormal(ctx, line, header);
    }

    @Override
    public void handle(int code, String line) {
        responseWriter.writeWithCode(ctx, line, code, header);
    }

    @Override
    public void handle(int code, byte[] data) {
        responseWriter.writeWithCode(ctx, data, code, header);
    }

    @Override
    public void handle(byte[] dataBytes) {
        responseWriter.writeNormal(ctx, dataBytes, header);
    }

    @Override
    public void handleError(int errorCode) {
        responseWriter.writeError(ctx, errorCode, header);
    }

    @Override
    public void handleError(String error) {
        responseWriter.writeError(ctx, error, header);
    }

    @Override
    public void handleError(Throwable throwable) {
        handleError("Agent error: " + formatException(throwable));
    }

    @Override
    public void handleEOF() {
        responseWriter.writeEOF(ctx, "", header);
    }

    @Override
    public void handleEOF(int exitCode) {
        responseWriter.writeEOF(ctx, String.valueOf(exitCode), header);
    }

    @Override
    public void handle(int code, byte[] data, Map<String, String> responseHeader) {
        RemotingHeader realHeader = RemotingBuilder.buildRemotingHeader(code, header.getId());
        realHeader.setProperties(responseHeader);
        responseWriter.writeFullResponse(ctx, data, realHeader);
    }

    private String formatException(Throwable throwable) {
        return throwable.getClass().getName() + "---" + throwable.getMessage();
    }
}

    