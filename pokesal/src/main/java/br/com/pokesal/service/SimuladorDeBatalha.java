package br.com.pokesal.service;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Servico responsavel pelo catalogo oficial, sorteios de equipes, arenas e gestao de batalhas.
 */
public class SimuladorDeBatalha {
  /** Quantidade minima de competidores para realizacao do sorteio da primeira partida. */
  public static final int QUANTIDADE_MINIMA_TREINADORES = 2;

  /** Indice do primeiro competidor sorteado para a partida. */
  public static final int INDICE_PRIMEIRO_CONFRONTO = 0;

  /** Indice do segundo competidor sorteado para a partida. */
  public static final int INDICE_SEGUNDO_CONFRONTO = 1;

  private final List<Treinador> treinadores;
  private final Random random;
  private int contadorBatalha;

  /**
   * Construtor padrao com gerador pseudoaleatorio padrao.
   */
  public SimuladorDeBatalha() {
    this(new Random());
  }

  /**
   * Construtor que permite injecao do gerador pseudoaleatorio para testes reproduziveis.
   *
   * @param random gerador aleatorio.
   */
  public SimuladorDeBatalha(Random random) {
    this.treinadores = new ArrayList<>();
    this.contadorBatalha = 0;
    this.random = random;
  }

  /**
   * Adiciona um treinador a lista oficial do torneio.
   *
   * @param treinador treinador a ser cadastrado.
   */
  public void adicionarTreinador(Treinador treinador) {
    if (treinador != null) {
      this.treinadores.add(treinador);
    }
  }

  /**
   * Cadastra os competidores iniciais padrao do torneio oficial UCSAL.
   */
  public void cadastrarTreinadores() {
    adicionarTreinador(new Treinador("Ash Sal"));
    adicionarTreinador(new Treinador("Gary Sal"));
  }

  /**
   * Sorteia um Pokesal do catalogo oficial e o associa ao competidor por clonagem profunda.
   *
   * @param treinador competidor que recebera a criatura.
   */
  public void sortearPokesal(Treinador treinador) {
    List<Pokesal> opcoes = obterCatalogoPokesal();
    Pokesal modelo = opcoes.get(random.nextInt(opcoes.size()));
    Pokesal sorteado = clonarPokesal(modelo);
    treinador.setPokesal(sorteado);

    associarItensMochila(treinador);
  }

  /**
   * Equipa a mochila do treinador com o kit oficial regulamentar de 3 itens de batalha.
   *
   * @param treinador competidor cuja mochila sera abastecida.
   */
  public void associarItensMochila(Treinador treinador) {
    if (treinador != null) {
      treinador.adicionarItem(new Potion());
      treinador.adicionarItem(new SuperPotion());
      treinador.adicionarItem(new Antidote());
    }
  }

  /**
   * Cria uma nova instancia independente de Pokesal baseada em um modelo existente.
   *
   * @param base modelo original de referencia.
   * @return nova instancia clonada isolada em memoria.
   */
  public static Pokesal clonarPokesal(Pokesal base) {
    return new Pokesal(base.getNome(), base.getHpMax(), base.getAtk(), base.getDef(),
        base.getSpd(), base.getTipoElemental(), base.getGolpes());
  }

  /**
   * Sorteia um item segurado do catalogo e equipa no Pokesal ativo do treinador.
   *
   * @param treinador treinador portador do Pokesal.
   */
  public void sortearHeldItem(Treinador treinador) {
    List<HeldItem> opcoes = obterCatalogoHeldItems();
    HeldItem sorteado = opcoes.get(random.nextInt(opcoes.size()));
    if (treinador.getPokesal() != null) {
      treinador.getPokesal().setHeldItem(sorteado);
    }
  }

  /**
   * Sorteia uma das 3 arenas oficiais cadastradas para o combate.
   *
   * @return instancia de terreno sorteada.
   */
  public Terreno sortearTerreno() {
    List<Terreno> opcoes = obterCatalogoTerrenos();
    return opcoes.get(random.nextInt(opcoes.size()));
  }

  /**
   * Realiza o sorteio da ordem de chaveamento para o confronto inicial.
   *
   * @return lista com os 2 competidores sorteados para o confronto.
   */
  public List<Treinador> sortearPrimeiraPartida() {
    if (treinadores.size() < QUANTIDADE_MINIMA_TREINADORES) {
      return new ArrayList<>(treinadores);
    }
    List<Treinador> copia = new ArrayList<>(treinadores);
    Collections.shuffle(copia, random);
    return Arrays.asList(
        copia.get(INDICE_PRIMEIRO_CONFRONTO),
        copia.get(INDICE_SEGUNDO_CONFRONTO));
  }

  /**
   * Inicia e registra uma nova batalha oficial incrementando o contador historico.
   *
   * @param treinador1 primeiro participante.
   * @param treinador2 segundo participante.
   * @param terrenoEscolhido arena de combate.
   * @return instancia de batalha inicializada.
   */
  public Batalha iniciarBatalha(Treinador treinador1, Treinador treinador2,
                                Terreno terrenoEscolhido) {
    this.contadorBatalha++;
    return new Batalha(treinador1, treinador2, terrenoEscolhido, random);
  }

  /**
   * Retorna a quantidade total de batalhas iniciadas neste simulador.
   *
   * @return contagem acumulada de batalhas.
   */
  public int getContadorBatalha() {
    return contadorBatalha;
  }

