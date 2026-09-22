package br.com.pokesal.model;

/**
 * Item consumivel que restaura pontos de vida basicos de um Pokesal.
 */
public class Potion extends ItemDeBatalha {
  /** Quantidade de pontos de vida restaurados pela pocao. */
  private static final int CURA = 20;

  /**
   * Construtor que inicializa a pocao basica de cura.
   */
  public Potion() {
    super("Potion");
  }

  /**
   * Aplica o efeito de cura sobre o Pokesal alvo.
   *
   * @param alvo o Pokesal que recebera os pontos de vida.
   */
  @Override
  public void aplicar(Pokesal alvo) {
    if (alvo != null) {
      alvo.curar(CURA);
    }
  }
}