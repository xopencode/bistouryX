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

package qunar.tc.bistoury.application.k8s.service;


import org.springframework.stereotype.Service;
import qunar.tc.bistoury.application.api.AppServerService;
import qunar.tc.bistoury.application.api.pojo.AppServer;
import qunar.tc.bistoury.application.k8s.util.K8SUtils;

import java.util.List;

/**
 * @author leix.xie
 * @date 2019/7/2 15:14
 * @describe
 */
@Service
public class AppServerServiceK8sImpl implements AppServerService {

    @Override
    public List<AppServer> getAppServerByAppCode(final String appCode) {
        return K8SUtils.getAppserverByAppCode(appCode);
    }

    @Override
    public int changeAutoJMapHistoEnable(final String serverId, final boolean enable, String loginUser) {
        return 0;
    }

    @Override
    public int changeAutoJStackEnable(final String serverId, final boolean enable, String loginUser) {
        return 0;
    }

    @Override
    public int deleteAppServerByServerId(final String serverId, String loginUser) {
        return 0;
    }

    @Override
    public int saveAppServer(AppServer appServer, String loginUser) {
        return 0;
    }

    @Override
    public AppServer getAppServerByIp(String ip) {
        return K8SUtils.getAppserverByIp(ip);
    }
}