  /**
   * Retorna o catalogo oficial com os 6 Pokesal homologados para a competicao.
   *
   * @return lista das 6 especies de Pokesal com seus golpes.
   */
  public static List<Pokesal> obterCatalogoPokesal() {
    List<Pokesal> lista = new ArrayList<>();

    Golpe b1 = new Golpe("Chicote de Vinha", 40, TipoElemental.PLANTA);
    Golpe b2 = new Golpe("Pó Venenoso", 20, TipoElemental.PLANTA,
        new EfeitoDeStatusGolpe(TipoStatus.ENVENENADO, Batalha.CHANCE_STATUS_SEGUNDO_GOLPE));
    Golpe b3 = new Golpe("Investida", 30, TipoElemental.PLANTA);
    lista.add(new Pokesal("BulbaSal", 120, 35, 30, 25,
        TipoElemental.PLANTA, Arrays.asList(b1, b2, b3)));

    Golpe c1 = new Golpe("Lança Chamas", 42, TipoElemental.FOGO);
    Golpe c2 = new Golpe("Brasas Queimantes", 21, TipoElemental.FOGO,
        new EfeitoDeStatusGolpe(TipoStatus.QUEIMADO, Batalha.CHANCE_STATUS_SEGUNDO_GOLPE));
    Golpe c3 = new Golpe("Arranhão", 28, TipoElemental.FOGO);
    lista.add(new Pokesal("CharSal", 110, 42, 25, 35,
        TipoElemental.FOGO, Arrays.asList(c1, c2, c3)));

    Golpe s1 = new Golpe("Jato de Água", 38, TipoElemental.AGUA);
    Golpe s2 = new Golpe("Onda Estática", 19, TipoElemental.AGUA,
        new EfeitoDeStatusGolpe(TipoStatus.PARALISADO, Batalha.CHANCE_STATUS_SEGUNDO_GOLPE));
    Golpe s3 = new Golpe("Cabeçada", 30, TipoElemental.AGUA);
    lista.add(new Pokesal("SquirtSal", 125, 32, 38, 22,
        TipoElemental.AGUA, Arrays.asList(s1, s2, s3)));

    Golpe ck1 = new Golpe("Folha Navalha", 39, TipoElemental.PLANTA);
    Golpe ck2 = new Golpe("Esporo Paralisante", 19, TipoElemental.PLANTA,
        new EfeitoDeStatusGolpe(TipoStatus.PARALISADO, Batalha.CHANCE_STATUS_SEGUNDO_GOLPE));
    Golpe ck3 = new Golpe("Pancada Suave", 27, TipoElemental.PLANTA);
    lista.add(new Pokesal("ChikoSal", 115, 33, 35, 28,
        TipoElemental.PLANTA, Arrays.asList(ck1, ck2, ck3)));

    Golpe cy1 = new Golpe("Roda de Fogo", 41, TipoElemental.FOGO);
    Golpe cy2 = new Golpe("Fumaça Tóxica", 20, TipoElemental.FOGO,
        new EfeitoDeStatusGolpe(TipoStatus.ENVENENADO, Batalha.CHANCE_STATUS_SEGUNDO_GOLPE));
    Golpe cy3 = new Golpe("Ataque Rápido", 30, TipoElemental.FOGO);
    lista.add(new Pokesal("CyndaSal", 108, 40, 26, 36,
        TipoElemental.FOGO, Arrays.asList(cy1, cy2, cy3)));

    Golpe t1 = new Golpe("Pistola d'Água", 39, TipoElemental.AGUA);
    Golpe t2 = new Golpe("Mordida Térmica", 19, TipoElemental.AGUA,
        new EfeitoDeStatusGolpe(TipoStatus.QUEIMADO, Batalha.CHANCE_STATUS_SEGUNDO_GOLPE));
    Golpe t3 = new Golpe("Arremesso", 29, TipoElemental.AGUA);
    lista.add(new Pokesal("TotoSal", 122, 36, 34, 26,
        TipoElemental.AGUA, Arrays.asList(t1, t2, t3)));

    return lista;
  }

  /**
   * Retorna o catalogo oficial com os 6 itens segurados (held items) homologados.
   *
   * @return lista dos 6 held items com suas bonificacoes passivas.
   */
  public static List<HeldItem> obterCatalogoHeldItems() {
    return Arrays.asList(
        new HeldItem("Carvão Incandescente",
            new EfeitoDeStatusHeld(6, 0, TipoElemental.FOGO, 1.1)),
        new HeldItem("Gota d'Água Sagrada",
            new EfeitoDeStatusHeld(6, 0, TipoElemental.AGUA, 1.1)),
        new HeldItem("Semente de Salto",
            new EfeitoDeStatusHeld(6, 0, TipoElemental.PLANTA, 1.1)),
        new HeldItem("Faixa de Foco UCSal",
            new EfeitoDeStatusHeld(5, 0, null, 1.0)),
        new HeldItem("Restos do RU (Leftovers)",
            new EfeitoDeStatusHeld(0, 6, null, 1.0)),
        new HeldItem("Colete de Pituaçu",
            new EfeitoDeStatusHeld(4, 3, null, 1.0))
    );
  }

  /**
   * Retorna o catalogo com as 3 arenas regulamentares de combate.
   *
   * @return lista dos 3 terrenos cadastrados.
   */
  public static List<Terreno> obterCatalogoTerrenos() {
    return Arrays.asList(
        new AsfaltoQuente(),
        new PocaDeChuva(),
        new CanteiroCentral()
    );
  }

  /**
   * Retorna uma visao imutavel da lista de competidores inscritos.
   *
   * @return lista imutavel de treinadores.
   */
  public List<Treinador> getTreinadores() {
    return Collections.unmodifiableList(treinadores);
  }
}