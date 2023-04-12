package fi.helsinki.tekoaly

import fi.helsinki.peliLogiikka.RistinollaPeli
import fi.helsinki.peliLogiikka.VoittotilanneTyyppi
import kotlin.math.max
import kotlin.math.min

/**
 * Luokka joka toteuttaa tekoälyn ristinollalle
 * @author Joonas Coatanea
 * @param etsintasyvyys Siirtomäärä jonka tekoäly laskee edelle, vähintään 1
 * @param ristinollaPeli Peli jossa tekoälyn täytyy tehdä siirtoja
 * @param siirtogeneraattori Tekoälyn käyttämä siirtogeneraattori
 * @throws IllegalArgumentException Jos etsintäsyvyys on alle 1
 */
class Tekoaly(
    private val etsintasyvyys: Int,
    private val ristinollaPeli: RistinollaPeli,
    private val siirtogeneraattori: Siirtogeneraattori,
) {
    private val pelitaulukonKoko: Int = ristinollaPeli.pelitaulukonKoko

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

    private fun tarkistaEtteiPeliOleLoppunut() {
        if (
            ristinollaPeli
                .voittotilanne
                .tyyppi != VoittotilanneTyyppi.EI_VOITTOA
        ) {
            throw IllegalStateException("Peli on loppunut")
        }
    }

    /**
     * Valitsee parhaan siirron jonka tekoäly voi tehdä
     * @return Paras siirto jonka tekoäly voi tehdä
     * @throws IllegalStateException Jos peli on loppunut
     */
    fun valitseSiirto(): Pair<Int, Int> {
        tarkistaEtteiPeliOleLoppunut()

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

        return parasSiirto ?: listOf(
            Pair(0, 0),
            Pair((pelitaulukonKoko - 1), 0),
            Pair(0, (pelitaulukonKoko - 1)),
            Pair((pelitaulukonKoko - 1), (pelitaulukonKoko - 1)),
        ).random()
    }

    /**
     * Merkitsee pelissä tehdyn siirron
     * @param siirronXKoordinaatti Tehdyn siirron X-koordinaatti
     * @param siirronYKoordinaatti Tehdyn siirron Y-koordinaatti
     * @param vastustajanSiirto Boolean-arvo joka merkitsee sitä, onko tehty siirto vastustajan tekemä
     * @throws IllegalStateException Jos peli on loppunut
     */
    fun merkitseTehtySiirto(siirronXKoordinaatti: Int, siirronYKoordinaatti: Int, vastustajanSiirto: Boolean) {
        tarkistaEtteiPeliOleLoppunut()

        val maksimoivanPelaajanSiirto: Boolean = !vastustajanSiirto
        siirtogeneraattori.merkitseSiirto(siirronXKoordinaatti, siirronYKoordinaatti, maksimoivanPelaajanSiirto)
    }
}
