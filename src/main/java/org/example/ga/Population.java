package org.example.ga;

import org.example.problem.TSPInstance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Reprezentuje populację, czyli zbiór chromosomów (potencjalnych rozwiązań).
 */
public class Population {

    private List<Chromosome> chromosomes;

    /**
     * Konstruktor tworzący populację na podstawie podanej listy chromosomów.
     * @param chromosomes lista chromosomów.
     */
    public Population(List<Chromosome> chromosomes) {
        this.chromosomes = chromosomes;
    }

    /**
     * Konstruktor tworzący nową, losową populację.
     * @param populationSize pożądana wielkość populacji.
     * @param instance instancja problemu TSP, potrzebna do inicjalizacji chromosomów.
     */
    public Population(int populationSize, TSPInstance instance) {
        this.chromosomes = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            this.chromosomes.add(new Chromosome(instance.getDimension()));
        }
    }

    /**
     * Oblicza wartość funkcji przystosowania
     * dla każdego chromosomu w populacji.
     * @param instance instancja problemu TSP z macierzą odległości.
     */
    public void calculateFitness(TSPInstance instance) {
        for (Chromosome chromosome : chromosomes) {
            chromosome.getFitness(instance);
        }
    }

    /**
     * Znajduje i zwraca najlepszy chromosom (o najniższym dystansie) w populacji.
     * @return najlepiej przystosowany chromosom.
     */
    public Chromosome getFittest() {
        return chromosomes.stream()
                .min(Comparator.comparingInt(Chromosome::getDistance))
                .orElse(null);
    }

    public List<Chromosome> getChromosomes() {
        return chromosomes;
    }

    public int getSize() {
        return chromosomes.size();
    }
}
