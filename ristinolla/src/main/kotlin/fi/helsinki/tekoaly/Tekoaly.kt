package fi.helsinki.tekoaly

import fi.helsinki.peliLogiikka.RistinollaPeli

class Tekoaly(ristinollaPeli: RistinollaPeli, private val omaPelaaja: Int) {
    private val vastustajanPelaaja: Int = (1 - omaPelaaja)

    private val pelitaulukko: Array<IntArray> = ristinollaPeli.getPelitaulukko()

    private val siirtogeneraattori = Siirtogeneraattori(ristinollaPeli)
}
