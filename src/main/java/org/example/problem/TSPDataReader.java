package org.example.problem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class TSPDataReader {
    /**
     * Wczytuje instancję problemu TSP z pliku znajdującego się w folderze 'resources'.
     *
     * @param fileName nazwa pliku (np. "ftv47.atsp")
     * @return obiekt TSPInstance zawierający wczytane dane.
     * @throws IOException jeśli wystąpi błąd podczas czytania pliku.
     */
    public static TSPInstance readInstance(String fileName, int optimalDistance) throws IOException {
        int dimension = 0;
        List<Integer> weights = new ArrayList<>();
        boolean matrixSection = false;

        ClassLoader classLoader = TSPDataReader.class.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("Nie można znaleźć pliku w folderze resources: " + fileName);
            }
            InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("DIMENSION")) {
                    dimension = Integer.parseInt(line.split(":")[1].trim());
                } else if (line.equals("EDGE_WEIGHT_SECTION")) {
                    matrixSection = true;
                    continue;
                } else if (line.equals("EOF")) {
                    break;
                }

                if (matrixSection) {
                    String[] values = line.split("\\s+");
                    for (String val : values) {
                        if (!val.isEmpty()) {
                            weights.add(Integer.parseInt(val));
                        }
                    }
                }
            }
        }

        if (dimension == 0 || weights.isEmpty()) {
            throw new IOException("Nie udało się wczytać wymiaru lub macierzy wag z pliku.");
        }

        int[][] matrix = new int[dimension][dimension];
        int weightIndex = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (weightIndex < weights.size()) {
                    matrix[i][j] = weights.get(weightIndex++);
                } else {
                    throw new IOException("Niewystarczająca ilość danych w macierzy wag.");
                }
            }
        }

        // Zamiast zwracać int[][], tworzymy i zwracamy obiekt TSPInstance
        return new TSPInstance(matrix, optimalDistance);
    }
}