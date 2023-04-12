package fi.helsinki.tekoaly

import apuohjelmat.TESTEISSA_KAYTETYN_PELITAULUKON_KOKO
import apuohjelmat.TESTEISSA_KAYTETYN_TEKOALYN_ETSINTASYVYYS
import apuohjelmat.TESTEISSA_KAYTETYN_VOITTORIVIN_PITUUS
import fi.helsinki.peliLogiikka.RistinollaPeli
import fi.helsinki.peliLogiikka.Voittotilanne
import fi.helsinki.peliLogiikka.VoittotilanneTyyppi
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TekoalyTest {
    private var ristinollaPeli: RistinollaPeli? = null
    private var tekoalynPelaaja: Int? = null
    private var tekoaly: Tekoaly? = null

    @BeforeTest
    fun rakennaTestiymparisto() {
        ristinollaPeli = RistinollaPeli(TESTEISSA_KAYTETYN_PELITAULUKON_KOKO, TESTEISSA_KAYTETYN_VOITTORIVIN_PITUUS)
        tekoalynPelaaja = (1 - (ristinollaPeli?.pelaajanVuoro as Int))
        val siirtogeneraattori = Siirtogeneraattori((ristinollaPeli as RistinollaPeli), (tekoalynPelaaja as Int))
        tekoaly = Tekoaly(
            TESTEISSA_KAYTETYN_TEKOALYN_ETSINTASYVYYS,
            (ristinollaPeli as RistinollaPeli),
            siirtogeneraattori,
        )
    }

    @Test
    fun tekoalynPitaisiPystyaVoittaaVaakarivilla() {
        tekoaly?.merkitseTehtySiirto(0, 0, true)
        ristinollaPeli?.teeSiirto(0, 0)
        tekoaly?.merkitseTehtySiirto(0, 1, false)
        ristinollaPeli?.teeSiirto(0, 1)
        tekoaly?.merkitseTehtySiirto(0, 2, true)
        ristinollaPeli?.teeSiirto(0, 2)
        tekoaly?.merkitseTehtySiirto(1, 1, false)
        ristinollaPeli?.teeSiirto(1, 1)
        tekoaly?.merkitseTehtySiirto(1, 2, true)
        ristinollaPeli?.teeSiirto(1, 2)

        val tekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(2, 1), tekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(tekoalynValitsemaSiirto.first, tekoalynValitsemaSiirto.second, false)
        assertEquals(
            Voittotilanne(VoittotilanneTyyppi.VOITTO, tekoalynPelaaja),
            ristinollaPeli?.teeSiirto(tekoalynValitsemaSiirto.first, tekoalynValitsemaSiirto.second),
        )
    }

    @Test
    fun tekoalynPitaisiPystyaVoittaaPystyrivilla() {
        tekoaly?.merkitseTehtySiirto(0, 0, true)
        ristinollaPeli?.teeSiirto(0, 0)
        tekoaly?.merkitseTehtySiirto(1, 0, false)
        ristinollaPeli?.teeSiirto(1, 0)
        tekoaly?.merkitseTehtySiirto(2, 0, true)
        ristinollaPeli?.teeSiirto(2, 0)
        tekoaly?.merkitseTehtySiirto(1, 1, false)
        ristinollaPeli?.teeSiirto(1, 1)
        tekoaly?.merkitseTehtySiirto(2, 1, true)
        ristinollaPeli?.teeSiirto(2, 1)

        val tekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(1, 2), tekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(tekoalynValitsemaSiirto.first, tekoalynValitsemaSiirto.second, false)
        assertEquals(
            Voittotilanne(VoittotilanneTyyppi.VOITTO, tekoalynPelaaja),
            ristinollaPeli?.teeSiirto(tekoalynValitsemaSiirto.first, tekoalynValitsemaSiirto.second),
        )
    }

    @Test
    fun tekoalynPitaisiPystyaVoittaaVasenOikeaVinorivilla() {
        tekoaly?.merkitseTehtySiirto(1, 0, true)
        ristinollaPeli?.teeSiirto(1, 0)
        tekoaly?.merkitseTehtySiirto(0, 0, false)
        ristinollaPeli?.teeSiirto(0, 0)
        tekoaly?.merkitseTehtySiirto(2, 0, true)
        ristinollaPeli?.teeSiirto(2, 0)
        tekoaly?.merkitseTehtySiirto(1, 1, false)
        ristinollaPeli?.teeSiirto(1, 1)
        tekoaly?.merkitseTehtySiirto(2, 1, true)
        ristinollaPeli?.teeSiirto(2, 1)

        val tekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(2, 2), tekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(tekoalynValitsemaSiirto.first, tekoalynValitsemaSiirto.second, false)
        assertEquals(
            Voittotilanne(VoittotilanneTyyppi.VOITTO, tekoalynPelaaja),
            ristinollaPeli?.teeSiirto(tekoalynValitsemaSiirto.first, tekoalynValitsemaSiirto.second),
        )
    }

    @Test
    fun tekoalynPitaisiPystyaVoittaaOikeaVasenVinorivilla() {
        tekoaly?.merkitseTehtySiirto(1, 0, true)
        ristinollaPeli?.teeSiirto(1, 0)
        tekoaly?.merkitseTehtySiirto(2, 0, false)
        ristinollaPeli?.teeSiirto(2, 0)
        tekoaly?.merkitseTehtySiirto(0, 0, true)
        ristinollaPeli?.teeSiirto(0, 0)
        tekoaly?.merkitseTehtySiirto(1, 1, false)
        ristinollaPeli?.teeSiirto(1, 1)
        tekoaly?.merkitseTehtySiirto(0, 1, true)
        ristinollaPeli?.teeSiirto(0, 1)

        val tekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(0, 2), tekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(tekoalynValitsemaSiirto.first, tekoalynValitsemaSiirto.second, false)
        assertEquals(
            Voittotilanne(VoittotilanneTyyppi.VOITTO, tekoalynPelaaja),
            ristinollaPeli?.teeSiirto(tekoalynValitsemaSiirto.first, tekoalynValitsemaSiirto.second),
        )
    }

    @Test
    fun tekoalynPitaisiPystyaValttaaHavioJaVoittaaVaakarivilla() {
        tekoaly?.merkitseTehtySiirto(0, 0, true)
        ristinollaPeli?.teeSiirto(0, 0)
        tekoaly?.merkitseTehtySiirto(0, 1, false)
        ristinollaPeli?.teeSiirto(0, 1)
        tekoaly?.merkitseTehtySiirto(1, 0, true)
        ristinollaPeli?.teeSiirto(1, 0)

        val ensimmainenTekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(2, 0), ensimmainenTekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(
            ensimmainenTekoalynValitsemaSiirto.first,
            ensimmainenTekoalynValitsemaSiirto.second,
            false,
        )
        ristinollaPeli?.teeSiirto(ensimmainenTekoalynValitsemaSiirto.first, ensimmainenTekoalynValitsemaSiirto.second)
        tekoaly?.merkitseTehtySiirto(0, 2, true)
        ristinollaPeli?.teeSiirto(0, 2)

        val toinenTekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(2, 1), toinenTekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(toinenTekoalynValitsemaSiirto.first, toinenTekoalynValitsemaSiirto.second, false)
        ristinollaPeli?.teeSiirto(toinenTekoalynValitsemaSiirto.first, toinenTekoalynValitsemaSiirto.second)
        tekoaly?.merkitseTehtySiirto(2, 2, true)
        ristinollaPeli?.teeSiirto(2, 2)

        val kolmasTekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(1, 1), kolmasTekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(kolmasTekoalynValitsemaSiirto.first, kolmasTekoalynValitsemaSiirto.second, false)
        assertEquals(
            Voittotilanne(VoittotilanneTyyppi.VOITTO, tekoalynPelaaja),
            ristinollaPeli?.teeSiirto(kolmasTekoalynValitsemaSiirto.first, kolmasTekoalynValitsemaSiirto.second),
        )
    }

    @Test
    fun tekoalynPitaisiPystyaValttaaHavioJaVoittaaPystyrivilla() {
        tekoaly?.merkitseTehtySiirto(0, 0, true)
        ristinollaPeli?.teeSiirto(0, 0)
        tekoaly?.merkitseTehtySiirto(1, 0, false)
        ristinollaPeli?.teeSiirto(1, 0)
        tekoaly?.merkitseTehtySiirto(0, 1, true)
        ristinollaPeli?.teeSiirto(0, 1)

        val ensimmainenTekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(0, 2), ensimmainenTekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(
            ensimmainenTekoalynValitsemaSiirto.first,
            ensimmainenTekoalynValitsemaSiirto.second,
            false,
        )
        ristinollaPeli?.teeSiirto(ensimmainenTekoalynValitsemaSiirto.first, ensimmainenTekoalynValitsemaSiirto.second)
        tekoaly?.merkitseTehtySiirto(2, 0, true)
        ristinollaPeli?.teeSiirto(2, 0)

        val toinenTekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(1, 2), toinenTekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(toinenTekoalynValitsemaSiirto.first, toinenTekoalynValitsemaSiirto.second, false)
        ristinollaPeli?.teeSiirto(toinenTekoalynValitsemaSiirto.first, toinenTekoalynValitsemaSiirto.second)
        tekoaly?.merkitseTehtySiirto(2, 2, true)
        ristinollaPeli?.teeSiirto(2, 2)

        val kolmasTekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(1, 1), kolmasTekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(kolmasTekoalynValitsemaSiirto.first, kolmasTekoalynValitsemaSiirto.second, false)
        assertEquals(
            Voittotilanne(VoittotilanneTyyppi.VOITTO, tekoalynPelaaja),
            ristinollaPeli?.teeSiirto(kolmasTekoalynValitsemaSiirto.first, kolmasTekoalynValitsemaSiirto.second),
        )
    }

    @Test
    fun tekoalynPitaisiPystyaValttaaHavioJaVoittaaVasenOikeaVinorivilla() {
        tekoaly?.merkitseTehtySiirto(2, 0, true)
        ristinollaPeli?.teeSiirto(2, 0)
        tekoaly?.merkitseTehtySiirto(0, 0, false)
        ristinollaPeli?.teeSiirto(0, 0)
        tekoaly?.merkitseTehtySiirto(0, 2, true)
        ristinollaPeli?.teeSiirto(0, 2)

        val ensimmainenTekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(1, 1), ensimmainenTekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(
            ensimmainenTekoalynValitsemaSiirto.first,
            ensimmainenTekoalynValitsemaSiirto.second,
            false,
        )
        ristinollaPeli?.teeSiirto(ensimmainenTekoalynValitsemaSiirto.first, ensimmainenTekoalynValitsemaSiirto.second)
        tekoaly?.merkitseTehtySiirto(1, 2, true)
        ristinollaPeli?.teeSiirto(1, 2)

        val toinenTekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(2, 2), toinenTekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(toinenTekoalynValitsemaSiirto.first, toinenTekoalynValitsemaSiirto.second, false)
        assertEquals(
            Voittotilanne(VoittotilanneTyyppi.VOITTO, tekoalynPelaaja),
            ristinollaPeli?.teeSiirto(toinenTekoalynValitsemaSiirto.first, toinenTekoalynValitsemaSiirto.second),
        )
    }

    @Test
    fun tekoalynPitaisiPystyaValttaaHavioJaVoittaaOikeaVasenVinorivilla() {
        tekoaly?.merkitseTehtySiirto(0, 0, true)
        ristinollaPeli?.teeSiirto(0, 0)
        tekoaly?.merkitseTehtySiirto(2, 0, false)
        ristinollaPeli?.teeSiirto(2, 0)
        tekoaly?.merkitseTehtySiirto(2, 2, true)
        ristinollaPeli?.teeSiirto(2, 2)

        val ensimmainenTekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(1, 1), ensimmainenTekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(
            ensimmainenTekoalynValitsemaSiirto.first,
            ensimmainenTekoalynValitsemaSiirto.second,
            false,
        )
        ristinollaPeli?.teeSiirto(ensimmainenTekoalynValitsemaSiirto.first, ensimmainenTekoalynValitsemaSiirto.second)
        tekoaly?.merkitseTehtySiirto(1, 2, true)
        ristinollaPeli?.teeSiirto(1, 2)

        val toinenTekoalynValitsemaSiirto: Pair<Int, Int> = (tekoaly?.valitseSiirto() as Pair<Int, Int>)
        assertEquals(Pair(0, 2), toinenTekoalynValitsemaSiirto)

        tekoaly?.merkitseTehtySiirto(toinenTekoalynValitsemaSiirto.first, toinenTekoalynValitsemaSiirto.second, false)
        assertEquals(
            Voittotilanne(VoittotilanneTyyppi.VOITTO, tekoalynPelaaja),
            ristinollaPeli?.teeSiirto(toinenTekoalynValitsemaSiirto.first, toinenTekoalynValitsemaSiirto.second),
        )
    }
}
