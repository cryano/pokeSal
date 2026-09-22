package br.com.pokesal.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma criatura de combate com seus atributos, golpes e condicoes de status.
 */
public class Pokesal {
  /** Quantidade exata de golpes que todo Pokesal deve possuir. */
  public static final int QUANTIDADE_EXATA_GOLPES = 3;

  /** Dano fixo sofrido a cada rodada pela condicao de queimadura. */
  public static final int DANO_QUEIMADURA_FIXO = 6;

  /** Reducao direta no atributo de ataque ao sofrer queimadura. */
  public static final int REDUCAO_ATK_QUEIMADURA = 4;

  /** Dano base progressivo sofrido por turno sob envenenamento. */
  public static final int DANO_ENVENENAMENTO_BASE = 4;

  /** Reducao direta no atributo de velocidade ao sofrer paralisia. */
  public static final int REDUCAO_SPD_PARALISIA = 5;

  /** Valor minimo permitido para atributos de combate apos penalidades. */
  public static final int ATRIBUTO_MINIMO = 1;

  /** Limite inferior de pontos de vida. */
  public static final int HP_MINIMO = 0;

  private final String nome;
  private final int hpMax;
  private final int atkBase;
  private final int spdBase;
  private final TipoElemental tipoElemental;
  private final List<Golpe> golpes;
  private int hpAtual;
  private int atk;
  private int def;
  private int spd;
  private HeldItem heldItem;
  private TipoStatus statusAtual;
  private int turnosEnvenenado;

  /**
   * Constroi um novo Pokesal com validacao da quantidade exata de golpes.
   *
   * @param nome o nome da criatura.
   * @param hpMax a quantidade maxima de pontos de vida.
   * @param atk o valor inicial de ataque.
   * @param def o valor de defesa.
   * @param spd o valor de velocidade de iniciativa.
   * @param tipoElemental o tipo elemental da criatura.
   * @param golpes lista contendo exatamente 3 golpes validos.
   * @throws IllegalArgumentException se a lista de golpes nao possuir exatamente 3 golpes.
   */
  public Pokesal(String nome, int hpMax, int atk, int def, int spd,
                 TipoElemental tipoElemental, List<Golpe> golpes) {
    if (golpes == null || golpes.size() != QUANTIDADE_EXATA_GOLPES) {
      throw new IllegalArgumentException("Um Pokesal deve possuir exatamente 3 golpes.");
    }
    this.nome = nome;
    this.hpMax = hpMax;
    this.hpAtual = hpMax;
    this.atk = atk;
    this.def = def;
    this.spd = spd;
    this.atkBase = atk;
    this.spdBase = spd;
    this.tipoElemental = tipoElemental;
    this.golpes = new ArrayList<>(golpes);
    this.statusAtual = TipoStatus.NENHUM;
    this.turnosEnvenenado = 0;
  }

  /**
   * Restaura o Pokesal completamente para uma nova batalha, restaurando HP e atributos base.
   */
  public void restaurarParaNovaBatalha() {
    this.hpAtual = this.hpMax;
    this.atk = this.atkBase;
    this.spd = this.spdBase;
    this.statusAtual = TipoStatus.NENHUM;
    this.turnosEnvenenado = 0;
  }

  /**
   * Verifica se a criatura esta desmaiada (sem pontos de vida).
   *
   * @return true se o HP for zero ou menor, false caso contrario.
   */
  public boolean estaDesmaiado() {
    return this.hpAtual <= HP_MINIMO;
  }

  /**
   * Aplica uma nova condicao de status se a criatura ainda nao possuir nenhuma ativa.
   *
   * @param novoStatus a condicao de status a ser aplicada.
   */
  public void aplicarCondicaoStatus(TipoStatus novoStatus) {
    if (this.statusAtual == TipoStatus.NENHUM && novoStatus != null
        && novoStatus != TipoStatus.NENHUM) {
      this.statusAtual = novoStatus;

      if (novoStatus == TipoStatus.QUEIMADO) {
        this.atk = Math.max(ATRIBUTO_MINIMO, this.atk - REDUCAO_ATK_QUEIMADURA);
        System.out.printf(
            "  [STATUS] %s foi afetado pelo efeito: QUEIMADO! "
                + "(ATK reduzido em %d para %d e sofrera dano a cada rodada)%n",
            this.nome, REDUCAO_ATK_QUEIMADURA, this.atk);
      } else if (novoStatus == TipoStatus.PARALISADO) {
        this.spd = Math.max(ATRIBUTO_MINIMO, this.spd - REDUCAO_SPD_PARALISIA);
        System.out.printf(
            "  [STATUS] %s foi afetado pelo efeito: PARALISADO! "
                + "(Velocidade SPD reduzida em %d para %d)%n",
            this.nome, REDUCAO_SPD_PARALISIA, this.spd);
      } else if (novoStatus == TipoStatus.ENVENENADO) {
        System.out.printf(
            "  [STATUS] %s foi afetado pelo efeito: ENVENENADO! "
                + "(Sofrera dano toxico progressivo a cada rodada)%n",
            this.nome);
      }
    }
  }

  /**
   * Restaura uma quantidade de pontos de vida sem ultrapassar o HP maximo da criatura.
   *
   * @param cura quantidade de pontos de vida a ser recuperada.
   */
  public void curar(int cura) {
    if (cura > 0 && !estaDesmaiado()) {
      this.hpAtual = Math.min(this.hpMax, this.hpAtual + cura);
    }
  }

