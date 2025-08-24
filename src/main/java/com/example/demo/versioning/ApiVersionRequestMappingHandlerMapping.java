package com.example.demo.versioning;

import com.example.demo.annotation.ApiVersion;
import com.example.demo.property.ApiVersioningProperties;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;

public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private final ApiVersioningProperties properties;
    private final Map<String, Method> registeredVersionedPaths = new HashMap<>();

    public ApiVersionRequestMappingHandlerMapping(ApiVersioningProperties properties) {
        this.properties = properties;
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo mappingInfo = super.getMappingForMethod(method, handlerType);
        if (mappingInfo == null) return null;

        // Find @ApiVersion on method or class
        ApiVersion apiVersion = AnnotatedElementUtils.findMergedAnnotation(method, ApiVersion.class);
        if (apiVersion == null) {
            apiVersion = AnnotatedElementUtils.findMergedAnnotation(handlerType, ApiVersion.class);
        }
        if (apiVersion == null) {
            return mappingInfo; // No versioning
        }

        // Decide strategy (annotation overrides global config)
        ApiVersion.Strategy strategy =
                (apiVersion.strategy() == ApiVersion.Strategy.DEFAULT)
                        ? mapStrategy(properties.getStrategy())
                        : apiVersion.strategy();

        // === PATH STRATEGY ===
        if (strategy == ApiVersion.Strategy.PATH) {
            Set<String> versionedPaths = new LinkedHashSet<>();

            Collection<String> originalPaths = new ArrayList<>();
            if (mappingInfo.getPatternsCondition() != null) {
                originalPaths.addAll(mappingInfo.getPatternsCondition().getPatterns());
            } else if (mappingInfo.getPathPatternsCondition() != null) {
                mappingInfo.getPathPatternsCondition().getPatterns()
                        .forEach(p -> originalPaths.add(p.getPatternString()));
            }

            for (String originalPath : originalPaths) {
                for (int version : apiVersion.value()) {
                    String versionPath = properties.getBasePath() + "/v" + version + originalPath;
                    versionedPaths.add(versionPath);
                    checkForDuplicateMappings(versionPath, method, mappingInfo);
                }
            }

            return RequestMappingInfo
                    .paths(versionedPaths.toArray(new String[0]))
                    .methods(mappingInfo.getMethodsCondition().getMethods().toArray(new RequestMethod[0]))
                    .params(mappingInfo.getParamsCondition().getExpressions().toArray(new String[0]))
                    .headers(mappingInfo.getHeadersCondition().getExpressions().toArray(new String[0]))
                    .consumes(mappingInfo.getConsumesCondition().getConsumableMediaTypes().toArray(new String[0]))
                    .produces(mappingInfo.getProducesCondition().getProducibleMediaTypes().toArray(new String[0]))
                    .build();
        }

        else if (strategy == ApiVersion.Strategy.HEADER) {
            RequestMappingInfo combined = null;

            for (int version : Arrays.stream(apiVersion.value()).distinct().toArray()) {
                RequestMappingInfo versioned = RequestMappingInfo
                        .paths(getPaths(mappingInfo).toArray(new String[0]))  // keep original path as-is
                        .methods(mappingInfo.getMethodsCondition().getMethods().toArray(new RequestMethod[0]))
                        .headers(properties.getHeaderName() + "=" + version)   // add version header only
                        .build();

                combined = (combined == null) ? versioned : combined.combine(versioned);
            }

            return combined;
        }


// === QUERY STRATEGY ===
        else if (strategy == ApiVersion.Strategy.QUERY) {
            Set<Integer> versions = new LinkedHashSet<>();
            for (int version : apiVersion.value()) {
                if (!versions.contains(version)) {
                    versions.add(version);
                }
            }

            RequestMappingInfo combined = null;
            for (int version : versions) {
                RequestMappingInfo versioned = RequestMappingInfo
                        .paths(getPaths(mappingInfo).toArray(new String[0]))
                        .methods(mappingInfo.getMethodsCondition().getMethods().toArray(new RequestMethod[0]))
                        .params(properties.getQueryParam() + "=" + version)
                        .build();

                combined = (combined == null) ? versioned : combined.combine(versioned);
            }

            return combined;
        }

        return mappingInfo;
    }

    private List<String> getPaths(RequestMappingInfo mappingInfo) {
        List<String> paths = new ArrayList<>();
        if (mappingInfo.getPatternsCondition() != null) {
            paths.addAll(mappingInfo.getPatternsCondition().getPatterns());
        } else if (mappingInfo.getPathPatternsCondition() != null) {
            mappingInfo.getPathPatternsCondition().getPatterns()
                    .forEach(p -> paths.add(p.getPatternString()));
        }
        return paths;
    }

    private ApiVersion.Strategy mapStrategy(ApiVersioningProperties.Strategy strategy) {
        return switch (strategy) {
            case PATH -> ApiVersion.Strategy.PATH;
            case HEADER -> ApiVersion.Strategy.HEADER;
            case QUERY -> ApiVersion.Strategy.QUERY;
        };
    }

    private void checkForDuplicateMappings(String path, Method method, RequestMappingInfo original) {
        String key = path + ":" + original.getMethodsCondition();
        if (registeredVersionedPaths.containsKey(key)) {
            Method existing = registeredVersionedPaths.get(key);
            throw new IllegalStateException("❌ Duplicate mapping: " + path +
                    " already mapped to " + existing + " and now " + method);
        }
        registeredVersionedPaths.put(key, method);
    }

    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        List<String> paths = getPaths(mapping);

        System.out.println("✅ Registered API: "
                + paths
                + " -> " + method.getDeclaringClass().getSimpleName() + "." + method.getName());

        super.registerHandlerMethod(handler, method, mapping);
    }

}
