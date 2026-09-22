package br.com.pokesal.model;

import br.com.pokesal.service.EfeitoDeStatusHeld;

/**
 * Representa um item segurado por um Pokesal que concede bonus passivos em combate.
 */
public class HeldItem {
  private final String nome;
  private final EfeitoDeStatusHeld efeitoDeStatus;

  /**
   * Cria um item segurado com nome e efeito passivo associado.
   *
   * @param nome o nome do item.
   * @param efeitoDeStatus o efeito de status passivo concedido pelo item.
   */
  public HeldItem(String nome, EfeitoDeStatusHeld efeitoDeStatus) {
    this.nome = nome;
    this.efeitoDeStatus = efeitoDeStatus;
  }

  /**
   * Retorna o nome do item segurado.
   *
   * @return nome do item.
   */
  public String getNome() {
    return nome;
  }

  /**
   * Retorna o efeito de status passivo do item.
   *
   * @return efeito passivo associado.
   */
  public EfeitoDeStatusHeld getEfeitoDeStatus() {
    return efeitoDeStatus;
  }

  /**
   * Aplica o efeito passivo do item ao portador.
   *
   * @param portador o Pokesal que segura o item.
   */
  public void aplicarEfeito(Pokesal portador) {
    if (efeitoDeStatus != null) {
      efeitoDeStatus.aplicar(portador);
    }
  }
}