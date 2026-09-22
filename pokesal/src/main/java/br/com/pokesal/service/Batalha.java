package br.com.pokesal.service;

import br.com.pokesal.model.Golpe;
import br.com.pokesal.model.Pokesal;
import br.com.pokesal.model.Terreno;
import br.com.pokesal.model.Treinador;
import java.util.Random;

/**
 * Mediador e maquina de estados responsavel por executar as regras de combate entre treinadores.
 */
public class Batalha {
  /** Limite maximo regulamentar de turnos da batalha (5 acoes para cada treinador). */
  public static final int LIMITE_MAXIMO_TURNOS = 10;

  /** Dano minimo garantido ao desferir um golpe bem-sucedido. */
  public static final int DANO_MINIMO_FINAL = 1;

  /** Multiplicador do segundo golpe nos catalogos de golpes. */
  public static final double MULTIPLICADOR_SEGUNDO_GOLPE = 0.5;

  /** Probabilidade de ativacao de status para golpes secundarios. */
  public static final double CHANCE_STATUS_SEGUNDO_GOLPE = 0.8;

  /** Indice do primeiro item da mochila para acao padrao. */
  public static final int INDICE_PRIMEIRO_ITEM = 0;

  /** Limiar neutro de modificador de terreno. */
  public static final double MODIFICADOR_NEUTRO_LIMIAR = 1.0;

  /** Multiplicador de dano super efetivo. */
  public static final double MODIFICADOR_SUPER_EFETIVO = 2.0;

  /** Multiplicador de dano pouco efetivo. */
  public static final double MODIFICADOR_POUCO_EFETIVO = 0.5;

  /** Dano minimo inicial antes de aplicacao de multiplicadores. */
  public static final int DANO_MINIMO_BASE = 1;

  /** Dano nulo retornado quando uma acao nao e concretizada. */
  public static final int DANO_NULO = 0;

  private final Treinador treinador1;
  private final Treinador treinador2;
  private final Terreno terredoDaBatalha;
  private final Random random;
  private int turnoDaBatalha;
  private Treinador vencedor;
  private boolean encerrada;

  /**
   * Construtor da batalha utilizando gerador pseudoaleatorio padrao.
   *
   * @param treinador1 primeiro treinador competidor.
   * @param treinador2 segundo treinador competidor.
   * @param terreno arena sorteada para o confronto.
   */
  public Batalha(Treinador treinador1, Treinador treinador2, Terreno terreno) {
    this(treinador1, treinador2, terreno, new Random());
  }

  /**
   * Construtor da batalha permitindo injecao de dependencia do gerador pseudoaleatorio.
   *
   * @param treinador1 primeiro treinador competidor.
   * @param treinador2 segundo treinador competidor.
   * @param terreno arena sorteada para o confronto.
   * @param random gerador aleatorio para calculo de iniciativa e status.
   */
  public Batalha(Treinador treinador1, Treinador treinador2, Terreno terreno, Random random) {
    this.treinador1 = treinador1;
    this.treinador2 = treinador2;
    this.terredoDaBatalha = terreno;
    this.turnoDaBatalha = 0;
    this.encerrada = false;
    this.random = random;
  }

  /**
   * Executa um ataque direto entre duas criaturas (compatibilidade UML).
   *
   * @param atacante a criatura que desfere o ataque.
   * @param defensor a criatura que recebe o impacto.
   * @param golpe o movimento de combate utilizado.
   */
  public void atacar(Pokesal atacante, Pokesal defensor, Golpe golpe) {
    if (encerrada || atacante.estaDesmaiado()) {
      return;
    }
    int dano = calcularDano(atacante, defensor, golpe);
    defensor.receberDano(dano);

    if (golpe.getEfeitoDeStatus() != null) {
      double rolagem = random.nextDouble();
      if (rolagem <= golpe.getEfeitoDeStatus().getChanceDeAtivacao()) {
        golpe.getEfeitoDeStatus().aplicar(defensor);
      }
    }
    verificarHp(treinador1, treinador2);
  }

