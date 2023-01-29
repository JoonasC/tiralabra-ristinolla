package fi.helsinki.peliLogiikka

/**
 * Luokka joka esittää pelin voittotilannetta
 * @author Joonas Coatanea
 * @param tyyppi Voittotilanteen tyyppi
 * @param voittavaPelaaja Voittava pelaaja
 */
data class Voittotilanne(val tyyppi: VoittotilanneTyyppi, val voittavaPelaaja: Int? = null)
