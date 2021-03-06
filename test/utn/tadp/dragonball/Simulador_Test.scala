package utn.tadp.dragonball

import utn.tadp.dragonball.Simulador._
import utn.tadp.dragonball.Guerrero._

import org.junit.Before
import org.junit.Test

import org.junit.Assert._

class Simulador_Test {
  
  val todosSaben: List[Movimiento] = List(DejarseFajar, CargarKi, MuchosGolpesNinja, Onda(10))
  val esferasDelDragon: List[Item] = List(EsferaDelDragon, EsferaDelDragon, EsferaDelDragon, EsferaDelDragon, EsferaDelDragon, EsferaDelDragon, EsferaDelDragon)
  
  val dejarInconsciente: Function1[Combatientes, Combatientes] = { case (atacante, oponente) => (atacante, oponente estas Inconsciente) }
  val convertirEnHumano: Function1[Combatientes, Combatientes] = { case (atacante, oponente) => (atacante, oponente transformateEn Humano) }
  
  val digerirMajinBuu: Function1[Combatientes, Guerrero] = { case (a, o) => {a tusMovimientos(o movimientos)} }
  val digerirCell: Function1[Combatientes, Guerrero] = { case (a, o) => { if(o.especie == Androide)
                                                                           a agregaMovimientos(o.movimientos)
                                                                         else
                                                                           a} }
      
  val raditz: Guerrero = new Guerrero("Raditz", List(), 250, 0, Saiyajin(false), Muerto, todosSaben, 0)
  val yamcha: Guerrero = new Guerrero("Yamcha", List(), 100, 20, Humano, Inconsciente, todosSaben, 0)
  
  val krilin: Guerrero = new Guerrero("Krilin", List(Arma(Roma))++esferasDelDragon, 100, 50, Humano, Luchando, todosSaben++List(UsarItem(Arma(Roma)), Magia(convertirEnHumano)))
  val numero18: Guerrero = new Guerrero("N18", List(Arma(Fuego(Ak47)), Municion(Ak47)), 300, 100, Androide, Luchando, todosSaben++List(Explotar, UsarItem(Arma(Fuego(Ak47)))))
  val piccolo : Guerrero = new Guerrero("Piccolo", List(), 500, 200, Namekusein, Luchando, todosSaben++List(Fusion(krilin), Magia(dejarInconsciente), Onda(40)))
  val majinBuu: Guerrero = new Guerrero("Majin Buu", List(Arma(Filosa)), 700, 300, Monstruo(digerirMajinBuu), Luchando, todosSaben++List(UsarItem(Arma(Filosa)), ComerseAlOponente))
  val cell: Guerrero = new Guerrero("Cell", List(), 500, 250, Monstruo(digerirCell), Luchando, todosSaben++List(Explotar, ComerseAlOponente))
  val mono : Guerrero = new Guerrero("Mono", List(), 3000, 3000, Saiyajin(true), MonoGigante(1000), todosSaben, 0)
  val goku : Guerrero = new Guerrero("Goku", List(SemillaDelErmitaño, FotoDeLaLuna), 2500, 1300, Saiyajin(true), SuperSaiyajin(1, 500), todosSaben++List(Onda(99), Genkidama))
  val vegeta : Guerrero = new Guerrero("Vegeta", List(), 1001, 801, Saiyajin(false), Luchando, todosSaben++List(Onda(100), Fusion(goku)))

  //Sobre DejarseFajar
  @Test
  def krilinSeDejaFajarTest() = {
    val (k, p) = DejarseFajar(krilin, piccolo)
    
    assertEquals(1, k turnosFajado)
    assertEquals(piccolo, p)
  }
  
  //Sobre CargarKi
  @Test
  def superSaiyajingCargaKiTest() = {
    val(g, k) = CargarKi(goku, krilin)
    
    assertEquals(1450, g energia)
    assertEquals(krilin, k)
  }
  
  @Test
  def androideNoCargaKiTest() = {
    val(n18, k) = CargarKi(numero18, krilin)
    
    assertEquals(numero18 energia, n18 energia)
    assertEquals(krilin, k)
  }
  
  @Test
  def otroCargaKiTest() = {
    val(p, k) = CargarKi(piccolo, krilin)
    
    assertEquals(300, p energia)
    assertEquals(krilin, k)
  }
  
  //Sobre UsarItem
  @Test
  def usaArmaRomaContraAndroideTest() = {
    val(k, n18) = UsarItem(Arma(Roma))(krilin, numero18)
    
    assertEquals(n18, numero18)
    assertEquals(krilin, k)
  }
  