  /**
   * Executa a acao de ataque de um competidor consumindo um turno de combate.
   *
   * @param atacante treinador que ordenou o ataque.
   * @param defensor treinador que recebe a ofensiva.
   * @param golpe movimento executado pelo Pokesal atacante.
   * @return quantidade de dano calculada e infligida.
   */
  public int executarAcaoDeAtaque(Treinador atacante, Treinador defensor, Golpe golpe) {
    if (encerrada || atacante.getPokesal().estaDesmaiado()) {
      return DANO_NULO;
    }
    this.turnoDaBatalha++;

    int dano = calcularDano(atacante.getPokesal(), defensor.getPokesal(), golpe);
    defensor.getPokesal().receberDano(dano);

    double modTerreno = terredoDaBatalha.obterModificadorDano(golpe.getTipo());
    if (modTerreno > MODIFICADOR_NEUTRO_LIMIAR) {
      System.out.printf(
          "  [TERRENO] O terreno '%s' potencializou o golpe %s em %d%%!%n",
          terredoDaBatalha.getNome(), golpe.getNome(), terredoDaBatalha.aplicarEfeito());
    }

    double modElemental = golpe.getTipo().obterMultiplicadorContra(
        defensor.getPokesal().getTipoElemental());
    if (modElemental >= MODIFICADOR_SUPER_EFETIVO) {
      System.out.printf(
          "  [VANTAGEM ELEMENTAL] Golpe SUPER EFETIVO contra %s! (Multiplicador x2.0)%n",
          defensor.getPokesal().getNome());
    } else if (modElemental <= MODIFICADOR_POUCO_EFETIVO) {
      System.out.printf(
          "  [DESVANTAGEM ELEMENTAL] Golpe POUCO EFETIVO contra %s... (Multiplicador x0.5)%n",
          defensor.getPokesal().getNome());
    }

    if (golpe.getEfeitoDeStatus() != null) {
      double rolagem = random.nextDouble();
      if (rolagem <= golpe.getEfeitoDeStatus().getChanceDeAtivacao()) {
        golpe.getEfeitoDeStatus().aplicar(defensor.getPokesal());
      }
    }

    verificarHp(treinador1, treinador2);
    if (this.turnoDaBatalha >= LIMITE_MAXIMO_TURNOS && !encerrada) {
      pontuarVencedor();
    }
    return dano;
  }

  /**
   * Executa uma rodada completa respeitando iniciativa SPD e encerrando se houver desmaio.
   *
   * @param golpeTreinador1 golpe escolhido pelo treinador 1.
   * @param golpeTreinador2 golpe escolhido pelo treinador 2.
   */
  public void executarRodada(Golpe golpeTreinador1, Golpe golpeTreinador2) {
    if (encerrada) {
      return;
    }

    Treinador primeiro = determinarPrimeiroAtacante();
    Treinador segundo = (primeiro == treinador1) ? treinador2 : treinador1;
    Golpe golpePrimeiro = (primeiro == treinador1) ? golpeTreinador1 : golpeTreinador2;
    Golpe golpeSegundo = (segundo == treinador1) ? golpeTreinador1 : golpeTreinador2;

    executarAcaoDeAtaque(primeiro, segundo, golpePrimeiro);

    if (!encerrada && !segundo.getPokesal().estaDesmaiado()) {
      executarAcaoDeAtaque(segundo, primeiro, golpeSegundo);
    }

    finalizarTurno();
  }

  /**
   * Executa um turno individual de combate com finalizacao de efeitos.
   *
   * @param atacante treinador em acao.
   * @param defensor treinador alvo.
   * @param golpe movimento executado.
   */
  public void executarTurno(Treinador atacante, Treinador defensor, Golpe golpe) {
    if (encerrada) {
      return;
    }
    executarAcaoDeAtaque(atacante, defensor, golpe);
    finalizarTurno();
  }

  /**
   * Consome o primeiro item disponivel da mochila do treinador (compatibilidade UML).
   *
   * @param treinador o competidor que utiliza o item.
   */
  public void usarItemdeBatalha(Treinador treinador) {
    consumirItemNoTurno(treinador, INDICE_PRIMEIRO_ITEM);
  }

