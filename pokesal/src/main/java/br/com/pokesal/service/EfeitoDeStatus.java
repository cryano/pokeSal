package br.com.pokesal.service;

import br.com.pokesal.model.Pokesal;

/**
 * Contrato para aplicacao de efeitos especiais decorrentes de golpes, itens ou terrenos.
 */
public interface EfeitoDeStatus {

  /**
   * Aplica a condicao ou efeito de status sobre a criatura alvo.
   *
   * @param alvo o Pokesal que sofrera a acao do efeito.
   */
  void aplicar(Pokesal alvo);

  /**
   * Executa ou calcula o valor numerico associado ao efeito (dano residual, bonus ou cura).
   *
   * @return valor numerico representativo do efeito.
   */
  int aplicarEfeito();
}
