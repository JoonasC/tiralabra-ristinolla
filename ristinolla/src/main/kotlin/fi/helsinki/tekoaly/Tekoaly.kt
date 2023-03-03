package fi.helsinki.tekoaly

import kotlin.math.max
import kotlin.math.min

/**
 * Luokka joka toteuttaa tekoälyn ristinollalle
 * @author Joonas Coatanea
 * @param etsintasyvyys Siirtomäärä jonka tekoäly laskee edelle, vähintään 1
 * @param siirtogeneraattori Tekoälyn käyttämä siirtogeneraattori
 * @throws IllegalArgumentException Jos etsintäsyvyys on alle 1
 */
class Tekoaly(private val etsintasyvyys: Int, private val siirtogeneraattori: Siirtogeneraattori) {
    init {
        if (etsintasyvyys < 1) {
            throw IllegalArgumentException("Etsintäsyvyyden täytyy olla vähintään 1")
        }
    }

    private fun minimax(
        viimeSiirronHeuristinenArvo: Double?,
        syvyys: Int,
        lahtoAlfa: Double,
        lahtoBeta: Double,
        maksimoivaPelaaja: Boolean,
    ): Double {
        val pelinPaattymistilanneJaVoittoarvo: Pair<Boolean, Double> =
            siirtogeneraattori.getPelinPaattymistilanneJaVoittoarvo()
        if (pelinPaattymistilanneJaVoittoarvo.first) {
            return pelinPaattymistilanneJaVoittoarvo.second
        }
        if (syvyys == etsintasyvyys) {
            return (viimeSiirronHeuristinenArvo as Double)
        }

        if (maksimoivaPelaaja) {
            var alfa: Double = lahtoAlfa
            var voittoarvo = -(Double.MAX_VALUE)
            val edellisetSiirrot: MutableSet<Pair<Int, Int>> = mutableSetOf()
            var siirtoJaSiirronHeuristinenArvo: Pair<Pair<Int, Int>, Double>? =
                siirtogeneraattori.otaSeuraavaParasMahdollinenSiirto(edellisetSiirrot)
            while (siirtoJaSiirronHeuristinenArvo != null) {
                edellisetSiirrot.add(siirtoJaSiirronHeuristinenArvo.first)

                voittoarvo = max(
                    voittoarvo,
                    minimax(siirtoJaSiirronHeuristinenArvo.second, (syvyys + 1), alfa, lahtoBeta, false),
                )
                alfa = max(alfa, voittoarvo)

                siirtogeneraattori.palautaSiirto(
                    siirtoJaSiirronHeuristinenArvo
                        .first
                        .first,
                    siirtoJaSiirronHeuristinenArvo
                        .first
                        .second,
                )
                if (voittoarvo >= lahtoBeta) {
                    break
                }
                siirtoJaSiirronHeuristinenArvo = siirtogeneraattori.otaSeuraavaParasMahdollinenSiirto(edellisetSiirrot)
            }

            return voittoarvo
        } else {
            var beta: Double = lahtoBeta
            var voittoarvo = Double.MAX_VALUE
            val edellisetSiirrot: MutableSet<Pair<Int, Int>> = mutableSetOf()
            var siirtoJaSiirronHeuristinenArvo: Pair<Pair<Int, Int>, Double>? =
                siirtogeneraattori.otaSeuraavaParasMahdollinenSiirto(edellisetSiirrot)
            while (siirtoJaSiirronHeuristinenArvo != null) {
                edellisetSiirrot.add(siirtoJaSiirronHeuristinenArvo.first)

                voittoarvo =
                    min(voittoarvo, minimax(siirtoJaSiirronHeuristinenArvo.second, (syvyys + 1), lahtoAlfa, beta, true))
                beta = min(beta, voittoarvo)

                siirtogeneraattori.palautaSiirto(
                    siirtoJaSiirronHeuristinenArvo
                        .first
                        .first,
                    siirtoJaSiirronHeuristinenArvo
                        .first
                        .second,
                )
                if (voittoarvo <= lahtoAlfa) {
                    break
                }
                siirtoJaSiirronHeuristinenArvo = siirtogeneraattori.otaSeuraavaParasMahdollinenSiirto(edellisetSiirrot)
            }

            return voittoarvo
        }
    }

    /**
     * Valitsee parhaan siirron jonka tekoäly voi tehdä
     * @return Paras siirto jonka tekoäly voi tehdä
     */
    fun valitseSiirto(): Pair<Int, Int> {
        var parasSiirto: Pair<Int, Int>? = null
        var parhaanSiirronVoittoarvo = -(Double.MAX_VALUE)
        val edellisetSiirrot: MutableSet<Pair<Int, Int>> = mutableSetOf()
        var siirtoJaSiirronHeuristinenArvo: Pair<Pair<Int, Int>, Double>? =
            siirtogeneraattori.otaSeuraavaParasMahdollinenSiirto(edellisetSiirrot)
        while (siirtoJaSiirronHeuristinenArvo != null) {
            edellisetSiirrot.add(siirtoJaSiirronHeuristinenArvo.first)

            val siirronVoittoarvo: Double = minimax(
                siirtoJaSiirronHeuristinenArvo.second,
                1,
                parhaanSiirronVoittoarvo,
                Double.MAX_VALUE,
                false,
            )
            if (siirronVoittoarvo > parhaanSiirronVoittoarvo || parasSiirto == null) {
                parasSiirto = siirtoJaSiirronHeuristinenArvo.first
                parhaanSiirronVoittoarvo = siirronVoittoarvo
            }

            siirtogeneraattori.palautaSiirto(
                siirtoJaSiirronHeuristinenArvo
                    .first
                    .first,
                siirtoJaSiirronHeuristinenArvo
                    .first
                    .second,
            )
            siirtoJaSiirronHeuristinenArvo = siirtogeneraattori.otaSeuraavaParasMahdollinenSiirto(edellisetSiirrot)
        }

        return (parasSiirto as Pair<Int, Int>)
    }

    /**
     * Merkitsee pelissä tehdyn siirron
     * @param siirronXKoordinaatti Tehdyn siirron X-koordinaatti
     * @param siirronYKoordinaatti Tehdyn siirron Y-koordinaatti
     * @param vastustajanSiirto Boolean-arvo joka merkitsee sitä, onko tehty siirto vastustajan tekemä
     */
    fun merkitseTehtySiirto(siirronXKoordinaatti: Int, siirronYKoordinaatti: Int, vastustajanSiirto: Boolean) {
        val maksimoivanPelaajanSiirto: Boolean = !vastustajanSiirto
        siirtogeneraattori.merkitseSiirto(siirronXKoordinaatti, siirronYKoordinaatti, maksimoivanPelaajanSiirto)
    }
}
