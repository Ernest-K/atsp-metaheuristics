package org.example;

import org.example.aco.AcoConfig;
import org.example.aco.AntColonyOptimization;
import org.example.ga.Chromosome;
import org.example.ga.GaConfig;
import org.example.ga.GeneticAlgorithm;
import org.example.ga.operator.crossover.CrossoverOperator;
import org.example.ga.operator.crossover.OrderedCrossover;
import org.example.ga.operator.crossover.PartiallyMappedCrossover;
import org.example.ga.operator.mutation.MutationOperator;
import org.example.ga.operator.mutation.ScrambleMutation;
import org.example.ga.operator.mutation.SwapMutation;
import org.example.ga.operator.selection.RouletteWheelSelection;
import org.example.ga.operator.selection.SelectionOperator;
import org.example.ga.operator.selection.TournamentSelection;
import org.example.problem.TSPDataReader;
import org.example.problem.TSPInstance;
import org.example.problem.TSPSolution;
import org.example.sa.SaConfig;
import org.example.sa.SaSolution;
import org.example.sa.SimulatedAnnealing;
import org.example.sa.operator.NeighborStrategy;
import org.example.sa.operator.SwapNeighbor;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        final String instanceFileName = "ftv47.atsp";
        final int instanceOptimalDistance = 1776;
        GaConfig config = new GaConfig(
                300,
                1000,
                0.90,
                0.05,
                1
        );

        AcoConfig acoConfig = new AcoConfig(
                250,     // Liczba mrówek (antCount)
                500,    // Liczba iteracji (iterations)
                1.0,    // Wpływ feromonów (alpha)
                5.0,    // Wpływ heurystyki/dystansu (beta)
                0.1,    // Współczynnik parowania (evaporationRate)
                0.1     // Początkowa wartość feromonu (initialPheromone)
        );

        try {
            System.out.println("Wczytywanie problemu z pliku: " + instanceFileName);
            TSPInstance tspInstance = TSPDataReader.readInstance(instanceFileName, instanceOptimalDistance);
            System.out.println("Liczba miast: " + tspInstance.getDimension());
            System.out.println("----------------------------------------------------");




//
////            SelectionOperator selection = new RouletteWheelSelection();
//            SelectionOperator selection = new TournamentSelection(5);
////            CrossoverOperator crossover = new PartiallyMappedCrossover();
//            CrossoverOperator crossover = new OrderedCrossover();
////            MutationOperator mutation = new SwapMutation();
//            MutationOperator mutation = new ScrambleMutation();

//            GeneticAlgorithm ga = new GeneticAlgorithm(
//                    tspInstance,
//                    config,
//                    selection,
//                    crossover,
//                    mutation
//            );

            AntColonyOptimization aco = new AntColonyOptimization(tspInstance, acoConfig);


            long gaStartTime = System.currentTimeMillis();
//            Chromosome gaBestSolution = ga.run();
            TSPSolution acoResult = aco.run();
            long gaEndTime = System.currentTimeMillis();

            TSPSolution gaResult = new TSPSolution(
                    acoResult.getTour(),
                    acoResult.getDistance(),
                    gaEndTime - gaStartTime
            );

            System.out.println("----------------------------------------------------");
            System.out.println("Zakończono ewolucję.");
            System.out.println("Czas wykonania: " + (gaEndTime - gaStartTime) + " ms");
            System.out.println("Najlepszy znaleziony dystans: " + acoResult.getDistance());
            System.out.println("Najlepsza trasa: ");
            System.out.println(acoResult.getTour());
            System.out.println("----------------------------------------------------");

            printComparison(gaResult);

        } catch (IOException e) {
            System.err.println("Wystąpił krytyczny błąd podczas wczytywania pliku problemu.");
            e.printStackTrace();
        }
    }


    private static void printComparison(TSPSolution gaResult) {
        System.out.printf("| %-15s | %-15s |\n", "Najlepszy Dystans", "Czas Wykonania");
        System.out.println("--------------------------------------");

        System.out.printf("| %-15d | %-15s |\n",
                gaResult.getDistance(),
                gaResult.getExecutionTimeMillis() + " ms");

        System.out.println("--------------------------------------");
    }
}