  @Test
  def usaArmaRomaYDejaInconscienteTest() = {
    val(k, p) = UsarItem(Arma(Roma))(krilin, piccolo)
    
    assertEquals(Inconsciente, p estado)
    assertEquals(krilin, k)
  }
  
  @Test
  def usaArmaRomaNoPasaNadaTest() = {
    val(k, g) = UsarItem(Arma(Roma))(krilin, goku)
    
    assertEquals(goku, g)
    assertEquals(krilin, k)
  }
  
  @Test
  def siNoTieneUnArmaRomaEnElInventarioNoPasaNadaTest() = {
    val(g, p) = UsarItem(Arma(Roma))(goku, piccolo)
    
    assertEquals(goku, g)
    assertEquals(piccolo, p)
  }
  
  @Test
  def usaArmaFilosaConMonoTest() = {
    val(buu, m) = UsarItem(Arma(Filosa))(majinBuu, mono)
    
    assertEquals(majinBuu, buu)
    assertEquals(Inconsciente, m estado)
    assertEquals(1, m energia)
    assertEquals(Saiyajin(false), m especie)
  }
  
  @Test
  def usaArmaFilosaConSSJTest() = {
    val(buu, g) = UsarItem(Arma(Filosa))(majinBuu, goku)
    
    assertEquals(majinBuu, buu)
    assertEquals(1, g energia)
    assertEquals(Saiyajin(false), g especie)
    assertEquals(SuperSaiyajin(1, 500), g estado)
  }
  
  @Test
  def usaArmaFilosaConSJSinColaTest() = {
    val(buu, v) = UsarItem(Arma(Filosa))(majinBuu, vegeta)
    
    assertEquals(majinBuu, buu)
    assertEquals(798, v energia)
    assertEquals(Saiyajin(false), v especie)
  }
  
  @Test
  def usaArmaFilosaConOtroTest() = {
    val(buu, c) = UsarItem(Arma(Filosa))(majinBuu, cell)
    
    assertEquals(majinBuu, buu)
    assertEquals(247, c energia)
  }
  
  @Test
  def usaArmaFuegoConHumanoTest() = {
    val(n18, k) = UsarItem(Arma(Fuego(Ak47)))(numero18, krilin)
    
    assertEquals(numero18 gastarItems List(Municion(Ak47)), n18)
    assertEquals(30, k energia)
  }
  
  @Test
  def usaArmaFuegoConNamekuseinInconscienteTest() = {
    val(n18, p) = UsarItem(Arma(Fuego(Ak47)))(numero18, piccolo estas Inconsciente)
    
    assertEquals(numero18 gastarItems(List(Municion(Ak47))), n18)
    assertEquals(190, p energia)
  }
  
  @Test
  def usaArmaFuegoConNamekuseinLuchandoTest() = {
    val(n18, p) = UsarItem(Arma(Fuego(Ak47)))(numero18, piccolo)
    
    assertEquals(numero18 gastarItems List(Municion(Ak47)), n18)
    assertEquals(piccolo, p)
  }
  
  @Test
  def usaSemillaDelErmitañoTest() = {
    val(g, p) = UsarItem(SemillaDelErmitaño)(goku, piccolo)
    
    assertEquals(goku energiaMaxima, g energia)
    assertEquals(piccolo, p)
  }
  
  @Test
  def usarUnaEsferaDelDragonSolaNoHaceNadaTest() {
    val(k, n18) = UsarItem(EsferaDelDragon)(krilin, numero18)
    
    assertEquals(krilin, k)
    assertEquals(numero18, n18)
  }
  
  //Sobre ComerseAlOponente
  @Test
  def majinBuuSeComeAGokuTest() = {
    val(m, g) = ComerseAlOponente(majinBuu, goku)
    
    assertEquals(goku estas Muerto , g)
    assertEquals(goku movimientos, m movimientos)
  }
  
  @Test
  def cellIntentaComerseAGokuSinEfectoTest() = {
    val(c, g) = ComerseAlOponente(cell, goku)
    
    assertEquals(goku estas Muerto, g)
    assertEquals(cell, c)
  }
  
  @Test
  def cellSeComeANumero18Test() = {
    val(c, n18) = ComerseAlOponente(cell, numero18)
    
    assertEquals(numero18 estas Muerto , n18)
    assertEquals(cell.movimientos++numero18.movimientos, c movimientos)
  }
  
  @Test
  def unNoMonstruoIntentaComerseAlOponentePeroSinSurgirEfectoTest() = {
    val(g, p) = ComerseAlOponente(goku, piccolo)
    
    assertEquals(goku, g)
    assertEquals(piccolo, p)
  }
  
