package org.example.benchmark;

import org.example.aco.AcoConfig;
import org.example.aco.AntColonyOptimization;
import org.example.ga.Chromosome;
import org.example.ga.GaConfig;
import org.example.ga.GeneticAlgorithm;
import org.example.ga.operator.crossover.CrossoverOperator;
import org.example.ga.operator.crossover.OrderedCrossover;
import org.example.ga.operator.crossover.PartiallyMappedCrossover;
import org.example.ga.operator.mutation.InversionMutation;
import org.example.ga.operator.mutation.MutationOperator;
import org.example.ga.operator.mutation.ScrambleMutation;
import org.example.ga.operator.mutation.SwapMutation;
import org.example.ga.operator.selection.RouletteWheelSelection;
import org.example.ga.operator.selection.SelectionOperator;
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
    private static final int ACO_ITERATIONS = 500;

    public static void main(String[] args) throws IOException {
        TSPInstance tspInstance = TSPDataReader.readInstance("ftv47.atsp", 1776);
//        List<ExperimentConfig> experiments = defineExperiments();
//        List<ExperimentStats> allResults = new ArrayList<>();
//
//        for (ExperimentConfig config : experiments) {
//            System.out.println("\n--- Uruchamianie eksperymentu: " + config.getName() + " ---");
//            List<TSPSolution> singleExperimentResults = new ArrayList<>();
//
//            for (int i = 1; i <= NUMBER_OF_RUNS; i++) {
//                System.out.printf("Uruchomienie %d/%d... ", i, NUMBER_OF_RUNS);
//                GeneticAlgorithm ga = new GeneticAlgorithm(
//                        tspInstance,
//                        config.getGaConfig(),
//                        config.getSelectionOperator(),
//                        config.getCrossoverOperator(),
//                        config.getMutationOperator()
//                );
//
//                long startTime = System.currentTimeMillis();
//                Chromosome bestChromosome = ga.run();
//                long endTime = System.currentTimeMillis();
//
//                singleExperimentResults.add(new TSPSolution(
//                        bestChromosome.getTour(),
//                        bestChromosome.getDistance(),
//                        endTime - startTime
//                ));
//                System.out.println("Zakończono. Wynik: " + bestChromosome.getDistance());
//            }
//            allResults.add(ExperimentStats.calculate(config.getName(), singleExperimentResults, tspInstance.getOptimalDistance()));
//        }
//
//        printSummaryTable(allResults);
//        ResultsWriter.writeToCsv(allResults, "ga_results_4.csv");

        List<ExperimentStats> acoResults = runAcoExperiments(tspInstance);
        printSummaryTable(acoResults, "ACO Results");
        ResultsWriter.writeToCsv(acoResults, "aco_results.csv");
    }

    private static List<ExperimentStats> runAcoExperiments(TSPInstance tspInstance) {
        List<AcoConfig> experiments = defineAcoExperiments();
        List<ExperimentStats> allResults = new ArrayList<>();

        for (AcoConfig config : experiments) {
            String experimentName = createAcoExperimentName(config);
            System.out.println("\n--- Uruchamianie eksperymentu ACO: " + experimentName + " ---");
            List<TSPSolution> singleExperimentResults = new ArrayList<>();

            for (int i = 1; i <= NUMBER_OF_RUNS; i++) {
                AntColonyOptimization aco = new AntColonyOptimization(tspInstance, config);
                TSPSolution result = aco.run(); // Metoda run w ACO już zwraca TSPSolution
                singleExperimentResults.add(result);
            }
            allResults.add(ExperimentStats.calculate(experimentName, singleExperimentResults, tspInstance.getOptimalDistance()));
        }
        return allResults;
    }

