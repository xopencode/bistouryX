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

package qunar.tc.bistoury.proxy.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import qunar.tc.bistoury.common.JacksonSerializer;
import qunar.tc.bistoury.proxy.config.AgentInfoManager;
import qunar.tc.bistoury.serverside.bean.ApiResult;
import qunar.tc.bistoury.serverside.util.ResultHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author leix.xie
 * @author  肖哥弹架构
 * @date 2019/5/23 16:22
 * @update 2021/9/07 18:45
 * @describe Agent元数据通知刷新控制层
 */
@Controller
public class AgentMetaRefreshNotifyController {
    private static final Logger logger = LoggerFactory.getLogger(AgentMetaRefreshNotifyController.class);

    /**
     * Json List类型定义
     */
    private static final TypeReference<List<String>> TYPE_REFERENCE = new TypeReference<List<String>>(){};

    /**
     * Agent信息管理对象
     */
    @Autowired
    private AgentInfoManager agentInfoManager;

    /**
     * 获取
     * @param req  HttpServletRequest
     * @return 响应结果
     */
    @RequestMapping("/proxy/agent/metaRefresh")
    @ResponseBody
    public ApiResult agentMetaRefresh(HttpServletRequest req) {
        try {
            //将HTTP请求内容序列化为AgentId集合
            List<String> agentIds = JacksonSerializer.deSerialize(req.getInputStream(), TYPE_REFERENCE);
            //更新AgentIds列表
            agentInfoManager.updateAgentInfo(agentIds);
            return ResultHelper.success();
        } catch (Exception e) {
            logger.error("meta refresh error", e);
            return ResultHelper.fail(-1, "error");
        }

    }
}
