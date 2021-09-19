
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

package qunar.tc.bistoury.serverside.configuration.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.bistoury.serverside.configuration.DynamicConfig;
import qunar.tc.bistoury.serverside.configuration.Listener;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author 肖哥弹架构
 * @date 2022-09-10
 * @desc 本地动态配置实体,并具备动态配置能力
 */
public class LocalDynamicConfig implements DynamicConfig<LocalDynamicConfig> {
    private static final Logger LOG = LoggerFactory.getLogger(LocalDynamicConfig.class);
    /**
     * 本地配置名
     */
    private final String name;
    /**
     * 本地配置变更监听器策略列表
     */
    private final CopyOnWriteArrayList<Listener> listeners;
    /**
     * 本地配置文件
     */
    private volatile File file;
    /**
     * 是否已加载
     */
    private volatile boolean loaded = false;
    /**
     * 配置值容器
     */
    private volatile Map<String, String> config;
    /**
     * 配置文件目录地址
     */
    private final String confDir;
    /**
     * 配置文件不存在则抛出异常
     */
    private final boolean failOnNotExist;

    LocalDynamicConfig(String name, boolean failOnNotExist) {
        this.failOnNotExist = failOnNotExist;
        this.name = name;
        this.listeners = new CopyOnWriteArrayList<>();
        this.config = new HashMap<>();
        //获取配置目录地址
        this.confDir = System.getProperty("bistoury.conf");
        //获取指定名配置文件
        this.file = getFileByName(name);
        //配置文件不存在则抛出异常
        if (failOnNotExist && (file == null || !file.exists())) {
            throw new RuntimeException("cannot find config file " + name);
        }
    }

    /**
     * 根据名字获取配置文件
     * @param name 配置文件名
     * @return 配置文件对象
     */
    private File getFileByName(final String name) {
        if (confDir != null && confDir.length() > 0) {
            return new File(confDir, name);
        }
        try {
            final URL res = this.getClass().getClassLoader().getResource(name);
            if (res == null) {
                return null;
            }
            return Paths.get(res.toURI()).toFile();
        } catch (URISyntaxException e) {
            throw new RuntimeException("load config file failed", e);
        }
    }

    /**
     *  配置文件最后修改时间
     * @return 最后修改时间
     */
    long getLastModified() {
        if (file == null) {
            file = getFileByName(name);
        }

        if (file == null) {
            return 0;
        } else {
            return file.lastModified();
        }
    }

    /**
     * 配置文件发生修改则加载最新并执行监听事件
     */
    synchronized void onConfigModified() {
        if (file == null) {
            return;
        }
        LOG.info("config {} has been modified", name);
        //加载配置
        loadConfig();
        //执行监听器
        executeListeners();
        //标记已加载
        loaded = true;
    }

    /**
     * 加载配置
     */
    private void loadConfig() {
        try {
            final Properties p = new Properties();
            try (Reader reader = new BufferedReader(new FileReader(file))) {
                p.load(reader);
            }
            final Map<String, String> map = new LinkedHashMap<>(p.size());
            for (String key : p.stringPropertyNames()) {
                map.put(key, tryTrim(p.getProperty(key)));
            }
            //加载配置信息并存储缓存中
            config = Collections.unmodifiableMap(map);
        } catch (IOException e) {
            if (e instanceof FileNotFoundException && !failOnNotExist) {
                LOG.warn("load local config failed. config: {}, file not found", file.getAbsolutePath());
            } else {
                LOG.error("load local config failed. config: {}", file.getAbsolutePath(), e);
            }
        }
    }

    /**
     * 配置值删除空表值
     * @param data 配置值
     * @return 无空格配置值
     */
    private String tryTrim(String data) {
        if (data == null) {
            return null;
        } else {
            return data.trim();
        }
    }

    /**
     * 执行所有的配置监听器
     */
    private void executeListeners() {
        for (Listener listener : listeners) {
            executeListener(listener);
        }
    }

