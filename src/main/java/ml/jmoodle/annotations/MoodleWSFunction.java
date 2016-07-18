package ml.jmoodle.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(value = { ElementType.TYPE })
public @interface MoodleWSFunction {
	String name();
}
