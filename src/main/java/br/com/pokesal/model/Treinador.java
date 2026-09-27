package br.com.pokesal.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um competidor ou treinador com sua equipe de Pokesal e mochila de itens.
 */
public class Treinador {
  /** Capacidade maxima de itens que a mochila do treinador suporta. */
  public static final int CAPACIDADE_MOCHILA = 3;

  /** Limite maximo de itens permitidos para uso em uma mesma batalha. */
  public static final int LIMITE_MAXIMO_ITENS = 2;

  private final String nome;
  private final List<ItemDeBatalha> mochila;
  private Pokesal pokesal;
  private int contItensUsados;
  private int batalhasVencidas;

  /**
   * Cria um novo treinador com mochila vazia e contadores zerados.
   *
   * @param nome o nome do treinador.
   */
  public Treinador(String nome) {
    this.nome = nome;
    this.mochila = new ArrayList<>();
    this.contItensUsados = 0;
    this.batalhasVencidas = 0;
  }

  /**
   * Adiciona um item consumivel a mochila, se houver capacidade disponivel.
   *
   * @param item o item de batalha a ser adicionado.
   */
  public void adicionarItem(ItemDeBatalha item) {
    if (this.mochila.size() < CAPACIDADE_MOCHILA && item != null) {
      this.mochila.add(item);
    }
  }

  /**
   * Utiliza um item da mochila indicado pelo indice sobre o Pokesal ativo.
   *
   * @param indiceItem o indice do item na lista da mochila.
   */
  public void usarItem(int indiceItem) {
    if (indiceItem >= 0 && indiceItem < mochila.size()) {
      ItemDeBatalha item = mochila.remove(indiceItem);
      if (this.pokesal != null && item != null) {
        item.aplicar(this.pokesal);
      }
      this.contItensUsados++;
    }
  }

  /**
   * Incrementa o contador de itens utilizados pelo treinador no combate.
   */
  public void incrementarContadorItensUtilizados() {
    this.contItensUsados++;
  }

  /**
   * Reinicia o contador de itens usados para que o treinador possa participar de nova batalha.
   */
  public void reiniciarParaNovaBatalha() {
    this.contItensUsados = 0;
  }

  /**
   * Incrementa a contagem de batalhas vencidas pelo treinador no torneio.
   */
  public void incrementarBatalhasVencidas() {
    this.batalhasVencidas++;
  }

  /**
   * Retorna o total de batalhas vencidas pelo treinador.
   *
   * @return quantidade de batalhas vencidas.
   */
  public int getBatalhasVencidas() {
    return batalhasVencidas;
  }

  /**
   * Retorna a quantidade de itens ja utilizados pelo treinador na batalha corrente.
   *
   * @return numero de itens consumidos.
   */
  public int getContItensUsados() {
    return contItensUsados;
  }

  /**
   * Retorna a quantidade de itens ja utilizados (compatibilidade com modelagem UML).
   *
   * @return numero de itens consumidos.
   */
  public int getItensUtilizados() {
    return contItensUsados;
  }

  /**
   * Retorna o nome do treinador.
   *
   * @return nome do competidor.
   */
  public String getNome() {
    return nome;
  }

  /**
   * Retorna o Pokesal ativo do treinador.
   *
   * @return o Pokesal do treinador.
   */
  public Pokesal getPokesal() {
    return pokesal;
  }

  /**
   * Retorna a lista de itens presentes na mochila do treinador.
   *
   * @return lista de itens de batalha.
   */
  public List<ItemDeBatalha> getMochila() {
    return mochila;
  }

  /**
   * Define o Pokesal ativo do treinador.
   *
   * @param pokesal o Pokesal a ser associado.
   */
  public void setPokesal(Pokesal pokesal) {
    this.pokesal = pokesal;
  }
}
