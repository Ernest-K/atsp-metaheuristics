package org.example.benchmark;

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

    private static final int NUMBER_OF_RUNS = 3;
    private static final int POPULATION_SIZE = 500;
    private static final int GENERATIONS = 10000;

    public static void main(String[] args) throws IOException {
        TSPInstance tspInstance = TSPDataReader.readInstance("ftv170.atsp", 2755);
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
        ResultsWriter.writeToCsv(allResults, "ga_results_4.csv");
    }

//    private static List<ExperimentConfig> defineExperiments() {
//        List<ExperimentConfig> configs = new ArrayList<>();
//
//        // --- Scenariusz 1: Badanie crossoverRate ---
//        double constMutationRate1 = 0.5;
//        int constTournamentSize1 = 2;
//        configs.add(createConfig("S1.1: CX Rate=0.70", 0.70, constMutationRate1, constTournamentSize1));
//        configs.add(createConfig("S1.2: CX Rate=0.80", 0.80, constMutationRate1, constTournamentSize1));
//        configs.add(createConfig("S1.3: CX Rate=0.90", 0.90, constMutationRate1, constTournamentSize1));
//        configs.add(createConfig("S1.4: CX Rate=0.95", 0.95, constMutationRate1, constTournamentSize1));
//        configs.add(createConfig("S1.5: CX Rate=0.99", 0.99, constMutationRate1, constTournamentSize1));
//
//
//
//        // --- Scenariusz 2: Badanie mutationRate ---
//        double bestCxRate = 0.80;
//        int constTournamentSize2 = 2;
//        configs.add(createConfig("S2.1: Mut Rate=0.05", bestCxRate, 0.05, constTournamentSize2));
//        configs.add(createConfig("S2.2: Mut Rate=0.1", bestCxRate, 0.1, constTournamentSize2));
//        configs.add(createConfig("S2.3: Mut Rate=0.2", bestCxRate, 0.2, constTournamentSize2));
//        configs.add(createConfig("S2.4: Mut Rate=0.5", bestCxRate, 0.5, constTournamentSize2));
//
//
//        // --- Scenariusz 3: Badanie tournamentSize ---
//        double bestMutRate = 0.5;
//        configs.add(createConfig("S3.1: Tourn Size=2", bestCxRate, bestMutRate, 2));
//        configs.add(createConfig("S3.2: Tourn Size=5", bestCxRate, bestMutRate, 5));
//        configs.add(createConfig("S3.3: Tourn Size=10", bestCxRate, bestMutRate, 10));
//
//        return configs;
//    }

