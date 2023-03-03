package fi.helsinki.tekoaly

import fi.helsinki.peliLogiikka.RistinollaPeli
import fi.helsinki.peliLogiikka.VoittotilanneTyyppi

/**
 * Luokka joka pitää kirjaa siitä minkälaisia siirtoja on mahdollista tehdä, ja mikä siirto on paras
 * (heuristiikkafunktion mukaan) joko maksimoivalle tai minimoivalle pelaajalle
 * @author Joonas Coatanea
 * @param ristinollaPeli Peli jonka mahdollisista siirroista tulee pitää kirjaa
 * @param maksimoivaPelaaja Se pelaaja, joka on maksimoiva
 */
class Siirtogeneraattori(private val ristinollaPeli: RistinollaPeli, private val maksimoivaPelaaja: Int) {
    private val minimoivaPelaaja: Int = (1 - maksimoivaPelaaja)

    private val pelitaulukonKoko: Int = ristinollaPeli.pelitaulukonKoko

    private val voittoonTarvittujenValloitettujenRuutujenMaara: Int =
        ristinollaPeli.voittoonTarvittujenValloitettujenRuutujenMaara

    private val pelitaulukko: Array<IntArray> = ristinollaPeli.getPelitaulukko()

    private val viimeksiOtetutSiirrot: MutableList<Pair<Pair<Int, Int>, Int>> = mutableListOf()

    private val vapaidenRuutujenKoordinaatit: MutableList<Pair<Int, Int>> = mutableListOf()
    private val vapaisiinRuutuihinTehtavienSiirtojenHeuristisetArvot: MutableMap<Pair<Int, Int>, Double> =
        mutableMapOf()