  //Sobre ConvertirseEnMono
  @Test
  def unMonoConvirtiendoseEnMonoMonoQuedaTest() = {
    val(m, k) = ConvertirseEnMono(mono, krilin)
    
    assertEquals(mono, m)
    assertEquals(krilin, k)
  }
  
  @Test
  def sinColaNoHayMonoTest() = {
    val(v, k) = ConvertirseEnMono(vegeta, krilin)
    
    assertEquals(vegeta, v)
    assertEquals(krilin, k)
  }
  
  @Test
  def sinSaiyajingNoHayMonoTest() = {
    val(p, k) = ConvertirseEnMono(piccolo, krilin)
    
    assertEquals(piccolo, p)
    assertEquals(krilin, k)
  }
  
  @Test
  def sinFotoDeLaLunaNoHayMonoTest() = {
    val(g, k) = ConvertirseEnMono(goku copy(inventario = List()), krilin)
    
    
    assertEquals(goku copy(inventario = List()), g)
    assertEquals(krilin, k)
  }
  
  @Test
  def gokuSeTransformaEnMonoTest() = {
    val(g, k) = ConvertirseEnMono(goku, krilin)
    
    assertEquals(Saiyajin(true), g especie)
    assertEquals(MonoGigante(500), g estado)
    assertEquals(1500, g energia)
    assertEquals(g energiaMaxima, g energia)
    assertEquals(krilin, k)
  }
  
  //Sobre ConvertirseEnSuperSaiyajin
  @Test
  def vegetaSeConvierteEnSSJTest() = {
    val (v, g) = ConvertirseEnSuperSaiyajing(vegeta, goku)
    
    assertEquals(5005, v energiaMaxima)
    assertEquals(Saiyajin(false), v especie)
    assertEquals(SuperSaiyajin(1, 1001), v estado)
    assertEquals(goku, g)
  }
  
  @Test
  def vegetaNoSeConvierteEnSSJConPocoKiTest() = {
    val (v, g) = ConvertirseEnSuperSaiyajing(vegeta disminuiEnergia 500, goku)
    
    assertEquals(v, vegeta disminuiEnergia 500)
    assertEquals(goku, g)
  }
  
  @Test
  def gokuSeConvierteEnSSJ2Test() = {
    val (g, v) = ConvertirseEnSuperSaiyajing(goku, vegeta)
    
    assertEquals(5000, g energiaMaxima)
    assertEquals(Saiyajin(true), g especie)
    assertEquals(SuperSaiyajin(2, 500), g estado)
    assertEquals(vegeta, v)
  }
  
  @Test
  def monoNoSeConvierteEnSSJTest() = {
    val (m, v) = ConvertirseEnSuperSaiyajing(mono, vegeta)
    
    assertEquals(mono, m)
    assertEquals(vegeta, v)
  }
  
  @Test
  def krilinNoSeConvierteEnSSJTest() = {
    val (k, v) = ConvertirseEnSuperSaiyajing(krilin, vegeta)
    
    assertEquals(krilin, k)
    assertEquals(vegeta, v)    
  }
  
  @Test
  def ssjSeVuelveNormalAlQuedarInconscienteTest() = {
    val (g, v) = (goku estas Inconsciente, vegeta)
    
    assertEquals(500, g energiaMaxima)
    assertEquals(Saiyajin(true), g especie)
    assertEquals(vegeta, v)
  }
  
  //Sobre Fusion
  @Test
  def krilinSeFusionaConPiccoloTest() = {
    val (f, v) = Fusion(krilin)(piccolo, vegeta)
    
    assertEquals(piccolo.inventario++krilin.inventario, f inventario)
    assertEquals(piccolo.energia + krilin.energia, f energia)
    assertEquals(piccolo.energiaMaxima + krilin.energiaMaxima, f energiaMaxima)
    assertEquals(Fusionado(piccolo, krilin), f especie)
    assertEquals(vegeta, v)
  }
  
  @Test
  def vegetaSeFusionaConGokuTest() = {
    val (f, m) = Fusion(goku)(vegeta, majinBuu)
    
    assertEquals(vegeta.inventario++goku.inventario, f inventario)
    assertEquals(vegeta.energia + goku.energia, f energia)
    assertEquals(vegeta.energiaMaxima + goku.energiaMaxima, f energiaMaxima)
    assertEquals(Fusionado(vegeta, goku), f especie)
    assertEquals(majinBuu, m)
  }
  
  @Test
  def noTodosPuedenFusionarseTest() = {
    val (k, c) = Fusion(numero18)(krilin, cell)
    
    assertEquals(krilin, k)
    assertEquals(cell, c)
  }
  
