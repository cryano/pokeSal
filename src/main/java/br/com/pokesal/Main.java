package br.com.pokesal;

import br.com.pokesal.model.Antidote;
import br.com.pokesal.model.AsfaltoQuente;
import br.com.pokesal.model.CanteiroCentral;
import br.com.pokesal.model.Golpe;
import br.com.pokesal.model.ItemDeBatalha;
import br.com.pokesal.model.PocaDeChuva;
import br.com.pokesal.model.Pokesal;
import br.com.pokesal.model.Potion;
import br.com.pokesal.model.SuperPotion;
import br.com.pokesal.model.Terreno;
import br.com.pokesal.model.Treinador;
import br.com.pokesal.service.Batalha;
import br.com.pokesal.service.SimuladorDeBatalha;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

/**
 * Ponto de entrada do sistema e condutor do fluxo do Torneio Oficial UCSAL.
 */
public class Main {
  /** Quantidade de turnos individuais que compoem uma rodada completa. */
  private static final int TURNOS_POR_RODADA = 2;

  /** Limiar de proporcao de HP (35%) para acionamento defensivo de pocoes pela IA. */
  private static final double LIMIAR_HP_USO_POCAO = 0.35;

  /** Indice sentinela indicando que nenhum item correspondente foi localizado. */
  private static final int INDICE_ITEM_INICIAL = -1;

  /** Indice do golpe principal do Pokesal. */
  private static final int INDICE_GOLPE_PADRAO = 0;

  /** Indice do golpe secundario contendo efeito de status. */
  private static final int INDICE_GOLPE_STATUS = 1;

  /** Quantidade minima de golpes necessarios para inspecionar golpe de status. */
  private static final int QUANTIDADE_MINIMA_GOLPES_STATUS = 1;

  /** Indice do primeiro competidor do chaveamento. */
  private static final int INDICE_PRIMEIRO_TREINADOR = 0;

  /** Indice do segundo competidor do chaveamento. */
  private static final int INDICE_SEGUNDO_TREINADOR = 1;

  /**
   * Metodo principal responsavel por executar as semifinais e a grande final do torneio.
   *
   * @param args argumentos de linha de comando.
   */
  public static void main(String[] args) {
    System.out.println("====================================================");
    System.out.println("    BEM-VINDO AO TORNEIO OFICIAL POKESAL - UCSAL    ");
    System.out.println("====================================================");

    SimuladorDeBatalha simulador = new SimuladorDeBatalha();
    simulador.cadastrarTreinadores();

    Treinador t1 = simulador.getTreinadores().get(INDICE_PRIMEIRO_TREINADOR);
    Treinador t2 = simulador.getTreinadores().get(INDICE_SEGUNDO_TREINADOR);

    Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());
    interagirEscolhaInicial(simulador, t1, scanner);

    simulador.sortearPokesal(t2);
    System.out.printf("-> %s definiu seu inicial: %s!%n",
        t2.getNome(), t2.getPokesal().getNome());

    simulador.sortearHeldItem(t1);
    simulador.sortearHeldItem(t2);

    List<Treinador> chavePrimeiraPartida = simulador.sortearPrimeiraPartida();
    System.out.println("\n[Sorteio da 1a Partida]: "
        + chavePrimeiraPartida.get(INDICE_PRIMEIRO_TREINADOR).getNome()
        + " vs "
        + chavePrimeiraPartida.get(INDICE_SEGUNDO_TREINADOR).getNome());

    Terreno terreno1 = simulador.sortearTerreno();

    System.out.println("\n----------------------------------------------------");
    System.out.println("       PRIMEIRA BATALHA (SEMIFINAL DO TORNEIO)      ");
    System.out.println("----------------------------------------------------");
    System.out.println("Arena do Confronto: " + terreno1.getNome());
    descreverEfeitoTerreno(terreno1);
    System.out.printf("Competidor 1: %s com %s (Item: %s | Mochila: %d itens)%n",
        t1.getNome(), t1.getPokesal().getNome(),
        t1.getPokesal().getHeldItem().getNome(), t1.getMochila().size());
    System.out.printf("Competidor 2: %s com %s (Item: %s | Mochila: %d itens)%n",
        t2.getNome(), t2.getPokesal().getNome(),
        t2.getPokesal().getHeldItem().getNome(), t2.getMochila().size());

