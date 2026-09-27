package br.com.pokesal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.pokesal.model.Antidote;
import br.com.pokesal.model.AsfaltoQuente;
import br.com.pokesal.model.CanteiroCentral;
import br.com.pokesal.model.Golpe;
import br.com.pokesal.model.HeldItem;
import br.com.pokesal.model.PocaDeChuva;
import br.com.pokesal.model.Pokesal;
import br.com.pokesal.model.Potion;
import br.com.pokesal.model.SuperPotion;
import br.com.pokesal.model.Terreno;
import br.com.pokesal.model.TipoElemental;
import br.com.pokesal.model.TipoStatus;
import br.com.pokesal.model.Treinador;
import br.com.pokesal.service.Batalha;
import br.com.pokesal.service.EfeitoDeStatusHeld;
import br.com.pokesal.service.LimiteItensExcedidosException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Suite oficial de testes unitarios automatizados para o sistema PokeSal v2.
 * Abrange requisitos formais da Fase 2, incluindo multiplicadores elementais,
 * efeitos de terreno da UCSAL, ordem de ataque por SPD, limite de itens,
 * analise de valores limite (boundary values) e os requisitos autorais RA001-RA003.
 */
public class PokeSalTest {

  private static final int HP_PADRAO = 100;
  private static final int ATK_PADRAO = 30;
  private static final int DEF_PADRAO = 20;
  private static final int SPD_RAPIDO = 40;
  private static final int SPD_LENTO = 20;
  private static final int PODER_GOLPE = 30;
  private static final int DANO_EXTREMO = 9999;
  private static final int DEF_EXTREMA = 999;

  private Treinador treinador1;
  private Treinador treinador2;
  private Terreno terrenoNeutro;

  /**
   * Inicializa o cenario padrao antes da execucao de cada teste.
   */
  @BeforeEach
  public void setUp() {
    treinador1 = new Treinador("Treinador Alfa");
    treinador2 = new Treinador("Treinador Beta");
    terrenoNeutro = new Terreno("Arena Neutra UCSal", null);
  }

  /**
   * Fabrica auxiliar para construcao de Pokesal com exatamente 3 golpes regulamentares.
   *
   * @param nome nome do Pokesal.
   * @param hp vida maxima e inicial.
   * @param atk valor de ataque.
   * @param def valor de defesa.
   * @param spd velocidade de iniciativa.
   * @param tipo tipo elemental do Pokesal.
   * @return nova instancia de Pokesal.
   */
  private Pokesal criarPokesal(String nome, int hp, int atk, int def, int spd,
                               TipoElemental tipo) {
    List<Golpe> golpes = Arrays.asList(
        new Golpe("Golpe Alpha", PODER_GOLPE, tipo),
        new Golpe("Golpe Beta", 20, tipo),
        new Golpe("Golpe Gamma", 25, tipo)
    );
    return new Pokesal(nome, hp, atk, def, spd, tipo, golpes);
  }

