package br.com.pokesal.model;

/**
 * Tipos elementais dos Pokesal e golpes, contendo relacoes de vantagem e desvantagem.
 */
public enum TipoElemental {
  FOGO,
  AGUA,
  PLANTA;

  /** Multiplicador aplicado quando ha vantagem elemental. */
  public static final double VANTAGEM_MULTIPLICADOR = 2.0;

  /** Multiplicador aplicado quando ha desvantagem elemental. */
  public static final double DESVANTAGEM_MULTIPLICADOR = 0.5;

  /** Multiplicador aplicado quando o combate elemental e neutro. */
  public static final double NEUTRO_MULTIPLICADOR = 1.0;

  /**
   * Calcula o multiplicador de dano em relacao ao tipo elemental do defensor.
   *
   * @param alvo tipo elemental do Pokesal defensor.
   * @return multiplicador de dano aplicavel (0.5, 1.0 ou 2.0).
   */
  public double obterMultiplicadorContra(TipoElemental alvo) {
    if (alvo == null) {
      return NEUTRO_MULTIPLICADOR;
    }
    if (this == FOGO && alvo == PLANTA) {
      return VANTAGEM_MULTIPLICADOR;
    }
    if (this == FOGO && alvo == AGUA) {
      return DESVANTAGEM_MULTIPLICADOR;
    }
    if (this == AGUA && alvo == FOGO) {
      return VANTAGEM_MULTIPLICADOR;
    }
    if (this == PLANTA && alvo == AGUA) {
      return VANTAGEM_MULTIPLICADOR;
    }
    if (this == PLANTA && alvo == FOGO) {
      return DESVANTAGEM_MULTIPLICADOR;
    }
    return NEUTRO_MULTIPLICADOR;
  }
}