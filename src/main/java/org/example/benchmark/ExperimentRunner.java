package org.example.benchmark;

import org.example.ga.Chromosome;
import org.example.ga.GaConfig;
import org.example.ga.GeneticAlgorithm;
import org.example.ga.operator.crossover.PartiallyMappedCrossover;
import org.example.ga.operator.mutation.SwapMutation;
import org.example.ga.operator.selection.TournamentSelection;
import org.example.problem.TSPDataReader;
import org.example.problem.TSPInstance;
import org.example.problem.TSPSolution;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExperimentRunner {

    private static final int NUMBER_OF_RUNS = 10;
    private static final int POPULATION_SIZE = 500;
    private static final int GENERATIONS = 1000;

    public static void main(String[] args) throws IOException {
        TSPInstance tspInstance = TSPDataReader.readInstance("ftv47.atsp", 1776);
        List<ExperimentConfig> experiments = defineExperiments();
        List<ExperimentStats> allResults = new ArrayList<>();

        for (ExperimentConfig config : experiments) {
            System.out.println("\n--- Uruchamianie eksperymentu: " + config.getName() + " ---");
            List<TSPSolution> singleExperimentResults = new ArrayList<>();

            for (int i = 1; i <= NUMBER_OF_RUNS; i++) {
                System.out.printf("Uruchomienie %d/%d... ", i, NUMBER_OF_RUNS);
                GeneticAlgorithm ga = new GeneticAlgorithm(
                        tspInstance,
                        config.getGaConfig(),
                        config.getSelectionOperator(),
                        config.getCrossoverOperator(),
                        config.getMutationOperator()
                );

                long startTime = System.currentTimeMillis();
                Chromosome bestChromosome = ga.run();
                long endTime = System.currentTimeMillis();

                singleExperimentResults.add(new TSPSolution(
                        bestChromosome.getTour(),
                        bestChromosome.getDistance(),
                        endTime - startTime
                ));
                System.out.println("Zakończono. Wynik: " + bestChromosome.getDistance());
            }
            allResults.add(ExperimentStats.calculate(config.getName(), singleExperimentResults, tspInstance.getOptimalDistance()));
        }

        printSummaryTable(allResults);
        ResultsWriter.writeToCsv(allResults, "ga_results.csv");
    }

    private static List<ExperimentConfig> defineExperiments() {
        List<ExperimentConfig> configs = new ArrayList<>();

        // --- Scenariusz 1: Badanie crossoverRate ---
        double constMutationRate1 = 0.5;
        int constTournamentSize1 = 2;
        configs.add(createConfig("S1.1: CX Rate=0.70", 0.70, constMutationRate1, constTournamentSize1));
        configs.add(createConfig("S1.2: CX Rate=0.80", 0.80, constMutationRate1, constTournamentSize1));
        configs.add(createConfig("S1.3: CX Rate=0.90", 0.90, constMutationRate1, constTournamentSize1));
        configs.add(createConfig("S1.4: CX Rate=0.95", 0.95, constMutationRate1, constTournamentSize1));
        configs.add(createConfig("S1.5: CX Rate=0.99", 0.99, constMutationRate1, constTournamentSize1));



        // --- Scenariusz 2: Badanie mutationRate ---
        double bestCxRate = 0.80; // Zakładamy, że to będzie najlepszy wynik z S1
        int constTournamentSize2 = 2;
        configs.add(createConfig("S2.1: Mut Rate=0.05", bestCxRate, 0.05, constTournamentSize2));
        configs.add(createConfig("S2.2: Mut Rate=0.1", bestCxRate, 0.1, constTournamentSize2));
        configs.add(createConfig("S2.3: Mut Rate=0.2", bestCxRate, 0.2, constTournamentSize2));
        configs.add(createConfig("S2.4: Mut Rate=0.5", bestCxRate, 0.5, constTournamentSize2));


        // --- Scenariusz 3: Badanie tournamentSize ---
        double bestMutRate = 0.5; // Zakładamy, że to będzie najlepszy wynik z S2
        configs.add(createConfig("S3.1: Tourn Size=2", bestCxRate, bestMutRate, 2));
        configs.add(createConfig("S3.2: Tourn Size=5", bestCxRate, bestMutRate, 5));
        configs.add(createConfig("S3.3: Tourn Size=10", bestCxRate, bestMutRate, 10));

        return configs;
    }

    private static ExperimentConfig createConfig(String name, double cxRate, double mutRate, int tournSize) {
        GaConfig gaConfig = new GaConfig(POPULATION_SIZE, GENERATIONS, cxRate, mutRate, tournSize, 0);
        return new ExperimentConfig(
                name, gaConfig,
                new TournamentSelection(tournSize),
                new PartiallyMappedCrossover(),
                new SwapMutation()
        );
    }

    private static void printSummaryTable(List<ExperimentStats> allStats) {
        System.out.println("\n\n--- PODSUMOWANIE EKSPERYMENTÓW ---");

        // Definiujemy format nagłówka i danych
        String headerFormat = "| %-25s | %-10s | %-10s | %-10s | %-15s | %-15s | %-15s |%n";
        String dataFormat =   "| %-25s | %-10d | %-10.2f | %-10.2f | %-15.2f | %-15.2f | %-15.2f |%n";

        int tableWidth = 124;
        System.out.println("-".repeat(tableWidth));

        // Drukujemy nagłówek
        System.out.printf(headerFormat,
                "Konfiguracja",
                "Najlepszy",
                "Średni",
                "Odch.Std.",
                "Śr. Czas (ms)",
                "Błąd Śr. (%)",
                "Błąd Najl. (%)"
        );
        System.out.println("-".repeat(tableWidth));

        // Drukujemy dane dla każdego eksperymentu
        for (ExperimentStats stats : allStats) {
            System.out.printf(dataFormat, // Używamy poprawnego formatu dla danych
                    stats.getExperimentName(),         // %s  (String)
                    stats.getBestDistance(),           // %d  (int)
                    stats.getAverageDistance(),        // %.2f (double)
                    stats.getStdDeviationDistance(),   // %.2f (double)
                    stats.getAverageTimeMillis(),      // %.2f (double)
                    stats.getAverageGapPercent(),      // %.2f (double)
                    stats.getBestGapPercent()          // %.2f (double)
            );
        }
        System.out.println("-".repeat(tableWidth));
    }
}