    Batalha batalha1 = simulador.iniciarBatalha(t1, t2, terreno1);
    System.out.println("\n--- INICIO DO COMBATE ---");
    executarCicloBatalha(batalha1, t1, t2);

    System.out.println("\n====================================================");
    Treinador finalista;
    if (batalha1.getVencedor() == t1) {
      t1.incrementarBatalhasVencidas();
      System.out.printf(" VENCEDOR DA 1a BATALHA: %s (%s)!%n",
          t1.getNome(), t1.getPokesal().getNome());
      System.out.printf(" %s avanca para a Grande Final!%n", t1.getNome());
      finalista = t1;
    } else if (batalha1.getVencedor() == t2) {
      t2.incrementarBatalhasVencidas();
      System.out.printf(" VENCEDOR DA 1a BATALHA: %s (%s)!%n",
          t2.getNome(), t2.getPokesal().getNome());
      System.out.printf(" %s avanca para a Grande Final!%n", t2.getNome());
      finalista = t2;
    } else {
      System.out.println(" A BATALHA TERMINOU EM EMPATE! Desempate por iniciativa aplicado.");
      finalista = (t1.getPokesal().getSpd() >= t2.getPokesal().getSpd()) ? t1 : t2;
      System.out.printf(" %s avanca para a Grande Final pelo criterio de velocidade!%n",
          finalista.getNome());
    }
    System.out.println("====================================================");

    finalista.getPokesal().restaurarParaNovaBatalha();
    finalista.reiniciarParaNovaBatalha();

    Treinador red = new Treinador("Red");
    simulador.adicionarTreinador(red);
    simulador.sortearPokesal(red);
    simulador.sortearHeldItem(red);

    Terreno terrenoFinal = simulador.sortearTerreno();

    System.out.println("\n----------------------------------------------------");
    System.out.println("            GRANDE FINAL DO TORNEIO UCSAL           ");
    System.out.println("----------------------------------------------------");
    System.out.println("Arena da Final: " + terrenoFinal.getNome());
    descreverEfeitoTerreno(terrenoFinal);
    System.out.printf("Finalista 1: %s com seu companheiro %s (Item: %s)%n",
        finalista.getNome(), finalista.getPokesal().getNome(),
        finalista.getPokesal().getHeldItem().getNome());
    System.out.printf("Finalista 2 (Mestre): %s com %s (Item: %s)%n",
        red.getNome(), red.getPokesal().getNome(),
        red.getPokesal().getHeldItem().getNome());

    Batalha batalhaFinal = simulador.iniciarBatalha(finalista, red, terrenoFinal);
    System.out.println("\n--- INICIO DA GRANDE FINAL ---");
    executarCicloBatalha(batalhaFinal, finalista, red);

