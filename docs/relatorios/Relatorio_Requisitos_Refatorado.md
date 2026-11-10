# Relatório de Refinamento de Requisitos e Dissociação dos Requisitos Autorais

**Projeto:** PokeSal - Simulador de Batalha (UCSal Pituaçu)  
**Equipe:** Gabriel Castro Freitas, Luiz Gustavo França Barreto Silva, Ryan David de Oliveira Silva  
**Orientador:** Prof. Pedro Arthur Melo  

---

## 1. Contexto e Motivação do Refinamento

Na avaliação das Fases 01 e 02, o orientador observou que:
> *"Os requisitos autorais 2 e 3 se sobrepõem, como o próprio relatório admite."*

Com base nessa devolutiva, realizou-se a dissociação conceitual e operacional formal entre **mecanismo de limitação temporal do combate** e **regra arbitral de desempate**, eliminando a sobreposição de escopo.

---

## 2. Especificação Oficial dos Requisitos Autorais Refatorados

### [RA001] Sistema Oficial de Itens Segurados (Held Items)
- **Declaração:** Cada Pokésal em combate pode portar exatamente 1 item segurado (Held Item) do catálogo regulamentar de 6 itens. O item confere efeitos passivos permanentes sem consumir a ação do turno.
- **Catálogo Homologado:**
  1. *Carvão Incandescente:* +6 dano base e multiplicador de $1.1\times$ para ataques do tipo Fogo.
  2. *Gota d'Água Sagrada:* +6 dano base e multiplicador de $1.1\times$ para ataques do tipo Água.
  3. *Semente de Salto:* +6 dano base e multiplicador de $1.1\times$ para ataques do tipo Planta.
  4. *Faixa de Foco UCSal:* +5 dano base neutro para qualquer ataque.
  5. *Restos do RU (Leftovers):* Regenera passivamente +6 HP ao final de cada rodada.
  6. *Colete de Pituaçu:* +4 dano base e +3 HP regenerado por rodada.

---

### [RA002] Mecanismo Regulamentar de Limite Temporal (Timeout / Morte Súbita)
- **Declaração:** Toda batalha no torneio oficial é restrita a uma janela máxima de **10 turnos individuais** (5 ações por combatente).
- **Escopo e Limites Operacionais:** 
  - Ao ser atingido o 10º turno, o combate entra imediatamente no estado `encerrada = true`.
  - Nenhuma ação subsequente de ataque ou uso de item pode alterar o estado de combate ou computar dano (retorno estrito de `DANO_NULO = 0`).
  - O RA002 trata estritamente da **trava do ciclo de vida da batalha e proteção contra laços infinitos**.

---

### [RA003] Protocolo Arbitral de Julgamento por Vitalidade Residual
- **Declaração:** Caso a batalha atinja o encerramento sem que nenhum combatente tenha desmaiado (`hpAtual > 0`), a decisão do vencedor é delegada ao comitê de arbitragem da UCSal segundo a métrica de vitalidade residual.
- **Regras Arbitrais:**
  - O combatente com **maior saldo absoluto de HP restante** no instante do encerramento é declarado vencedor oficial.
  - Em caso de **paridade estrita de pontos de vida**, declara-se empate técnico (`vencedor = null`), resultando na desclassificação mútua ou aplicação de critério de desempate por iniciativa veloz na fase de chaveamento.
  - O RA003 trata estritamente da **lógica decisória de pontuação**, sendo independente da causa de encerramento da batalha.

---

## 3. Resumo da Conformidade com os Requisitos Funcionais Centrais

- **[RF001 / Regra 1]:** Seleção formal do inicial entre os 6 permitidos (BulbaSal, CharSal, SquirtSal, ChikoSal, CyndaSal ou TotoSal), implementada com validação e suporte interativo.
- **[RF002 / Regra 2]:** Matriz elemental clássica triangular ($2.0\times, 0.5\times, 1.0\times$).
- **[RF003 / Regra 3]:** Efeitos de terreno do estacionamento da UCSal: Asfalto Quente (+15% Fogo), Poça de Chuva (+10% Água) e Canteiro Central (+5% HP/turno Planta).
- **[RF004 / Regra 4]:** Iniciativa estrita por SPD e sistema de status (Queimado reduz ATK, Envenenado com dano progressivo, Paralisado reduz SPD).
- **[RF005 / Regra 5]:** Mochila com limite de 2 itens usáveis por batalha (consumindo o turno).
- **[RF006]:** Chaveamento com semifinais e final, avançando legitimamente o competidor vencedor para a disputa do título.
