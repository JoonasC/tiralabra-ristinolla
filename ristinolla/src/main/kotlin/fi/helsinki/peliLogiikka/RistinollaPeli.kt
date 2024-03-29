package fi.helsinki.peliLogiikka

import fi.helsinki.apuohjelmat.liikutaHakuaOikeaVasenVinorivillaOikealle
import fi.helsinki.apuohjelmat.liikutaHakuaOikeaVasenVinorivillaVasemmalle
import fi.helsinki.apuohjelmat.liikutaHakuaPystyrivillaOikealle
import fi.helsinki.apuohjelmat.liikutaHakuaPystyrivillaVasemmalle
import fi.helsinki.apuohjelmat.liikutaHakuaVaakarivillaOikealle
import fi.helsinki.apuohjelmat.liikutaHakuaVaakarivillaVasemmalle
import fi.helsinki.apuohjelmat.liikutaHakuaVasenOikeaVinorivillaOikealle
import fi.helsinki.apuohjelmat.liikutaHakuaVasenOikeaVinorivillaVasemmalle
import kotlin.random.Random

/**
 * Luokka joka toteuttaa pelilogiikan ristinollalle
 * @author Joonas Coatanea
 * @param pelitaulukonKoko Ristinolla-pelitaulukon koko. Vähintään 3
 * @param voittoonTarvittujenValloitettujenRuutujenMaara Se määrä ruutuja joita pelaajan tarvitsee voittaakseen
 * valloittaa vaaka-, pysty- tai vinorivillä. Vähintään 3
 * @throws IllegalArgumentException Jos pelitaulukon koko on alle 3 tai voittoon tarvittujen valloitettujen ruutujen
 * määrä on alle 3
 */
class RistinollaPeli(val pelitaulukonKoko: Int, val voittoonTarvittujenValloitettujenRuutujenMaara: Int) {
    /**
     * Pelin voittotilanne
     */
    var voittotilanne: Voittotilanne = Voittotilanne(VoittotilanneTyyppi.EI_VOITTOA)
        private set

    /**
     * Se pelaaja, kenen vuoro on tehdä siirto
     */
    var pelaajanVuoro = Random.nextInt(0, 2)
        private set

    private var siirtojaJaljella: Int = (pelitaulukonKoko * pelitaulukonKoko)
    private val pelitaulukko: Array<IntArray> = Array(pelitaulukonKoko) {
        IntArray(pelitaulukonKoko) { -1 }
    }

    init {
        if (pelitaulukonKoko < 3) {
            throw IllegalArgumentException("Pelitaulukon koon täytyy olla vähintään 3")
        }
        if (voittoonTarvittujenValloitettujenRuutujenMaara < 3) {
            throw IllegalArgumentException("Voittoon tarvittujen valloitettujen ruutujen määrän täytyy olla vähintään 3")
        }
    }

    /**
     * Nollaa pelin
     */
    fun nollaa() {
        val pelitaulukonKokoLukuvali: IntRange = (0 until pelitaulukonKoko)

        voittotilanne = Voittotilanne(VoittotilanneTyyppi.EI_VOITTOA)
        siirtojaJaljella = (pelitaulukonKoko * pelitaulukonKoko)
        pelitaulukonKokoLukuvali.forEach { rivi ->
            pelitaulukonKokoLukuvali.forEach { sarake ->
                pelitaulukko[rivi][sarake] = -1
            }
        }
    }

