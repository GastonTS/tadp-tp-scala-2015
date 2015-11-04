package utn.tadp.dragonball

import utn.tadp.dragonball.Simulador._
import utn.tadp.dragonball.Guerrero._
import utn.tadp.dragonball.Especie._

import org.junit.Before
import org.junit.Test

import org.junit.Assert._

class Simulador_Test {
  
  //Guerrero(nombre, inventario, energiaMaxima, energia, especie, estado, movimientos)
  val todosSaben: List[Movimiento] = List(DejarseFajar, CargarKi, MuchosGolpesNinja, Onda(10))
  val esferasDelDragon: List[Item] = List(EsferaDelDragon(1), EsferaDelDragon(2), EsferaDelDragon(3), EsferaDelDragon(4), EsferaDelDragon(5), EsferaDelDragon(6), EsferaDelDragon(7))
  val dejarInconsciente: Function1[Combatientes, Combatientes] = { case (atacante, oponente) => (atacante, oponente.estas(Inconsciente)) }
  
  val krilin: Guerrero = new Guerrero("Krilin", List(Arma(Roma)), 100, 50, Humano, Luchando, todosSaben++List(UsarItem(Arma(Roma))))
  val numero18: Guerrero = new Guerrero("N18", List(Arma(Fuego)), 300, 100, Androide, Luchando, todosSaben++List(Explotar, UsarItem(Arma(Fuego))))
  val piccolo : Guerrero = new Guerrero("Piccolo", esferasDelDragon, 500, 200, Namekusein, Luchando, todosSaben++List(Fusion(krilin), Magia(dejarInconsciente), Onda(40)))
  val majinBuu: Guerrero = new Guerrero("Majin Buu", List(Arma(Filosa)), 700, 300, Monstruo({case (a, o) => {a.tusMovimientos(o.movimientos)} }), Luchando, todosSaben++List(UsarItem(Arma(Filosa)), ComerseAlOponente))
  val cell: Guerrero = new Guerrero("Cell", List(), 500, 250, Monstruo({case (a, o) => {a.agregaMovimientos(o.movimientos)} }), Luchando, todosSaben++List(Explotar, ComerseAlOponente))
  val mono : Guerrero = new Guerrero("Mono", List(), 3000, 3000, Saiyajing(MonoGigante(1000), true), Luchando, todosSaben)
  val goku : Guerrero = new Guerrero("Goku", List(SemillaDelErmitaño), 2500, 800, Saiyajing(SuperSaiyajing(1, 500), true), Luchando, todosSaben++List(Onda(99), Genkidama))
  val vegeta : Guerrero = new Guerrero("Vegeta", List(), 1000, 801, Saiyajing(Normal, false), Luchando, todosSaben++List(Onda(100)))

  @Test
  def krilinSeDejaFajarTest() = {
    val (k, p) = DejarseFajar(krilin, piccolo)
    
    assertEquals(krilin, k)
    assertEquals(piccolo, p)
  }
  
  @Test
  def superSaiyajingCargaKiTest() ={
    val(g, k) = CargarKi(goku, krilin)
    
    assertEquals(950, g.energia)
    assertEquals(krilin, k)
  }
  
  @Test
  def androideNoCargaKiTest() ={
    val(n18, k) = CargarKi(numero18, krilin)
    
    assertEquals(numero18.energia, n18.energia)
    assertEquals(krilin, k)
  }
  
  @Test
  def otroCargaKiTest() ={
    val(p, k) = CargarKi(piccolo, krilin)
    
    assertEquals(300, p.energia)
    assertEquals(krilin, k)
  }
  
  @Test
  def usaArmaRomaContraAndroideTest() ={
    val(k, n18) = UsarItem(Arma(Roma))(krilin, numero18)
    
    assertEquals(n18, numero18)
    assertEquals(krilin, k)
  }
  
  @Test
  def usaArmaRomaYDejaInconscienteTest() ={
    val(k, p) = UsarItem(Arma(Roma))(krilin, piccolo)
    
    assertEquals(Inconsciente, p.estado)
    assertEquals(krilin, k)
  }
  
  @Test
  def usaArmaRomaNoPasaNadaTest() ={
    val(k, g) = UsarItem(Arma(Roma))(krilin, goku)
    
    assertEquals(goku, g)
    assertEquals(krilin, k)
  }
  
  @Test
  def siNoTieneUnArmaRomaEnElInventarioNoPasaNadaTest() ={
    val(g, p) = UsarItem(Arma(Roma))(goku, piccolo)
    
    assertEquals(goku, g)
    assertEquals(piccolo, p)
  }
  
  @Test
  def usaArmaFilosaConMonoTest() ={
    val(buu, m) = UsarItem(Arma(Filosa))(majinBuu, mono)
    
    assertEquals(majinBuu, buu)
    assertEquals(Inconsciente, m.estado)
    assertEquals(1, m.energia)
    assertEquals(Saiyajing(Normal, false), m.especie)
    
  }
  
  @Test
  def usaArmaFilosaConSSJTest() ={
    val(buu, g) = UsarItem(Arma(Filosa))(majinBuu, goku)
    
    assertEquals(majinBuu, buu)
    assertEquals(1, g.energia)
    assertEquals(Saiyajing(SuperSaiyajing(1, 500), false), g.especie)
    
  }
  
  @Test
  def usaArmaFilosaConSJSinColaTest() ={
    val(buu, v) = UsarItem(Arma(Filosa))(majinBuu, vegeta)
    
    assertEquals(majinBuu, buu)
    assertEquals(798, v.energia)
    assertEquals(Saiyajing(Normal, false), v.especie)
    
  }
  
  @Test
  def usaArmaFilosaConOtroTest() ={
    val(buu, c) = UsarItem(Arma(Filosa))(majinBuu, cell)
    
    assertEquals(majinBuu, buu)
    assertEquals(247, c.energia)
    
  }
  
  @Test
  def usaArmaFuegoConHumanoTest() ={
    val(n18, k) = UsarItem(Arma(Fuego))(numero18, krilin)
    
    assertEquals(numero18, n18)
    assertEquals(30, k.energia)
    
  }
  
  @Test
  def usaArmaFuegoConNamekuseinInconscienteTest() ={
    val(n18, p) = UsarItem(Arma(Fuego))(numero18, piccolo.estas(Inconsciente))
    
    assertEquals(numero18, n18)
    assertEquals(190, p.energia)
    
  }
  
  @Test
  def usaArmaFuegoConNamekuseinLuchandoTest() ={
    val(n18, p) = UsarItem(Arma(Fuego))(numero18, piccolo)
    
    assertEquals(numero18, n18)
    assertEquals(piccolo, p)
    
  }
  
  @Test
  def usaSemillaDelErmitañoTest() ={
    val(g, p) = UsarItem(SemillaDelErmitaño)(goku, piccolo)
    
    assertEquals(goku.energiaMaxima, g.energia)
    assertEquals(piccolo, p)
    
  }
  
  @Test
  def usaSemillaDelErmitañoAunEstandoInconscienteTest() ={
    val(g, p) = UsarItem(SemillaDelErmitaño)(goku.estas(Inconsciente), piccolo)
    
    assertEquals(goku.energiaMaxima, g.energia)
    assertEquals(piccolo, p)
    
  }
  
  
}