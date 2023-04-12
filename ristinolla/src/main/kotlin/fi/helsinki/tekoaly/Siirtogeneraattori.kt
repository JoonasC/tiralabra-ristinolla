package fi.helsinki.tekoaly

import fi.helsinki.apuohjelmat.liikutaHakuaOikeaVasenVinorivillaOikealle
import fi.helsinki.apuohjelmat.liikutaHakuaOikeaVasenVinorivillaVasemmalle
import fi.helsinki.apuohjelmat.liikutaHakuaPystyrivillaOikealle
import fi.helsinki.apuohjelmat.liikutaHakuaPystyrivillaVasemmalle
import fi.helsinki.apuohjelmat.liikutaHakuaVaakarivillaOikealle
import fi.helsinki.apuohjelmat.liikutaHakuaVaakarivillaVasemmalle
import fi.helsinki.apuohjelmat.liikutaHakuaVasenOikeaVinorivillaOikealle
import fi.helsinki.apuohjelmat.liikutaHakuaVasenOikeaVinorivillaVasemmalle
import fi.helsinki.peliLogiikka.RistinollaPeli
import fi.helsinki.peliLogiikka.VoittotilanneTyyppi
import kotlin.math.max
import kotlin.math.min

/**
 * Luokka joka pitää kirjaa siitä minkälaisia siirtoja on mahdollista tehdä, ja mikä siirto on paras
 * (heuristiikkafunktion mukaan) joko maksimoivalle tai minimoivalle pelaajalle
 * @author Joonas Coatanea
 * @param ristinollaPeli Peli jonka mahdollisista siirroista tulee pitää kirjaa
 * @param maksimoivaPelaaja Se pelaaja, joka on maksimoiva
 * @throws IllegalArgumentException Jos peli on jo aloitettu
 */
class Siirtogeneraattori(private val ristinollaPeli: RistinollaPeli, private val maksimoivaPelaaja: Int) {
    private val minimoivaPelaaja: Int = (1 - maksimoivaPelaaja)

    private val pelitaulukonKoko: Int = ristinollaPeli.pelitaulukonKoko

    private val voittoonTarvittujenValloitettujenRuutujenMaara: Int =
        ristinollaPeli.voittoonTarvittujenValloitettujenRuutujenMaara

    private val pelitaulukko: Array<IntArray> = ristinollaPeli.getPelitaulukko()

    private val viimeksiTehdytSiirrot: MutableList<Pair<Pair<Int, Int>, Int>> = mutableListOf()
    private val viimeksiOtetutSiirrot: MutableList<Pair<Pair<Int, Int>, Int>> = mutableListOf()

    private val mahdollistenSiirtojenHeuristisetArvot: MutableMap<Pair<Int, Int>, Double> = mutableMapOf()

    init {
        val pelitaulukonKokoLukuvali: IntRange = (0 until pelitaulukonKoko)

        pelitaulukonKokoLukuvali.forEach { ruudunYKoordinaatti ->
            pelitaulukonKokoLukuvali.forEach { ruudunXKoordinaatti ->
                if (pelitaulukko[ruudunYKoordinaatti][ruudunXKoordinaatti] != -1) {
                    throw IllegalArgumentException("Siirtogeneraattoria ei ole mahdollista luoda aloitetulle pelille")
                }
            }
        }
    }