    System.out.println("\n====================================================");
    if (batalhaFinal.getVencedor() == finalista) {
      finalista.incrementarBatalhasVencidas();
      System.out.printf(" PARABENS! %s (%s) FOI O CAMPEAO DO TORNEIO!%n",
          finalista.getNome(), finalista.getPokesal().getNome());
    } else {
      String nomeVencedor = (batalhaFinal.getVencedor() != null)
          ? batalhaFinal.getVencedor().getNome() : "Nenhum (Empate)";
      System.out.printf(" %s FOI DERROTADO NA FINAL! O campeao foi %s!%n",
          finalista.getNome(), nomeVencedor);
    }
    System.out.println("====================================================");
  }

  /**
   * Conduz a interacao no console para que o jogador escolha o Pokesal inicial.
   *
   * @param simulador instancia do servico de simulacao.
   * @param treinador treinador que recebera a criatura escolhida.
   * @param scanner leitor de fluxo de entrada.
   */
  private static void interagirEscolhaInicial(SimuladorDeBatalha simulador,
                                              Treinador treinador,
                                              Scanner scanner) {
    System.out.printf("%n>>> Selecao de Pokesal Inicial para o Treinador: %s <<<%n",
        treinador.getNome());
    System.out.println("Opcoes homologadas no Estacionamento da UCSal:");
    List<Pokesal> catalogo = SimuladorDeBatalha.obterCatalogoPokesal();
    for (int i = 0; i < catalogo.size(); i++) {
      Pokesal p = catalogo.get(i);
      System.out.printf("  [%d] %-9s | Tipo: %-6s | HP: %3d | ATK: %2d | DEF: %2d | SPD: %2d%n",
          i + 1, p.getNome(), p.getTipoElemental(), p.getHpMax(),
          p.getAtk(), p.getDef(), p.getSpd());
    }

    boolean escolhido = false;
    if (scanner != null) {
      System.out.print("Digite o numero (1 a 6) ou nome do Pokesal desejado: ");
      try {
        if (scanner.hasNextLine()) {
          String linha = scanner.nextLine().trim();
          if (!linha.isEmpty()) {
            try {
              int opcao = Integer.parseInt(linha);
              simulador.escolherPokesal(treinador, opcao);
              escolhido = true;
            } catch (NumberFormatException e) {
              simulador.escolherPokesal(treinador, linha);
              escolhido = true;
            }
          }
        }
      } catch (Exception e) {
        System.out.println("Entrada nao reconhecida. Aplicando selecao padrao.");
      }
    }

    if (!escolhido) {
      System.out.println("Nenhuma entrada. Selecionando BulbaSal por padrao regulamentar.");
      simulador.escolherPokesal(treinador, 1);
    }

    System.out.printf("-> %s escolheu com sucesso: %s!%n",
        treinador.getNome(), treinador.getPokesal().getNome());
  }

  /**
   * Conduz o loop principal de rodadas de uma batalha ate a definicao de um desfecho.
   *
   * @param batalha instancia da batalha mediada.
   * @param t1 competidor 1.
   * @param t2 competidor 2.
   */
  private static void executarCicloBatalha(Batalha batalha, Treinador t1, Treinador t2) {
    while (!batalha.isEncerrada()) {
      int rodada = (batalha.getTurnoDaBatalha() / TURNOS_POR_RODADA) + 1;
      System.out.printf("%n--- RODADA %d (Turno %d de %d) ---%n",
          rodada, batalha.getTurnoDaBatalha() + 1, Batalha.LIMITE_MAXIMO_TURNOS);

      Treinador primeiro = batalha.determinarPrimeiroAtacante();
      Treinador segundo = (primeiro == t1) ? t2 : t1;

      System.out.printf("Iniciativa: %s (SPD %d) vs %s (SPD %d) -> %s atua primeiro!%n",
          primeiro.getPokesal().getNome(), primeiro.getPokesal().getSpd(),
          segundo.getPokesal().getNome(), segundo.getPokesal().getSpd(),
          primeiro.getPokesal().getNome());

      executarAcaoCompetidor(batalha, primeiro, segundo);

      if (!batalha.isEncerrada() && !segundo.getPokesal().estaDesmaiado()) {
        executarAcaoCompetidor(batalha, segundo, primeiro);
      } else if (segundo.getPokesal().estaDesmaiado()) {
        System.out.printf("  %s (%s) desmaiou e nao pode agir!%n",
            segundo.getNome(), segundo.getPokesal().getNome());
      }

      batalha.finalizarTurno();

      System.out.printf("Status Fim da Rodada %d: %s HP: %d/%d | %s HP: %d/%d%n",
          rodada,
          t1.getPokesal().getNome(), t1.getPokesal().getHpAtual(),
          t1.getPokesal().getHpMax(),
          t2.getPokesal().getNome(), t2.getPokesal().getHpAtual(),
          t2.getPokesal().getHpMax());
    }
  }

  /**
   * Executa a decisao estrategica do treinador ativo (usar antidoto, usar pocao ou atacar).
   *
   * @param batalha instancia da batalha mediada.
   * @param ativo treinador que possui a iniciativa da vez.
   * @param oponente treinador adversario.
   */
  private static void executarAcaoCompetidor(Batalha batalha, Treinador ativo, Treinador oponente) {
    boolean podeUsarItem = ativo.getContItensUsados() < Treinador.LIMITE_MAXIMO_ITENS
        && !ativo.getMochila().isEmpty();

    if (ativo.getPokesal().temStatus() && podeUsarItem) {
      int idxAntidoto = INDICE_ITEM_INICIAL;
      for (int i = 0; i < ativo.getMochila().size(); i++) {
        if (ativo.getMochila().get(i) instanceof Antidote) {
          idxAntidoto = i;
          break;
        }
      }
      if (idxAntidoto >= 0) {
        ItemDeBatalha item = ativo.getMochila().get(idxAntidoto);
        System.out.printf(
            "  [ITEM] %s usou %s e abriu mao do ataque nesta rodada para curar o status de %s!%n",
            ativo.getNome(), item.getNome(), ativo.getPokesal().getNome());
        batalha.consumirItemNoTurno(ativo, idxAntidoto);
        return;
      }
    }

    boolean precisaCurarHp = ativo.getPokesal().getHpAtual()
        <= (ativo.getPokesal().getHpMax() * LIMIAR_HP_USO_POCAO);
    if (precisaCurarHp && podeUsarItem) {
      int idxPocao = INDICE_ITEM_INICIAL;
      for (int i = 0; i < ativo.getMochila().size(); i++) {
        if (ativo.getMochila().get(i) instanceof Potion
            || ativo.getMochila().get(i) instanceof SuperPotion) {
          idxPocao = i;
          break;
        }
      }
      if (idxPocao >= 0) {
        ItemDeBatalha itemUsado = ativo.getMochila().get(idxPocao);
        System.out.printf(
            "  [ITEM] %s decidiu usar %s e abriu mao do ataque nesta rodada! (HP atual: %d)%n",
            ativo.getNome(), itemUsado.getNome(), ativo.getPokesal().getHpAtual());
        batalha.consumirItemNoTurno(ativo, idxPocao);
        System.out.printf("  -> %s agora tem HP %d/%d!%n",
            ativo.getPokesal().getNome(), ativo.getPokesal().getHpAtual(),
            ativo.getPokesal().getHpMax());
        return;
      }
    }

    List<Golpe> golpes = ativo.getPokesal().getGolpes();
    Golpe golpeEscolhido = golpes.get(INDICE_GOLPE_PADRAO);
    if (!oponente.getPokesal().temStatus() && golpes.size() > QUANTIDADE_MINIMA_GOLPES_STATUS
        && golpes.get(INDICE_GOLPE_STATUS).getEfeitoDeStatus() != null) {
      golpeEscolhido = golpes.get(INDICE_GOLPE_STATUS);
    }

    System.out.printf("  %s ordenou %s usando %s!%n",
        ativo.getNome(), ativo.getPokesal().getNome(), golpeEscolhido.getNome());
    int dano = batalha.executarAcaoDeAtaque(ativo, oponente, golpeEscolhido);
    System.out.printf("  -> Dano causado: %d%n", dano);
  }

  /**
   * Imprime a descricao dos efeitos e bonificacoes proporcionados pelo terreno sorteado.
   *
   * @param terreno arena de combate selecionada.
   */
  private static void descreverEfeitoTerreno(Terreno terreno) {
    if (terreno instanceof AsfaltoQuente) {
      System.out.println(
          "  -> Efeito do Terreno: Asfalto Quente aumenta o dano de golpes do tipo "
              + "FOGO em 15%!");
    } else if (terreno instanceof PocaDeChuva) {
      System.out.println(
          "  -> Efeito do Terreno: Poca de Chuva potencializa golpes do tipo AGUA "
              + "com +10% de dano!");
    } else if (terreno instanceof CanteiroCentral) {
      System.out.println(
          "  -> Efeito do Terreno: Canteiro Central regenera 5% do HP maximo "
              + "de Pokesals do tipo PLANTA a cada rodada!");
    }
  }
}