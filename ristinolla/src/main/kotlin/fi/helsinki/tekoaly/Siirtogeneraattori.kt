package fi.helsinki.tekoaly

import fi.helsinki.peliLogiikka.RistinollaPeli

/**
 * Luokka joka pitää kirjaa siitä, mitä siirtoja tekoälyn on mahdollista tehdä
 * @author Joonas Coatanea
 * @param ristinollaPeli Peli jonka mahdollisista siirroista tulee pitää kirjaa
 */
class Siirtogeneraattori(private val ristinollaPeli: RistinollaPeli) {
    private val vapaidenRuutujenKoordinaatit: MutableSet<Pair<Int, Int>> = mutableSetOf()

    init {
        val pelitaulukonKokoLukuvali: IntRange = (0 until ristinollaPeli.pelitaulukonKoko)

        val pelitaulukko: Array<IntArray> = ristinollaPeli.getPelitaulukko()

        pelitaulukonKokoLukuvali.forEach { ruudunYKoordinaatti ->
            pelitaulukonKokoLukuvali.forEach { ruudunXKoordinaatti ->
                if (pelitaulukko[ruudunYKoordinaatti][ruudunXKoordinaatti] == -1) {
                    vapaidenRuutujenKoordinaatit.add(Pair(ruudunXKoordinaatti, ruudunYKoordinaatti))
                }
            }
        }
    }

    /**
     * Ottaa siirron pois mahdollisten siirtojen joukosta
     * @param siirronXKoordinaatti Otettavan siirron X-koordinaatti
     * @param siirronYKoordinaatti Otettavan siirron Y-koordinaatti
     * @throws IllegalArgumentException Jos siirto on mahdoton tai siirto on jo otettu
     */
    fun otaSiirto(siirronXKoordinaatti: Int, siirronYKoordinaatti: Int) {
        if (
            siirronXKoordinaatti < 0 ||
            siirronXKoordinaatti >= ristinollaPeli.pelitaulukonKoko ||
            siirronYKoordinaatti < 0 ||
            siirronYKoordinaatti >= ristinollaPeli.pelitaulukonKoko
        ) {
            throw IllegalArgumentException("Siirto on mahdoton")
        }

        val otettavaSiirto: Pair<Int, Int> = Pair(siirronXKoordinaatti, siirronYKoordinaatti)

        if (vapaidenRuutujenKoordinaatit.contains(otettavaSiirto)) {
            vapaidenRuutujenKoordinaatit.remove(otettavaSiirto)
        } else {
            throw IllegalArgumentException("Siirto ($siirronXKoordinaatti, $siirronYKoordinaatti) on jo otettu")
        }
    }

    /**
     * Palauttaa siirron mahdollisten siirtojen joukkoon
     * @param siirronXKoordinaatti Palautettavan siirron X-koordinaatti
     * @param siirronYKoordinaatti Palautettavan siirron Y-koordinaatti
     * @throws IllegalArgumentException Jos siirto on mahdoton tai siirto on jo palautettu
     */
    fun palautaSiirto(siirronXKoordinaatti: Int, siirronYKoordinaatti: Int) {
        if (
            siirronXKoordinaatti < 0 ||
            siirronXKoordinaatti >= ristinollaPeli.pelitaulukonKoko ||
            siirronYKoordinaatti < 0 ||
            siirronYKoordinaatti >= ristinollaPeli.pelitaulukonKoko
        ) {
            throw IllegalArgumentException("Siirto on mahdoton")
        }

        val palautettavaSiirto: Pair<Int, Int> = Pair(siirronXKoordinaatti, siirronYKoordinaatti)

        if (vapaidenRuutujenKoordinaatit.contains(palautettavaSiirto)) {
            throw IllegalArgumentException("Siirto ($siirronXKoordinaatti, $siirronYKoordinaatti) on jo palautettu")
        } else {
            vapaidenRuutujenKoordinaatit.add(palautettavaSiirto)
        }
    }

    /**
     * Palauttaa mahdolliset siirrot
     * @return Joukon mahdollisia siirtoja
     */
    fun getMahdollisetSiirrot(): Set<Pair<Int, Int>> = vapaidenRuutujenKoordinaatit
}
