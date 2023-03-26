package app.common.event.spring.bpp;

import app.common.event.GenericEvent;
import app.common.event.publisher.EventPublisher;
import app.common.event.spring.annotation.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class EventHandlerRegisterBeanPostProcessor implements BeanPostProcessor {
    private final Map<String, List<Method>> handlers = new HashMap<>();
    private final EventPublisher publisher;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        List<Method> handleMethods = Stream.of(bean.getClass().getMethods())
                .filter(x -> x.isAnnotationPresent(EventHandler.class))
                .toList();
        if (!handleMethods.isEmpty()) {
            handlers.put(beanName, handleMethods);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (handlers.containsKey(beanName)) {
            handlers.get(beanName).forEach(it -> addWithInvoke(bean, it));
        }
        return bean;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addWithInvoke(Object object, Method handleMethod) {
        Class[] classes = handleMethod.getParameterTypes();
        if (classes.length != 1 || !GenericEvent.class.isAssignableFrom(classes[0])) {
            throw new RuntimeException("Event handlers must have one GenericEvent argument");
        }
        publisher.registerEventHandler(classes[0], a -> invoke(handleMethod, object, a));
    }

    @SneakyThrows
    private void invoke(Method handleMethod, Object object, Object param) {
        handleMethod.invoke(object, param);
    }
}
