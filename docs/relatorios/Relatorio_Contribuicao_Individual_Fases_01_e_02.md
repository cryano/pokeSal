# Relatório de Contribuição Individual (Fases 01 e 02)

**Projeto:** PokeSal - Simulador de Batalha (UCSal Pituaçu)  
**Orientador:** Prof. Pedro Arthur Melo  
**Instituição:** Universidade Católica do Salvador (UCSAL) - Escola de Tecnologias  

---

## 1. Membros da Equipe

1. **Gabriel Castro Freitas** (`gabrielcastro.freitas@ucsal.edu.br`)
2. **Luiz Gustavo França Barreto Silva** (`luizgustavofranca.silva@ucsal.edu.br`)
3. **Ryan David de Oliveira Silva** (`ryan.silva@ucsal.edu.br`)

---

## 2. Distribuição de Responsabilidades e Contribuições - Fase 01

### Gabriel Castro Freitas
- **Engenharia de Requisitos:** Concepção e especificação dos requisitos funcionais (RF001 a RF007) e do primeiro requisito autoral (RA001 - Sistema de 6 Held Items).
- **Modelagem UML:** Elaboração do Diagrama de Casos de Uso e estruturação dos cenários de interação dos treinadores.
- **Implementação Java:** Estruturação das classes de modelo de dados (`Pokesal`, `Golpe`, `TipoElemental`, `HeldItem`).

### Luiz Gustavo França Barreto Silva
- **Inspeção de Requisitos:** Execução do teste estático de requisitos, mapeamento da matriz de rastreabilidade inicial e identificação de ambiguidades.
- **Modelagem UML:** Apoio no detalhamento dos atributos e métodos do Diagrama de Classes.
- **Implementação Java:** Construção da hierarquia de itens de batalha (`ItemDeBatalha`, `Potion`, `SuperPotion`, `Antidote`) e da exceção de negócio `LimiteItensExcedidosException`.

### Ryan David de Oliveira Silva
- **Regras de Negócio e Concepção:** Concepção e formalização dos requisitos autorais RA002 (Limite Regulamentar de 10 Turnos) e RA003 (Critério Arbitral por HP Residual).
- **Implementação do Motor de Combate:** Desenvolvimento do serviço principal de mediação (`Batalha.java`) e da hierarquia de terrenos da UCSal (`AsfaltoQuente`, `PocaDeChuva`, `CanteiroCentral`).
- **Qualidade de Código (Checkstyle):** Configuração do plugin `maven-checkstyle-plugin` com `google_checks.xml`, garantindo zero avisos de estilo no código inicial.

---

## 3. Distribuição de Responsabilidades e Contribuições - Fase 02

### Gabriel Castro Freitas
- **Suíte de Testes JUnit:** Implementação dos casos de teste essenciais: `testVantagemElemental()`, `testEfeitoTerrenoEstacionamentoUCSal()` e `testEfeitoHeldItemRequisitoAutoral()`.
- **Análise SonarQube:** Execução do SonarScanner, parametrização do `sonar-project.properties` e elaboração do relatório técnico de métricas de qualidade (Code Smells, Bugs, Vulnerabilities).

### Luiz Gustavo França Barreto Silva
- **Checklist Estático Manual:** Elaboração e condução do checklist de verificação estática de código-fonte, checando ausência de código inacessível, duplicações e correta inicialização de variáveis.
- **Testes de Exceção e Itens:** Implementação e validação do teste `testUsoLimiteDeItensExcedido()`, assegurando o lançamento da exceção no terceiro uso.

### Ryan David de Oliveira Silva
- **Testes de Fluxo Crítico e Boundary Values:** Implementação dos testes de iniciativa por velocidade (`testOrdemDeAtaquePorVelocidade()`), análise de valores limite (`testCalculoDanoBoundaryValues()`), finalização temporal (`testLimiteDeDezTurnosEncerraBatalha()`) e julgamento por HP (`testDesempatePorHpRestante()`).
- **Correção de Regras e Refatoração:** Refatoração de `Batalha.java` para prevenir ações após encerramento da batalha e garantia do dano mínimo não negativo.
