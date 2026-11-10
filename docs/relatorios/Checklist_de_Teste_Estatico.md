# Checklist de Teste Estático de Código-Fonte (Revisão Manual)

**Projeto:** PokeSal - Simulador de Batalha (UCSal Pituaçu)  
**Equipe:** Gabriel Castro Freitas, Luiz Gustavo França Barreto Silva, Ryan David de Oliveira Silva  
**Orientador:** Prof. Pedro Arthur Melo  
**Data da Revisão/Refatoração:** Novembro de 2026  

---

## 1. Respostas Formais às 9 Questões Regulamentares

| Questão Regulamentar | Avaliação | Parecer Técnico Detalhado e Evidências no Código |
|---|:---:|---|
| **1. Todas as variáveis estão inicializadas antes do uso?** | **SIM** | Todos os atributos de instância em `Pokesal`, `Treinador`, `Golpe`, `Batalha` e nos modelos de terreno/itens são explicitamente inicializados via construtor ou com valores padrão sentinelas. Variáveis locais nos loops de combate (`Main.java`, `Batalha.java`) são estritamente atribuídas antes de qualquer leitura. O compilador Java (`javac 21`) e a verificação estática asseguram a ausência de variáveis não inicializadas. |
| **2. Há variáveis declaradas e nunca usadas?** | **NÃO** | Todas as variáveis declaradas possuem papel funcional ativo. Constantes de classe em `Batalha`, `Pokesal` e `Main` parametrizam limites de turnos, danos mínimos e percentuais. A auditoria Checkstyle com regras do Google valida que nenhum parâmetro ou variável local permanece órfão (`UnusedImports` e `UnusedLocalVariables` com zero ocorrências). |
| **3. Existe código inacessível?** | **NÃO** | Todos os ramos condicionais em métodos como `calcularDano()`, `determinarPrimeiroAtacante()` e `processarEfeitosFimDeTurno()` possuem caminhos de execução alcançáveis. Na refatoração do método `main()` em `Main.java`, foi eliminado um ponto crítico de código inacessível onde a Grande Final nunca era disputada caso o Treinador 2 vencesse a semifinal. |
| **4. Há código duplicado?** | **NÃO** | A lógica de criação das criaturas foi centralizada no catálogo oficial (`SimuladorDeBatalha.obterCatalogoPokesal()`) com suporte a clonagem profunda (`clonarPokesal()`). A hierarquia polimórfica de itens (`ItemDeBatalha`, `Potion`, `SuperPotion`, `Antidote`) e terrenos (`Terreno`, `AsfaltoQuente`, `PocaDeChuva`, `CanteiroCentral`) elimina redundâncias estruturais. |
| **5. Existem erros de sintaxe?** | **NÃO** | O código compila sem nenhum erro ou advertência de sintaxe em Java 21 LTS (`mvn compile` e `mvn test-compile` com sucesso absoluto). |
| **6. Há erros de lógica que quebram regras do negócio?** | **CORRIGIDOS** | **Bugs identificados e sanados nesta revisão:**<br>1. **Bug do Chaveamento da Semifinal:** O código encerrava precocemente com `return` se o segundo competidor vencesse a primeira batalha, ignorando a final. Corrigido para avançar o vencedor legítimo para disputar contra o Mestre Red.<br>2. **Violação da Regra 1 (Seleção do Inicial):** O sistema apenas sorteava o inicial. Foi implementado o método `escolherPokesal()` com validação das 6 espécies permitidas e interface interativa via console no `Main.java`.<br>3. **Sobreposição RA002/RA003:** O limite de 10 turnos e o critério de desempate por HP residual foram formalmente dissociados e refinados. |
| **7. Há erros de tipagem?** | **NÃO** | Fortemente tipado com enums específicos (`TipoElemental`, `TipoStatus`), evitando erros de strings mágicas. Coleções utilizam generics estritos (`List<Golpe>`, `List<Treinador>`, `List<ItemDeBatalha>`) e retornos imutáveis (`Collections.unmodifiableList`). |
| **8. O fluxo de controle é válido (sem loops infinitos, condições impossíveis)?** | **SIM** | O laço `while (!batalha.isEncerrada())` em `Main.java` possui término garantido por duas travas simultâneas: desmaio de uma das criaturas (`hpAtual <= 0`) e teto de 10 turnos regulamentares gerenciado em `Batalha.java`. Não há recursões abertas nem deadlocks. |
| **9. Outros tipos de erros identificados?** | **SIM (TRATADOS)** | O método `executarAcaoDeAtaque()` e `consumirItemNoTurno()` permitiam execução de ações após a batalha estar marcada como encerrada. Foi inserida guarda de retorno antecipado (`if (encerrada) return;`) assegurando a integridade do estado. |

---

## 2. Resumo da Verificação de Boas Práticas (Checkstyle)

- **Configuração:** `google_checks.xml`
- **Violations:** 0 (Zero violações)
- **Tamanho máximo de linha:** $\le$ 100 caracteres
- **Indentação padrão:** 2 espaços
- **Documentação:** 100% dos métodos e classes públicas com Javadoc estruturado.