  /**
   * Consome um item especifico da mochila, consumindo a acao do turno e validando limites.
   *
   * @param treinador o competidor que utiliza o item.
   * @param indiceItem o indice do item na mochila.
   * @throws LimiteItensExcedidosException se o treinador ja tiver usado 2 itens na batalha.
   */
  public void consumirItemNoTurno(Treinador treinador, int indiceItem) {
    if (encerrada) {
      return;
    }
    if (treinador.getContItensUsados() >= Treinador.LIMITE_MAXIMO_ITENS) {
      throw new LimiteItensExcedidosException(
          "Limite excedido! O treinador so pode usar ate 2 itens por batalha.");
    }
    this.turnoDaBatalha++;
    treinador.usarItem(indiceItem);
    if (this.turnoDaBatalha >= LIMITE_MAXIMO_TURNOS && !encerrada) {
      pontuarVencedor();
    }
  }

  /**
   * Calcula o dano final do golpe considerando defesa, tipos, terrenos e held items.
   *
   * @param atacante Pokesal que desfere o ataque.
   * @param defensor Pokesal alvo do ataque.
   * @param golpe movimento ofensivo utilizado.
   * @return dano inteiro final garantindo no minimo 1 de dano.
   */
  public int calcularDano(Pokesal atacante, Pokesal defensor, Golpe golpe) {
    int diferencaAtkDef = atacante.getAtk() - defensor.getDef();
    int baseDano = Math.max(DANO_MINIMO_BASE, diferencaAtkDef + golpe.getPoder());

    double modElemental = golpe.getTipo().obterMultiplicadorContra(defensor.getTipoElemental());
    double modTerreno = terredoDaBatalha.obterModificadorDano(golpe.getTipo());

    double danoCalculado = (baseDano * modElemental * modTerreno);

    int efeitoHeldItem = 0;
    if (atacante.getHeldItem() != null && atacante.getHeldItem().getEfeitoDeStatus() != null) {
      EfeitoDeStatusHeld efeito = atacante.getHeldItem().getEfeitoDeStatus();
      if (efeito.getTipoBeneficiado() == null || efeito.getTipoBeneficiado() == golpe.getTipo()) {
        efeitoHeldItem = efeito.getBonusDanoAtaque();
        danoCalculado *= efeito.getMultiplicadorElemental();
      }
    }

    int danoFinal = (int) Math.round(danoCalculado) + efeitoHeldItem;
    return Math.max(DANO_MINIMO_FINAL, danoFinal);
  }

  /**
   * Determina qual treinador tem a prioridade de agir com base na velocidade SPD.
   *
   * @return o treinador cujo Pokesal possui maior velocidade (ou sorteio se empatado).
   */
  public Treinador determinarPrimeiroAtacante() {
    int spd1 = treinador1.getPokesal().getSpd();
    int spd2 = treinador2.getPokesal().getSpd();

    if (spd1 > spd2) {
      return treinador1;
    } else if (spd2 > spd1) {
      return treinador2;
    }
    return random.nextBoolean() ? treinador1 : treinador2;
  }

