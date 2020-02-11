package com.expense.app.common.cqrs.command;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class HandlerFactory {
	
	private ConfigurableListableBeanFactory beanFactory;
	
	private Map<Class<?>, String> handlers = new HashMap<>();
	
	public HandlerFactory(ConfigurableListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@PostConstruct
	public void init() {
		String[] handlerBeanNames = beanFactory.getBeanNamesForType(CommandHandler.class);
		for (String handlerBeanName : handlerBeanNames) {
			BeanDefinition handlerBeanDefinition = beanFactory.getBeanDefinition(handlerBeanName);
			try {
				Class<?> handlerClass = Class.forName(handlerBeanDefinition.getBeanClassName());
				Type[] genericInterfaces = handlerClass.getGenericInterfaces();
				for (Type genericInterface : genericInterfaces) {
					if (genericInterface instanceof ParameterizedType) {
						ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
						Class<?> commandClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
						handlers.put(commandClass, handlerBeanName);
					}
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Command> CommandHandler<T> getHandler(T command) {
		String beanName = handlers.get(command.getClass());
		if (beanName == null) {
			throw new RuntimeException("Cannot find handler for command class: " + command.getClass());
		}
		CommandHandler<T> handler = beanFactory.getBean(beanName, CommandHandler.class);
		return handler;
	}
}