    private fun laskeVoittoonTarvittujenValloitettujenRuutujenMaaraPelaajalle(
        pelaaja: Int,
        lahtoRuudunXKoordinaatti: Int,
        lahtoRuudunYKoordinaatti: Int,
        liikutaHakuaVasemmalle: (Int, Int) -> Pair<Int, Int>,
        liikutaHakuaOikealle: (Int, Int) -> Pair<Int, Int>,
    ): Int? {
        val vastustajanPelaaja: Int = (1 - pelaaja)

        var pieninPelaajanVoittoonTarvittujenValloitettujenRuutujenMaara: Int? = null

        var tarkastettavienRuutujenMaara = 1
        var tarkastusalueenAlkuruudunXKoordinaatti = lahtoRuudunXKoordinaatti
        var tarkastusalueenAlkuruudunYKoordinaatti = lahtoRuudunYKoordinaatti
        var tarkastusalueenLoppuruudunXKoordinaatti = lahtoRuudunXKoordinaatti
        var tarkastusalueenLoppuruudunYKoordinaatti = lahtoRuudunYKoordinaatti

        while (tarkastettavienRuutujenMaara < voittoonTarvittujenValloitettujenRuutujenMaara) {
            val (
                suurennetunTarkastusalueenAlkuruudunXKoordinaatti: Int,
                suurennetunTarkastusalueenAlkuruudunYKoordinaatti: Int,
            ) = liikutaHakuaVasemmalle(tarkastusalueenAlkuruudunXKoordinaatti, tarkastusalueenAlkuruudunYKoordinaatti)

            if (
                suurennetunTarkastusalueenAlkuruudunXKoordinaatti < 0 ||
                suurennetunTarkastusalueenAlkuruudunYKoordinaatti < 0 ||
                suurennetunTarkastusalueenAlkuruudunXKoordinaatti >= pelitaulukonKoko ||
                suurennetunTarkastusalueenAlkuruudunYKoordinaatti >= pelitaulukonKoko ||
                pelitaulukko[suurennetunTarkastusalueenAlkuruudunYKoordinaatti][suurennetunTarkastusalueenAlkuruudunXKoordinaatti] == vastustajanPelaaja
            ) {
                break
            }

            tarkastusalueenAlkuruudunXKoordinaatti = suurennetunTarkastusalueenAlkuruudunXKoordinaatti
            tarkastusalueenAlkuruudunYKoordinaatti = suurennetunTarkastusalueenAlkuruudunYKoordinaatti
            tarkastettavienRuutujenMaara++
        }

        while (true) {
            if (tarkastettavienRuutujenMaara == voittoonTarvittujenValloitettujenRuutujenMaara) {
                var tarkastusalueellaPelaajanVoittoonTarvittujenValloitettujenRuutujenMaara = 0

                var pysaytaTarkastusalueenTutkiminen = false
                var tutkittavanRuudunXKoordinaatti: Int = tarkastusalueenAlkuruudunXKoordinaatti
                var tutkittavanRuudunYKoordinaatti: Int = tarkastusalueenAlkuruudunYKoordinaatti
                while (true) {
                    if (pelitaulukko[tutkittavanRuudunYKoordinaatti][tutkittavanRuudunXKoordinaatti] == -1) {
                        tarkastusalueellaPelaajanVoittoonTarvittujenValloitettujenRuutujenMaara++
                    }

                    if (pysaytaTarkastusalueenTutkiminen) {
                        break
                    }
                    liikutaHakuaOikealle(tutkittavanRuudunXKoordinaatti, tutkittavanRuudunYKoordinaatti).let {
                        tutkittavanRuudunXKoordinaatti = it.first
                        tutkittavanRuudunYKoordinaatti = it.second
                    }

                    if (
                        tutkittavanRuudunXKoordinaatti == tarkastusalueenLoppuruudunXKoordinaatti &&
                        tutkittavanRuudunYKoordinaatti == tarkastusalueenLoppuruudunYKoordinaatti
                    ) {
                        pysaytaTarkastusalueenTutkiminen = true
                    }
                }

                if (
                    pieninPelaajanVoittoonTarvittujenValloitettujenRuutujenMaara == null ||
                    tarkastusalueellaPelaajanVoittoonTarvittujenValloitettujenRuutujenMaara < pieninPelaajanVoittoonTarvittujenValloitettujenRuutujenMaara
                ) {
                    pieninPelaajanVoittoonTarvittujenValloitettujenRuutujenMaara =
                        tarkastusalueellaPelaajanVoittoonTarvittujenValloitettujenRuutujenMaara
                }

                if (
                    tarkastusalueenAlkuruudunXKoordinaatti == lahtoRuudunXKoordinaatti &&
                    tarkastusalueenAlkuruudunYKoordinaatti == lahtoRuudunYKoordinaatti
                ) {
                    break
                }
                liikutaHakuaOikealle(
                    tarkastusalueenAlkuruudunXKoordinaatti,
                    tarkastusalueenAlkuruudunYKoordinaatti,
                ).let {
                    tarkastusalueenAlkuruudunXKoordinaatti = it.first
                    tarkastusalueenAlkuruudunYKoordinaatti = it.second
                }
            }

            val (
                suurennetunTarkastusalueenLoppuruudunXKoordinaatti: Int,
                suurennetunTarkastusalueenLoppuruudunYKoordinaatti: Int,
            ) = liikutaHakuaOikealle(tarkastusalueenLoppuruudunXKoordinaatti, tarkastusalueenLoppuruudunYKoordinaatti)

            if (
                suurennetunTarkastusalueenLoppuruudunXKoordinaatti < 0 ||
                suurennetunTarkastusalueenLoppuruudunYKoordinaatti < 0 ||
                suurennetunTarkastusalueenLoppuruudunXKoordinaatti >= pelitaulukonKoko ||
                suurennetunTarkastusalueenLoppuruudunYKoordinaatti >= pelitaulukonKoko ||
                pelitaulukko[suurennetunTarkastusalueenLoppuruudunYKoordinaatti][suurennetunTarkastusalueenLoppuruudunXKoordinaatti] == vastustajanPelaaja
            ) {
                break
            }

            tarkastusalueenLoppuruudunXKoordinaatti = suurennetunTarkastusalueenLoppuruudunXKoordinaatti
            tarkastusalueenLoppuruudunYKoordinaatti = suurennetunTarkastusalueenLoppuruudunYKoordinaatti
            if (tarkastettavienRuutujenMaara < voittoonTarvittujenValloitettujenRuutujenMaara) {
                tarkastettavienRuutujenMaara++
            }
        }

        return pieninPelaajanVoittoonTarvittujenValloitettujenRuutujenMaara
    }

