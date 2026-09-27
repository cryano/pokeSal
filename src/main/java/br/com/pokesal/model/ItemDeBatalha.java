package br.com.pokesal.model;

/**
 * Representa um item consumivel utilizavel por um treinador durante a batalha.
 */
public abstract class ItemDeBatalha {
  private final String nome;

  /**
   * Construtor base do item de batalha.
   *
   * @param nome nome descritivo do item.
   */
  public ItemDeBatalha(String nome) {
    this.nome = nome;
  }

  /**
   * Retorna o nome descritivo do item.
   *
   * @return nome do item.
   */
  public String getNome() {
    return nome;
  }

  /**
   * Aplica o efeito correspondente do item sobre o Pokesal alvo.
   *
   * @param alvo o Pokesal que recebera o efeito do item.
   */
  public abstract void aplicar(Pokesal alvo);
}