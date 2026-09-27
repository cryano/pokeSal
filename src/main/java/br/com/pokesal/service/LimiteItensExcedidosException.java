package br.com.pokesal.service;

/**
 * Excecao lancada quando um treinador tenta exceder o limite regulamentar de itens por batalha.
 */
public class LimiteItensExcedidosException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Construtor da excecao com mensagem explicativa da violacao de regra.
   *
   * @param message descricao detalhada do erro.
   */
  public LimiteItensExcedidosException(String message) {
    super(message);
  }
}
