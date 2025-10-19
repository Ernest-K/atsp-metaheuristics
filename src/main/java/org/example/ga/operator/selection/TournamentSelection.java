package org.example.ga.operator.selection;

import org.example.ga.Chromosome;
import org.example.ga.Population;

import java.util.Random;

/**
 * Implementacja selekcji turniejowej.
 * Z populacji losowanych jest N osobników, a następnie wybierany jest najlepszy z nich.
 */
public class TournamentSelection implements SelectionOperator {

    private final int tournamentSize;
    private final Random random = new Random();

    public TournamentSelection(int tournamentSize) {
        if (tournamentSize < 1) {
            throw new IllegalArgumentException("Rozmiar turnieju musi być większy od 0.");
        }
        this.tournamentSize = tournamentSize;
    }

    @Override
    public Chromosome select(Population population) {
        Chromosome bestInTournament = null;

        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.getSize());
            Chromosome randomCompetitor = population.getChromosomes().get(randomIndex);

            if (bestInTournament == null || randomCompetitor.getDistance() < bestInTournament.getDistance()) {
                bestInTournament = randomCompetitor;
            }
        }
        return bestInTournament;
    }
}