  @Test
  def vegetaSeFusionaConGokuYMuereTest() = {
    val (f, m) = Fusion(goku)(vegeta, majinBuu)
    
    
    assertEquals(vegeta estas Muerto, f estas Muerto)
    assertEquals(majinBuu, m)
  }
  
  @Test
  def vegetaSeFusionaConGokuYQuedaInconscienteTest() = {
    val (f, m) = Fusion(goku)(vegeta, majinBuu)
    
    
    assertEquals(vegeta estas Inconsciente, f estas Inconsciente)
    assertEquals(majinBuu, m)
  }
  
  //Sobre Magia
  @Test
  def piccoloUsaMagiaTest() = {
    val (p, g) = Magia(dejarInconsciente)(piccolo, goku)
    
    assertEquals(piccolo, p)
    assertEquals(goku estas Inconsciente, g)
  }
  
  @Test
  def majinBuuUsaMagiaTest() = {
    val (m, g) = Magia(dejarInconsciente)(majinBuu, goku)
    
    assertEquals(majinBuu, m)
    assertEquals(goku estas Inconsciente, g)
  }
  
  @Test
  def numero18NoUsaMagiaTest() = {
    val (n18, g) = Magia(convertirEnHumano)(numero18, numero18)
    
    assertEquals(numero18, n18)
  }
  
  @Test
  def krilinUsaMagiaTest() = {
    val (k, n18) = Magia(convertirEnHumano)(krilin, numero18)
    
    assertEquals(krilin.inventario diff esferasDelDragon, k.inventario)
    assertEquals(numero18 transformateEn Humano, n18)
  }
  
  //Sobre MuchosGolpesNinja
  @Test
  def krilinSePeleaConNumero18YSeLastmiaLosDeditosTest() = {
    val (k, n18) = MuchosGolpesNinja(krilin, numero18)
    
    assertEquals(krilin disminuiEnergia 10, k)
    assertEquals(numero18, n18)
  }
  
  @Test
  def numero18SePeleaConKrilinYLoLastimaTest() = {
    val (n18, k) = MuchosGolpesNinja(numero18, krilin)
    
    assertEquals(krilin disminuiEnergia 20, k)
    assertEquals(numero18, n18)
  }
  
  @Test
  def piccoloMataAVegetaAGolpesTest() = {
    val (p, v) = MuchosGolpesNinja(piccolo,vegeta tuEnergiaEs(1))
    
    assertEquals(0, v energia)
    assertEquals(Muerto, v estado)
    assertEquals(piccolo, p)
  }
  
  //Sobre Explotar
  @Test
  def numero18ExplotaYMataAKrilinTest() = {
    val (n18, k) = Explotar(numero18, krilin)
    
    assertEquals(0, n18 energia)
    assertEquals(Muerto, n18 estado)
    assertEquals(0, k energia)
    assertEquals(Muerto, k estado)
  }
  
  @Test
  def majinBuuExplotaYDañaAMonoTest() = {
    val (m, mo) = Explotar(majinBuu, mono)
    
    assertEquals(0, m energia)
    assertEquals(Muerto, m estado)
    assertEquals(mono.energia - majinBuu.energia * 2, mo energia)
    assertEquals(MonoGigante(1000), mo estado)
  }
  
  @Test
  def numero18ExplotaYDañaAMonoTest() = {
    val (n18, mo) = Explotar(numero18, mono)
    
    assertEquals(0, n18 energia)
    assertEquals(Muerto, n18 estado)
    assertEquals(mono.energia - numero18.energia * 3, mo energia)
    assertEquals(MonoGigante(1000), mo estado)
  }
  
  @Test
  def cellExplotaYNoMataAPiccoloTest() = {
    val (c, p) = Explotar(cell, piccolo)
    
    assertEquals(0, c energia)
    assertEquals(Muerto, c estado)
    assertEquals(1, p energia)
    assertEquals(Luchando, p estado)
  }
  
  @Test
  def numero18ExplotaYNoMataAPiccoloTest() = {
    val (n18, p) = Explotar(numero18, piccolo)
    
    assertEquals(0, n18 energia)
    assertEquals(Muerto, n18 estado)
    assertEquals(1, p energia)
    assertEquals(Luchando, p estado)
  }
  
  @Test
  def krilinNoExplotaTest() = {
    val (k, v) = Explotar(krilin, vegeta)
    
    assertEquals(krilin, k)
    assertEquals(vegeta, v)
  }
  
