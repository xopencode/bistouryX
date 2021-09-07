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

package qunar.tc.bistoury.agent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Strings;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.bistoury.common.AsyncHttpClientHolder;
import qunar.tc.bistoury.common.JacksonSerializer;
import qunar.tc.bistoury.common.JsonResult;

/**
 * @author zhenyu.nie created on 2018 2018/10/25 17:08
 * @author xiaoailiang update on 2021 2021/09/03 19:00
 * Agent配置类
 */
class Configs {

    private static final Logger logger = LoggerFactory.getLogger(Configs.class);
    //ProxyConfig类型的Json结构体
    private static final TypeReference<JsonResult<ProxyConfig>> PROXY_REFERENCE = new TypeReference<JsonResult<ProxyConfig>>() {};
    //Proxy请求资源地址
    private static final String PROXY_URI = "/proxy/config/foragent";

    /**
     * 获取远程连接的ProxyConfig类
     * @return
     */
    public static ProxyConfig getProxyConfig() {
        String bistouryProxyHost = System.getProperty("bistoury.proxy.host");
        if (Strings.isNullOrEmpty(bistouryProxyHost)) {
            throw new RuntimeException("system property [bistoury.proxy.host] cannot be null or empty");
        }
        return getProxyConfig(bistouryProxyHost);
    }

    /**
     * 远程请求获取需要远程连接的ProxyConfig类
     * @param bistouryHost
     * @return
     */
    private static ProxyConfig getProxyConfig(String bistouryHost) {
        String url = "http://" + bistouryHost + PROXY_URI;
        try {
            AsyncHttpClient client = AsyncHttpClientHolder.getInstance();
            AsyncHttpClient.BoundRequestBuilder builder = client.prepareGet(url);
            builder.setHeader("content-type", "application/json;charset=utf-8");
            Response response = client.executeRequest(builder.build()).get();
            if (response.getStatusCode() != 200) {
                logger.error("get proxy config error, http code [{}], url [{}]", response.getStatusCode(), url);
                return null;
            }

            JsonResult<ProxyConfig> result = JacksonSerializer.deSerialize(response.getResponseBody("utf8"), PROXY_REFERENCE);
            if (!result.isOK()) {
                logger.error("get proxy config error, status code [{}], url [{}]", result.getStatus(), url);
                return null;
            }

            return result.getData();
        } catch (Throwable e) {
            logger.error("get proxy config error, url [{}]", url, e);
            return null;
        }
    }
}