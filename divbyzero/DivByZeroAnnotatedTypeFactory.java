package divbyzero;

import com.sun.source.tree.*;
import java.lang.annotation.Annotation;
import org.checkerframework.common.basetype.BaseAnnotatedTypeFactory;
import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.framework.type.AnnotatedTypeFactory;
import org.checkerframework.framework.type.AnnotatedTypeMirror;

import java.util.Set;
import java.util.EnumSet;

import divbyzero.qual.*;

public class DivByZeroAnnotatedTypeFactory extends BaseAnnotatedTypeFactory {
    @Override
    protected void addComputedTypeAnnotations(Tree tree, AnnotatedTypeMirror type, boolean b) {
        if (tree instanceof LiteralTree) {
            LiteralTree literal = (LiteralTree) tree;
            if (literal.getKind() == Tree.Kind.INT_LITERAL) {
                int intValue = (Integer)literal.getValue();
                if (intValue > 0) {
                    type.addAnnotation(Positive.class);
                } else if (intValue == 0) {
                    type.addAnnotation(NonNegative.class);
                }
            }
        }
        super.addComputedTypeAnnotations(tree, type, b);
    }
    
    public DivByZeroAnnotatedTypeFactory(BaseTypeChecker c) {
        super(c);
        postInit();
    }
}