    private fun laskeValloitettujenRuutujenMaara(
        lahtoRuudunXKoordinaatti: Int,
        lahtoRuudunYKoordinaatti: Int,
        liikutaHakuaVasemmalle: (Int, Int) -> Pair<Int, Int>,
        liikutaHakuaOikealle: (Int, Int) -> Pair<Int, Int>,
    ): Int {
        var valloitettujenRuutujenMaara = 1

        var liikuVasemmalle = true
        var liikuOikealle = true
        var (vasemmaltaTutkittavanRuudunXKoordinaatti: Int, vasemmaltaTutkittavanRuudunYKoordinaatti: Int) =
            liikutaHakuaVasemmalle(lahtoRuudunXKoordinaatti, lahtoRuudunYKoordinaatti)
        var (oikealtaTutkittavanRuudunXKoordinaatti: Int, oikealtaTutkittavanRuudunYKoordinaatti: Int) =
            liikutaHakuaOikealle(lahtoRuudunXKoordinaatti, lahtoRuudunYKoordinaatti)
        while (true) {
            if (
                liikuVasemmalle &&
                vasemmaltaTutkittavanRuudunXKoordinaatti < 0 ||
                vasemmaltaTutkittavanRuudunYKoordinaatti < 0 ||
                vasemmaltaTutkittavanRuudunXKoordinaatti >= pelitaulukonKoko ||
                vasemmaltaTutkittavanRuudunYKoordinaatti >= pelitaulukonKoko
            ) {
                liikuVasemmalle = false
            }
            if (
                liikuOikealle &&
                oikealtaTutkittavanRuudunXKoordinaatti < 0 ||
                oikealtaTutkittavanRuudunYKoordinaatti < 0 ||
                oikealtaTutkittavanRuudunXKoordinaatti >= pelitaulukonKoko ||
                oikealtaTutkittavanRuudunYKoordinaatti >= pelitaulukonKoko
            ) {
                liikuOikealle = false
            }

            if (
                liikuVasemmalle &&
                pelitaulukko[vasemmaltaTutkittavanRuudunYKoordinaatti][vasemmaltaTutkittavanRuudunXKoordinaatti] == pelaajanVuoro
            ) {
                valloitettujenRuutujenMaara++
                if (valloitettujenRuutujenMaara == voittoonTarvittujenValloitettujenRuutujenMaara) {
                    break
                }
            } else if (liikuVasemmalle) {
                liikuVasemmalle = false
            }
            if (
                liikuOikealle &&
                pelitaulukko[oikealtaTutkittavanRuudunYKoordinaatti][oikealtaTutkittavanRuudunXKoordinaatti] == pelaajanVuoro
            ) {
                valloitettujenRuutujenMaara++
                if (valloitettujenRuutujenMaara == voittoonTarvittujenValloitettujenRuutujenMaara) {
                    break
                }
            } else if (liikuOikealle) {
                liikuOikealle = false
            }

            if (!liikuVasemmalle && !liikuOikealle) {
                break
            }
            if (liikuVasemmalle) {
                liikutaHakuaVasemmalle(
                    vasemmaltaTutkittavanRuudunXKoordinaatti,
                    vasemmaltaTutkittavanRuudunYKoordinaatti,
                ).let {
                    vasemmaltaTutkittavanRuudunXKoordinaatti = it.first
                    vasemmaltaTutkittavanRuudunYKoordinaatti = it.second
                }
            }
            if (liikuOikealle) {
                liikutaHakuaOikealle(
                    oikealtaTutkittavanRuudunXKoordinaatti,
                    oikealtaTutkittavanRuudunYKoordinaatti,
                ).let {
                    oikealtaTutkittavanRuudunXKoordinaatti = it.first
                    oikealtaTutkittavanRuudunYKoordinaatti = it.second
                }
            }
        }

        return valloitettujenRuutujenMaara
    }

