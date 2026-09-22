package br.com.pokesal.service;

import br.com.pokesal.model.Pokesal;
import br.com.pokesal.model.TipoElemental;

/**
 * Representa os efeitos e modificadores passivos concedidos por itens segurados.
 */
public class EfeitoDeStatusHeld implements EfeitoDeStatus {
  /** Valor de acrescimo neutro para atributos nao modificados. */
  public static final int BONUS_NEUTRO = 0;

  private final int bonusDanoAtaque;
  private final int curaTurno;
  private final TipoElemental tipoBeneficiado;
  private final double multiplicadorElemental;

  /**
   * Construtor com parametros completos do efeito passivo do item.
   *
   * @param bonusDanoAtaque valor fixo somado ao poder de ataque.
   * @param curaTurno quantidade de pontos de vida regenerados a cada fim de turno.
   * @param tipoBeneficiado tipo elemental bonificado pelo item ou null se universal.
   * @param multiplicadorElemental multiplicador percentual de dano elemental.
   */
  public EfeitoDeStatusHeld(int bonusDanoAtaque, int curaTurno,
                            TipoElemental tipoBeneficiado, double multiplicadorElemental) {
    this.bonusDanoAtaque = bonusDanoAtaque;
    this.curaTurno = curaTurno;
    this.tipoBeneficiado = tipoBeneficiado;
    this.multiplicadorElemental = multiplicadorElemental;
  }

  /**
   * Aplica a regeneracao passiva sobre o Pokesal portador ao final do turno.
   *
   * @param alvo o Pokesal que segura o item.
   */
  @Override
  public void aplicar(Pokesal alvo) {
    if (alvo != null && this.curaTurno > 0) {
      alvo.curar(this.curaTurno);
    }
  }

  /**
   * Retorna o acrescimo numerico concedido ao atributo de ataque.
   *
   * @return valor de acrescimo de ataque.
   */
  public int efeitoAumentarAtk() {
    return this.bonusDanoAtaque;
  }

  /**
   * Retorna o acrescimo numerico concedido a defesa (compatibilidade UML).
   *
   * @return valor de acrescimo de defesa.
   */
  public int efeitoAumentarDef() {
    return BONUS_NEUTRO;
  }

  /**
   * Retorna o acrescimo numerico concedido a velocidade (compatibilidade UML).
   *
   * @return valor de acrescimo de velocidade.
   */
  public int efeitoAumentarSpd() {
    return BONUS_NEUTRO;
  }

  /**
   * Retorna o valor de efeito principal do item segurado.
   *
   * @return valor do bonus de ataque.
   */
  @Override
  public int aplicarEfeito() {
    return efeitoAumentarAtk();
  }

  /**
   * Retorna o bonus fixo concedido ao ataque.
   *
   * @return bonus de dano de ataque.
   */
  public int getBonusDanoAtaque() {
    return bonusDanoAtaque;
  }

  /**
   * Retorna a quantidade de regeneracao de HP por turno.
   *
   * @return pontos de vida regenerados por turno.
   */
  public int getCuraTurno() {
    return curaTurno;
  }

  /**
   * Retorna o tipo elemental exclusivamente beneficiado, se houver.
   *
   * @return tipo elemental ou null.
   */
  public TipoElemental getTipoBeneficiado() {
    return tipoBeneficiado;
  }

  /**
   * Retorna o multiplicador de amplificacao elemental.
   *
   * @return multiplicador elemental.
   */
  public double getMultiplicadorElemental() {
    return multiplicadorElemental;
  }
}
