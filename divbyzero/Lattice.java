package divbyzero;

import java.lang.annotation.Annotation;
import divbyzero.qual.*;

public enum Lattice {
    unknown (Top.class),
    nonnegative (NonNegative.class),
    positive (Positive.class);

    public final Class<? extends Annotation> cls;
    private Lattice(Class<? extends Annotation> cls) {
        this.cls = cls;
    }

    public static Lattice transferPlus(Lattice lhs, Lattice rhs) {
        if ((lhs == nonnegative || lhs == positive) && (rhs == nonnegative || rhs == positive)) {
            if (lhs == positive || rhs == positive) {
                return positive;
            } else {
                return nonnegative;
            }
        } else {
            return unknown;
        }
    }

    public static Lattice.Pair refineGt(boolean result, Lattice lhs, Lattice rhs) {
        if (result) {
            switch (rhs) {
            case positive:
            case nonnegative:
                return new Lattice.Pair(positive, positive);
            default:
                return new Lattice.Pair(unknown, unknown);
            }
        } else {
            switch (lhs) {
            case positive:
                return new Lattice.Pair(positive, positive);
            case nonnegative:
                return new Lattice.Pair(nonnegative, nonnegative);
            default:
                return new Lattice.Pair(unknown, unknown);
            }
        }
    }

    public static class Pair {
        public final Lattice lhs;
        public final Lattice rhs;

        public Pair(Lattice lhs, Lattice rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }
    }
}
