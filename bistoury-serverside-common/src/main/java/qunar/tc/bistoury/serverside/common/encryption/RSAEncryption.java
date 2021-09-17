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
import com.ning.http.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * @author 肖哥弹架构
 * @date 2022-09-11
 * @desc RSA 编码解码
 */
public class RSAEncryption implements Encryption {
    /**
     * 加密算法类型
     */
    private static final String ALGORITHM = "RSA";
    /**
     * RSA公钥
     */
    private static final String RSA_PUBLIC_KEY = "/rsa-public-key.pem";
    /**
     * RSA私钥
     */
    private static final String RSA_PRIVATE_KEY = "/rsa-private-key.pem";
    /**
     * 公钥组件
     */
    private PublicKey publicKey;
    /**
     * 私钥组件
     */
    private PrivateKey privateKey;

    public RSAEncryption() throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException {
        this(RSA_PUBLIC_KEY, RSA_PRIVATE_KEY);
    }

    /**
     * RSA加解密构造方法
     * @param publicKeyPath 公钥文件地址
     * @param privateKeyPath 私钥文件地址
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public RSAEncryption(String publicKeyPath, String privateKeyPath) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException {
        this.publicKey = EncryptionUtils.loadRSAPublicKey(publicKeyPath);
        this.privateKey = EncryptionUtils.loadRSAPrivateKey(privateKeyPath);

        // fail fast
        Cipher  cipher = Cipher.getInstance(ALGORITHM);//RSA加密算法
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);//加密
                cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, privateKey);//解密

    }

    /**
     *  RSA加密
     * @param source 原文
     * @return 密文
     * @throws EncryptException
     */
    @Override
    public String encrypt(String source) throws EncryptException {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = source.getBytes(Charsets.UTF_8);
            return Base64.encode(cipher.doFinal(bytes));
        } catch (Exception e) {
            throw new EncryptException(e);
        }
    }

    /**
     * RSA解密
     * @param source 密文
     * @return 原文
     * @throws DecryptException
     */
    @Override
    public String decrypt(String source) throws DecryptException {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(Base64.decode(source)), Charsets.UTF_8);
        } catch (Exception e) {
            throw new DecryptException(e);
        }
    }
}
