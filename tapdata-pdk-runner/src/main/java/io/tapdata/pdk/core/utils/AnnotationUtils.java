package io.tapdata.pdk.core.utils;

import io.tapdata.pdk.apis.logger.PDKLogger;
import io.tapdata.pdk.core.reflection.ClassAnnotationHandler;
import org.reflections.Reflections;

public class AnnotationUtils {
    public static void runClassAnnotationHandlers(Reflections reflections, ClassAnnotationHandler[] handlers, String tag) {
        if(handlers != null) {
            for(ClassAnnotationHandler classAnnotationHandler : handlers) {
                if(classAnnotationHandler != null && classAnnotationHandler.watchAnnotation() != null) {
                    try {
                        classAnnotationHandler.handle(reflections.getTypesAnnotatedWith(classAnnotationHandler.watchAnnotation()));
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        PDKLogger.error(tag, "Handle class annotation {} failed, {}", classAnnotationHandler.getClass().getSimpleName(), throwable.getMessage());
                    }
                }
            }
        }
    }

}