  /**
   * Processa os efeitos de fim de turno (regeneracao de terreno, status e itens segurados).
   */
  public void finalizarTurno() {
    if (terredoDaBatalha.getEfeitoDeStatus() != null) {
      int hpAntes1 = treinador1.getPokesal().getHpAtual();
      terredoDaBatalha.getEfeitoDeStatus().aplicar(treinador1.getPokesal());
      int cura1 = treinador1.getPokesal().getHpAtual() - hpAntes1;
      if (cura1 > 0) {
        System.out.printf(
            "  [TERRENO] O terreno '%s' regenerou +%d HP de %s (tipo %s)! (HP atual: %d/%d)%n",
            terredoDaBatalha.getNome(), cura1, treinador1.getPokesal().getNome(),
            treinador1.getPokesal().getTipoElemental(), treinador1.getPokesal().getHpAtual(),
            treinador1.getPokesal().getHpMax());
      }

      int hpAntes2 = treinador2.getPokesal().getHpAtual();
      terredoDaBatalha.getEfeitoDeStatus().aplicar(treinador2.getPokesal());
      int cura2 = treinador2.getPokesal().getHpAtual() - hpAntes2;
      if (cura2 > 0) {
        System.out.printf(
            "  [TERRENO] O terreno '%s' regenerou +%d HP de %s (tipo %s)! (HP atual: %d/%d)%n",
            terredoDaBatalha.getNome(), cura2, treinador2.getPokesal().getNome(),
            treinador2.getPokesal().getTipoElemental(), treinador2.getPokesal().getHpAtual(),
            treinador2.getPokesal().getHpMax());
      }
    }

    treinador1.getPokesal().processarEfeitosFimDeTurno();
    treinador2.getPokesal().processarEfeitosFimDeTurno();

    verificarHp(treinador1, treinador2);

    if (this.turnoDaBatalha >= LIMITE_MAXIMO_TURNOS && !encerrada) {
      pontuarVencedor();
    }
  }

  /**
   * Avalia a condicao de desmaio das criaturas e encerra o combate se houver nocaute.
   *
   * @param t1 primeiro treinador avaliado.
   * @param t2 segundo treinador avaliado.
   */
  public void verificarHp(Treinador t1, Treinador t2) {
    if (t1.getPokesal().estaDesmaiado() && t2.getPokesal().estaDesmaiado()) {
      encerrada = true;
      vencedor = null;
    } else if (t1.getPokesal().estaDesmaiado()) {
      encerrada = true;
      vencedor = t2;
    } else if (t2.getPokesal().estaDesmaiado()) {
      encerrada = true;
      vencedor = t1;
    }
  }

  /**
   * Determina o vencedor por desempate de HP remanescente ao atingir o limite maximo de turnos.
   */
  public void pontuarVencedor() {
    encerrada = true;
    int hp1 = treinador1.getPokesal().getHpAtual();
    int hp2 = treinador2.getPokesal().getHpAtual();

    if (hp1 > hp2) {
      vencedor = treinador1;
    } else if (hp2 > hp1) {
      vencedor = treinador2;
    } else {
      vencedor = null;
    }
  }

  /**
   * Retorna o turno atual da batalha.
   *
   * @return numero do turno da batalha.
   */
  public int getTurnoDaBatalha() {
    return turnoDaBatalha;
  }

  /**
   * Retorna o turno corrente da batalha.
   *
   * @return numero do turno.
   */
  public int getTurnoAtual() {
    return turnoDaBatalha;
  }

  /**
   * Retorna a referencia ao primeiro treinador da batalha.
   *
   * @return treinador 1.
   */
  public Treinador getTreinador1() {
    return treinador1;
  }

  /**
   * Retorna a referencia ao segundo treinador da batalha.
   *
   * @return treinador 2.
   */
  public Treinador getTreinador2() {
    return treinador2;
  }

  /**
   * Retorna o terreno associado a batalha.
   *
   * @return terreno da arena.
   */
  public Terreno getTerreno() {
    return terredoDaBatalha;
  }

  /**
   * Retorna o terreno (nome mantido por compatibilidade com modelagem UML).
   *
   * @return terreno da arena.
   */
  public Terreno getTerredoDaBatalha() {
    return terredoDaBatalha;
  }

  /**
   * Retorna o terreno da batalha.
   *
   * @return terreno da arena.
   */
  public Terreno getTerrenoDaBatalha() {
    return terredoDaBatalha;
  }

  /**
   * Retorna o treinador vencedor do confronto, ou null em caso de empate.
   *
   * @return treinador vencedor ou null.
   */
  public Treinador getVencedor() {
    return vencedor;
  }

  /**
   * Indica se a batalha ja foi encerrada por desmaio ou limite de turnos.
   *
   * @return true se encerrada, false se ainda em andamento.
   */
  public boolean isEncerrada() {
    return encerrada;
  }
}