package org.example.benchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

public class ResultsWriter {

    public static void writeToCsv(List<ExperimentStats> statsList, String fileName) throws IOException {
        // Używamy try-with-resources, aby plik został automatycznie zamknięty
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Ustawiamy Locale.US, aby separatorem dziesiętnym była kropka, co jest standardem w CSV
            writer.println("Konfiguracja,Najlepszy,Najgorszy,Sredni,OdchStd,SredniCzasMs,BladSredniProc,BladNajlepszyProc");

            for (ExperimentStats stats : statsList) {
                writer.format(Locale.US, "%s,%d,%d,%.2f,%.2f,%.2f,%.2f,%.2f\n",
                        stats.getExperimentName(),
                        stats.getBestDistance(),
                        stats.getWorstDistance(),
                        stats.getAverageDistance(),
                        stats.getStdDeviationDistance(),
                        stats.getAverageTimeMillis(),
                        stats.getAverageGapPercent(),
                        stats.getBestGapPercent()
                );
            }
        }
        System.out.println("\nWyniki zostały pomyślnie zapisane do pliku: " + fileName);
    }
}