//    private static List<ExperimentConfig> defineExperiments() {
//        List<ExperimentConfig> configs = new ArrayList<>();
//
//        final double CX_RATE_90 = 0.90;
//        final double MUTATION_RATE_50 = 0.5;
//        final double MUTATION_RATE_5 = 0.05;
//        final int TOURNAMENT_SIZE_2 = 2;
//        final int TOURNAMENT_SIZE_5 = 5;
//        final int TOURNAMENT_SIZE_10 = 10;
//
//        final SelectionOperator TOURNAMENT_SELECTION_2 = new TournamentSelection(TOURNAMENT_SIZE_2);
//        final SelectionOperator TOURNAMENT_SELECTION_5 = new TournamentSelection(TOURNAMENT_SIZE_5);
//        final SelectionOperator TOURNAMENT_SELECTION_10 = new TournamentSelection(TOURNAMENT_SIZE_10);
//        final SelectionOperator ROULETTE_SELECTION = new RouletteWheelSelection();
//        final CrossoverOperator PMX_CROSSOVER = new PartiallyMappedCrossover();
//        final CrossoverOperator ORDERED_CROSSOVER = new OrderedCrossover();
//        final MutationOperator SWAP_MUTATION = new SwapMutation();
//
//        configs.add(createConfig("S1.1: Tourn 2 + Elitism 0 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 0,
//                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S1.2: Tourn 2 + Elitism 1 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 1,
//                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S1.3: Tourn 2 + Elitism 2 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 2,
//                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S1.4: Tourn 2 + Elitism 3 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 3,
//                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S1.5: Tourn 5 + Elitism 0 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 0,
//                TOURNAMENT_SELECTION_5, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S1.6: Tourn 5 + Elitism 1 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 1,
//                TOURNAMENT_SELECTION_5, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S1.7: Tourn 5 + Elitism 2 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 2,
//                TOURNAMENT_SELECTION_5, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S1.8: Tourn 5 + Elitism 3 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 3,
//                TOURNAMENT_SELECTION_5, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S1.9: Tourn 10 + Elitism 0 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 0,
//                TOURNAMENT_SELECTION_10, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S1.10: Tourn 10 + Elitism 1 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 1,
//                TOURNAMENT_SELECTION_10, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S1.11: Tourn 10 + Elitism 2 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 2,
//                TOURNAMENT_SELECTION_10, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S1.12: Tourn 10 + Elitism 3 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 3,
//                TOURNAMENT_SELECTION_10, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//
//        configs.add(createConfig("S2.1: Roulette + Elitism 0 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 0,
//                ROULETTE_SELECTION, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S2.2: Roulette + Elitism 1 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 1,
//                ROULETTE_SELECTION, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S2.3: Roulette + Elitism 2 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 2,
//                ROULETTE_SELECTION, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S2.4: Roulette + Elitism 3 + PMX 0.9 + SWAP 0.5",
//                CX_RATE_90, MUTATION_RATE_50, 3,
//                ROULETTE_SELECTION, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        final int ELITISM_1 = 1;
//
//        configs.add(createConfig("S3.1: Tourn 2 + Elitism 1 + PMX 0.7 + SWAP 0.5",
//                0.7, MUTATION_RATE_50, ELITISM_1,
//                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S3.2: Tourn 2 + Elitism 1 + PMX 0.8 + SWAP 0.5",
//                0.8, MUTATION_RATE_50, ELITISM_1,
//                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S3.3: Tourn 2 + Elitism 1 + PMX 0.9 + SWAP 0.5",
//                0.9, MUTATION_RATE_50, ELITISM_1,
//                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S3.4: Tourn 2 + Elitism 1 + PMX 0.95 + SWAP 0.5",
//                0.95, MUTATION_RATE_50, ELITISM_1,
//                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
//        ));
//
//
//        configs.add(createConfig("S4.1: Tourn 2 + Elitism 1 + OX1 0.7 + SWAP 0.05",
//                0.7, MUTATION_RATE_5, ELITISM_1,
//                TOURNAMENT_SELECTION_2, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S4.2: Tourn 2 + Elitism 1 + OX1 0.8 + SWAP 0.05",
//                0.8, MUTATION_RATE_5, ELITISM_1,
//                TOURNAMENT_SELECTION_2, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S4.3: Tourn 2 + Elitism 1 + OX1 0.9 + SWAP 0.05",
//                0.90, MUTATION_RATE_5, ELITISM_1,
//                TOURNAMENT_SELECTION_2, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S4.4: Tourn 2 + Elitism 1 + OX1 0.95 + SWAP 0.05",
//                0.95, MUTATION_RATE_5, ELITISM_1,
//                TOURNAMENT_SELECTION_2, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S4.5: Tourn 5 + Elitism 1 + OX1 0.7 + SWAP 0.05",
//                0.7, MUTATION_RATE_5, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S4.6: Tourn 5 + Elitism 1 + OX1 0.8 + SWAP 0.05",
//                0.8, MUTATION_RATE_5, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S4.7: Tourn 5 + Elitism 1 + OX1 0.9 + SWAP 0.05",
//                0.90, MUTATION_RATE_5, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S4.8: Tourn 5 + Elitism 1 + OX1 0.95 + SWAP 0.05",
//                0.95, MUTATION_RATE_5, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S4.9: Tourn 10 + Elitism 1 + OX1 0.7 + SWAP 0.05",
//                0.7, MUTATION_RATE_5, ELITISM_1,
//                TOURNAMENT_SELECTION_10, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S4.10: Tourn 10 + Elitism 1 + OX1 0.8 + SWAP 0.05",
//                0.8, MUTATION_RATE_5, ELITISM_1,
//                TOURNAMENT_SELECTION_10, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S4.11: Tourn 10 + Elitism 1 + OX1 0.9 + SWAP 0.05",
//                0.90, MUTATION_RATE_5, ELITISM_1,
//                TOURNAMENT_SELECTION_10, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S4.12: Tourn 10 + Elitism 1 + OX1 0.95 + SWAP 0.05",
//                0.95, MUTATION_RATE_5, ELITISM_1,
//                TOURNAMENT_SELECTION_10, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//
//        configs.add(createConfig("S5.1: Tourn 5 + Elitism 1 + OX1 0.9 + SWAP 0.02",
//                CX_RATE_90, 0.02, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S5.2: Tourn 5 + Elitism 1 + OX1 0.9 + SWAP 0.05",
//                CX_RATE_90, 0.05, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S5.3: Tourn 5 + Elitism 1 + OX1 0.9 + SWAP 0.1",
//                CX_RATE_90, 0.1, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        configs.add(createConfig("S5.4: Tourn 5 + Elitism 1 + OX1 0.9 + SWAP 0.5",
//                CX_RATE_90, 0.5, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
//        ));
//
//        final MutationOperator INVERSION_MUTATION = new InversionMutation();
//
//        configs.add(createConfig("S6.1: Tourn 5 + Elitism 1 + OX1 0.9 + INVERSION 0.02",
//                CX_RATE_90, 0.02, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, INVERSION_MUTATION
//        ));
//
//        configs.add(createConfig("S6.2: Tourn 5 + Elitism 1 + OX1 0.9 + INVERSION 0.05",
//                CX_RATE_90, 0.05, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, INVERSION_MUTATION
//        ));
//
//        configs.add(createConfig("S6.3: Tourn 5 + Elitism 1 + OX1 0.9 + INVERSION 0.1",
//                CX_RATE_90, 0.1, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, INVERSION_MUTATION
//        ));
//
//        configs.add(createConfig("S6.4: Tourn 5 + Elitism 1 + OX1 0.9 + INVERSION 0.5",
//                CX_RATE_90, 0.5, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, INVERSION_MUTATION
//        ));
//
//        final MutationOperator SCRAMBLE_MUTATION = new ScrambleMutation();
//
//        configs.add(createConfig("S7.1: Tourn 5 + Elitism 1 + OX1 0.9 + SCRAMBLE 0.02",
//                CX_RATE_90, 0.02, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SCRAMBLE_MUTATION
//        ));
//
//        configs.add(createConfig("S7.2: Tourn 5 + Elitism 1 + OX1 0.9 + SCRAMBLE 0.05",
//                CX_RATE_90, 0.05, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SCRAMBLE_MUTATION
//        ));
//
//        configs.add(createConfig("S7.3: Tourn 5 + Elitism 1 + OX1 0.9 + SCRAMBLE 0.1",
//                CX_RATE_90, 0.1, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SCRAMBLE_MUTATION
//        ));
//
//        configs.add(createConfig("S7.4: Tourn 5 + Elitism 1 + OX1 0.9 + SCRAMBLE 0.5",
//                CX_RATE_90, 0.5, ELITISM_1,
//                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SCRAMBLE_MUTATION
//        ));
//
//
//        return configs;
//    }

    private static ExperimentConfig createConfig(
            String name,
            double cxRate, double mutRate, int elitismCount,
            SelectionOperator selectionOperator,
            CrossoverOperator crossoverOperator,
            MutationOperator mutationOperator
    ) {
        GaConfig gaConfig = new GaConfig(POPULATION_SIZE, GENERATIONS, cxRate, mutRate, elitismCount);

        return new ExperimentConfig(
                name,
                gaConfig,
                selectionOperator,
                crossoverOperator,
                mutationOperator
        );
    }

    private static List<AcoConfig> defineAcoExperiments() {
        List<AcoConfig> configs = new ArrayList<>();
        final double INITIAL_PHEROMONE = 0.1;
        final int ITERATIONS = 500; // Stała liczba iteracji dla wszystkich testów

        // =========================================================================
        // FAZA 1: Badanie wpływu liczby mrówek (antCount)
        // =========================================================================
        System.out.println("Definiowanie Fazy 1: Badanie liczby mrówek...");
        final double PHASE_1_ALPHA = 1.0;
        final double PHASE_1_BETA = 5.0;
        final double PHASE_1_RHO = 0.1;

        for (int antCount = 10; antCount <= 70; antCount += 10) {
            configs.add(new AcoConfig(antCount, ITERATIONS, PHASE_1_ALPHA, PHASE_1_BETA, PHASE_1_RHO, INITIAL_PHEROMONE));
        }

        // =========================================================================
        // FAZA 2: Badanie wpływu wagi feromonu (alpha)
        // =========================================================================
        System.out.println("Definiowanie Fazy 2: Badanie wagi feromonu (alpha)...");
        // Używamy najlepszej wartości `antCount` z Fazy 1. Załóżmy, że będzie to 50.
        final int BEST_ANT_COUNT = 50;
        final double PHASE_2_BETA = 5.0;
        final double PHASE_2_RHO = 0.1;

        configs.add(new AcoConfig(BEST_ANT_COUNT, ITERATIONS, 0.5, PHASE_2_BETA, PHASE_2_RHO, INITIAL_PHEROMONE));
        configs.add(new AcoConfig(BEST_ANT_COUNT, ITERATIONS, 0.7, PHASE_2_BETA, PHASE_2_RHO, INITIAL_PHEROMONE));
        configs.add(new AcoConfig(BEST_ANT_COUNT, ITERATIONS, 1, PHASE_2_BETA, PHASE_2_RHO, INITIAL_PHEROMONE));
        configs.add(new AcoConfig(BEST_ANT_COUNT, ITERATIONS, 1.5, PHASE_2_BETA, PHASE_2_RHO, INITIAL_PHEROMONE));
        configs.add(new AcoConfig(BEST_ANT_COUNT, ITERATIONS, 2, PHASE_2_BETA, PHASE_2_RHO, INITIAL_PHEROMONE));


        // =========================================================================
        // FAZA 3: Badanie wpływu wagi heurystyki (beta)
        // =========================================================================
        System.out.println("Definiowanie Fazy 3: Badanie wagi heurystyki (beta)...");
        // Używamy najlepszych `antCount` (z F1) i `alpha` (z F2). Załóżmy, że alpha=1.0.
        final double BEST_ALPHA = 1.0;
        final double PHASE_3_RHO = 0.1;

        for (int beta = 2; beta <= 8; beta += 1) {
            configs.add(new AcoConfig(BEST_ANT_COUNT, ITERATIONS, BEST_ALPHA, beta, PHASE_3_RHO, INITIAL_PHEROMONE));
        }

        // =========================================================================
        // FAZA 4: Badanie wpływu współczynnika parowania (rho)
        // =========================================================================
        System.out.println("Definiowanie Fazy 4: Badanie współczynnika parowania (rho)...");
        // Używamy najlepszych parametrów z F1, F2 i F3. Załóżmy, że beta=5.0.
        final double BEST_BETA = 5.0;

        for (double rho = 0.05; rho < 0.9; rho += 0.15) { // Krok co 0.15, aby nie było zbyt wielu testów
            configs.add(new AcoConfig(BEST_ANT_COUNT, ITERATIONS, BEST_ALPHA, BEST_BETA, rho, INITIAL_PHEROMONE));
        }

        return configs;
    }

    private static String createAcoExperimentName(AcoConfig config) {
        return String.format(java.util.Locale.US, "Ants=%d,a=%.1f,b=%.1f,rho=%.2f",
                config.getAntCount(), config.getAlpha(), config.getBeta(), config.getEvaporationRate());
    }


    private static void printSummaryTable(List<ExperimentStats> allStats, String title) {
        System.out.printf("\n\n--- PODSUMOWANIE EKSPERYMENTÓW: %s ---%n", title);

        String headerFormat = "| %-25s | %-10s | %-10s | %-10s | %-15s | %-15s | %-15s |%n";
        String dataFormat =   "| %-25s | %-10d | %-10.2f | %-10.2f | %-15.2f | %-15.2f | %-15.2f |%n";

        int tableWidth = 124;
        System.out.println("-".repeat(tableWidth));

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

        for (ExperimentStats stats : allStats) {
            System.out.printf(dataFormat,
                    stats.getExperimentName(),
                    stats.getBestDistance(),
                    stats.getAverageDistance(),
                    stats.getStdDeviationDistance(),
                    stats.getAverageTimeMillis(),
                    stats.getAverageGapPercent(),
                    stats.getBestGapPercent()
            );
        }
        System.out.println("-".repeat(tableWidth));
    }
}