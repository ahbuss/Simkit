package simkit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Single-Value annotation type to mark any (public) method
 * as a Simkit event-completion method.
 * <p>
 * The value of the annotation is the name of the SimEvent to be
 * associated with the method being annotated.
 * 
 * @author Kirk Stork, The MOVES Institute, NPS
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

public @interface SimEventMethod {
    String value();
}
