package fi.helsinki.peliLogiikka

import apuohjelmat.TESTEISSA_KAYTETYN_PELITAULUKON_KOKO
import kotlin.test.BeforeTest
import kotlin.test.Test

class RistinollaPeliTest {
    private var ristinollaPeli: RistinollaPeli? = null

    @BeforeTest
    fun rakennaTestiymparisto() {
        ristinollaPeli = RistinollaPeli(TESTEISSA_KAYTETYN_PELITAULUKON_KOKO)
    }

    @Test
    fun pelinPitaisiPaattyaTasapeliinJosKaikkiRuudutOnValloitettu() {
    }
}
