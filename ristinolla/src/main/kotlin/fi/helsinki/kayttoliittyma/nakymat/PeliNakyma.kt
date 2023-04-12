package fi.helsinki.kayttoliittyma.nakymat

import fi.helsinki.peliLogiikka.RistinollaPeli
import fi.helsinki.peliLogiikka.VoittotilanneTyyppi
import fi.helsinki.tekoaly.Siirtogeneraattori
import fi.helsinki.tekoaly.Tekoaly
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.Parent
import tornadofx.View
import tornadofx.action
import tornadofx.button
import tornadofx.hbox
import tornadofx.label
import tornadofx.vbox

class PeliNakyma : View() {
    private val pelitaulukonKoko = 25
    private val voittoonTarvittujenValloitettujenRuutujenMaara = 5
    private val etsintasyvyys = 6

    private val ristinollaPeli = RistinollaPeli(pelitaulukonKoko, voittoonTarvittujenValloitettujenRuutujenMaara)
    private var tekoaly = Tekoaly(etsintasyvyys, ristinollaPeli, Siirtogeneraattori(ristinollaPeli, 1))

    private var taustaoperaatioMeneillaan = false
    private val peliOnPaattynyt: Boolean
        get() = (
            ristinollaPeli
                .voittotilanne
                .tyyppi != VoittotilanneTyyppi.EI_VOITTOA
            )

    private val ruutujenSisallot: Array<Array<SimpleStringProperty>> = Array(pelitaulukonKoko) {
        Array(pelitaulukonKoko) { SimpleStringProperty(" ") }
    }

    private val voittoilmoittajanSisalto = SimpleStringProperty()

    override val root: Parent = vbox {
        alignment = Pos.CENTER

        val pelitaulukonKokoLukuvali: IntRange = (0 until pelitaulukonKoko)

        pelitaulukonKokoLukuvali.forEach { rivi ->
            hbox {
                alignment = Pos.CENTER

                pelitaulukonKokoLukuvali.forEach { sarake ->
                    button(ruutujenSisallot[rivi][sarake]) {
                        prefWidth = 100.0
                        prefHeight = 100.0

                        action {
                            if (!taustaoperaatioMeneillaan && !peliOnPaattynyt && ruutujenSisallot[rivi][sarake].value == " ") {
                                teeSiirto(sarake, rivi)
                            }
                        }
                    }
                }
            }
        }
        label(voittoilmoittajanSisalto)
        button("Nollaa") {
            action {
                if (!taustaoperaatioMeneillaan) {
                    ristinollaPeli.nollaa()
                    tekoaly = Tekoaly(etsintasyvyys, ristinollaPeli, Siirtogeneraattori(ristinollaPeli, 1))

                    pelitaulukonKokoLukuvali.forEach { rivi ->
                        pelitaulukonKokoLukuvali.forEach { sarake ->
                            ruutujenSisallot[rivi][sarake].value = " "
                        }
                    }
                    voittoilmoittajanSisalto.value = ""

                    if (ristinollaPeli.pelaajanVuoro == 1) {
                        teeTekoalynSiirto()
                    }
                }
            }
        }
    }

    init {
        if (ristinollaPeli.pelaajanVuoro == 1) {
            teeTekoalynSiirto()
        }
    }

    private fun teeSiirto(siirronXKoordinaatti: Int, siirronYKoordinaatti: Int) {
        if (ristinollaPeli.pelaajanVuoro == 0) {
            ruutujenSisallot[siirronYKoordinaatti][siirronXKoordinaatti].value = "O"
        } else {
            ruutujenSisallot[siirronYKoordinaatti][siirronXKoordinaatti].value = "X"
        }

        taustaoperaatioMeneillaan = true
        val pelaajanSiirto: Boolean = (ristinollaPeli.pelaajanVuoro == 0)
        runAsync {
            tekoaly.merkitseTehtySiirto(siirronXKoordinaatti, siirronYKoordinaatti, pelaajanSiirto)
            ristinollaPeli.teeSiirto(siirronXKoordinaatti, siirronYKoordinaatti)
        } ui { voittotilanne ->
            taustaoperaatioMeneillaan = false
            if (voittotilanne.tyyppi == VoittotilanneTyyppi.VOITTO) {
                if (voittotilanne.voittavaPelaaja == 0) {
                    voittoilmoittajanSisalto.value = "Sinä voitit"
                } else {
                    voittoilmoittajanSisalto.value = "Tietokone voitti"
                }
            } else if (voittotilanne.tyyppi == VoittotilanneTyyppi.TASAPELI) {
                voittoilmoittajanSisalto.value = "Tasapeli"
            } else if (pelaajanSiirto) {
                teeTekoalynSiirto()
            }
        }
    }

    private fun teeTekoalynSiirto() {
        taustaoperaatioMeneillaan = true
        runAsync {
            tekoaly.valitseSiirto()
        } ui { tekoalynValitsemaSiirto ->
            taustaoperaatioMeneillaan = false
            teeSiirto(tekoalynValitsemaSiirto.first, tekoalynValitsemaSiirto.second)
        }
    }
}
