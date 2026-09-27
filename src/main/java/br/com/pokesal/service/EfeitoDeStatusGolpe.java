package br.com.pokesal.service;

import br.com.pokesal.model.Pokesal;
import br.com.pokesal.model.TipoStatus;

/**
 * Representa a aplicacao de condicoes de status decorrentes do uso de golpes em combate.
 */
public class EfeitoDeStatusGolpe implements EfeitoDeStatus {
  /** Chance padrao de 100% de ativacao do status quando nao parametrizada. */
  public static final double CHANCE_PADRAO = 1.0;

  /** Valor retornado quando nenhum efeito numerico e produzido. */
  public static final int EFEITO_NULO = 0;

  private final TipoStatus tipoStatus;
  private final double chanceDeAtivacao;

  /**
   * Construtor com definicao do tipo de status e probabilidade de ativacao.
   *
   * @param tipoStatus o tipo de status anormal gerado pelo golpe.
   * @param chanceDeAtivacao probabilidade percentual (entre 0.0 e 1.0).
   */
  public EfeitoDeStatusGolpe(TipoStatus tipoStatus, double chanceDeAtivacao) {
    this.tipoStatus = tipoStatus;
    this.chanceDeAtivacao = chanceDeAtivacao;
  }

  /**
   * Construtor de conveniencia que assume probabilidade garantida de 100%.
   *
   * @param tipoStatus o tipo de status anormal gerado pelo golpe.
   */
  public EfeitoDeStatusGolpe(TipoStatus tipoStatus) {
    this(tipoStatus, CHANCE_PADRAO);
  }

  /**
   * Aplica a condicao de status correspondente sobre o Pokesal atingido.
   *
   * @param alvo o Pokesal receptor do golpe.
   */
  @Override
  public void aplicar(Pokesal alvo) {
    if (alvo != null && this.tipoStatus != TipoStatus.NENHUM) {
      alvo.aplicarCondicaoStatus(this.tipoStatus);
    }
  }

  /**
   * Retorna o valor fixo de dano por queimadura.
   *
   * @return dano por queimadura.
   */
  public int efeitoQueimado() {
    return Pokesal.DANO_QUEIMADURA_FIXO;
  }

  /**
   * Retorna o dano base por envenenamento.
   *
   * @return dano de envenenamento.
   */
  public int efeitoEnvenenamento() {
    return Pokesal.DANO_ENVENENAMENTO_BASE;
  }

  /**
   * Retorna o valor de reducao de velocidade por paralisia.
   *
   * @return reducao de SPD.
   */
  public int efeitoParalisado() {
    return Pokesal.REDUCAO_SPD_PARALISIA;
  }

  /**
   * Executa o calculo do valor de efeito com base no tipo de status atual.
   *
   * @return valor numerico da penalidade ou dano do status.
   */
  @Override
  public int aplicarEfeito() {
    if (this.tipoStatus == TipoStatus.QUEIMADO) {
      return efeitoQueimado();
    }
    if (this.tipoStatus == TipoStatus.ENVENENADO) {
      return efeitoEnvenenamento();
    }
    if (this.tipoStatus == TipoStatus.PARALISADO) {
      return efeitoParalisado();
    }
    return EFEITO_NULO;
  }

  /**
   * Retorna o tipo de status atribuido ao golpe.
   *
   * @return tipo de status.
   */
  public TipoStatus getTipoStatus() {
    return tipoStatus;
  }

  /**
   * Retorna a probabilidade de ativacao do status.
   *
   * @return chance entre 0.0 e 1.0.
   */
  public double getChanceDeAtivacao() {
    return chanceDeAtivacao;
  }
}
