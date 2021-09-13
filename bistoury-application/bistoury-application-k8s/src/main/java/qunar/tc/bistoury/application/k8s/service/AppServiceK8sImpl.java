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


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import qunar.tc.bistoury.application.api.AppService;
import qunar.tc.bistoury.application.api.pojo.Application;
import qunar.tc.bistoury.application.k8s.util.K8SUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhenyu.nie created on 2018 2018/10/31 13:56
 */
@Service
public class AppServiceK8sImpl implements AppService {


    @Override
    public Set<String> getApps(String userCode) {
        Set<String> set = new HashSet<>();
        List<Application> applications = K8SUtils.getAllAppOrServer(K8SUtils.APPLICATION);
        applications.forEach(application -> {
            set.add(application.getName());
        });
        return set;
    }

    @Override
    public Application getAppInfo(String appCode) {
        Set<Application> set = new HashSet<>();
        List<Application> applications = K8SUtils.getAllAppOrServer(K8SUtils.APPLICATION);
        applications.forEach(application -> {
            if (StringUtils.equals(appCode, application.getCode())) {
                set.add(application);
            }
        });
        return set.iterator().next();
    }

    @Override
    public boolean checkUserPermission(final String appCode, final String usercode) {
        return true;
    }

}