  /**
   * Remove qualquer condicao de status anormal ativa e restabelece os atributos base originais.
   */
  public void curarStatus() {
    if (this.statusAtual != null && this.statusAtual != TipoStatus.NENHUM) {
      System.out.printf(
          "  [STATUS] %s foi curado da condicao de %s! Seus atributos foram restaurados.%n",
          this.nome, this.statusAtual);
    }
    this.statusAtual = TipoStatus.NENHUM;
    this.atk = this.atkBase;
    this.spd = this.spdBase;
    this.turnosEnvenenado = 0;
  }

  /**
   * Retorna o identificador ordinal do status atual (compatibilidade com UML).
   *
   * @return codigo ordinal da condicao de status.
   */
  public int getStatusDoPokesal() {
    return (this.statusAtual != null) ? this.statusAtual.ordinal() : 0;
  }

  /**
   * Verifica se o Pokesal esta sob efeito de alguma condicao prejudicial de status.
   *
   * @return true se houver status negativo ativo, false se estiver sem alteracao.
   */
  public boolean temStatus() {
    return this.statusAtual != null && this.statusAtual != TipoStatus.NENHUM;
  }

  /**
   * Processa os efeitos continuos de status e itens segurados ao final da rodada de combate.
   */
  public void processarEfeitosFimDeTurno() {
    if (estaDesmaiado()) {
      return;
    }

    if (this.statusAtual == TipoStatus.QUEIMADO) {
      receberDano(DANO_QUEIMADURA_FIXO);
      System.out.printf(
          "  [STATUS] %s sofreu %d de dano continuo por QUEIMADURA! (HP restante: %d/%d)%n",
          this.nome, DANO_QUEIMADURA_FIXO, this.hpAtual, this.hpMax);
    } else if (this.statusAtual == TipoStatus.ENVENENADO) {
      this.turnosEnvenenado++;
      int danoProgressivo = DANO_ENVENENAMENTO_BASE * this.turnosEnvenenado;
      receberDano(danoProgressivo);
      System.out.printf(
          "  [STATUS] %s sofreu %d de dano continuo por ENVENENAMENTO (Turno %d)! "
              + "(HP restante: %d/%d)%n",
          this.nome, danoProgressivo, this.turnosEnvenenado, this.hpAtual, this.hpMax);
    }
    if (this.heldItem != null) {
      int hpAntes = this.hpAtual;
      this.heldItem.aplicarEfeito(this);
      int cura = this.hpAtual - hpAntes;
      if (cura > 0) {
        System.out.printf(
            "  [HELD ITEM] %s ativou '%s' e regenerou +%d HP! (HP atual: %d/%d)%n",
            this.nome, this.heldItem.getNome(), cura, this.hpAtual, this.hpMax);
      }
    }
  }

  /**
   * Deduz uma quantidade de dano dos pontos de vida atuais, sem permitir valores negativos.
   *
   * @param dano quantidade de dano a ser deduzida.
   */
  public void receberDano(int dano) {
    if (dano > 0) {
      this.hpAtual = Math.max(HP_MINIMO, this.hpAtual - dano);
    }
  }

  /**
   * Retorna o nome da criatura.
   *
   * @return nome do Pokesal.
   */
  public String getNome() {
    return nome;
  }

  /**
   * Retorna a quantidade maxima de pontos de vida.
   *
   * @return HP maximo.
   */
  public int getHpMax() {
    return hpMax;
  }

  /**
   * Retorna a quantidade atual de pontos de vida restantes.
   *
   * @return HP atual.
   */
  public int getHpAtual() {
    return hpAtual;
  }

  /**
   * Retorna o valor de ataque efetivo atual.
   *
   * @return valor de ataque.
   */
  public int getAtk() {
    return atk;
  }

  /**
   * Retorna o valor de defesa do Pokesal.
   *
   * @return valor de defesa.
   */
  public int getDef() {
    return def;
  }

  /**
   * Retorna a velocidade efetiva atual para calculo de iniciativa.
   *
   * @return valor de velocidade (SPD).
   */
  public int getSpd() {
    return spd;
  }

  /**
   * Retorna o tipo elemental primario da criatura.
   *
   * @return tipo elemental.
   */
  public TipoElemental getTipoElemental() {
    return tipoElemental;
  }

  /**
   * Retorna a lista de golpes cadastrados para a criatura.
   *
   * @return lista com os golpes.
   */
  public List<Golpe> getGolpes() {
    return golpes;
  }

  /**
   * Retorna o item segurado equipado na criatura, se houver.
   *
   * @return item segurado ou null.
   */
  public HeldItem getHeldItem() {
    return heldItem;
  }

  /**
   * Retorna a condicao de status anormal atual da criatura.
   *
   * @return condicao de status atual.
   */
  public TipoStatus getStatusAtual() {
    return statusAtual;
  }

  /**
   * Retorna a contagem de turnos acumulados sob envenenamento.
   *
   * @return turnos sob efeito de envenenamento.
   */
  public int getTurnosEnvenenado() {
    return turnosEnvenenado;
  }

  /**
   * Equipa um item segurado no Pokesal.
   *
   * @param heldItem o item segurado a ser equipado.
   */
  public void setHeldItem(HeldItem heldItem) {
    this.heldItem = heldItem;
  }
}