    private fun laskeVoittoonTarvittujenValloitettujenRuutujenMaaranKeskiarvoPelaajalle(
        pelaaja: Int,
        lahtoRuudunXKoordinaatti: Int,
        lahtoRuudunYKoordinaatti: Int,
    ): Double? {
        var voittoonTarvittujenValloitettujenRuutujenMaarienSumma = 0.0
        var mukaanluettujenSuuntienMaara = 0.0

        val vaakariviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara: Int? =
            laskeVoittoonTarvittujenValloitettujenRuutujenMaaraPelaajalle(
                pelaaja,
                lahtoRuudunXKoordinaatti,
                lahtoRuudunYKoordinaatti,
                ::liikutaHakuaVaakarivillaVasemmalle,
                ::liikutaHakuaVaakarivillaOikealle,
            )
        if (vaakariviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara != null) {
            voittoonTarvittujenValloitettujenRuutujenMaarienSumma +=
                vaakariviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara
            mukaanluettujenSuuntienMaara++
        }

        val pystyriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara: Int? =
            laskeVoittoonTarvittujenValloitettujenRuutujenMaaraPelaajalle(
                pelaaja,
                lahtoRuudunXKoordinaatti,
                lahtoRuudunYKoordinaatti,
                ::liikutaHakuaPystyrivillaVasemmalle,
                ::liikutaHakuaPystyrivillaOikealle,
            )
        if (pystyriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara != null) {
            voittoonTarvittujenValloitettujenRuutujenMaarienSumma +=
                pystyriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara
            mukaanluettujenSuuntienMaara++
        }

        val vasenOikeaVinoriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara: Int? =
            laskeVoittoonTarvittujenValloitettujenRuutujenMaaraPelaajalle(
                pelaaja,
                lahtoRuudunXKoordinaatti,
                lahtoRuudunYKoordinaatti,
                ::liikutaHakuaVasenOikeaVinorivillaVasemmalle,
                ::liikutaHakuaVasenOikeaVinorivillaOikealle,
            )
        if (vasenOikeaVinoriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara != null) {
            voittoonTarvittujenValloitettujenRuutujenMaarienSumma +=
                vasenOikeaVinoriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara
            mukaanluettujenSuuntienMaara++
        }

        val oikeaVasenVinoriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara: Int? =
            laskeVoittoonTarvittujenValloitettujenRuutujenMaaraPelaajalle(
                pelaaja,
                lahtoRuudunXKoordinaatti,
                lahtoRuudunYKoordinaatti,
                ::liikutaHakuaOikeaVasenVinorivillaVasemmalle,
                ::liikutaHakuaOikeaVasenVinorivillaOikealle,
            )
        if (oikeaVasenVinoriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara != null) {
            voittoonTarvittujenValloitettujenRuutujenMaarienSumma +=
                oikeaVasenVinoriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara
            mukaanluettujenSuuntienMaara++
        }

        return if (mukaanluettujenSuuntienMaara == 0.0) {
            null
        } else {
            (voittoonTarvittujenValloitettujenRuutujenMaarienSumma / mukaanluettujenSuuntienMaara)
        }
    }

