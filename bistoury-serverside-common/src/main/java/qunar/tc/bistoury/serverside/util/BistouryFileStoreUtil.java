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

package qunar.tc.bistoury.serverside.util;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import java.io.File;
import java.io.IOException;

/**
 * @author 肖哥弹架构
 * @date 2021/09/06
 * @describe 根据{catalina.base}|{bistoury.cache}|{java.io.tmpdir}
 */
public class BistouryFileStoreUtil {

    /**
     * 获取缓存或临时存储地址函数
     */
    private static Supplier<String> store = Suppliers.memoize(() -> {
        String path = System.getProperty("bistoury.cache", null);

        if (path == null) {
            path = System.getProperty("catalina.base");
            if (path == null) path = System.getProperty("java.io.tmpdir");
            path = path + File.separator + "cache";
            System.setProperty("bistoury.cache", path);
        }

        File file = new File(path);
             file.mkdirs();

        try {
            path = file.getCanonicalPath();
        } catch (IOException e){e.printStackTrace();}

        return path;
    });

    /**
     * @return 获取缓存或临时存储地址
     */
    public static String getBistouryStore() {
        return store.get();
    }
}