  /**
   * RF002: Validacao dos multiplicadores de dano da matriz elemental.
   * Verifica vantagem (2.0x), desvantagem (0.5x) e neutralidade (1.0x).
   */
  @Test
  @DisplayName("testVantagemElemental - Validacao dos multiplicadores da matriz elemental")
  public void testVantagemElemental() {
    // 1. Fogo contra Planta (Super Efetivo x2.0) e contra Agua (Pouco Efetivo x0.5)
    assertEquals(2.0, TipoElemental.FOGO.obterMultiplicadorContra(TipoElemental.PLANTA), 0.001);
    assertEquals(0.5, TipoElemental.FOGO.obterMultiplicadorContra(TipoElemental.AGUA), 0.001);

    // 2. Agua contra Fogo (Super Efetivo x2.0) e contra Planta (Pouco Efetivo x0.5)
    assertEquals(2.0, TipoElemental.AGUA.obterMultiplicadorContra(TipoElemental.FOGO), 0.001);
    assertEquals(0.5, TipoElemental.AGUA.obterMultiplicadorContra(TipoElemental.PLANTA), 0.001);

    // 3. Planta contra Agua (Super Efetivo x2.0) e contra Fogo (Pouco Efetivo x0.5)
    assertEquals(2.0, TipoElemental.PLANTA.obterMultiplicadorContra(TipoElemental.AGUA), 0.001);
    assertEquals(0.5, TipoElemental.PLANTA.obterMultiplicadorContra(TipoElemental.FOGO), 0.001);

    // 4. Confrontos entre mesmos elementos (Neutro x1.0)
    assertEquals(1.0, TipoElemental.FOGO.obterMultiplicadorContra(TipoElemental.FOGO), 0.001);
    assertEquals(1.0, TipoElemental.AGUA.obterMultiplicadorContra(TipoElemental.AGUA), 0.001);
    assertEquals(1.0, TipoElemental.PLANTA.obterMultiplicadorContra(TipoElemental.PLANTA), 0.001);

    // 5. Integracao de calculo de dano em Batalha: base = (ATK 30 - DEF 20) + Poder 30 = 40
    Pokesal atacanteFogo = criarPokesal("CharSal", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        SPD_RAPIDO, TipoElemental.FOGO);
    Pokesal defensorPlanta = criarPokesal("BulbaSal", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        SPD_LENTO, TipoElemental.PLANTA);
    Pokesal defensorAgua = criarPokesal("SquirtSal", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        SPD_LENTO, TipoElemental.AGUA);
    Pokesal defensorFogo = criarPokesal("CyndaSal", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        SPD_LENTO, TipoElemental.FOGO);

    Batalha batalha = new Batalha(treinador1, treinador2, terrenoNeutro);
    Golpe golpeFogo = atacanteFogo.getGolpes().get(0);

    // Dano Vantagem: 40 * 2.0 = 80
    assertEquals(80, batalha.calcularDano(atacanteFogo, defensorPlanta, golpeFogo));
    // Dano Desvantagem: 40 * 0.5 = 20
    assertEquals(20, batalha.calcularDano(atacanteFogo, defensorAgua, golpeFogo));
    // Dano Neutro: 40 * 1.0 = 40
    assertEquals(40, batalha.calcularDano(atacanteFogo, defensorFogo, golpeFogo));
  }

  /**
   * RF003: Validacao do impacto dos terrenos do estacionamento da UCSAL.
   * Asfalto Quente (+15% Fogo), Poca de Chuva (+10% Agua) e Canteiro Central (5% cura Planta).
   */
  @Test
  @DisplayName("testEfeitoTerrenoEstacionamentoUCSal - Validacao do impacto dos terrenos")
  public void testEfeitoTerrenoEstacionamentoUCSal() {
    Pokesal atacanteFogo = criarPokesal("CharSal", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        SPD_RAPIDO, TipoElemental.FOGO);
    Pokesal defensorFogo = criarPokesal("CyndaSal", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        SPD_LENTO, TipoElemental.FOGO);
    Golpe golpeFogo = atacanteFogo.getGolpes().get(0);

    // 1. Asfalto Quente: base 40 * 1.0 (neutro) * 1.15 = 46
    Terreno asfalto = new AsfaltoQuente();
    Batalha batalhaAsfalto = new Batalha(treinador1, treinador2, asfalto);
    assertEquals(1.15, asfalto.obterModificadorDano(TipoElemental.FOGO), 0.001);
    assertEquals(1.0, asfalto.obterModificadorDano(TipoElemental.AGUA), 0.001);
    assertEquals(46, batalhaAsfalto.calcularDano(atacanteFogo, defensorFogo, golpeFogo));

    // 2. Poca de Chuva: base 40 * 1.0 (neutro) * 1.10 = 44
    Pokesal atacanteAgua = criarPokesal("SquirtSal", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        SPD_RAPIDO, TipoElemental.AGUA);
    Pokesal defensorAgua = criarPokesal("TotoSal", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        SPD_LENTO, TipoElemental.AGUA);
    Golpe golpeAgua = atacanteAgua.getGolpes().get(0);

    Terreno poca = new PocaDeChuva();
    Batalha batalhaPoca = new Batalha(treinador1, treinador2, poca);
    assertEquals(1.10, poca.obterModificadorDano(TipoElemental.AGUA), 0.001);
    assertEquals(1.0, poca.obterModificadorDano(TipoElemental.FOGO), 0.001);
    assertEquals(44, batalhaPoca.calcularDano(atacanteAgua, defensorAgua, golpeAgua));

    // 3. Canteiro Central: regenera 5% de HP para Planta no fim de turno
    Pokesal plantaFerido = criarPokesal("BulbaSal", 100, ATK_PADRAO, DEF_PADRAO,
        SPD_LENTO, TipoElemental.PLANTA);
    plantaFerido.receberDano(30); // HP = 70/100

    Pokesal fogoFerido = criarPokesal("CharSal", 100, ATK_PADRAO, DEF_PADRAO,
        SPD_LENTO, TipoElemental.FOGO);
    fogoFerido.receberDano(30); // HP = 70/100

    treinador1.setPokesal(plantaFerido);
    treinador2.setPokesal(fogoFerido);

    Terreno canteiro = new CanteiroCentral();
    Batalha batalhaCanteiro = new Batalha(treinador1, treinador2, canteiro);
    batalhaCanteiro.finalizarTurno();

    // Planta regenerou 5% de 100 = 5 HP (70 + 5 = 75)
    assertEquals(75, plantaFerido.getHpAtual());
    // Fogo nao pertence ao tipo do canteiro, logo nao regenera (permanece 70)
    assertEquals(70, fogoFerido.getHpAtual());
  }

