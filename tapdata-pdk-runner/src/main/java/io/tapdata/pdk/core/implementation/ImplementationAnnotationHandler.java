package io.tapdata.pdk.core.implementation;

import io.tapdata.pdk.apis.logger.PDKLogger;
import io.tapdata.entity.annotations.Implementation;
import io.tapdata.pdk.core.error.CoreException;
import io.tapdata.pdk.core.reflection.ClassAnnotationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ImplementationAnnotationHandler extends ClassAnnotationHandler {
    private static final String TAG = ImplementationAnnotationHandler.class.getSimpleName();

    private Map<Class<?>, ImplClasses> interfaceImplementationClassMap = new ConcurrentHashMap<>();
    private Map<Class<?>, ImplClasses> newInterfaceImplementationClassMap = new ConcurrentHashMap<>();

    @Override
    public void handle(Set<Class<?>> classes) throws CoreException {
        if(classes != null) {
            newInterfaceImplementationClassMap = new ConcurrentHashMap<>();
            PDKLogger.info(TAG, "--------------Implementation Classes Start-------------");
            for(Class<?> clazz : classes) {
                Implementation implementation = clazz.getAnnotation(Implementation.class);
                if(implementation != null) {
                    Class<?> interfaceClass = implementation.value();
                    int buildNumber = implementation.buildNumber();
                    String type = implementation.type();

                    //Check class can be initialized for non-args constructor
                    String canNotInitialized = null;
                    try {
                        Constructor<?> constructor = clazz.getConstructor();
                        if (!Modifier.isPublic(constructor.getModifiers())) {
                            canNotInitialized = "Constructor is not public";
                        }
                    } catch (Throwable e) {
                        canNotInitialized = e.getMessage();
                    }
                    if(canNotInitialized != null) {
                        PDKLogger.error(TAG, "Implementation {} don't have non-args public constructor for type {} buildNumber {}, will be ignored, message {}", clazz, type, buildNumber, canNotInitialized);
                        continue;
                    }
                    if(!interfaceClass.isAssignableFrom(clazz)) {
                        PDKLogger.error(TAG, "Implementation {} don't implement interface {} for type {} buildNumber {}, will be ignored", clazz, interfaceClass, type, buildNumber);
                        continue;
                    }

                    ImplClasses implClasses = newInterfaceImplementationClassMap.get(interfaceClass);
                    if(implClasses == null) {
                        implClasses = new ImplClasses();
                        Map<String, ImplClass> typeClassHolderMap = new ConcurrentHashMap<>();
                        ImplClass implClass = new ImplClass();
                        implClass.setBuildNumber(buildNumber);
                        implClass.setClazz(clazz);
                        typeClassHolderMap.put(type, implClass);
                        implClasses.setTypeClassHolderMap(typeClassHolderMap);
                        newInterfaceImplementationClassMap.put(interfaceClass, implClasses);
                        PDKLogger.info(TAG, "(New Interface) Implementation {} buildNumber {} type {} for interface {} will be applied", clazz, buildNumber, type, interfaceClass);
                    } else {
                        ImplClass implClass = implClasses.getTypeClassHolderMap().get(type);
                        if(implClass == null) {
                            implClass = new ImplClass();
                            implClass.setClazz(clazz);
                            implClass.setBuildNumber(buildNumber);
                            implClasses.getTypeClassHolderMap().put(type, implClass);
                            PDKLogger.info(TAG, "(New ClassHolder) Implementation {} buildNumber {} type {} for interface {} will be applied", clazz, buildNumber, type, interfaceClass);
                        } else {
                            if(buildNumber > implClass.getBuildNumber()) {
                                implClass.setBuildNumber(buildNumber);
                                implClass.setClazz(clazz);
                                PDKLogger.info(TAG, "Implementation {} buildNumber {} type {} for interface {} will be applied, as buildNumber is bigger than current {} implementation class {}", clazz, buildNumber, type, interfaceClass, implClass.getBuildNumber(), implClass.getClazz());
                            } else {
                                PDKLogger.warn(TAG, "Implementation {} buildNumber {} type {} for interface {} will be ignored, as buildNumber is smaller(or equal) than current {} implementation class {}", clazz, buildNumber, type, interfaceClass, implClass.getBuildNumber(), implClass.getClazz());
                            }
                        }
                    }
                }
            }
            PDKLogger.info(TAG, "--------------Implementation Classes End-------------");
        }
    }

    @Override
    public Class<? extends Annotation> watchAnnotation() {
        return Implementation.class;
    }

    public void apply() {
        if(newInterfaceImplementationClassMap != null) {
            interfaceImplementationClassMap = newInterfaceImplementationClassMap;
            newInterfaceImplementationClassMap = null;
        }
    }

    public ImplClasses getImplementationClassHolder(Class<?> interfaceClass) {
        if(interfaceImplementationClassMap == null)
            return null;
        return interfaceImplementationClassMap.get(interfaceClass);
    }

    public ImplClass getClassHolder(Class<?> interfaceClass, String type) {
        ImplClasses implClasses = getImplementationClassHolder(interfaceClass);
        if(implClasses == null)
            return null;
        return implClasses.getImplementationClass(type);
    }
}
