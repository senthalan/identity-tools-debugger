package org.wso2.carbon.identity.java.agent.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentConfig {

    private boolean isEnabled;
    private Map<String, InterceptorConfig> interceptors= new HashMap<>();
    private List<String> executionTimeLoggerInterceptors = new ArrayList<>();

    public boolean isEnabled() {

        return isEnabled;
    }

    void setEnabled(boolean enabled) {

        isEnabled = enabled;
    }

    public Map<String, InterceptorConfig> getInterceptors() {

        return interceptors;
    }

    public InterceptorConfig getInterceptor(String  name) {

        return interceptors.get(name);
    }

    void addInterceptor(String name, InterceptorConfig interceptor) {

        this.interceptors.put(name, interceptor);
    }

    public List<String> getExecutionTimeLoggerInterceptors() {

        return executionTimeLoggerInterceptors;
    }

    void addExecutionTimeLoggerInterceptors(String executionTimeLoggerInterceptor) {

        this.executionTimeLoggerInterceptors.add(executionTimeLoggerInterceptor);
    }
}