    private fun laskeSiirronHeuristinenArvo(
        pelaaja: Int,
        siirronXKoordinaatti: Int,
        siirronYKoordinaatti: Int,
    ): Double {
        val vastustajanPelaaja: Int = (1 - pelaaja)
        var heuristinenArvo = 0.0

        pelitaulukko[siirronYKoordinaatti][siirronXKoordinaatti] = pelaaja

        val siirronEtaisyysVoitosta: Double? = laskeVoittoonTarvittujenValloitettujenRuutujenMaaranKeskiarvoPelaajalle(
            pelaaja,
            siirronXKoordinaatti,
            siirronYKoordinaatti,
        )
        if (siirronEtaisyysVoitosta != null) {
            val siirronHyvyys: Double = if (siirronEtaisyysVoitosta == 0.0) {
                Double.MAX_VALUE
            } else {
                (1 / siirronEtaisyysVoitosta)
            }

            if (pelaaja == maksimoivaPelaaja) {
                heuristinenArvo += siirronHyvyys
            } else {
                heuristinenArvo -= siirronHyvyys
            }
        }

        if (viimeksiOtetutSiirrot.isNotEmpty() || viimeksiTehdytSiirrot.isNotEmpty()) {
            val vastustajanViimeisinSiirto: Pair<Int, Int> = (
                viimeksiOtetutSiirrot
                    .lastOrNull()
                    ?.first ?: viimeksiTehdytSiirrot
                    .last()
                    .first
                )
            val vastustajanViimeisimmanSiirronEtaisyysVoitosta: Double? =
                laskeVoittoonTarvittujenValloitettujenRuutujenMaaranKeskiarvoPelaajalle(
                    vastustajanPelaaja,
                    vastustajanViimeisinSiirto.first,
                    vastustajanViimeisinSiirto.second,
                )
            if (vastustajanViimeisimmanSiirronEtaisyysVoitosta != null) {
                val vastustajanViimeisimmanSiirronHyvyys: Double =
                    if (vastustajanViimeisimmanSiirronEtaisyysVoitosta == 0.0) {
                        Double.MAX_VALUE
                    } else {
                        (1 / vastustajanViimeisimmanSiirronEtaisyysVoitosta)
                    }

                if (vastustajanPelaaja == minimoivaPelaaja) {
                    heuristinenArvo -= vastustajanViimeisimmanSiirronHyvyys
                } else {
                    heuristinenArvo += vastustajanViimeisimmanSiirronHyvyys
                }
            }
        }

        pelitaulukko[siirronYKoordinaatti][siirronXKoordinaatti] = -1

        return heuristinenArvo
    }

    private fun laskeMahdollistenSiirtojenHeuristisetArvot(pelaaja: Int, poisluettavatSiirrot: Set<Pair<Int, Int>>) {
        mahdollistenSiirtojenHeuristisetArvot
            .keys
            .forEach { mahdollinenSiirto ->
                if (!poisluettavatSiirrot.contains(mahdollinenSiirto)) {
                    mahdollistenSiirtojenHeuristisetArvot[mahdollinenSiirto] =
                        laskeSiirronHeuristinenArvo(
                            pelaaja,
                            mahdollinenSiirto.first,
                            mahdollinenSiirto.second,
                        )
                }
            }
    }

