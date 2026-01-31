package no.mincci.mercspeak;

import java.util.function.BiFunction;

// All functions (and formulas) credit to https://easings.net, by Andrey Sitnik and Ivan Solovev.
public enum EasingFunction {
    LINEAR((t, cp) -> t),
    EASE_IN_SINE((t, cp) -> 1 - Math.cos((t * Math.PI) / 2)),
    EASE_IN_QUADRATIC((t, cp) -> t * t),
    EASE_IN_CUBIC((t, cp) -> t * t * t),
    EASE_IN_QUARTIC((t, cp) -> t * t * t * t),
    EASE_IN_QUINTIC((t, cp) -> t * t * t * t * t),
    EASE_IN_EXPONENTIAL((t, cp) -> (t == 0) ? 0 : Math.pow(2, 10 * t - 10)),
    EASE_IN_CIRCULAR((t, cp) -> 1 - Math.sqrt(1 - Math.pow(t,2))),
    EASE_IN_BACK((t, cp) -> {
        double c1 = 1.70158;
        double c3 = c1 + 1;

        return c3 * Math.pow(t, 3) - c1 * Math.pow(t, 2);
    }),
    EASE_IN_ELASTIC((t, cp) -> {
        double c4 = (2 * Math.PI) / 3;
        return (t == 0)
                ? 0
                : ((t == 1)
                ? 1
                : -Math.pow(2, 10 * t - 10) * Math.sin((t * 10 - 10.75) * c4));
    }),
    //
    // NOTE: Bounce ease function requires recursion, which you can't exactly do with lambdas. Fix later? It looks a little outdated anyways, so perhaps just cut it.
    //

    EASE_OUT_SINE((t, cp) -> Math.sin((t * Math.PI) / 2)),
    EASE_OUT_QUADRATIC((t, cp) -> 1 - Math.pow(1 - t, 2)),
    EASE_OUT_CUBIC((t, cp) -> 1 - Math.pow(1 - t, 3)),
    EASE_OUT_QUARTIC((t, cp) -> 1 - Math.pow(1 - t, 4)),
    EASE_OUT_QUINTIC((t, cp) -> 1 - Math.pow(1 - t, 5)),
    EASE_OUT_EXPONENTIAL((t, cp) -> (t == 1) ? 1 : (1 - Math.pow(2, -10 * t))),
    EASE_OUT_CIRCULAR((t, cp) -> Math.sqrt(1 - Math.pow(t - 1, 2))),
    EASE_OUT_BACK((t, cp) -> {
        double c1 = 1.70158;
        double c3 = c1 + 1;

        return 1 + c3 * Math.pow(t - 1, 3) + c1 * Math.pow(t - 1, 2);
    }),
    EASE_OUT_ELASTIC((t, cp) -> {
        double c4 = (2 * Math.PI) / 3;

        return t == 0
                ? 0
                : (t == 1
                ? 1
                : Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * c4) + 1);
    }),

    EASE_IN_OUT_SINE((t, cp) -> (-(Math.cos(Math.PI * t) - 1) / 2)),
    EASE_IN_OUT_QUADRATIC((t, cp) -> (t < 0.5) ? 2 * Math.pow(t, 2) : 1 - Math.pow(-2 * t + 2, 2) / 2),
    EASE_IN_OUT_CUBIC((t, cp) -> (t < 0.5) ? 4 * Math.pow(t, 3) : 1 - Math.pow(-2 * t + 2, 3) / 2),
    EASE_IN_OUT_QUARTIC((t, cp) -> (t < 0.5) ? 8 * Math.pow(t, 4) : 1 - Math.pow(-2 * t + 2, 4) / 2),
    EASE_IN_OUT_QUINTIC((t, cp) -> (t < 0.5) ? 16 * Math.pow(t, 5) : 1 - Math.pow(-2 * t + 2, 5) / 2),
    EASE_IN_OUT_EXPONENTIAL((t, cp) -> t == 0
            ? 0
            : (t == 1
            ? 1
            : t < 0.5 ? Math.pow(2, 20 * t - 10) / 2
            : (2 - Math.pow(2, -20 * t + 10)) / 2)),
    EASE_IN_OUT_BACK((t, cp) -> {
        double c1 = 1.70158;
        double c2 = c1 * 1.525;

        return (t < 0.5
                ? (Math.pow(2 * t, 2) * ((c2 + 1) * 2 * t - c2)) / 2
                : (Math.pow(2 * t - 2, 2) * ((c2 + 1) * (t * 2 - 2) + c2) + 2) / 2);
    }),
    EASE_IN_OUT_CIRCULAR((t, cp) -> (t < 0.5
            ? (1 - Math.sqrt(1 - Math.pow(2 * t, 2))) / 2
            : (Math.sqrt(1 - Math.pow(-2 * t + 2, 2)) + 1) / 2)),
    EASE_IN_OUT_ELASTIC ((t, cp) -> {
        float c5 = (float) ((2 * Math.PI) / 4.5);

        return t == 0
                ? 0
                : (t == 1
                ? 1
                : t < 0.5
                ? -(Math.pow(2, 20 * t - 10) * Math.sin((20 * t - 11.125) * c5)) / 2
                : (Math.pow(2, -20 * t + 10) * Math.sin((20 * t - 11.125) * c5)) / 2 + 1);
    }),

    SMOOTHERSTEP ((t, cp) -> { // Adapted from Perlin's improvement on smoothstep, based on  C++ implementation from https://en.wikipedia.org/wiki/Smoothstep
        return t * t * t * (t * (6 * t - 15) + 10);
    }),

    CUBIC_BEZIER ((t, cp) -> {
        assert cp.length == 4 : "Violation of: cubic bezier must only accept 4 control points";
        assert cp[0] >= 0 && cp[0] <= 1 && cp[1] >= 0 && cp[1] <= 1 && cp[2] >= 0 && cp[2] <= 1 && cp[3] >= 0 && cp[3] <= 1 : "Violation of: cubic bezier control points must be within [0,1]";

        return (1 - t) * (1 - t) * (1 - t) * cp[0] + 3 * (1 - t) * (1 - t) * t * cp[1] + 3 * (1 - t) * t * t * cp[2] + t * t * t * cp[3];
        // (1 - t)^3 * P0 + 3t(1 - t)^2 * P1 + 3t^2(1 - t) * P2 + t^3 * P3
    });

    public double apply(double t, double... cp) {
        return this.formula.apply(t, cp);
    }

    EasingFunction(BiFunction<Double, double[], Double> formula) {
        this.formula = formula;
    }

    private BiFunction<Double, double[], Double> formula;
}