  //Sobre Onda
  @Test
  def piccoloTiraOndaAVegetaTest() = {
    val (p, v) = Onda(40)(piccolo,vegeta)
    
    assertEquals(piccolo disminuiEnergia 40, p)
    assertEquals(vegeta disminuiEnergia 80, v)
  }
  
  @Test
  def numero18TiraOndaACellTest() = {
    val (n18, c) = Onda(40)(numero18, cell)
    
    assertEquals(numero18 disminuiEnergia 40, n18)
    assertEquals(cell disminuiEnergia 20, c)
  }
  
  @Test
  def krilinTiraOndaANumero18Test() = {
    val (k, n18) = Onda(40)(krilin, numero18)
    
    assertEquals(krilin disminuiEnergia 40, k)
    assertEquals(numero18 aumentaEnergia 80, n18)
  }
  
  @Test
  def krilinNoPuedeTirarOndaAVegetaTest() = {
    val (k, v) = Onda(100)(krilin,vegeta)
    
    assertEquals(krilin, k)
    assertEquals(vegeta, v)
  }
  
  //Sobre Genkidama
  @Test
  def gokuTiraUnaGenkidamaSinDejarseFajarYNoHaceNadaTest() = {
    val (g, k) = Genkidama(goku, krilin)
    
    assertEquals(goku, g)
    assertEquals(krilin disminuiEnergia 1, k)
  }
  
  @Test
  def gokuTiraUnaGenkidamaDejandonseFajar1TurnoTest() = {
    val(gokuFajado, kr) = DejarseFajar(goku, krilin)
    
    assertEquals(1, gokuFajado turnosFajado)
    
    val (g, k) = Genkidama(gokuFajado, kr)
    
    assertEquals(goku, g)
    assertEquals(krilin disminuiEnergia 10, k)
  }
  
  @Test
  def gokuTiraUnaGenkidamaDejandonseFajar1TurnoTestYCuraANumero18Test() = {
    val(gokuFajado, n18) = DejarseFajar(goku, numero18)
    
    assertEquals(1, gokuFajado turnosFajado)
    
    val (g, nro18) = Genkidama(gokuFajado, n18)
    
    assertEquals(goku, g)
    assertEquals(numero18 aumentaEnergia 10, nro18)
  }
  
  @Test
  def gokuTiraUnaGenkidamaDejandonseFajar2TurnoTest() = {
    val(gokuFajado, kr) = DejarseFajar(DejarseFajar(goku, krilin))
    
    assertEquals(2, gokuFajado turnosFajado)
    
    val (g, k) = Genkidama(gokuFajado, kr)
    
    assertEquals(goku, g)
    assertEquals(0, k energia)
    assertEquals(Muerto, k estado)
  }
  
  @Test
  def gokuTiraUnaGenkidamaDejandonseFajar3TurnoTest() = {
    val(gokuFajado, mo) = DejarseFajar(DejarseFajar(DejarseFajar(goku, mono)))
    
    assertEquals(3, gokuFajado turnosFajado)
    
    val (g, m) = Genkidama(gokuFajado, mo)
    
    assertEquals(goku, g)
    assertEquals(mono disminuiEnergia 1000, m)
  }
  
  //Sobre Estados, podrian ser mas detallistas pero hay mucho testeado implicitamente en los anteriores
  @Test
  def unMuertoNoPuedeRealizarNingunMovimientoTest() = {
    val(r, g) = CargarKi(raditz, goku)
    
    assertEquals(raditz, r)
    assertEquals(goku, g)
  }
  
  @Test
  def usaSemillaDelErmitañoAunEstandoInconscienteTest() = {
    val(g, p) = UsarItem(SemillaDelErmitaño)(goku estas Inconsciente, piccolo)
    
    assertEquals(g energiaMaxima, g energia)
    assertEquals(piccolo, p)
  }
  
  @Test
  def unInconscienteNoPuedeRealizarCasiNingunMovimientoTest() = {
    val(y, g) = CargarKi(yamcha, goku)
    
    assertEquals(yamcha, y)
    assertEquals(goku, g)
  }
  
  @Test
  def unFajadoSeDejaFajarYAumentanLosRoundsFajadoTest(){
    val(k, g) = (DejarseFajar andThen DejarseFajar)(krilin, goku)
    
    assertEquals(2, k turnosFajado)
    assertEquals(goku, g)
  }
  
  @Test
  def unFajadoRealizaMovimientoYPasaALuchandoTest(){
    val(k, g) = CargarKi(krilin.pasarTurnoFajado.pasarTurnoFajado, goku)
    
    assertEquals(krilin aumentaEnergia 100, k)
    assertEquals(goku, g)
  }
  
}