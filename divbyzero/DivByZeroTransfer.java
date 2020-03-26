package divbyzero;

import org.checkerframework.framework.type.QualifierHierarchy;
import org.checkerframework.framework.flow.CFTransfer;
import org.checkerframework.framework.flow.CFValue;
import org.checkerframework.framework.flow.CFStore;
import org.checkerframework.framework.flow.CFAnalysis;
import org.checkerframework.dataflow.cfg.node.*;
import org.checkerframework.dataflow.analysis.TransferInput;
import org.checkerframework.dataflow.analysis.TransferResult;
import org.checkerframework.dataflow.analysis.RegularTransferResult;
import org.checkerframework.dataflow.analysis.ConditionalTransferResult;
import org.checkerframework.dataflow.analysis.FlowExpressions;
import org.checkerframework.javacutil.AnnotationUtils;
import org.checkerframework.javacutil.AnnotationBuilder;
import javax.lang.model.element.AnnotationMirror;
import java.lang.annotation.Annotation;

import java.util.Set;

import divbyzero.qual.*;
import divbyzero.Lattice;

public class DivByZeroTransfer extends CFTransfer {

    // ========================================================================
    // Useful helpers

    /** Compute the least-upper-bound of two points in the lattice */
    private AnnotationMirror lub(AnnotationMirror x, AnnotationMirror y) {
        return analysis.getTypeFactory().getQualifierHierarchy().leastUpperBound(x, y);
    }

    // ========================================================================
    // Checker Framework plumbing

    public DivByZeroTransfer(CFAnalysis analysis) {
        super(analysis);
    }
    
    private AnnotationMirror getAnnotation(Node n) {
        QualifierHierarchy hierarchy = analysis.getTypeFactory().getQualifierHierarchy();
        Set<AnnotationMirror> set = analysis.getValue(n).getAnnotations();
        if (set.size() == 0) {
            return null;
        }
        Set<? extends AnnotationMirror> tops = hierarchy.getTopAnnotations();
        return hierarchy.findAnnotationInSameHierarchy(set, tops.iterator().next());
    }

    private Lattice getLattice(Node n) {
        AnnotationMirror a = getAnnotation(n);
        if (a == null) {
            return Lattice.unknown;
        } else if (AnnotationUtils.areSame(a, export(Lattice.positive))) {
            return Lattice.positive;
        } else if (AnnotationUtils.areSame(a, export(Lattice.nonnegative))) {
            return Lattice.nonnegative;
        } else {
            return Lattice.unknown;
        }
    }
    
    public AnnotationMirror export(Lattice l) {
        Class<? extends Annotation> c;
        switch (l) {
        case positive:
            c = Positive.class;
            break;
        case nonnegative:
            c = NonNegative.class;
            break;
        default:
            c = Top.class;
            break;
        }
        return AnnotationBuilder.fromClass(analysis.getTypeFactory().getElementUtils(), c);
    }
   
    private TransferResult<CFValue, CFStore> refine(BinaryOperationNode n, Lattice.Pair resultTrue, Lattice.Pair resultFalse, TransferResult<CFValue, CFStore> out) {
        AnnotationMirror l = getAnnotation(n.getLeftOperand());
        AnnotationMirror r = getAnnotation(n.getRightOperand());

        AnnotationMirror tl = export(resultTrue.lhs);
        AnnotationMirror tr = export(resultTrue.rhs);
        AnnotationMirror fl = export(resultFalse.lhs);
        AnnotationMirror fr = export(resultFalse.rhs);

        CFStore thenStore = out.getThenStore().copy();
        thenStore.insertValue(
            FlowExpressions.internalReprOf(analysis.getTypeFactory(), n.getLeftOperand()),
            lub(l, tl));

        thenStore.insertValue(
            FlowExpressions.internalReprOf(analysis.getTypeFactory(), n.getRightOperand()),
            lub(r, tr));

        CFStore elseStore = out.getElseStore().copy();
        elseStore.insertValue(
            FlowExpressions.internalReprOf(analysis.getTypeFactory(), n.getLeftOperand()),
            lub(l, fl));

        elseStore.insertValue(
            FlowExpressions.internalReprOf(analysis.getTypeFactory(), n.getRightOperand()),
            lub(r, fr));

        return new ConditionalTransferResult<>(out.getResultValue(), thenStore, elseStore);
    }
    
    private TransferResult<CFValue, CFStore> transfer(BinaryOperationNode n, Lattice result, TransferResult<CFValue, CFStore> out) {
        CFValue newResultValue = analysis.createSingleAnnotationValue(export(result), out.getResultValue().getUnderlyingType());
        return new RegularTransferResult<>(newResultValue, out.getRegularStore());
    }

    @Override
    public TransferResult<CFValue, CFStore> visitGreaterThan(GreaterThanNode n, TransferInput<CFValue, CFStore> p) {
        Lattice l = getLattice(n.getLeftOperand());
        Lattice r = getLattice(n.getRightOperand());
        Lattice.Pair outTrue = Lattice.refineGt(true, l, r);
        Lattice.Pair outFalse = Lattice.refineGt(false, l, r);
        return refine(n, outTrue, outFalse, super.visitGreaterThan(n, p));
    }

    @Override
    public TransferResult<CFValue, CFStore> visitNumericalAddition(NumericalAdditionNode n, TransferInput<CFValue, CFStore> p) {
        Lattice l = getLattice(n.getLeftOperand());
        Lattice r = getLattice(n.getRightOperand());
        Lattice result = Lattice.transferPlus(l, r);
        return transfer(n, result, super.visitNumericalAddition(n, p));
    }

}
