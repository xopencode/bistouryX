package qunar.tc.bistoury.application.k8s.service;

import org.springframework.stereotype.Service;
import qunar.tc.bistoury.application.api.ApplicationService;
import qunar.tc.bistoury.application.api.pojo.Application;
import qunar.tc.bistoury.application.k8s.util.K8SUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xkrivzooh
 * @since 2019/8/14
 */
@Service
public class ApplicationServiceK8sImpl implements ApplicationService {

    @Override
    public List<Application> getAllApplications() {
        return K8SUtils.getAllAppOrServer(K8SUtils.APPLICATION);
    }

    @Override
    public List<Application> getAllApplications(String userCode) {
        return K8SUtils.getAllAppOrServer(K8SUtils.APPLICATION);
    }

    @Override
    public List<String> getAppOwner(String appCode) {
        return new ArrayList<>();
    }

    @Override

    public int save(Application application, String loginUser, boolean admin) {
        return 0;
    }

}
