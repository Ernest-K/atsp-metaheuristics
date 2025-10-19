package org.example;

import org.example.ga.Chromosome;
import org.example.ga.GaConfig;
import org.example.ga.GeneticAlgorithm;
import org.example.ga.operator.crossover.CrossoverOperator;
import org.example.ga.operator.crossover.OrderedCrossover;
import org.example.ga.operator.crossover.PartiallyMappedCrossover;
import org.example.ga.operator.mutation.MutationOperator;
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
                500,
                1000,
                0.90,
                0.5,
                2,
                0
        );

        try {
            System.out.println("Wczytywanie problemu z pliku: " + instanceFileName);
            TSPInstance tspInstance = TSPDataReader.readInstance(instanceFileName, instanceOptimalDistance);
            System.out.println("Liczba miast: " + tspInstance.getDimension());
            System.out.println("----------------------------------------------------");

//            SelectionOperator selection = new RouletteWheelSelection();
            SelectionOperator selection = new TournamentSelection(config.getTournamentSize());
            CrossoverOperator crossover = new PartiallyMappedCrossover();
            MutationOperator mutation = new SwapMutation();


            GeneticAlgorithm ga = new GeneticAlgorithm(
                    tspInstance,
                    config,
                    selection,
                    crossover,
                    mutation
            );

            long gaStartTime = System.currentTimeMillis();
            Chromosome gaBestSolution = ga.run();
            long gaEndTime = System.currentTimeMillis();

            TSPSolution gaResult = new TSPSolution(
                    gaBestSolution.getTour(),
                    gaBestSolution.getDistance(),
                    gaEndTime - gaStartTime
            );

            System.out.println("----------------------------------------------------");
            System.out.println("Zakończono ewolucję.");
            System.out.println("Czas wykonania: " + (gaEndTime - gaStartTime) + " ms");
            System.out.println("Najlepszy znaleziony dystans: " + gaBestSolution.getDistance());
            System.out.println("Najlepsza trasa: ");
            System.out.println(gaBestSolution.getTour());
            System.out.println("----------------------------------------------------");

            printComparison(gaResult);

        } catch (IOException e) {
            System.err.println("Wystąpił krytyczny błąd podczas wczytywania pliku problemu.");
            e.printStackTrace();
        }
    }


    private static void printComparison(TSPSolution gaResult) {
        System.out.printf("| %-25s | %-15s | %-15s |\n", "Algorytm", "Najlepszy Dystans", "Czas Wykonania");
        System.out.println("-----------------------------------------------------------------");

        System.out.printf("| %-25s | %-15d | %-15s |\n",
                "Algorytm Genetyczny",
                gaResult.getDistance(),
                gaResult.getExecutionTimeMillis() + " ms");

        System.out.println("-----------------------------------------------------------------");
    }
}