  /**
   * RF004: Validacao da ordem de ataque e iniciativa por SPD.
   */
  @Test
  @DisplayName("testOrdemDeAtaquePorVelocidade - Validacao da prioridade de acao por SPD")
  public void testOrdemDeAtaquePorVelocidade() {
    // Caso 1: Treinador 1 mais rapido (SPD 40 vs SPD 20)
    Pokesal rapido = criarPokesal("Veloz", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        SPD_RAPIDO, TipoElemental.FOGO);
    Pokesal lento = criarPokesal("Lento", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        SPD_LENTO, TipoElemental.PLANTA);

    treinador1.setPokesal(rapido);
    treinador2.setPokesal(lento);

    Batalha batalha1 = new Batalha(treinador1, treinador2, terrenoNeutro);
    assertEquals(treinador1, batalha1.determinarPrimeiroAtacante(),
        "O competidor com maior SPD deve agir primeiro.");

    // Caso 2: Treinador 2 mais rapido (SPD 15 vs SPD 35)
    Pokesal lento2 = criarPokesal("Lento2", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        15, TipoElemental.AGUA);
    Pokesal rapido2 = criarPokesal("Veloz2", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        35, TipoElemental.PLANTA);

    treinador1.setPokesal(lento2);
    treinador2.setPokesal(rapido2);

    Batalha batalha2 = new Batalha(treinador1, treinador2, terrenoNeutro);
    assertEquals(treinador2, batalha2.determinarPrimeiroAtacante(),
        "Treinador 2 possui maior SPD e deve receber a iniciativa.");

    // Caso 3: Empate de SPD (resolucao por sorteio deterministico)
    Pokesal empate1 = criarPokesal("Empate1", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        30, TipoElemental.FOGO);
    Pokesal empate2 = criarPokesal("Empate2", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        30, TipoElemental.AGUA);

    treinador1.setPokesal(empate1);
    treinador2.setPokesal(empate2);

    // Random com retorno false sorteia treinador2 no operador ternario
    Random randomFalso = new Random() {
      @Override
      public boolean nextBoolean() {
        return false;
      }
    };
    Batalha batalhaEmpate = new Batalha(treinador1, treinador2, terrenoNeutro, randomFalso);
    assertEquals(treinador2, batalhaEmpate.determinarPrimeiroAtacante(),
        "No empate de SPD, o gerador pseudoaleatorio deve definir o desempate.");
  }

