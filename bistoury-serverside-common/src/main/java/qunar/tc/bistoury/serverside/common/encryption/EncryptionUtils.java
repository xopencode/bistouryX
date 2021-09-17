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

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.springframework.core.io.ClassPathResource;
import qunar.tc.bistoury.common.FileUtil;
import qunar.tc.bistoury.serverside.agile.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc 编解码工具类
 */
public class EncryptionUtils {

    /**
     * 根据文件加载公钥对象
     * @param path rsa-public-key.pem地址
     * @return 公钥对象
     * @throws IOException
     * @throws InvalidKeySpecException
     */
    public static PublicKey loadRSAPublicKey(String path) throws IOException, InvalidKeySpecException {
        ClassPathResource pathResource = new ClassPathResource(path);
        byte[] bb = FileUtil.readBytes(pathResource.getInputStream());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decode(bb));
        try {
            return KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (NoSuchAlgorithmException e) {
            // ignore;
            return null;
        }
    }

    /**
     * 根据文件加载私钥对象
     * @param path rsa-private-key.pem 地址
     * @return 私钥对象
     * @throws IOException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey loadRSAPrivateKey(String path) throws IOException, InvalidKeySpecException {
        ClassPathResource pathResource = new ClassPathResource(path);
        byte[] bb = FileUtil.readBytes(pathResource.getInputStream());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.decode(bb));
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        } catch (NoSuchAlgorithmException e) {
            // ignore;
            return null;
        }
    }

    /**
     * 加载DES秘钥
     * @param path 秘钥文件地址
     * @return 秘钥键
     * @throws InvalidKeySpecException
     * @throws IOException
     * @throws InvalidKeyException
     */
    public static SecretKey loadDesKey(String path) throws InvalidKeySpecException, IOException, InvalidKeyException {
        String s = FileUtil.readString(new File(EncryptionUtils.class.getResource(path).getPath()), Charsets.UTF_8.name());
        DESKeySpec spec = new DESKeySpec(Base64.decode(s));
        try {
            return SecretKeyFactory.getInstance("DES").generateSecret(spec);
        } catch (NoSuchAlgorithmException e) {
            // ignore
            return null;
        }
    }

    /**
     * 创建密钥对
     * @param algorithm 算法
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static KeyPair createKeyPair(String algorithm) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
        generator.initialize(1024);
        return generator.generateKeyPair();
    }

    /**
     * 创建秘钥
     * @param algorithm 加密算法
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static SecretKey createKey(String algorithm) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator generator = KeyGenerator.getInstance(algorithm);
        return generator.generateKey();
    }

    /**
     * 存储公钥或私钥
     * @param dstFile 目标文件
     * @param key 公钥或私钥
     * @throws IOException
     */
    public static void serializeKey(String dstFile, Key key) throws IOException {
        Files.write(Base64.encode(key.getEncoded()), new File(dstFile), Charsets.UTF_8);
    }

    /**
     * DES解码
     * @param data 被解密数据
     * @param keyString 秘钥键
     * @return 原文
     * @throws Encryption.DecryptException
     */
    public static String decryptDes(String data, String keyString) throws Encryption.DecryptException {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            DESKeySpec keySpec = new DESKeySpec(keyString.getBytes(Charsets.UTF_8));
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] bytes = Base64.decode(data);
            return new String(cipher.doFinal(bytes), Charsets.UTF_8);
        } catch (Exception e) {
            throw new Encryption.DecryptException(e);
        }
    }

    /**
     * DES加密
     * @param data 被加密数据
     * @param keyString 秘钥键
     * @return 密文
     * @throws Encryption.EncryptException
     */
    public static String encryptDes(String data, String keyString) throws Encryption.EncryptException {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            DESKeySpec keySpec = new DESKeySpec(keyString.getBytes(Charsets.UTF_8));
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encode(cipher.doFinal(data.getBytes(Charsets.UTF_8)));
        } catch (Exception e) {
            throw new Encryption.EncryptException(e);
        }

    }
}
