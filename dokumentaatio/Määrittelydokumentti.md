# Määrittelydokumentti

## Projektin tiedot tiivistetysti

| Ohjelman tarkoitus              | Käytetty ohjelmointikieli | Käytetyt algoritmit            | Käytetyt tietorakenteet                             | Ohjelman syötteet        | Tavoiteltu aikavaativuus                                                                                                  | Tavoiteltu tilavaativuus                                                                            | Opetusohjelma | Kieli |
| ------------------------------- | ------------------------- | ------------------------------ | --------------------------------------------------- | ------------------------ | ------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------- | ------------- | ----- |
| Tekoäly ristinolla-peliä varten | Kotlin                    | Minimax alfa-beta -karsinnalla | Kaksiulotteinen taulukko (pelitilan tallentamiseen) | Pelaajan tekemät siirrot | $O\left(\sqrt{b^d}\right)$, missä $b$ on mahdollisten siirtojen keskimäärä ja $d$ on keskimääräisen pelin pituus vuoroina | $O\left(rc\right)$, missä $r$ on pelitaulukon rivien määrä ja $c$ on pelitaulukon sarakkeiden määrä | TKT           | Suomi |

## Ohjelman kuvaus

Tarkoituksena on tehdä ristinolla-peli ja sille myös tekoälyvastustaja. Käytän tekoälyn tekemiseen alfa-beta -karsinnalla paranneltua Minimax-algoritmia, koska se soveltuu hyvin ristinollan kaltaisiin vuoropohjaisiin peleihin. Tämän lisäksi käytän ohjelmassa kaksiulotteista taulukkoa pelitilan tallentamiseen. Tavoittelen Minimax-algoritmille $O\left(\sqrt{b^d}\right)$ aikavaativuutta, missä $b$ on mahdollisten siirtojen keskimäärä ja $d$ on keskimääräisen pelin pituus vuoroina. Aion tehdä ohjelman Kotlin-ohjelmointikielellä ja tehdä sille graafisen käyttöliittymän [TornadoFX](https://tornadofx.io/) kirjastolla. Käyttöliittymässä tulee olemaan taulukko, jonka ruutuja klikkaamalla pelaaja pystyy merkitsemään ruudun valloitetuksi. Pelaajan siirron jälkeen, tekoäly tulee laskemaan parhaan siirron Minimax algoritmilla.

## Lisätietoja

Olen tietojenkäsittelytieteen kandiohjelmassa (TKT) ja aion käyttää projektissa Suomen kieltä, lisäksi osaan myös ohjelmoida Javalla ja Pythonilla.

## Lähteet

[Kurssin Minimax ohje](https://tiralabra.github.io/2023_p3/fi/aiheet/minimax.pdf)

[Alpha–beta pruning - Wikipedia](https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning)

[Kotlin](https://kotlinlang.org/)

[TornadoFX](https://tornadofx.io/)
