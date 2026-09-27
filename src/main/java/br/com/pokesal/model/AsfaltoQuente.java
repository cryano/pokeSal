package br.com.pokesal.model;

import br.com.pokesal.service.EfeitoDeStatusTerreno;

/**
 * Terreno de asfalto quente que favorece golpes do tipo Fogo.
 */
public class AsfaltoQuente extends Terreno {
  /** Percentual de amplificacao de dano para golpes do tipo Fogo. */
  public static final int PERCENTUAL_BONUS_FOGO = 15;

  /**
   * Cria o terreno Asfalto Quente com bonus para golpes do tipo Fogo.
   */
  public AsfaltoQuente() {
    super("Asfalto Quente (Dia)",
        new EfeitoDeStatusTerreno(TipoElemental.FOGO, Terreno.MODIFICADOR_ASFALTO_QUENTE,
            EfeitoDeStatusTerreno.SEM_CURA));
  }

  /**
   * Retorna o percentual de bonus de dano fornecido pelo terreno.
   *
   * @return percentual de bonus (15).
   */
  @Override
  public int aplicarEfeito() {
    return PERCENTUAL_BONUS_FOGO;
  }
}
