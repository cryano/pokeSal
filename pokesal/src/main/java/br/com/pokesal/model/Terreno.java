package br.com.pokesal.model;

import br.com.pokesal.service.EfeitoDeStatusTerreno;

/**
 * Representa a arena de batalha com modificadores elementais e regenerativos especificos.
 */
public class Terreno {
  /** Modificador de amplificacao de dano para o terreno Asfalto Quente. */
  public static final double MODIFICADOR_ASFALTO_QUENTE = 1.15;

  /** Modificador de amplificacao de dano para o terreno Poca de Chuva. */
  public static final double MODIFICADOR_POCA_CHUVA = 1.10;

  /** Percentual de regeneracao do terreno Canteiro Central sobre o HP maximo. */
  public static final double CURA_CANTEIRO_CENTRAL = 0.05;

  private final String nome;
  private final EfeitoDeStatusTerreno efeitoDeStatus;

  /**
   * Construtor da arena de combate.
   *
   * @param nome o nome da arena ou terreno.
   * @param efeitoDeStatus o efeito passivo atribuido a arena.
   */
  public Terreno(String nome, EfeitoDeStatusTerreno efeitoDeStatus) {
    this.nome = nome;
    this.efeitoDeStatus = efeitoDeStatus;
  }

  /**
   * Retorna o multiplicador de dano proporcionado pelo terreno ao tipo do golpe.
   *
   * @param tipoGolpe o tipo elemental do golpe em execucao.
   * @return multiplicador de dano (1.15, 1.10 ou neutro 1.0).
   */
  public double obterModificadorDano(TipoElemental tipoGolpe) {
    if (efeitoDeStatus != null && efeitoDeStatus.getTipoModificado() == tipoGolpe) {
      return efeitoDeStatus.getMultiplicadorDano();
    }
    return EfeitoDeStatusTerreno.MULTIPLICADOR_NEUTRO;
  }

  /**
   * Executa a acao de efeito do terreno, retornando a porcentagem de bonus ou regeneracao.
   *
   * @return valor numerico representativo do efeito.
   */
  public int aplicarEfeito() {
    return (efeitoDeStatus != null) ? efeitoDeStatus.aplicarEfeito() : 0;
  }

  /**
   * Retorna o nome da arena.
   *
   * @return nome do terreno.
   */
  public String getNome() {
    return nome;
  }

  /**
   * Retorna a definicao de efeito de status associada a arena.
   *
   * @return efeito de status do terreno.
   */
  public EfeitoDeStatusTerreno getEfeitoDeStatus() {
    return efeitoDeStatus;
  }
}