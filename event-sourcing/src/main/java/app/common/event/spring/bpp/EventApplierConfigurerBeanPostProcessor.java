package app.common.event.spring.bpp;

import app.common.event.GenericEvent;
import app.common.event.applier.ApplierContainer;
import app.common.event.applier.ApplierFunction;
import app.common.event.spring.annotation.Applier;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class EventApplierConfigurerBeanPostProcessor implements BeanPostProcessor {
    private final Map<String, List<Method>> appliers = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplierContainer) {
            List<Method> applierMethods = Stream.of(bean.getClass().getMethods())
                    .filter(x -> x.isAnnotationPresent(Applier.class))
                    .collect(Collectors.toList());
            appliers.put(beanName, applierMethods);
        }
        return bean;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (appliers.containsKey(beanName)) {
            if (!appliers.get(beanName).isEmpty()) {
                for (Method method : appliers.get(beanName)) {
                    ((ApplierContainer<?>) bean).register(
                            (Class<? extends GenericEvent>) method.getParameterTypes()[0],
                            toApplier(bean, method)
                    );
                }
            }
        }
        return bean;
    }

    @SuppressWarnings("rawtypes")
    private ApplierFunction toApplier(Object object, Method applyMethod) {
        return (event, obj) -> invokeApplierFunction(object, applyMethod, obj, event);
    }

    @SneakyThrows
    private Object invokeApplierFunction(
            Object bean,
            Method applyMethod,
            Object processingObject,
            GenericEvent event
    ) {
        return applyMethod.invoke(bean, event, processingObject);
    }
}
