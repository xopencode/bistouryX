package qunar.tc.bistoury.application.k8s.util;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.credentials.AccessTokenAuthentication;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qunar.tc.bistoury.application.api.pojo.AppServer;
import qunar.tc.bistoury.application.api.pojo.Application;

import java.util.*;

public class K8SUtils {

    private static final Logger logger = LoggerFactory.getLogger(K8SUtils.class);
    private static V1PodList list;
    private static CoreV1Api api;

    public static final String APPLICATION = "application";
    public static final String APP_SERVER = "appServer";

    public static final String BASE_PATH = "https://39.102.58.133:6443";
    public static final String TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IiJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJ0ZXN0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6InRlc3QtYWRtaW4tdG9rZW4tZGRuZzgiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoidGVzdC1hZG1pbiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6Ijc4MGJmZTBhLTY5M2QtMTFlYi05MmJkLTAwMTYzZTJlODA5MCIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDp0ZXN0OnRlc3QtYWRtaW4ifQ.xHmfrJ2krzKTjdUGpnQzFBwuIlfRAFqPjbd--rYB7tSlWC6wA-QdOa_Y4_fKE916all5eBQy5Im9oiqr8rdZ9KxpNzEM6FUsRKBKne9zbWbVE8NmQvxdT8drUR95g0dUmf2G0t9j1AcGPFoRDmkqc2r8QliQ-FcIAd3vEo3RoIzkM4zJftXYcCVfJ4cdW5A4JXyHoUhzqOeNLLiQ3KO3VoM-s1JPW2XuFjMXRfHnySCIsKFtlxrce3MiraXe3jbkb6FPzcP4nI4TLlz9bu_yxB5M9K5ZreO7HtjaCCooh0-lDdCPyu6VxJlftYWyoNDkTxskXJd7bpg9-WEdC3PElw";


    static {
        ApiClient client = new ClientBuilder().setBasePath(BASE_PATH).setVerifyingSsl(false)
                .setAuthentication(new AccessTokenAuthentication(TOKEN)).build();
        Configuration.setDefaultApiClient(client);
        System.out.println(client.getClass());
        api = new CoreV1Api();
    }

    public static AppServer getAppserverByIp(String ip) {
        List<AppServer> appServers = new ArrayList<>();
        try {
            list = api.listPodForAllNamespaces(true, null, null, null, null, null, null, null, null, null);
            for (V1Pod item : list.getItems()) {
                if (StringUtils.equals(item.getMetadata().getNamespace(), "kuick-prod")) {
                    if (StringUtils.isNotBlank(item.getStatus().getPodIP()) && StringUtils.isNotBlank(item.getStatus().getHostIP()) && StringUtils.equals(ip, item.getStatus().getHostIP())) {
                        AppServer appServer = new AppServer();
                        appServer.setServerId("ServerId" + RandomUtils.nextInt());
                        appServer.setIp(item.getStatus().getHostIP());
                        appServer.setPort(80);
                        appServer.setHost(item.getSpec().getContainers().get(0).getName() + "(" + RandomUtils.nextInt() + ")");
                        appServer.setLogDir("/kuick/servers/bistoury-agent/logs/");
                        appServer.setRoom("Room" + RandomUtils.nextInt());
                        appServer.setAppCode(item.getSpec().getContainers().get(0).getName());
                        appServers.add(appServer);
                    }
                }
            }
        } catch (ApiException e) {
            logger.info("getAppserverByIp =======" + e.getMessage());
        }
        if (appServers.size() > 0) {
            return appServers.get(0);
        }
        return null;
    }

    public static List<AppServer> getAppserverByAppCode(String appCode) {
        List<AppServer> appServers = new ArrayList<>();
        List<AppServer> allApplication = getAllAppOrServer(K8SUtils.APP_SERVER);
        allApplication.forEach(appServer -> {
            if (StringUtils.isNotBlank(appServer.getIp()) && StringUtils.isNotBlank(appServer.getHost()) && StringUtils.equals(appCode, appServer.getAppCode())) {
                appServers.add(appServer);
            }
        });
        return appServers;
    }

    public static <T> List<T> getAllAppOrServer(String beanName) {
        List<T> beanList = new ArrayList<>();
        Set<String> set = new HashSet<>();
        try {
            list = api.listPodForAllNamespaces(true, null, null, null, null, null, null, null, null, null);
            for (V1Pod item : list.getItems()) {
                if (StringUtils.equals(item.getMetadata().getNamespace(), "kuick-prod")) {
                    set.add(item.getSpec().getContainers().get(0).getName());
                    addBean(beanList, item, beanName);
                }
            }
        } catch (ApiException a) {
            logger.info("a.getMessage()========" + a.getMessage());
        }
        return beanList;
    }

    private static <T> void addBean(List<T> beanList, V1Pod item, String beanName) {
        if (StringUtils.equals(beanName, APPLICATION)) {
            Application application = getApplication(item);
            beanList.add((T) application);
        } else if (StringUtils.equals(beanName, APP_SERVER)) {
            AppServer appServer1 = getAppServer(item);
            beanList.add((T) appServer1);
        }

    }

    @NotNull
    private static Application getApplication(V1Pod item) {
        Application application = new Application();
        application.setId(RandomUtils.nextInt());
        application.setCode(item.getSpec().getContainers().get(0).getName());
        application.setName(item.getSpec().getContainers().get(0).getName());
        application.setStatus(1);
        application.setCreateTime(new Date());
        application.setCreator("xdw");
        application.setGroupCode(item.getSpec().getNodeName());
        return application;
    }

    @NotNull
    private static AppServer getAppServer(V1Pod item) {
        AppServer appServer = new AppServer();
        appServer.setServerId("ServerId" + RandomUtils.nextInt());
        appServer.setIp(item.getStatus().getPodIP());
        appServer.setPort(80);
        appServer.setHost(item.getSpec().getContainers().get(0).getName());
        appServer.setLogDir("/kuick/servers/bistoury-agent/logs/");
        appServer.setRoom("Room" + RandomUtils.nextInt());
        appServer.setAppCode(item.getSpec().getContainers().get(0).getName());
        return appServer;
    }
}