  /**
   * RF006 / RF007: Validacao da restricao de uso de itens na batalha.
   * Limite de ate 2 itens consumidos por batalha, lancando excecao ao exceder.
   */
  @Test
  @DisplayName("testUsoLimiteDeItensExcedido - Lancamento de excecao ao exceder limite de itens")
  public void testUsoLimiteDeItensExcedido() {
    Pokesal pokesal = criarPokesal("CharSal", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        SPD_RAPIDO, TipoElemental.FOGO);
    pokesal.receberDano(60); // HP = 40/100
    pokesal.aplicarCondicaoStatus(TipoStatus.QUEIMADO);
    treinador1.setPokesal(pokesal);

    treinador1.adicionarItem(new Potion());
    treinador1.adicionarItem(new SuperPotion());
    treinador1.adicionarItem(new Antidote());

    assertEquals(3, treinador1.getMochila().size());
    assertEquals(0, treinador1.getContItensUsados());

    Batalha batalha = new Batalha(treinador1, treinador2, terrenoNeutro);

    // 1o uso de item (permitido)
    batalha.consumirItemNoTurno(treinador1, 0);
    assertEquals(1, treinador1.getContItensUsados());

    // 2o uso de item (permitido)
    batalha.consumirItemNoTurno(treinador1, 0);
    assertEquals(2, treinador1.getContItensUsados());

    // 3o uso de item (violacao do limite de 2 itens)
    LimiteItensExcedidosException ex = assertThrows(
        LimiteItensExcedidosException.class,
        () -> batalha.consumirItemNoTurno(treinador1, 0),
        "Deve lancar LimiteItensExcedidosException ao tentar consumir o 3o item."
    );

    assertTrue(ex.getMessage().contains("Limite excedido"),
        "A mensagem da excecao deve apontar a violacao do limite regulamentar.");
    assertEquals(1, treinador1.getMochila().size(),
        "O item restante nao deve ser consumido apos a excecao.");
  }

  /**
   * RNF001: Validacao de valores limite (Boundary Values) de HP, ATK e DEF.
   */
  @Test
  @DisplayName("testCalculoDanoBoundaryValues - Teste de valores limite de dano, HP e atributos")
  public void testCalculoDanoBoundaryValues() {
    Batalha batalha = new Batalha(treinador1, treinador2, terrenoNeutro);

    // Boundary 1: Dano minimo garantido de 1, mesmo quando DEF supera enormemente o ataque
    Pokesal atacanteFraco = criarPokesal("Fraco", HP_PADRAO, 1, DEF_PADRAO,
        SPD_LENTO, TipoElemental.FOGO);
    Pokesal defensorBlindado = criarPokesal("Blindado", HP_PADRAO, ATK_PADRAO, DEF_EXTREMA,
        SPD_LENTO, TipoElemental.AGUA);
    Golpe golpePoderMinimo = new Golpe("Picada", 1, TipoElemental.FOGO);

    int danoMinimo = batalha.calcularDano(atacanteFraco, defensorBlindado, golpePoderMinimo);
    assertEquals(Batalha.DANO_MINIMO_FINAL, danoMinimo,
        "O dano calculado jamais deve ser inferior ao piso regulamentar de 1.");

    // Boundary 2: HP minimo em caso de dano letal (overkill) nao pode ser negativo
    Pokesal alvoDanoExtremo = criarPokesal("Alvo", 50, ATK_PADRAO, DEF_PADRAO,
        SPD_LENTO, TipoElemental.PLANTA);
    alvoDanoExtremo.receberDano(DANO_EXTREMO);
    assertEquals(Pokesal.HP_MINIMO, alvoDanoExtremo.getHpAtual(),
        "O HP de uma criatura nao deve ficar negativo.");
    assertTrue(alvoDanoExtremo.estaDesmaiado(),
        "Criatura com HP zero deve constar como desmaiada.");

    // Boundary 3: Teto de cura nao pode exceder o HP maximo (overheal)
    Pokesal alvoCura = criarPokesal("Curavel", 100, ATK_PADRAO, DEF_PADRAO,
        SPD_LENTO, TipoElemental.AGUA);
    alvoCura.receberDano(10); // HP = 90/100
    alvoCura.curar(50); // 90 + 50 = 140, mas deve limitar em 100
    assertEquals(100, alvoCura.getHpAtual(),
        "A cura jamais pode ultrapassar o teto maximo de pontos de vida.");

    // Boundary 4: Atributo minimo pos-penalidade de status
    Pokesal alvoQueimadura = criarPokesal("Queimavel", HP_PADRAO, 3, DEF_PADRAO,
        SPD_LENTO, TipoElemental.PLANTA);
    alvoQueimadura.aplicarCondicaoStatus(TipoStatus.QUEIMADO); // Reduz 4 de ATK: 3 - 4 = -1
    assertEquals(Pokesal.ATRIBUTO_MINIMO, alvoQueimadura.getAtk(),
        "Atributo penalizado deve respeitar o piso minimo de 1.");
  }