    /**
     * 添加配置监听器
     * @param listener 配置监听对象
     */
    @Override
    public void addListener(Listener<LocalDynamicConfig> listener) {
        //如果配置已加载则执行配置监听对象
        if (loaded) {
            executeListener(listener);
        }
        //加入监听容器
        listeners.add(listener);
    }

    /**
     * 执行监听器
     * @param listener 监听器对象
     */
    private void executeListener(Listener<LocalDynamicConfig> listener) {
        try {
            //执行监听器
            listener.onLoad(this);
        } catch (Throwable e) {
            LOG.error("trigger config listener failed. config: {}", name, e);
        }
    }

    /**
     * 获取字符串配置值
     * @param name 键
     * @return 配置值
     */
    @Override
    public String getString(String name) {
        return getValueWithCheck(name);
    }

    /**
     * 获取字符串配置值
     * @param name  键
     * @param defaultValue 默认值
     * @return 配置值
     */
    @Override
    public String getString(String name, String defaultValue) {
        String value = getValue(name);
        if (isBlank(value))
            return defaultValue;
        return value;
    }

    /**
     * 获取整形配置值
     * @param name 键
     * @return 配置值
     */
    @Override
    public int getInt(String name) {
        return Integer.valueOf(getValueWithCheck(name));
    }

    /**
     * 获取整形配置值
     * @param name 键
     * @param defaultValue 默认值
     * @return 配置值
     */
    @Override
    public int getInt(String name, int defaultValue) {
        String value = getValue(name);
        if (isBlank(value))
            return defaultValue;
        return Integer.valueOf(value);
    }
    /**
     * 获取长整形配置值
     * @param name 键
     * @return 配置值
     */
    @Override
    public long getLong(String name) {
        return Long.valueOf(getValueWithCheck(name));
    }
    /**
     * 获取长整形配置值
     * @param name 键
     * @param defaultValue 默认值
     * @return 配置值
     */
    @Override
    public long getLong(String name, long defaultValue) {
        String value = getValue(name);
        if (isBlank(value))
            return defaultValue;
        return Long.valueOf(value);
    }
    /**
     * 获取双精度配置值
     * @param name 键
     * @return 配置值
     */
    @Override
    public double getDouble(final String name) {
        return Double.valueOf(getValueWithCheck(name));
    }
    /**
     * 获取双精度配置值
     * @param name 键
     * @param defaultValue 默认值
     * @return 配置值
     */
    @Override
    public double getDouble(final String name, final double defaultValue) {
        String value = getValue(name);
        if (isBlank(value))
            return defaultValue;
        return Double.valueOf(value);
    }
    /**
     * 获取布尔配置值
     * @param name 键
     * @param defaultValue 默认值
     * @return 配置值
     */
    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        String value = getValue(name);
        if (isBlank(value))
            return defaultValue;
        return Boolean.valueOf(value);
    }

    /**
     * 检测配置值是否为空
     * @param name 配置键
     * @return 配置值
     */
    private String getValueWithCheck(String name) {
        String value = getValue(name);
        if (isBlank(value)) {
            throw new RuntimeException("配置项: " + name + " 值为空");
        } else {
            return value;
        }
    }

    /**
     * 获取配置值
     * @param name 键
     * @return 配置值
     */
    private String getValue(String name) {
        return config.get(name);
    }

    /**
     * 判断配置值是否为空
     * @param s 配置值
     * @return 是否为空
     */
    private boolean isBlank(final String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }

        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否存在此配置键
     * @param name 键
     * @return 是否存在
     */
    @Override
    public boolean exist(String name) {
        return config.containsKey(name);
    }

    /**
     * 将所有配置值转换成Map
     * @return
     */
    @Override
    public Map<String, String> asMap() {
        return new HashMap<>(config);
    }

    @Override
    public String toString() {
        return "LocalDynamicConfig{" +
                "name='" + name + '\'' +
                '}';
    }
}
