package fi.helsinki.apuohjelmat

/**
 * Liikuttaa ristinolla-pelitaulukon vaakarivillä tapahtuvaa hakua vasemmalle
 * @author Joonas Coatanea
 * @param ruudunXKoordinaatti Alkuperäisen ruudun X-koordinaatti
 * @param ruudunYKoordinaatti Alkuperäisen ruudun Y-koordinaatti
 * @return Parin joka sisältää alkuperäisen ruudun vasemmalla (vaakasuunnassa) sijaitsevan ruudun X- ja Y-koordinaatit
 */
fun liikutaHakuaVaakarivillaVasemmalle(ruudunXKoordinaatti: Int, ruudunYKoordinaatti: Int): Pair<Int, Int> =
    Pair((ruudunXKoordinaatti - 1), ruudunYKoordinaatti)

/**
 * Liikuttaa ristinolla-pelitaulukon vaakarivillä tapahtuvaa hakua oikealle
 * @author Joonas Coatanea
 * @param ruudunXKoordinaatti Alkuperäisen ruudun X-koordinaatti
 * @param ruudunYKoordinaatti Alkuperäisen ruudun Y-koordinaatti
 * @return Parin joka sisältää alkuperäisen ruudun oikealla (vaakasuunnassa) sijaitsevan ruudun X- ja Y-koordinaatit
 */
fun liikutaHakuaVaakarivillaOikealle(ruudunXKoordinaatti: Int, ruudunYKoordinaatti: Int): Pair<Int, Int> =
    Pair((ruudunXKoordinaatti + 1), ruudunYKoordinaatti)

/**
 * Liikuttaa ristinolla-pelitaulukon pystyrivillä tapahtuvaa hakua vasemmalle
 * @author Joonas Coatanea
 * @param ruudunXKoordinaatti Alkuperäisen ruudun X-koordinaatti
 * @param ruudunYKoordinaatti Alkuperäisen ruudun Y-koordinaatti
 * @return Parin joka sisältää alkuperäisen ruudun vasemmalla (pystysuunnassa) sijaitsevan ruudun X- ja Y-koordinaatit
 */
fun liikutaHakuaPystyrivillaVasemmalle(ruudunXKoordinaatti: Int, ruudunYKoordinaatti: Int): Pair<Int, Int> =
    Pair(ruudunXKoordinaatti, (ruudunYKoordinaatti - 1))

/**
 * Liikuttaa ristinolla-pelitaulukon pystyrivillä tapahtuvaa hakua oikealle
 * @author Joonas Coatanea
 * @param ruudunXKoordinaatti Alkuperäisen ruudun X-koordinaatti
 * @param ruudunYKoordinaatti Alkuperäisen ruudun Y-koordinaatti
 * @return Parin joka sisältää alkuperäisen ruudun oikealla (pystysuunnassa) sijaitsevan ruudun X- ja Y-koordinaatit
 */
fun liikutaHakuaPystyrivillaOikealle(ruudunXKoordinaatti: Int, ruudunYKoordinaatti: Int): Pair<Int, Int> =
    Pair(ruudunXKoordinaatti, (ruudunYKoordinaatti + 1))

/**
 * Liikuttaa ristinolla-pelitaulukon vasen-oikea vinorivillä tapahtuvaa hakua vasemmalle
 * @author Joonas Coatanea
 * @param ruudunXKoordinaatti Alkuperäisen ruudun X-koordinaatti
 * @param ruudunYKoordinaatti Alkuperäisen ruudun Y-koordinaatti
 * @return Parin joka sisältää alkuperäisen ruudun vasemmalla (vasen-oikea vinosuunnassa) sijaitsevan ruudun X- ja
 * Y-koordinaatit
 */
fun liikutaHakuaVasenOikeaVinorivillaVasemmalle(ruudunXKoordinaatti: Int, ruudunYKoordinaatti: Int): Pair<Int, Int> =
    Pair((ruudunXKoordinaatti - 1), (ruudunYKoordinaatti - 1))

/**
 * Liikuttaa ristinolla-pelitaulukon vasen-oikea vinorivillä tapahtuvaa hakua oikealle
 * @author Joonas Coatanea
 * @param ruudunXKoordinaatti Alkuperäisen ruudun X-koordinaatti
 * @param ruudunYKoordinaatti Alkuperäisen ruudun Y-koordinaatti
 * @return Parin joka sisältää alkuperäisen ruudun oikealla (vasen-oikea vinosuunnassa) sijaitsevan ruudun X- ja
 * Y-koordinaatit
 */
fun liikutaHakuaVasenOikeaVinorivillaOikealle(ruudunXKoordinaatti: Int, ruudunYKoordinaatti: Int): Pair<Int, Int> =
    Pair((ruudunXKoordinaatti + 1), (ruudunYKoordinaatti + 1))

/**
 * Liikuttaa ristinolla-pelitaulukon oikea-vasen vinorivillä tapahtuvaa hakua vasemmalle
 * @author Joonas Coatanea
 * @param ruudunXKoordinaatti Alkuperäisen ruudun X-koordinaatti
 * @param ruudunYKoordinaatti Alkuperäisen ruudun Y-koordinaatti
 * @return Parin joka sisältää alkuperäisen ruudun vasemmalla (oikea-vasen vinosuunnassa) sijaitsevan ruudun X- ja
 * Y-koordinaatit
 */
fun liikutaHakuaOikeaVasenVinorivillaVasemmalle(ruudunXKoordinaatti: Int, ruudunYKoordinaatti: Int): Pair<Int, Int> =
    Pair((ruudunXKoordinaatti - 1), (ruudunYKoordinaatti + 1))

/**
 * Liikuttaa ristinolla-pelitaulukon oikea-vasen vinorivillä tapahtuvaa hakua oikealle
 * @author Joonas Coatanea
 * @param ruudunXKoordinaatti Alkuperäisen ruudun X-koordinaatti
 * @param ruudunYKoordinaatti Alkuperäisen ruudun Y-koordinaatti
 * @return Parin joka sisältää alkuperäisen ruudun oikealla (oikea-vasen vinosuunnassa) sijaitsevan ruudun X- ja
 * Y-koordinaatit
 */
fun liikutaHakuaOikeaVasenVinorivillaOikealle(ruudunXKoordinaatti: Int, ruudunYKoordinaatti: Int): Pair<Int, Int> =
    Pair((ruudunXKoordinaatti + 1), (ruudunYKoordinaatti - 1))
