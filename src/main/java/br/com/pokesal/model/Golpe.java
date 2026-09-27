package br.com.pokesal.model;

import br.com.pokesal.service.EfeitoDeStatusGolpe;

/**
 * Representa um golpe que um Pokesal pode executar, incluindo possivel efeito de status.
 */
public class Golpe {
  private final String nome;
  private final int poder;
  private final TipoElemental tipo;
  private final EfeitoDeStatusGolpe efeitoDeStatus;

  /**
   * Cria um novo golpe com atributos e efeito de status secundario.
   *
   * @param nome nome do golpe.
   * @param poder valor base de dano do golpe.
   * @param tipo tipo elemental do golpe.
   * @param efeitoDeStatus efeito de status opcional (pode ser null).
   */
  public Golpe(String nome, int poder, TipoElemental tipo, EfeitoDeStatusGolpe efeitoDeStatus) {
    this.nome = nome;
    this.poder = poder;
    this.tipo = tipo;
    this.efeitoDeStatus = efeitoDeStatus;
  }

  /**
   * Cria um novo golpe sem efeito de status.
   *
   * @param nome nome do golpe.
   * @param poder valor base de dano do golpe.
   * @param tipo tipo elemental do golpe.
   */
  public Golpe(String nome, int poder, TipoElemental tipo) {
    this(nome, poder, tipo, null);
  }

  /**
   * Executa o golpe retornando o valor base de poder.
   *
   * @return valor de poder do golpe.
   */
  public int atacar() {
    return this.poder;
  }

  /**
   * Retorna o nome do golpe.
   *
   * @return nome do golpe.
   */
  public String getNome() {
    return nome;
  }

  /**
   * Retorna o poder base do golpe.
   *
   * @return poder base.
   */
  public int getPoder() {
    return poder;
  }

  /**
   * Retorna o tipo elemental do golpe.
   *
   * @return tipo elemental.
   */
  public TipoElemental getTipo() {
    return tipo;
  }

  /**
   * Retorna o efeito de status associado ao golpe, se houver.
   *
   * @return efeito de status ou null se nao houver.
   */
  public EfeitoDeStatusGolpe getEfeitoDeStatus() {
    return efeitoDeStatus;
  }
}
