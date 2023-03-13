package fi.helsinki

import fi.helsinki.kayttoliittyma.nakymat.PeliNakyma
import tornadofx.App
import tornadofx.launch

class Application : App(PeliNakyma::class)

fun main() {
    launch<Application>()
}
