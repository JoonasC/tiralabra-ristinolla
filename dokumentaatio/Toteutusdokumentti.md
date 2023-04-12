# Toteutusdokumentti

## Ohjelman toteutus tiivistetysti

Ohjelma on jaettu kolmeen osaan:

- `RistinollaPeli`-luokka toteuttaa pelilogiikan ristinollalle.

- `Tekoaly`-luokka toteuttaa siirron valitsemisen Minimax-algoritmilla.

- `Siirtogeneraattori`-luokka toteuttaa kandidaattisiirtojen generoinnin, heuristiikkafunktion sekä kandidaattisiirtojen järjestämisen heuristiikkafunktiota käyttäen.

## Aika- ja tilavaativuusanalyysit

`Siirtogeneraattori`-luokan funktiolla joka generoi seuraavan kandidaattisiirron on aikavaativuus $O\left(b+d\right)$, missä $b$ on mahdollisten siirtojen keskimäärä ja $d$ on keskimääräisen pelin pituus vuoroina. Tästä seuraa, että `Tekoaly`-luokan funktiolla joka valitsee siirron on aikavaativuus $O\left(\sqrt{b^d}\left(b+d\right)\right)$.

`Siirtogeneraattori`-luokalla on tilavaativuus $O\left(rc+d+b\right)$, missä $r$ on pelitaulukon rivien määrä ja $c$ on pelitaulukon sarakkeiden määrä.

## Puutteet ja parannusehdotukset

`Siirtogeneraattori`-luokkaa ei ole toteutettu optimaalisesti. Sen aika- ja tilavaativuuksia voisi parantaa huomattavasti. `Siirtogeneraattori`-luokka rikkoo myös hyvän koodin DRY-periaatetta, eli sen toiminnallisuuksia voisi jakaa aliluokkiin.

## Suorituskyky

Tekoälyllä menee keskimäärin viisi sekunttia siirron valitsemiseen omalla koneellani.
