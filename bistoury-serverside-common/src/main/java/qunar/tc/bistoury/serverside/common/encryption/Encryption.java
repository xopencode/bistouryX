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

import java.io.IOException;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc 编码解码策略
 */
public interface Encryption {
    /**
     * 编码
     * @param source 输入源
     * @return 编码内容
     * @throws EncryptException
     */
    String encrypt(String source) throws EncryptException;

    /**
     *  解码
     * @param source 输入源
     * @return 解码内容
     * @throws DecryptException
     */
    String decrypt(String source) throws DecryptException;

    /**
     * 编码异常类
     */
    class EncryptException extends IOException {
        public EncryptException(Throwable cause) {
            super(cause);
        }
    }

    /**
     * 解码异常类
     */
    class DecryptException extends IOException {
        public DecryptException(Throwable cause) {
            super(cause);
        }
    }
}
