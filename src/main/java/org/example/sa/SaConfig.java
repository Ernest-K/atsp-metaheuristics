package org.example.sa;

/**
 * Przechowuje wszystkie parametry konfiguracyjne dla algorytmu Symulowanego Wyżarzania.
 */
public final class SaConfig {

    private final double initialTemperature;
    private final double minimumTemperature;
    private final double coolingRate;
    private final int iterationsPerTemperature;

    /**
     * @param initialTemperature Temperatura początkowa. Musi być wystarczająco wysoka,
     *                           aby na początku akceptować wiele gorszych rozwiązań.
     * @param minimumTemperature Temperatura końcowa. Warunek stopu algorytmu.
     * @param coolingRate Współczynnik schładzania (np. 0.999). Temperatura jest mnożona
     *                    przez tę wartość w każdej iteracji schładzania.
     * @param iterationsPerTemperature Liczba prób znalezienia nowego rozwiązania
     *                                 na jednym poziomie temperatury.
     */
    public SaConfig(double initialTemperature, double minimumTemperature, double coolingRate, int iterationsPerTemperature) {
        this.initialTemperature = initialTemperature;
        this.minimumTemperature = minimumTemperature;
        this.coolingRate = coolingRate;
        this.iterationsPerTemperature = iterationsPerTemperature;
    }

    // Gettery
    public double getInitialTemperature() {
        return initialTemperature;
    }

    public double getMinimumTemperature() {
        return minimumTemperature;
    }

    public double getCoolingRate() {
        return coolingRate;
    }

    public int getIterationsPerTemperature() {
        return iterationsPerTemperature;
    }
}
