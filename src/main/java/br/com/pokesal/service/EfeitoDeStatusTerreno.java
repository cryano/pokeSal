package br.com.pokesal.service;

import br.com.pokesal.model.Pokesal;
import br.com.pokesal.model.TipoElemental;

/**
 * Representa os efeitos ambientais do terreno sobre o combate e a regeneracao de criaturas.
 */
public class EfeitoDeStatusTerreno implements EfeitoDeStatus {
  /** Multiplicador neutro para cenarios sem amplificacao elemental. */
  public static final double MULTIPLICADOR_NEUTRO = 1.0;

  /** Indicador de ausencia de cura passiva pelo terreno. */
  public static final double SEM_CURA = 0.0;

  /** Fator de multiplicacao para conversao de escala unitaria em percentual. */
  private static final int FATOR_PERCENTUAL = 100;

  private final TipoElemental tipoModificado;
  private final double multiplicadorDano;
  private final double percentualCuraPorTurno;

  /**
   * Construtor do efeito de terreno com modificadores elementais e regenerativos.
   *
   * @param tipoModificado tipo elemental afetado pela arena.
   * @param multiplicadorDano fator de aumento de dano aplicado aos ataques.
   * @param percentualCuraPorTurno proporcao do HP maximo regenerada a cada rodada.
   */
  public EfeitoDeStatusTerreno(TipoElemental tipoModificado, double multiplicadorDano,
                               double percentualCuraPorTurno) {
    this.tipoModificado = tipoModificado;
    this.multiplicadorDano = multiplicadorDano;
    this.percentualCuraPorTurno = percentualCuraPorTurno;
  }

  /**
   * Aplica a regeneracao passiva sobre a criatura se ela pertencer ao tipo elemental favorecido.
   *
   * @param alvo o Pokesal presente no terreno.
   */
  @Override
  public void aplicar(Pokesal alvo) {
    if (alvo != null && alvo.getTipoElemental() == this.tipoModificado
        && this.percentualCuraPorTurno > SEM_CURA) {
      int valorCura = (int) Math.round(alvo.getHpMax() * this.percentualCuraPorTurno);
      alvo.curar(valorCura);
    }
  }

  /**
   * Retorna o percentual inteiro equivalente ao multiplicador de dano da arena.
   *
   * @return valor percentual inteiro (ex: 115 para 1.15).
   */
  @Override
  public int aplicarEfeito() {
    return (int) Math.round(this.multiplicadorDano * FATOR_PERCENTUAL);
  }

  /**
   * Retorna o tipo elemental bonificado pela arena.
   *
   * @return tipo elemental.
   */
  public TipoElemental getTipoModificado() {
    return tipoModificado;
  }

  /**
   * Retorna o fator multiplicador de dano do terreno.
   *
   * @return multiplicador de dano.
   */
  public double getMultiplicadorDano() {
    return multiplicadorDano;
  }

  /**
   * Retorna o percentual de cura por turno concedido pela arena.
   *
   * @return percentual de regeneracao.
   */
  public double getPercentualCuraPorTurno() {
    return percentualCuraPorTurno;
  }
}
