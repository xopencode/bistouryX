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

package qunar.tc.bistoury.agent.common;

import java.util.Map;

/**
 * @author 肖哥弹架构
 * @date 2022-09-13
 * @desc 响应处理策略
 */
public interface ResponseHandler {
    /**
     * 是否可写
     * @return
     */
    boolean isWritable();

    /**
     * 是否已激活
     * @return
     */
    boolean isActive();

    /**
     * 处理响应数据
     * @param line 数据
     */
    void handle(String line);

    /**
     * 处理响应数据
     * @param code 响应状态码
     * @param line 响应数据
     */
    void handle(int code, String line);

    /**
     * 处理响应数据
     * @param code 编号
     * @param data 响应内容
     */
    void handle(int code, byte[] data);

    /**
     * 处理响应数据
     * @param dataBytes 响应内容
     */
    void handle(byte[] dataBytes);

    /**
     * 处理响应数据
     * @param errorCode 错误编码
     */
    void handleError(int errorCode);

    /**
     * 处理响应数据
     * @param error 错误编码
     */
    void handleError(String error);

    /**
     * 处理响应数据
     * @param throwable 异常处理
     */
    void handleError(Throwable throwable);

    /**
     * 处理EOF
     */
    void handleEOF();

    /**
     * 处理EOF
     * @param exitCode 异常编码
     */
    void handleEOF(int exitCode);

    /**
     * 处理响应数据
     * @param code 编码
     * @param data 响应数据
     * @param responseHeader 响应协议头数据
     */
    void handle(int code, byte[] data, Map<String, String> responseHeader);
}

    