//    private static ExperimentConfig createConfig(String name, double cxRate, double mutRate, int tournSize) {
//        GaConfig gaConfig = new GaConfig(POPULATION_SIZE, GENERATIONS, cxRate, mutRate, tournSize, 0);
//        return new ExperimentConfig(
//                name, gaConfig,
//                new TournamentSelection(tournSize),
//                new PartiallyMappedCrossover(),
//                new SwapMutation()
//        );
//    }

    private static List<ExperimentConfig> defineExperiments() {
        List<ExperimentConfig> configs = new ArrayList<>();

        final double CX_RATE_90 = 0.90;
        final double MUTATION_RATE_50 = 0.5;
        final double MUTATION_RATE_5 = 0.05;
        final int TOURNAMENT_SIZE_2 = 2;
        final int TOURNAMENT_SIZE_5 = 5;
        final int TOURNAMENT_SIZE_10 = 10;

        final SelectionOperator TOURNAMENT_SELECTION_2 = new TournamentSelection(TOURNAMENT_SIZE_2);
        final SelectionOperator TOURNAMENT_SELECTION_5 = new TournamentSelection(TOURNAMENT_SIZE_5);
        final SelectionOperator TOURNAMENT_SELECTION_10 = new TournamentSelection(TOURNAMENT_SIZE_10);
        final SelectionOperator ROULETTE_SELECTION = new RouletteWheelSelection();
        final CrossoverOperator PMX_CROSSOVER = new PartiallyMappedCrossover();
        final CrossoverOperator ORDERED_CROSSOVER = new OrderedCrossover();
        final MutationOperator SWAP_MUTATION = new SwapMutation();

        configs.add(createConfig("S1.1: Tourn 2 + Elitism 0 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 0,
                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S1.2: Tourn 2 + Elitism 1 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 1,
                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S1.3: Tourn 2 + Elitism 2 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 2,
                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S1.4: Tourn 2 + Elitism 3 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 3,
                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S1.5: Tourn 5 + Elitism 0 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 0,
                TOURNAMENT_SELECTION_5, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S1.6: Tourn 5 + Elitism 1 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 1,
                TOURNAMENT_SELECTION_5, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S1.7: Tourn 5 + Elitism 2 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 2,
                TOURNAMENT_SELECTION_5, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S1.8: Tourn 5 + Elitism 3 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 3,
                TOURNAMENT_SELECTION_5, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S1.9: Tourn 10 + Elitism 0 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 0,
                TOURNAMENT_SELECTION_10, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S1.10: Tourn 10 + Elitism 1 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 1,
                TOURNAMENT_SELECTION_10, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S1.11: Tourn 10 + Elitism 2 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 2,
                TOURNAMENT_SELECTION_10, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S1.12: Tourn 10 + Elitism 3 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 3,
                TOURNAMENT_SELECTION_10, PMX_CROSSOVER, SWAP_MUTATION
        ));


        configs.add(createConfig("S2.1: Roulette + Elitism 0 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 0,
                ROULETTE_SELECTION, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S2.2: Roulette + Elitism 1 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 1,
                ROULETTE_SELECTION, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S2.3: Roulette + Elitism 2 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 2,
                ROULETTE_SELECTION, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S2.4: Roulette + Elitism 3 + PMX 0.9 + SWAP 0.5",
                CX_RATE_90, MUTATION_RATE_50, 3,
                ROULETTE_SELECTION, PMX_CROSSOVER, SWAP_MUTATION
        ));

        final int ELITISM_1 = 1;

        configs.add(createConfig("S3.1: Tourn 2 + Elitism 1 + PMX 0.7 + SWAP 0.5",
                0.7, MUTATION_RATE_50, ELITISM_1,
                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S3.2: Tourn 2 + Elitism 1 + PMX 0.8 + SWAP 0.5",
                0.8, MUTATION_RATE_50, ELITISM_1,
                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S3.3: Tourn 2 + Elitism 1 + PMX 0.9 + SWAP 0.5",
                0.9, MUTATION_RATE_50, ELITISM_1,
                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S3.4: Tourn 2 + Elitism 1 + PMX 0.95 + SWAP 0.5",
                0.95, MUTATION_RATE_50, ELITISM_1,
                TOURNAMENT_SELECTION_2, PMX_CROSSOVER, SWAP_MUTATION
        ));


        configs.add(createConfig("S4.1: Tourn 2 + Elitism 1 + OX1 0.7 + SWAP 0.05",
                0.7, MUTATION_RATE_5, ELITISM_1,
                TOURNAMENT_SELECTION_2, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S4.2: Tourn 2 + Elitism 1 + OX1 0.8 + SWAP 0.05",
                0.8, MUTATION_RATE_5, ELITISM_1,
                TOURNAMENT_SELECTION_2, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S4.3: Tourn 2 + Elitism 1 + OX1 0.9 + SWAP 0.05",
                0.90, MUTATION_RATE_5, ELITISM_1,
                TOURNAMENT_SELECTION_2, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S4.4: Tourn 2 + Elitism 1 + OX1 0.95 + SWAP 0.05",
                0.95, MUTATION_RATE_5, ELITISM_1,
                TOURNAMENT_SELECTION_2, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S4.5: Tourn 5 + Elitism 1 + OX1 0.7 + SWAP 0.05",
                0.7, MUTATION_RATE_5, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S4.6: Tourn 5 + Elitism 1 + OX1 0.8 + SWAP 0.05",
                0.8, MUTATION_RATE_5, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S4.7: Tourn 5 + Elitism 1 + OX1 0.9 + SWAP 0.05",
                0.90, MUTATION_RATE_5, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S4.8: Tourn 5 + Elitism 1 + OX1 0.95 + SWAP 0.05",
                0.95, MUTATION_RATE_5, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S4.9: Tourn 10 + Elitism 1 + OX1 0.7 + SWAP 0.05",
                0.7, MUTATION_RATE_5, ELITISM_1,
                TOURNAMENT_SELECTION_10, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S4.10: Tourn 10 + Elitism 1 + OX1 0.8 + SWAP 0.05",
                0.8, MUTATION_RATE_5, ELITISM_1,
                TOURNAMENT_SELECTION_10, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S4.11: Tourn 10 + Elitism 1 + OX1 0.9 + SWAP 0.05",
                0.90, MUTATION_RATE_5, ELITISM_1,
                TOURNAMENT_SELECTION_10, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S4.12: Tourn 10 + Elitism 1 + OX1 0.95 + SWAP 0.05",
                0.95, MUTATION_RATE_5, ELITISM_1,
                TOURNAMENT_SELECTION_10, ORDERED_CROSSOVER, SWAP_MUTATION
        ));


        configs.add(createConfig("S5.1: Tourn 5 + Elitism 1 + OX1 0.9 + SWAP 0.02",
                CX_RATE_90, 0.02, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S5.2: Tourn 5 + Elitism 1 + OX1 0.9 + SWAP 0.05",
                CX_RATE_90, 0.05, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S5.3: Tourn 5 + Elitism 1 + OX1 0.9 + SWAP 0.1",
                CX_RATE_90, 0.1, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        configs.add(createConfig("S5.4: Tourn 5 + Elitism 1 + OX1 0.9 + SWAP 0.5",
                CX_RATE_90, 0.5, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SWAP_MUTATION
        ));

        final MutationOperator INVERSION_MUTATION = new InversionMutation();

        configs.add(createConfig("S6.1: Tourn 5 + Elitism 1 + OX1 0.9 + INVERSION 0.02",
                CX_RATE_90, 0.02, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, INVERSION_MUTATION
        ));

        configs.add(createConfig("S6.2: Tourn 5 + Elitism 1 + OX1 0.9 + INVERSION 0.05",
                CX_RATE_90, 0.05, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, INVERSION_MUTATION
        ));

        configs.add(createConfig("S6.3: Tourn 5 + Elitism 1 + OX1 0.9 + INVERSION 0.1",
                CX_RATE_90, 0.1, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, INVERSION_MUTATION
        ));

        configs.add(createConfig("S6.4: Tourn 5 + Elitism 1 + OX1 0.9 + INVERSION 0.5",
                CX_RATE_90, 0.5, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, INVERSION_MUTATION
        ));

        final MutationOperator SCRAMBLE_MUTATION = new ScrambleMutation();

        configs.add(createConfig("S7.1: Tourn 5 + Elitism 1 + OX1 0.9 + SCRAMBLE 0.02",
                CX_RATE_90, 0.02, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SCRAMBLE_MUTATION
        ));

        configs.add(createConfig("S7.2: Tourn 5 + Elitism 1 + OX1 0.9 + SCRAMBLE 0.05",
                CX_RATE_90, 0.05, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SCRAMBLE_MUTATION
        ));

        configs.add(createConfig("S7.3: Tourn 5 + Elitism 1 + OX1 0.9 + SCRAMBLE 0.1",
                CX_RATE_90, 0.1, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SCRAMBLE_MUTATION
        ));

        configs.add(createConfig("S7.4: Tourn 5 + Elitism 1 + OX1 0.9 + SCRAMBLE 0.5",
                CX_RATE_90, 0.5, ELITISM_1,
                TOURNAMENT_SELECTION_5, ORDERED_CROSSOVER, SCRAMBLE_MUTATION
        ));


        return configs;
    }


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

    private static void printSummaryTable(List<ExperimentStats> allStats) {
        System.out.println("\n\n--- PODSUMOWANIE EKSPERYMENTÓW ---");

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