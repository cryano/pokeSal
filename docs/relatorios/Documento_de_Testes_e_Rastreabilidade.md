# Documento de Testes Automatizados e Matriz de Rastreabilidade

**Projeto:** PokeSal - Simulador de Batalha (UCSal Pituaçu)  
**Equipe:** Gabriel Castro Freitas, Luiz Gustavo França Barreto Silva, Ryan David de Oliveira Silva  
**Orientador:** Prof. Pedro Arthur Melo  
**Framework:** JUnit 5 (Jupiter Engine) | **Qualidade:** SonarQube  

---

## 1. Visão Geral da Suíte de Testes

A suíte de testes unitários foi elaborada com foco na cobertura das regras de negócio oficiais estabelecidas para as Fases 01 e 02 do projeto PokeSal. Os testes encontram-se implementados na classe [`PokeSalTest`](file:///home/cryano/workspace/pokeSal/src/test/java/br/com/pokesal/PokeSalTest.java).

---

## 2. Matriz de Rastreabilidade de Requisitos (Fases 01 e 02)

| Requisito | Descrição da Regra | Método de Teste (JUnit 5) | Cenário e Entradas | Resultado Esperado | Resultado Obtido | Status |
|:---:|---|---|---|---|---|:---:|
| **RF001 / Regra 1** | Seleção formal do Pokésal inicial pelo treinador | `testEscolhaInicialTreinadorValidaEInvalida()` | Escolha válida por nome ("CharSal"), por índice (3) e rejeição com exceção para nome inexistente ("Pikachu") e índice inválido (99). | Inicial atribuído com mochila de 3 itens; disparo de `IllegalArgumentException` para entradas inválidas. | Comportamento validado com sucesso. | **PASS** |
| **RF002 / Regra 2** | Matriz de vantagens e desvantagens elementais | `testVantagemElemental()` | Combates entre Fogo, Água e Planta. Dano base de 40 pontos com golpes elementais cruzados. | Vantagem $\times 2.0$ (80 de dano), desvantagem $\times 0.5$ (20 de dano), neutro $\times 1.0$ (40 de dano). | Multiplicadores e cálculo final de dano assertivos. | **PASS** |
| **RF007 / Regra 3** | Efeitos de terreno da UCSal (Asfalto, Poça, Canteiro) | `testEfeitoTerrenoEstacionamentoUCSal()` | Asfalto Quente (+15% Fogo), Poça de Chuva (+10% Água) e Canteiro Central (+5% HP para Planta no fim de turno). | Fogo amplificado para 46; Água amplificada para 44; BulbaSal regenerando +5 HP. | Efeitos aplicados estritamente conforme as porcentagens da UCSal. | **PASS** |
| **RF008 / Regra 4** | Iniciativa por Velocidade (SPD) e efeitos de status | `testOrdemDeAtaquePorVelocidade()` | Treinador rápido (SPD 40) vs Lento (SPD 20); aplicação de Queimadura (reduz ATK em 4). | Primeiro a atacar é o de maior SPD; Queimadura reduz ATK de 30 para 26 com dano de 6 por turno. | Iniciativa ordenada e penalidades aplicadas. | **PASS** |
| **RF005 / Regra 5** | Limite máximo de uso de 2 itens de batalha por treinador | `testUsoLimiteDeItensExcedido()` | Treinador consome 2 poções sucessivas e tenta usar o 3º item. | Sucesso nos 2 primeiros usos; lançamento de `LimiteItensExcedidosException` no 3º uso. | Exceção customizada interceptada corretamente. | **PASS** |
| **Qualidade / Limites** | Análise de valores limite (Boundary Value Analysis) | `testCalculoDanoBoundaryValues()` | Dano extremo (9999) contra HP normal; alta defesa (DEF 999) superando ATK base; HP zerado. | HP não pode ficar negativo (piso 0); golpe bem-sucedido causa dano mínimo de 1; confirmação de desmaio. | Valores de borda respeitam limites da especificação. | **PASS** |
| **RA001** | Requisito Autoral: 6 Held Items com bônus passivo em combate | `testEfeitoHeldItemRequisitoAutoral()` | Carvão Incandescente (+6 dano e $\times 1.1$ para Fogo) e Restos do RU (+6 HP por turno). | Dano com item passa de 40 para 50; regeneração de +6 HP no final do turno. | Bonificações passivas aplicadas em conformidade. | **PASS** |
| **RA002** | Requisito Autoral: Limite temporal de 10 turnos (Morte Súbita) | `testLimiteDeDezTurnosEncerraBatalha()` | Execução de 10 turnos alternados entre os combatentes e tentativa de ação posterior. | Batalha marca `encerrada = true` exatamente no turno 10; ações no turno 11 produzem dano zero. | Encerramento obrigatório e bloqueio operacional validados. | **PASS** |
| **RA003** | Requisito Autoral: Julgamento arbitral por HP restante | `testDesempatePorHpRestante()` | Cenário A: T1 encerra com mais HP que T2.<br>Cenário B: Ambos encerram com HP rigorosamente idêntico. | Cenário A: Treinador 1 é coroado vencedor.<br>Cenário B: Vencedor nulo (empate técnico registrado). | Julgamento executado com fidelidade aos critérios. | **PASS** |

---

## 3. Resumo da Execução dos Testes Automatizados

- **Total de Testes:** 9 testes automatizados
- **Falhas:** 0
- **Erros:** 0
- **Compilação e Estilo:** 100% de sucesso sem violações de Checkstyle.
