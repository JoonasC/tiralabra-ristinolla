package fi.helsinki.peliLogiikka

import apuohjelmat.TESTEISSA_KAYTETYN_PELITAULUKON_KOKO
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class RistinollaPeliTest {
    private var ristinollaPeli: RistinollaPeli? = null

    @BeforeTest
    fun rakennaTestiymparisto() {
        ristinollaPeli = RistinollaPeli(TESTEISSA_KAYTETYN_PELITAULUKON_KOKO)
    }

    @Test
    fun pelaajanVuoronPitaisiVaihtua() {
        val aloittavanPelaajanVuoro: Int = (ristinollaPeli?.pelaajanVuoro as Int)
        ristinollaPeli?.teeSiirto(0, 0)
        val vuorolla2OlevanPelaajanVuoro: Int = (ristinollaPeli?.pelaajanVuoro as Int)
        ristinollaPeli?.teeSiirto(1, 0)
        val vuorolla3OlevanPelaajanVuoro: Int = (ristinollaPeli?.pelaajanVuoro as Int)
        ristinollaPeli?.teeSiirto(2, 0)
        val vuorolla4OlevanPelaajanVuoro: Int = (ristinollaPeli?.pelaajanVuoro as Int)

        assertEquals(aloittavanPelaajanVuoro, vuorolla3OlevanPelaajanVuoro)
        assertEquals(vuorolla2OlevanPelaajanVuoro, vuorolla4OlevanPelaajanVuoro)
        assertNotEquals(aloittavanPelaajanVuoro, vuorolla2OlevanPelaajanVuoro)
        assertNotEquals(vuorolla2OlevanPelaajanVuoro, vuorolla3OlevanPelaajanVuoro)
    }

    @Test
    fun pelinPitaisiPaattyaTasapeliinJosKaikkiRuudutOnValloitettu() {
        val eiVoittoaVoittotilanne = Voittotilanne(VoittotilanneTyyppi.EI_VOITTOA)
        val tasapeliVoittotilanne = Voittotilanne(VoittotilanneTyyppi.TASAPELI)

        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(0, 0))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(1, 0))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(2, 0))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(1, 1))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(0, 1))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(0, 2))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(1, 2))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(2, 1))
        assertEquals(tasapeliVoittotilanne, ristinollaPeli?.teeSiirto(2, 2))

        assertEquals(tasapeliVoittotilanne, ristinollaPeli?.voittotilanne)
    }

    @Test
    fun pelaajanPitaisiPystyaVoittaaVaakarivilla() {
        val voittavaPelaaja: Int = (ristinollaPeli?.pelaajanVuoro as Int)

        val eiVoittoaVoittotilanne = Voittotilanne(VoittotilanneTyyppi.EI_VOITTOA)
        val voittoVoittotilanne = Voittotilanne(VoittotilanneTyyppi.VOITTO, voittavaPelaaja)

        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(0, 0))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(0, 1))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(1, 0))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(1, 1))
        assertEquals(voittoVoittotilanne, ristinollaPeli?.teeSiirto(2, 0))

        assertEquals(voittoVoittotilanne, ristinollaPeli?.voittotilanne)
    }

    @Test
    fun pelaajanPitaisiPystyaVoittaaPystyrivilla() {
        val voittavaPelaaja: Int = (ristinollaPeli?.pelaajanVuoro as Int)

        val eiVoittoaVoittotilanne = Voittotilanne(VoittotilanneTyyppi.EI_VOITTOA)
        val voittoVoittotilanne = Voittotilanne(VoittotilanneTyyppi.VOITTO, voittavaPelaaja)

        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(0, 0))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(1, 0))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(0, 1))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(1, 1))
        assertEquals(voittoVoittotilanne, ristinollaPeli?.teeSiirto(0, 2))

        assertEquals(voittoVoittotilanne, ristinollaPeli?.voittotilanne)
    }

    @Test
    fun pelaajanPitaisiPystyaVoittaaVasenOikeaVinorivilla() {
        val voittavaPelaaja: Int = (ristinollaPeli?.pelaajanVuoro as Int)

        val eiVoittoaVoittotilanne = Voittotilanne(VoittotilanneTyyppi.EI_VOITTOA)
        val voittoVoittotilanne = Voittotilanne(VoittotilanneTyyppi.VOITTO, voittavaPelaaja)

        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(0, 0))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(1, 0))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(1, 1))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(2, 0))
        assertEquals(voittoVoittotilanne, ristinollaPeli?.teeSiirto(2, 2))

        assertEquals(voittoVoittotilanne, ristinollaPeli?.voittotilanne)
    }

    @Test
    fun pelaajanPitaisiPystyaVoittaaOikeaVasenVinorivilla() {
        val voittavaPelaaja: Int = (ristinollaPeli?.pelaajanVuoro as Int)

        val eiVoittoaVoittotilanne = Voittotilanne(VoittotilanneTyyppi.EI_VOITTOA)
        val voittoVoittotilanne = Voittotilanne(VoittotilanneTyyppi.VOITTO, voittavaPelaaja)

        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(2, 0))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(0, 0))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(1, 1))
        assertEquals(eiVoittoaVoittotilanne, ristinollaPeli?.teeSiirto(1, 0))
        assertEquals(voittoVoittotilanne, ristinollaPeli?.teeSiirto(0, 2))

        assertEquals(voittoVoittotilanne, ristinollaPeli?.voittotilanne)
    }

    @Test
    fun peliPitaisiPystyaNollaamaan() {
        ristinollaPeli?.teeSiirto(0, 0)
        ristinollaPeli?.teeSiirto(0, 1)
        ristinollaPeli?.teeSiirto(1, 0)
        ristinollaPeli?.teeSiirto(1, 1)
        ristinollaPeli?.teeSiirto(2, 0)
        assertEquals(
            VoittotilanneTyyppi.VOITTO,
            ristinollaPeli
                ?.voittotilanne
                ?.tyyppi
        )
        ristinollaPeli?.nollaa()
        assertEquals(
            VoittotilanneTyyppi.EI_VOITTOA,
            ristinollaPeli
                ?.voittotilanne
                ?.tyyppi
        )

        ristinollaPeli?.teeSiirto(0, 0)
        ristinollaPeli?.nollaa()
        assertDoesNotThrow {
            ristinollaPeli?.teeSiirto(0, 0)
        }
    }

    @Test
    fun pelinPelitaulukkoPitaisiOllaSaatavilla() {
        val ensimmainenPelaaja: Int = (ristinollaPeli?.pelaajanVuoro as Int)
        val toinenPelaaja: Int = (1 - ensimmainenPelaaja)

        val odotettuPelitaulukko: Array<IntArray> = Array(TESTEISSA_KAYTETYN_PELITAULUKON_KOKO) {
            IntArray(TESTEISSA_KAYTETYN_PELITAULUKON_KOKO) { -1 }
        }
        odotettuPelitaulukko[0][0] = ensimmainenPelaaja
        odotettuPelitaulukko[1][0] = toinenPelaaja

        ristinollaPeli?.teeSiirto(0, 0)
        ristinollaPeli?.teeSiirto(0, 1)
        assertTrue(odotettuPelitaulukko.contentDeepEquals(ristinollaPeli?.getPelitaulukko()))
    }

    @Test
    fun pelinPelitaulukkoaEiPitaisiPystyaMuuttamaan() {
        ristinollaPeli?.teeSiirto(0, 0)
        (ristinollaPeli?.getPelitaulukko() as Array<IntArray>)[0][0] = -1
        val exc: IllegalArgumentException = assertFailsWith {
            ristinollaPeli?.teeSiirto(0, 0)
        }
        assertEquals("Siirtoa ei ole mahdollista tehdä jo-valloitettuun ruutuun", exc.message)
    }

    @Test
    fun pelinPelitaulukonKokoEiVoiOllaPienempiKuin3() {
        val exc: IllegalArgumentException = assertFailsWith {
            RistinollaPeli(2)
        }
        assertEquals("Pelitaulukon koon täytyy olla vähintään 3", exc.message)
    }

    @Test
    fun loppuneeseenPeliinEiVoiTehdaSiirtoa() {
        ristinollaPeli?.teeSiirto(0, 0)
        ristinollaPeli?.teeSiirto(0, 1)
        ristinollaPeli?.teeSiirto(1, 0)
        ristinollaPeli?.teeSiirto(1, 1)
        ristinollaPeli?.teeSiirto(2, 0)
        val exc: IllegalStateException = assertFailsWith {
            ristinollaPeli?.teeSiirto(2, 1)
        }
        assertEquals("Peli on loppunut", exc.message)
    }

    @Test
    fun siirtoaEiVoiTehdaPelinPelitaulukonRajojenUlkopuolelle() {
        val virheviesti = "Siirron koordinaattien täytyy sijoittua pelitaulukon sisään"

        val exc: IllegalArgumentException = assertFailsWith {
            ristinollaPeli?.teeSiirto(-1, 0)
        }
        val exc2: IllegalArgumentException = assertFailsWith {
            ristinollaPeli?.teeSiirto((TESTEISSA_KAYTETYN_PELITAULUKON_KOKO + 1), 0)
        }
        val exc3: IllegalArgumentException = assertFailsWith {
            ristinollaPeli?.teeSiirto(0, -1)
        }
        val exc4: IllegalArgumentException = assertFailsWith {
            ristinollaPeli?.teeSiirto(0, (TESTEISSA_KAYTETYN_PELITAULUKON_KOKO + 1))
        }
        assertEquals(virheviesti, exc.message)
        assertEquals(virheviesti, exc2.message)
        assertEquals(virheviesti, exc3.message)
        assertEquals(virheviesti, exc4.message)
    }

    @Test
    fun siirtoaEiVoiTehdaJoValloitettuunRuutuun() {
        ristinollaPeli?.teeSiirto(0, 0)
        val exc: IllegalArgumentException = assertFailsWith {
            ristinollaPeli?.teeSiirto(0, 0)
        }
        assertEquals("Siirtoa ei ole mahdollista tehdä jo-valloitettuun ruutuun", exc.message)
    }
}
