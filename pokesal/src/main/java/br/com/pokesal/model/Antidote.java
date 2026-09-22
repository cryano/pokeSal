package br.com.pokesal.model;

/**
 * Item consumivel que cura condicoes anormais de status de um Pokesal.
 */
public class Antidote extends ItemDeBatalha {
  /** Valor numerico de efeito associado ao antidoto para compatibilidade. */
  public static final int EFEITO = 0;

  /**
   * Construtor que inicializa o antidoto.
   */
  public Antidote() {
    super("Antidote");
  }

  /**
   * Aplica a remocao de condicoes de status sobre o Pokesal alvo.
   *
   * @param alvo o Pokesal que tera seu status restaurado.
   */
  @Override
  public void aplicar(Pokesal alvo) {
    if (alvo != null) {
      alvo.curarStatus();
    }
  }

  /**
   * Retorna o valor numerico de efeito do antidoto.
   *
   * @return valor de efeito.
   */
  public int getEfeito() {
    return EFEITO;
  }
}
