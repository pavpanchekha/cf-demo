package divbyzero;

import org.checkerframework.common.basetype.BaseTypeChecker;
import org.checkerframework.common.basetype.BaseTypeVisitor;
import org.checkerframework.framework.source.Result;

import javax.lang.model.type.TypeKind;
import java.lang.annotation.Annotation;
import com.sun.source.tree.*;

import java.util.Set;
import java.util.EnumSet;

import divbyzero.qual.*;

public class DivByZeroVisitor extends BaseTypeVisitor<DivByZeroAnnotatedTypeFactory> {

    /** Set of operators we care about */
    private static final Set<Tree.Kind> DIVISION_OPERATORS = EnumSet.of(
        Tree.Kind.DIVIDE,
        Tree.Kind.DIVIDE_ASSIGNMENT,
        Tree.Kind.REMAINDER,
        Tree.Kind.REMAINDER_ASSIGNMENT);

    private String validDivision(Tree.Kind operator, Tree rhs) {
        if (DIVISION_OPERATORS.contains(operator)) {
            if (!atypeFactory.getAnnotatedType(rhs).hasAnnotation(Positive.class)) {
                return "Possible division by zero";
            }
        }
        return null;
    }

    // ========================================================================
    // Checker Framework plumbing

    public DivByZeroVisitor(BaseTypeChecker c) {
        super(c);
    }

    private boolean isInt(Tree node) {
        return atypeFactory.getAnnotatedType(node).getKind().equals(TypeKind.INT);
    }

    @Override
    public Void visitBinary(BinaryTree node, Void p) {
        if (isInt(node)) {
            String err = validDivision(node.getKind(), node.getRightOperand());
            if (err != null) {
                checker.report(Result.failure(err), node);
            }
        }
        return super.visitBinary(node, p);
    }

    @Override
    public Void visitCompoundAssignment(CompoundAssignmentTree node, Void p) {
        if (isInt(node.getExpression())) {
            String err = validDivision(node.getKind(), node.getExpression());
            if (err != null) {
                checker.report(Result.failure(err), node);
            }
        }
        return super.visitCompoundAssignment(node, p);
    }
}
