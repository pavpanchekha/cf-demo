package divbyzero.qual;

import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.RelevantJavaTypes;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@DefaultQualifierInHierarchy
@SubtypeOf({})
@RelevantJavaTypes({Integer.class})
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface Top { }