    private fun tarkistaVoittikoSiirronTehnytPelaaja(siirronXKoordinaatti: Int, siirronYKoordinaatti: Int): Boolean {
        val vaakarivillaOlevienValloitettujenRuutujenMaara: Int = laskeValloitettujenRuutujenMaara(
            siirronXKoordinaatti,
            siirronYKoordinaatti,
            ::liikutaHakuaVaakarivillaVasemmalle,
            ::liikutaHakuaVaakarivillaOikealle,
        )
        if (vaakarivillaOlevienValloitettujenRuutujenMaara == voittoonTarvittujenValloitettujenRuutujenMaara) {
            return true
        }

        val pystyrivillaOlevienValloitettujenRuutujenMaara: Int = laskeValloitettujenRuutujenMaara(
            siirronXKoordinaatti,
            siirronYKoordinaatti,
            ::liikutaHakuaPystyrivillaVasemmalle,
            ::liikutaHakuaPystyrivillaOikealle,
        )
        if (pystyrivillaOlevienValloitettujenRuutujenMaara == voittoonTarvittujenValloitettujenRuutujenMaara) {
            return true
        }

        val vasenOikeaVinorivillaOlevienValloitettujenRuutujenMaara: Int = laskeValloitettujenRuutujenMaara(
            siirronXKoordinaatti,
            siirronYKoordinaatti,
            ::liikutaHakuaVasenOikeaVinorivillaVasemmalle,
            ::liikutaHakuaVasenOikeaVinorivillaOikealle,
        )
        if (vasenOikeaVinorivillaOlevienValloitettujenRuutujenMaara == voittoonTarvittujenValloitettujenRuutujenMaara) {
            return true
        }

        val oikeaVasenVinorivillaOlevienValloitettujenRuutujenMaara: Int = laskeValloitettujenRuutujenMaara(
            siirronXKoordinaatti,
            siirronYKoordinaatti,
            ::liikutaHakuaOikeaVasenVinorivillaVasemmalle,
            ::liikutaHakuaOikeaVasenVinorivillaOikealle,
        )
        if (oikeaVasenVinorivillaOlevienValloitettujenRuutujenMaara == voittoonTarvittujenValloitettujenRuutujenMaara) {
            return true
        }

        return false
    }

    /**
     * Tekee siirron ja palauttaa pelin voittotilanteen
     * @param siirronXKoordinaatti Siirron X-koordinaatti
     * @param siirronYKoordinaatti Siirron Y-koordinaatti
     * @return Pelin voittotilanteen
     * @throws IllegalStateException Jos peli on loppunut
     * @throws IllegalArgumentException Jos siirtoa yritetään tehdä jo-valloitettuun ruutuun tai siirron koordinaatit
     * sijoittuvat pelitaulukon ulkopuolelle
     */
    fun teeSiirto(siirronXKoordinaatti: Int, siirronYKoordinaatti: Int): Voittotilanne {
        if (voittotilanne.tyyppi != VoittotilanneTyyppi.EI_VOITTOA) {
            throw IllegalStateException("Peli on loppunut")
        }
        if (
            siirronXKoordinaatti < 0 ||
            siirronXKoordinaatti >= pelitaulukonKoko ||
            siirronYKoordinaatti < 0 ||
            siirronYKoordinaatti >= pelitaulukonKoko
        ) {
            throw IllegalArgumentException("Siirron koordinaattien täytyy sijoittua pelitaulukon sisään")
        }
        if (pelitaulukko[siirronYKoordinaatti][siirronXKoordinaatti] != -1) {
            throw IllegalArgumentException("Siirtoa ei ole mahdollista tehdä jo-valloitettuun ruutuun")
        }

        pelitaulukko[siirronYKoordinaatti][siirronXKoordinaatti] = pelaajanVuoro
        siirtojaJaljella--

        if (tarkistaVoittikoSiirronTehnytPelaaja(siirronXKoordinaatti, siirronYKoordinaatti)) {
            voittotilanne = Voittotilanne(VoittotilanneTyyppi.VOITTO, pelaajanVuoro)
        }
        if (siirtojaJaljella == 0) {
            voittotilanne = Voittotilanne(VoittotilanneTyyppi.TASAPELI)
        }

        pelaajanVuoro = (1 - pelaajanVuoro)

        return voittotilanne
    }

    /**
     * Palauttaa pelitaulukon
     * @return Pelitaulukon
     */
    fun getPelitaulukko(): Array<IntArray> = Array(pelitaulukonKoko) { pelitaulukko[it].copyOf() }
}
