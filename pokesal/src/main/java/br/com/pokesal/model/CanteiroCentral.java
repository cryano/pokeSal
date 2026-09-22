package br.com.pokesal.model;

import br.com.pokesal.service.EfeitoDeStatusTerreno;

/**
 * Terreno de grama e canteiro que favorece a regeneracao de Pokesals do tipo Planta.
 */
public class CanteiroCentral extends Terreno {
  /** Percentual nominal de regeneracao fornecido pelo terreno. */
  public static final int PERCENTUAL_REGENERACAO_PLANTA = 5;

  /**
   * Cria o terreno Canteiro Central com bonus regenerativo para Planta.
   */
  public CanteiroCentral() {
    super("Canteiro Central",
        new EfeitoDeStatusTerreno(TipoElemental.PLANTA, EfeitoDeStatusTerreno.MULTIPLICADOR_NEUTRO,
            Terreno.CURA_CANTEIRO_CENTRAL));
  }

  /**
   * Retorna o percentual de regeneracao do terreno.
   *
   * @return percentual de regeneracao (5).
   */
  @Override
  public int aplicarEfeito() {
    return PERCENTUAL_REGENERACAO_PLANTA;
  }
}