    private fun etsiMahdollisetSiirrot() {
        val etsintaAlueenKoko = 2

        mahdollistenSiirtojenHeuristisetArvot.clear()
        (viimeksiTehdytSiirrot + viimeksiOtetutSiirrot).forEach { (viimeksiTehtyTaiOtettuSiirto, _) ->
            val etsintaAlueenYlaVasemmanRuudunXKoordinaatti: Int = max(
                (viimeksiTehtyTaiOtettuSiirto.first - (etsintaAlueenKoko - 1)),
                0,
            )
            val etsintaAlueenYlaVasemmanRuudunYKoordinaatti: Int = max(
                (viimeksiTehtyTaiOtettuSiirto.second - (etsintaAlueenKoko - 1)),
                0,
            )
            val etsintaAlueenAlaOikeanRuudunXKoordinaatti: Int = min(
                (viimeksiTehtyTaiOtettuSiirto.first + (etsintaAlueenKoko - 1)),
                (pelitaulukonKoko - 1),
            )
            val etsintaAlueenAlaOikeanRuudunYKoordinaatti: Int = min(
                (viimeksiTehtyTaiOtettuSiirto.second + (etsintaAlueenKoko - 1)),
                (pelitaulukonKoko - 1),
            )

            (etsintaAlueenYlaVasemmanRuudunYKoordinaatti..etsintaAlueenAlaOikeanRuudunYKoordinaatti)
                .forEach { etsittavanRuudunYKoordinaatti ->
                    (etsintaAlueenYlaVasemmanRuudunXKoordinaatti..etsintaAlueenAlaOikeanRuudunXKoordinaatti)
                        .forEach { etsittavanRuudunXKoordinaatti ->
                            val etsittavaRuutu: Pair<Int, Int> =
                                Pair(etsittavanRuudunXKoordinaatti, etsittavanRuudunYKoordinaatti)

                            if (
                                !mahdollistenSiirtojenHeuristisetArvot.containsKey(etsittavaRuutu) &&
                                pelitaulukko[etsittavaRuutu.second][etsittavaRuutu.first] == -1
                            ) {
                                mahdollistenSiirtojenHeuristisetArvot[etsittavaRuutu] = 0.0
                            }
                        }
                }
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
     * Ottaa edellisiä siirtoja seuraavan parhaan (heuristiikkafunktion mukaan) mahdollisen siirron pois mahdollisten
     * siirtojen joukosta ja palauttaa sen heuristisen arvon kera
     * @param edellisetSiirrot Edelliset siirrot
     * @return Edellisiä siirtoja seuraavan parhaan mahdollisen siirron ja sen heuristisen arvon tai null jos sitä ei ole
     * @throws IllegalStateException Jos peli on loppunut tai kaikki mahdolliset siirrot on jo otettu
     */
    fun otaSeuraavaParasMahdollinenSiirto(edellisetSiirrot: Set<Pair<Int, Int>>): Pair<Pair<Int, Int>, Double>? {
        tarkistaEtteiPeliOleLoppunut()
        etsiMahdollisetSiirrot()
        if (
            (viimeksiTehdytSiirrot.isNotEmpty() || viimeksiOtetutSiirrot.isNotEmpty()) &&
            mahdollistenSiirtojenHeuristisetArvot.isEmpty()
        ) {
            throw IllegalStateException("Kaikki mahdolliset siirrot on jo otettu")
        }

        if (edellisetSiirrot.size == mahdollistenSiirtojenHeuristisetArvot.size) {
            return null
        }

        val pelaajanVuoro: Int = if (viimeksiTehdytSiirrot.isNotEmpty() || viimeksiOtetutSiirrot.isNotEmpty()) {
            (
                1 - (
                    viimeksiOtetutSiirrot
                        .lastOrNull()
                        ?.second ?: viimeksiTehdytSiirrot
                        .last()
                        .second
                    )
                )
        } else {
            maksimoivaPelaaja
        }

        laskeMahdollistenSiirtojenHeuristisetArvot(pelaajanVuoro, edellisetSiirrot)

        val (parasMahdollinenSiirto: Pair<Int, Int>, parhaanMahdollisenSiirronHeuristinenArvo: Double) =
            if (pelaajanVuoro == maksimoivaPelaaja) {
                var parasMahdollinenSiirtoJaSenHeuristinenArvo: Map.Entry<Pair<Int, Int>, Double>? = null
                mahdollistenSiirtojenHeuristisetArvot.forEach { siirtoJaSenHeuristinenArvo ->
                    if (
                        !edellisetSiirrot.contains(siirtoJaSenHeuristinenArvo.key) &&
                        (
                            parasMahdollinenSiirtoJaSenHeuristinenArvo == null ||
                                siirtoJaSenHeuristinenArvo.value > (parasMahdollinenSiirtoJaSenHeuristinenArvo?.value as Double)
                            )
                    ) {
                        parasMahdollinenSiirtoJaSenHeuristinenArvo = siirtoJaSenHeuristinenArvo
                    }
                }

                (parasMahdollinenSiirtoJaSenHeuristinenArvo as Map.Entry<Pair<Int, Int>, Double>)
            } else {
                var parasMahdollinenSiirtoJaSenHeuristinenArvo: Map.Entry<Pair<Int, Int>, Double>? = null
                mahdollistenSiirtojenHeuristisetArvot.forEach { siirtoJaSenHeuristinenArvo ->
                    if (
                        !edellisetSiirrot.contains(siirtoJaSenHeuristinenArvo.key) &&
                        (
                            parasMahdollinenSiirtoJaSenHeuristinenArvo == null ||
                                siirtoJaSenHeuristinenArvo.value < (parasMahdollinenSiirtoJaSenHeuristinenArvo?.value as Double)
                            )
                    ) {
                        parasMahdollinenSiirtoJaSenHeuristinenArvo = siirtoJaSenHeuristinenArvo
                    }
                }

                (parasMahdollinenSiirtoJaSenHeuristinenArvo as Map.Entry<Pair<Int, Int>, Double>)
            }

        pelitaulukko[parasMahdollinenSiirto.second][parasMahdollinenSiirto.first] = pelaajanVuoro
        viimeksiOtetutSiirrot.add(Pair(parasMahdollinenSiirto, pelaajanVuoro))

        return Pair(parasMahdollinenSiirto, parhaanMahdollisenSiirronHeuristinenArvo)
    }

    /**
     * Palauttaa siirron mahdollisten siirtojen joukkoon
     * @param siirronXKoordinaatti Palautettavan siirron X-koordinaatti
     * @param siirronYKoordinaatti Palautettavan siirron Y-koordinaatti
     * @throws IllegalStateException Jos peli on loppunut
     * @throws IllegalArgumentException Jos siirto on mahdoton tai siirtoa ei voi palauttaa
     */
    fun palautaSiirto(siirronXKoordinaatti: Int, siirronYKoordinaatti: Int) {
        tarkistaEtteiPeliOleLoppunut()
        if (
            siirronXKoordinaatti < 0 ||
            siirronXKoordinaatti >= pelitaulukonKoko ||
            siirronYKoordinaatti < 0 ||
            siirronYKoordinaatti >= pelitaulukonKoko
        ) {
            throw IllegalArgumentException("Siirto on mahdoton")
        }

        val palautettavaSiirto: Pair<Int, Int> = Pair(siirronXKoordinaatti, siirronYKoordinaatti)

        if (
            viimeksiOtetutSiirrot
                .lastOrNull()
                ?.first != palautettavaSiirto
        ) {
            throw IllegalArgumentException("Siirtoa ei voi palauttaa")
        }

        pelitaulukko[palautettavaSiirto.second][palautettavaSiirto.first] = -1
        viimeksiOtetutSiirrot.removeLast()
    }

    private fun tarkistaVoittikoSiirronOttanutPelaaja(
        pelaaja: Int,
        siirronXKoordinaatti: Int,
        siirronYKoordinaatti: Int,
    ): Boolean {
        val vaakariviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara: Int? =
            laskeVoittoonTarvittujenValloitettujenRuutujenMaaraPelaajalle(
                pelaaja,
                siirronXKoordinaatti,
                siirronYKoordinaatti,
                ::liikutaHakuaVaakarivillaVasemmalle,
                ::liikutaHakuaVaakarivillaOikealle,
            )
        if (vaakariviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara == 0) {
            return true
        }

        val pystyriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara: Int? =
            laskeVoittoonTarvittujenValloitettujenRuutujenMaaraPelaajalle(
                pelaaja,
                siirronXKoordinaatti,
                siirronYKoordinaatti,
                ::liikutaHakuaPystyrivillaVasemmalle,
                ::liikutaHakuaPystyrivillaOikealle,
            )
        if (pystyriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara == 0) {
            return true
        }

        val vasenOikeaVinoriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara: Int? =
            laskeVoittoonTarvittujenValloitettujenRuutujenMaaraPelaajalle(
                pelaaja,
                siirronXKoordinaatti,
                siirronYKoordinaatti,
                ::liikutaHakuaVasenOikeaVinorivillaVasemmalle,
                ::liikutaHakuaVasenOikeaVinorivillaOikealle,
            )
        if (vasenOikeaVinoriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara == 0) {
            return true
        }

        val oikeaVasenVinoriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara: Int? =
            laskeVoittoonTarvittujenValloitettujenRuutujenMaaraPelaajalle(
                pelaaja,
                siirronXKoordinaatti,
                siirronYKoordinaatti,
                ::liikutaHakuaOikeaVasenVinorivillaVasemmalle,
                ::liikutaHakuaOikeaVasenVinorivillaOikealle,
            )
        if (oikeaVasenVinoriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara == 0) {
            return true
        }

        return false
    }

    /**
     * Palauttaa otettujen siirtojen perusteella pelatun pelin päättymistilanteen ja voittoarvon
     * @return Otettujen siirtojen perusteella pelatun pelin päättymistilanteen ja voittoarvon
     * @throws IllegalStateException Jos peli on loppunut tai otettuja siirtoja ei ole
     */
    fun getPelinPaattymistilanneJaVoittoarvo(): Pair<Boolean, Double> {
        tarkistaEtteiPeliOleLoppunut()

        val (viimeksiOtettuSiirto: Pair<Int, Int>, viimeksiOtetunSiirronOttanutPelaaja: Int) =
            viimeksiOtetutSiirrot.lastOrNull() ?: throw IllegalStateException("Otettuja siirtoja ei ole")
        if (
            tarkistaVoittikoSiirronOttanutPelaaja(
                viimeksiOtetunSiirronOttanutPelaaja,
                viimeksiOtettuSiirto.first,
                viimeksiOtettuSiirto.second,
            )
        ) {
            val voittoarvo: Double = if (viimeksiOtetunSiirronOttanutPelaaja == maksimoivaPelaaja) {
                Double.MAX_VALUE
            } else {
                -(Double.MAX_VALUE)
            }
            return Pair(true, voittoarvo)
        }
        if (mahdollistenSiirtojenHeuristisetArvot.isEmpty()) {
            return Pair(true, 0.0)
        }

        return Pair(false, 0.0)
    }

    /**
     * Merkitsee tehdyn siirron (pysyvästi toisin kuin ottaminen ja palauttaminen)
     * @param siirronXKoordinaatti Merkittävän siirron X-koordinaatti
     * @param siirronYKoordinaatti Merkittävän siirron Y-koordinaatti
     * @param maksimoivanPelaajanSiirto Boolean-arvo joka merkitsee sitä, onko merkittävä siirto maksimoivan pelaajan
     * tekemä
     * @throws IllegalStateException Jos peli on loppunut tai kaikkia otettuja siirtoja ei ole palautettu
     * @throws IllegalArgumentException Jos siirto on mahdoton tai siirtoa yritetään tehdä jo-valloitettuun ruutuun
     */
    fun merkitseSiirto(siirronXKoordinaatti: Int, siirronYKoordinaatti: Int, maksimoivanPelaajanSiirto: Boolean) {
        tarkistaEtteiPeliOleLoppunut()
        if (
            siirronXKoordinaatti < 0 ||
            siirronXKoordinaatti >= pelitaulukonKoko ||
            siirronYKoordinaatti < 0 ||
            siirronYKoordinaatti >= pelitaulukonKoko
        ) {
            throw IllegalArgumentException("Siirto on mahdoton")
        }
        if (viimeksiOtetutSiirrot.isNotEmpty()) {
            throw IllegalStateException("Kaikkia otettuja siirtoja ei ole palautettu")
        }
        if (pelitaulukko[siirronYKoordinaatti][siirronXKoordinaatti] != -1) {
            throw IllegalArgumentException("Siirtoa ei ole mahdollista tehdä jo-valloitettuun ruutuun")
        }

        val pelaaja: Int = if (maksimoivanPelaajanSiirto) {
            maksimoivaPelaaja
        } else {
            minimoivaPelaaja
        }
        val merkittavaSiirto: Pair<Int, Int> = Pair(siirronXKoordinaatti, siirronYKoordinaatti)

        pelitaulukko[merkittavaSiirto.second][merkittavaSiirto.first] = pelaaja
        viimeksiTehdytSiirrot.add(Pair(merkittavaSiirto, pelaaja))
    }
}
