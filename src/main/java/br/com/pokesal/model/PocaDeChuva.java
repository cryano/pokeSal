package br.com.pokesal.model;

import br.com.pokesal.service.EfeitoDeStatusTerreno;

/**
 * Terreno alagado com pocas de chuva que favorece golpes do tipo Agua.
 */
public class PocaDeChuva extends Terreno {
  /** Percentual de amplificacao de dano para golpes do tipo Agua. */
  public static final int PERCENTUAL_BONUS_AGUA = 10;

  /**
   * Cria o terreno Poca de Chuva com bonus para golpes do tipo Agua.
   */
  public PocaDeChuva() {
    super("Poça de Chuva / Piso Escorregadio",
        new EfeitoDeStatusTerreno(TipoElemental.AGUA, Terreno.MODIFICADOR_POCA_CHUVA,
            EfeitoDeStatusTerreno.SEM_CURA));
  }

  /**
   * Retorna o percentual de bonus de dano fornecido pelo terreno.
   *
   * @return percentual de bonus (10).
   */
  @Override
  public int aplicarEfeito() {
    return PERCENTUAL_BONUS_AGUA;
  }
}