  /**
   * Requisito Autoral RA002: A batalha deve finalizar em no maximo 10 turnos.
   */
  @Test
  @DisplayName("testBatalhaFinalizaEmNoMaximoDezTurnos - RA002: Batalha encerra em ate 10 turnos")
  public void testBatalhaFinalizaEmNoMaximoDezTurnos() {
    Pokesal poke1 = criarPokesal("Tanque1", 500, 10, 50, SPD_RAPIDO, TipoElemental.AGUA);
    Pokesal poke2 = criarPokesal("Tanque2", 500, 10, 50, SPD_LENTO, TipoElemental.AGUA);

    treinador1.setPokesal(poke1);
    treinador2.setPokesal(poke2);

    Batalha batalha = new Batalha(treinador1, treinador2, terrenoNeutro);
    Golpe golpe1 = poke1.getGolpes().get(0);
    Golpe golpe2 = poke2.getGolpes().get(0);

    assertFalse(batalha.isEncerrada(), "Batalha recem-iniciada nao deve estar encerrada.");
    assertEquals(0, batalha.getTurnoDaBatalha());

    // Executa 9 acoes consecutivas: batalha ainda ativa
    for (int i = 1; i <= 9; i++) {
      Treinador atacante = (i % 2 != 0) ? treinador1 : treinador2;
      Treinador defensor = (atacante == treinador1) ? treinador2 : treinador1;
      Golpe golpe = (atacante == treinador1) ? golpe1 : golpe2;

      batalha.executarAcaoDeAtaque(atacante, defensor, golpe);
      assertEquals(i, batalha.getTurnoDaBatalha());
      assertFalse(batalha.isEncerrada(),
          "A batalha nao deve encerrar antes de atingir o 10o turno regulamentar.");
    }

    // 10a acao: atinge o limite regulamentar maximo (RA002)
    batalha.executarAcaoDeAtaque(treinador2, treinador1, golpe2);
    assertEquals(Batalha.LIMITE_MAXIMO_TURNOS, batalha.getTurnoDaBatalha());
    assertTrue(batalha.isEncerrada(),
        "Ao alcancar 10 turnos, a batalha deve ser imediatamente encerrada.");

    // Acoes adicionais posteriores devem retornar dano nulo e manter a batalha encerrada
    int danoExcedente = batalha.executarAcaoDeAtaque(treinador1, treinador2, golpe1);
    assertEquals(Batalha.DANO_NULO, danoExcedente,
        "Tentativas de acao apos o encerramento devem produzir dano zero.");
  }

