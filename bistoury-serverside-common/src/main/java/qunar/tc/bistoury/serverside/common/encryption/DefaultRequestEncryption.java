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

import com.fasterxml.jackson.core.type.TypeReference;
import qunar.tc.bistoury.common.JacksonSerializer;
import qunar.tc.bistoury.remoting.protocol.RequestData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc 请求对象编码解码默认实现
 */
public class DefaultRequestEncryption implements RequestEncryption {

    /**
     * Json Map类型定义
     */
    private static final TypeReference<Map<String, Object>> mapReference = new TypeReference<Map<String, Object>>() {};
    /**
     * Json RequestData类型定义
     */
    private static final TypeReference<RequestData<String>> inputType = new TypeReference<RequestData<String>>() {};
    /**
     * 键索引位
     */
    private static final String KEY_INDEX = "0";
    /**
     * 值索引位
     */
    private static final String DATA_INDEX = "1";
    /**
     * RSA 加解密组件
     */
    private final RSAEncryption rsa;

    public DefaultRequestEncryption(RSAEncryption rsa) {
        this.rsa = rsa;
    }

    /**
     * 解密
     * @param in 输入内容
     * @return 返回请求数据对象
     * @throws IOException
     */
    @Override
    public RequestData<String> decrypt(String in) throws IOException {
        //内容序列化为MAP
        Map<String, Object> map = JacksonSerializer.deSerialize(in, mapReference);
        //获取键值
        String rsaData = (String) map.get(KEY_INDEX);
        //获取数据值
        String data = (String) map.get(DATA_INDEX);
        //解密键值
        String desKey = rsa.decrypt(rsaData);
        //解密请求内容
        String requestStr = EncryptionUtils.decryptDes(data, desKey);
        //序列化请求对象
        return JacksonSerializer.deSerialize(requestStr, inputType);
    }

    /**
     * 加密
     * @param requestData 请求数据
     * @param key 键
     * @return 加密后的字符串
     * @throws IOException
     */
    @Override
    public String encrypt(RequestData<String> requestData, final String key) throws IOException {
        Map<String, String> map = new HashMap<>();
        String encrypt = rsa.encrypt(key);
        map.put(KEY_INDEX, encrypt);

        String encryptDes = EncryptionUtils.encryptDes(JacksonSerializer.serialize(requestData), key);
        map.put(DATA_INDEX, encryptDes);
        return JacksonSerializer.serialize(map);
    }


}
