package br.com.pokesal.model;

/**
 * Item consumivel que restaura uma quantidade elevada de pontos de vida de um Pokesal.
 */
public class SuperPotion extends ItemDeBatalha {
  /** Quantidade elevada de pontos de vida restaurados pela Super Potion. */
  private static final int CURA = 50;

  /**
   * Construtor que inicializa a super pocao de cura.
   */
  public SuperPotion() {
    super("Super Potion");
  }

  /**
   * Aplica o efeito de cura intensa sobre o Pokesal alvo.
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