  /**
   * Requisito Autoral RA003: O desempate deve ocorrer e ser estritamente por HP restante.
   */
  @Test
  @DisplayName("testDesempatePorHpRestante - RA003: Criterio de desempate por maior HP")
  public void testDesempatePorHpRestante() {
    // Cenario 1: Treinador 1 encerra com HP superior
    Pokesal poke1 = criarPokesal("VencedorHP", 200, 10, 50, SPD_RAPIDO, TipoElemental.AGUA);
    Pokesal poke2 = criarPokesal("PerdedorHP", 150, 10, 50, SPD_LENTO, TipoElemental.AGUA);

    treinador1.setPokesal(poke1);
    treinador2.setPokesal(poke2);

    Batalha batalha1 = new Batalha(treinador1, treinador2, terrenoNeutro);
    Golpe golpeFraco = poke1.getGolpes().get(0);

    for (int i = 0; i < Batalha.LIMITE_MAXIMO_TURNOS; i++) {
      Treinador at = (i % 2 == 0) ? treinador1 : treinador2;
      Treinador df = (at == treinador1) ? treinador2 : treinador1;
      batalha1.executarAcaoDeAtaque(at, df, golpeFraco);
    }

    assertTrue(batalha1.isEncerrada());
    assertTrue(poke1.getHpAtual() > poke2.getHpAtual(),
        "Treinador 1 deve ter finalizado com mais HP.");
    assertEquals(treinador1, batalha1.getVencedor(),
        "O competidor com maior HP restante ao final de 10 turnos deve ser o vencedor.");

    // Cenario 2: Empate exato de HP entre ambos os competidores
    Pokesal pokeIgual1 = criarPokesal("Goleiro1", 100, 1, DEF_EXTREMA,
        SPD_RAPIDO, TipoElemental.AGUA);
    Pokesal pokeIgual2 = criarPokesal("Goleiro2", 100, 1, DEF_EXTREMA,
        SPD_LENTO, TipoElemental.AGUA);

    treinador1.setPokesal(pokeIgual1);
    treinador2.setPokesal(pokeIgual2);

    Batalha batalhaEmpate = new Batalha(treinador1, treinador2, terrenoNeutro);
    Golpe golpeMin = pokeIgual1.getGolpes().get(0);

    // Cada um sofre exatamente 5 golpes de 1 de dano
    for (int i = 0; i < Batalha.LIMITE_MAXIMO_TURNOS; i++) {
      Treinador at = (i % 2 == 0) ? treinador1 : treinador2;
      Treinador df = (at == treinador1) ? treinador2 : treinador1;
      batalhaEmpate.executarAcaoDeAtaque(at, df, golpeMin);
    }

    assertTrue(batalhaEmpate.isEncerrada());
    assertEquals(pokeIgual1.getHpAtual(), pokeIgual2.getHpAtual(),
        "Ambos os combatentes devem possuir o mesmo HP ao fim.");
    assertNull(batalhaEmpate.getVencedor(),
        "Em caso de igualdade estrita de HP, o resultado deve ser empate (vencedor nulo).");
  }

  /**
   * Requisito Autoral RA001: Validacao dos efeitos passivos de Held Items em combate.
   */
  @Test
  @DisplayName("testEfeitoHeldItemRequisitoAutoral - RA001: Bonus ofensivo e regenerativo de itens")
  public void testEfeitoHeldItemRequisitoAutoral() {
    // 1. Held Item Ofensivo: Carvao Incandescente (+6 dano e x1.1 para Fogo)
    Pokesal atacante = criarPokesal("CharSal", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        SPD_RAPIDO, TipoElemental.FOGO);
    Pokesal defensor = criarPokesal("CyndaSal", HP_PADRAO, ATK_PADRAO, DEF_PADRAO,
        SPD_LENTO, TipoElemental.FOGO);
    Golpe golpeFogo = atacante.getGolpes().get(0);

    Batalha batalha = new Batalha(treinador1, treinador2, terrenoNeutro);
    int danoSemItem = batalha.calcularDano(atacante, defensor, golpeFogo); // Base: 40

    HeldItem carvao = new HeldItem("Carvao Incandescente",
        new EfeitoDeStatusHeld(6, 0, TipoElemental.FOGO, 1.1));
    atacante.setHeldItem(carvao);

    // Calculo esperado: round(40 * 1.0 * 1.0 * 1.1) + 6 = 44 + 6 = 50
    int danoComItem = batalha.calcularDano(atacante, defensor, golpeFogo);
    assertEquals(50, danoComItem,
        "O item segurado Carvao Incandescente deve amplificar o dano do golpe de Fogo.");
    assertTrue(danoComItem > danoSemItem,
        "Dano com item ofensivo deve ser estritamente superior ao dano sem item.");

    // 2. Held Item Regenerativo: Restos do RU (+6 HP por turno)
    Pokesal portadorLeftovers = criarPokesal("BulbaSal", 100, ATK_PADRAO, DEF_PADRAO,
        SPD_LENTO, TipoElemental.PLANTA);
    portadorLeftovers.receberDano(30); // HP = 70/100

    HeldItem restos = new HeldItem("Restos do RU (Leftovers)",
        new EfeitoDeStatusHeld(0, 6, null, 1.0));
    portadorLeftovers.setHeldItem(restos);

    portadorLeftovers.processarEfeitosFimDeTurno();
    assertEquals(76, portadorLeftovers.getHpAtual(),
        "O item segurado Restos do RU deve regenerar 6 HP ao processar fim de turno.");
  }
}
