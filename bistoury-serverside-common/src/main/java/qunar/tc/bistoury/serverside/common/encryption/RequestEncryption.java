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

package qunar.tc.bistoury.serverside.common.encryption;

import qunar.tc.bistoury.remoting.protocol.RequestData;

import java.io.IOException;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc 请求对象编码解码
 */
public interface RequestEncryption {
    /**
     * 解码
     * @param in 输入内容
     * @return 请求数据对象
     * @throws IOException
     */
    RequestData<String> decrypt(String in) throws IOException;

    /**
     * 编码
     * @param requestData 请求数据
     * @param key 键
     * @return 编码后的值
     * @throws IOException
     */
    String encrypt(RequestData<String> requestData, final String key) throws IOException;
}