    init {
        val pelitaulukonKokoLukuvali: IntRange = (0 until pelitaulukonKoko)

        pelitaulukonKokoLukuvali.forEach { ruudunYKoordinaatti ->
            pelitaulukonKokoLukuvali.forEach { ruudunXKoordinaatti ->
                if (pelitaulukko[ruudunYKoordinaatti][ruudunXKoordinaatti] == -1) {
                    vapaidenRuutujenKoordinaatit.add(Pair(ruudunXKoordinaatti, ruudunYKoordinaatti))
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

        var potentiaalinenValloitettujenRuutujenMaara = 1
        var pelaajanVoittoonTarvittujenValloitettujenRuutujenMaara = 0

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
                pelitaulukko[vasemmaltaTutkittavanRuudunYKoordinaatti][vasemmaltaTutkittavanRuudunXKoordinaatti] != vastustajanPelaaja
            ) {
                potentiaalinenValloitettujenRuutujenMaara++

                if (pelitaulukko[vasemmaltaTutkittavanRuudunYKoordinaatti][vasemmaltaTutkittavanRuudunXKoordinaatti] == -1) {
                    pelaajanVoittoonTarvittujenValloitettujenRuutujenMaara++
                }
            } else if (liikuVasemmalle) {
                liikuVasemmalle = false
            }
            if (
                liikuOikealle &&
                pelitaulukko[oikealtaTutkittavanRuudunYKoordinaatti][oikealtaTutkittavanRuudunXKoordinaatti] != vastustajanPelaaja
            ) {
                potentiaalinenValloitettujenRuutujenMaara++

                if (pelitaulukko[oikealtaTutkittavanRuudunYKoordinaatti][oikealtaTutkittavanRuudunXKoordinaatti] == -1) {
                    pelaajanVoittoonTarvittujenValloitettujenRuutujenMaara++
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

        return if (potentiaalinenValloitettujenRuutujenMaara < voittoonTarvittujenValloitettujenRuutujenMaara) {
            null
        } else {
            pelaajanVoittoonTarvittujenValloitettujenRuutujenMaara
        }
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
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair((ruudunXKoordinaatti - 1), ruudunYKoordinaatti)
                },
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair((ruudunXKoordinaatti + 1), ruudunYKoordinaatti)
                },
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
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair(ruudunXKoordinaatti, (ruudunYKoordinaatti - 1))
                },
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair(ruudunXKoordinaatti, (ruudunYKoordinaatti + 1))
                },
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
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair((ruudunXKoordinaatti - 1), (ruudunYKoordinaatti - 1))
                },
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair((ruudunXKoordinaatti + 1), (ruudunYKoordinaatti + 1))
                },
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
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair((ruudunXKoordinaatti - 1), (ruudunYKoordinaatti + 1))
                },
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair((ruudunXKoordinaatti + 1), (ruudunYKoordinaatti - 1))
                },
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

        if (viimeksiOtetutSiirrot.isNotEmpty()) {
            val vastustajanViimeisinSiirto: Pair<Int, Int> = viimeksiOtetutSiirrot
                .last()
                .first
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
        vapaidenRuutujenKoordinaatit.forEach { vapaanRuudunKoordinaatit ->
            if (!poisluettavatSiirrot.contains(vapaanRuudunKoordinaatit)) {
                vapaisiinRuutuihinTehtavienSiirtojenHeuristisetArvot[vapaanRuudunKoordinaatit] =
                    laskeSiirronHeuristinenArvo(
                        pelaaja,
                        vapaanRuudunKoordinaatit.first,
                        vapaanRuudunKoordinaatit.second,
                    )
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
        if (vapaidenRuutujenKoordinaatit.isEmpty()) {
            throw IllegalStateException("Kaikki mahdolliset siirrot on jo otettu")
        }

        if (edellisetSiirrot.size == vapaidenRuutujenKoordinaatit.size) {
            return null
        }

        val pelaajanVuoro: Int = if (viimeksiOtetutSiirrot.isNotEmpty()) {
            (
                1 - viimeksiOtetutSiirrot
                    .last()
                    .second
                )
        } else {
            maksimoivaPelaaja
        }

        laskeMahdollistenSiirtojenHeuristisetArvot(pelaajanVuoro, edellisetSiirrot)

        val (parasMahdollinenSiirto: Pair<Int, Int>, parhaanMahdollisenSiirronHeuristinenArvo: Double) =
            if (pelaajanVuoro == maksimoivaPelaaja) {
                var parasMahdollinenSiirtoJaSenHeuristinenArvo: Map.Entry<Pair<Int, Int>, Double>? = null
                vapaisiinRuutuihinTehtavienSiirtojenHeuristisetArvot.forEach { siirtoJaSenHeuristinenArvo ->
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
                vapaisiinRuutuihinTehtavienSiirtojenHeuristisetArvot.forEach { siirtoJaSenHeuristinenArvo ->
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
        vapaidenRuutujenKoordinaatit.remove(parasMahdollinenSiirto)
        vapaisiinRuutuihinTehtavienSiirtojenHeuristisetArvot.remove(
            parasMahdollinenSiirto,
            parhaanMahdollisenSiirronHeuristinenArvo,
        )

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
            siirronXKoordinaatti >= ristinollaPeli.pelitaulukonKoko ||
            siirronYKoordinaatti < 0 ||
            siirronYKoordinaatti >= ristinollaPeli.pelitaulukonKoko
        ) {
            throw IllegalArgumentException("Siirto on mahdoton")
        }

        val palautettavaSiirto: Pair<Int, Int> = Pair(siirronXKoordinaatti, siirronYKoordinaatti)

        if (
            viimeksiOtetutSiirrot
                .last()
                .first != palautettavaSiirto ||
            viimeksiOtetutSiirrot.size <= 1
        ) {
            throw IllegalArgumentException("Siirtoa ei voi palauttaa")
        }

        vapaidenRuutujenKoordinaatit.add(palautettavaSiirto)
        vapaisiinRuutuihinTehtavienSiirtojenHeuristisetArvot[palautettavaSiirto] = 0.0
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
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair((ruudunXKoordinaatti - 1), ruudunYKoordinaatti)
                },
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair((ruudunXKoordinaatti + 1), ruudunYKoordinaatti)
                },
            )
        if (vaakariviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara == 0) {
            return true
        }

        val pystyriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara: Int? =
            laskeVoittoonTarvittujenValloitettujenRuutujenMaaraPelaajalle(
                pelaaja,
                siirronXKoordinaatti,
                siirronYKoordinaatti,
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair(ruudunXKoordinaatti, (ruudunYKoordinaatti - 1))
                },
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair(ruudunXKoordinaatti, (ruudunYKoordinaatti + 1))
                },
            )
        if (pystyriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara == 0) {
            return true
        }

        val vasenOikeaVinoriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara: Int? =
            laskeVoittoonTarvittujenValloitettujenRuutujenMaaraPelaajalle(
                pelaaja,
                siirronXKoordinaatti,
                siirronYKoordinaatti,
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair((ruudunXKoordinaatti - 1), (ruudunYKoordinaatti - 1))
                },
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair((ruudunXKoordinaatti + 1), (ruudunYKoordinaatti + 1))
                },
            )
        if (vasenOikeaVinoriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara == 0) {
            return true
        }

        val oikeaVasenVinoriviSuunnastaVoittoonTarvittujenValloitettujenRuutujenMaara: Int? =
            laskeVoittoonTarvittujenValloitettujenRuutujenMaaraPelaajalle(
                pelaaja,
                siirronXKoordinaatti,
                siirronYKoordinaatti,
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair((ruudunXKoordinaatti - 1), (ruudunYKoordinaatti + 1))
                },
                { ruudunXKoordinaatti, ruudunYKoordinaatti ->
                    Pair((ruudunXKoordinaatti + 1), (ruudunYKoordinaatti - 1))
                },
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
        if (viimeksiOtetutSiirrot.size <= 1) {
            throw IllegalStateException("Otettuja siirtoja ei ole")
        }

        val (viimeksiOtettuSiirto: Pair<Int, Int>, viimeksiOtetunSiirronOttanutPelaaja: Int) =
            viimeksiOtetutSiirrot.last()
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
        if (vapaidenRuutujenKoordinaatit.isEmpty()) {
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
            siirronXKoordinaatti >= ristinollaPeli.pelitaulukonKoko ||
            siirronYKoordinaatti < 0 ||
            siirronYKoordinaatti >= ristinollaPeli.pelitaulukonKoko
        ) {
            throw IllegalArgumentException("Siirto on mahdoton")
        }
        if (viimeksiOtetutSiirrot.size > 1) {
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

        vapaidenRuutujenKoordinaatit.remove(merkittavaSiirto)
        vapaisiinRuutuihinTehtavienSiirtojenHeuristisetArvot.remove(merkittavaSiirto)
        pelitaulukko[merkittavaSiirto.second][merkittavaSiirto.first] = pelaaja

        if (!maksimoivanPelaajanSiirto) {
            viimeksiOtetutSiirrot[0] = Pair(merkittavaSiirto, pelaaja)
        }
    }
}
