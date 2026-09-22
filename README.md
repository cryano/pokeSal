# PokéSal v2 - Sistema de Batalhas e Torneio Oficial UCSAL

Projeto acadêmico em Java que implementa o simulador de batalhas Pokémon customizado (**PokéSal**), modelado segundo diagramas UML de Casos de Uso e Classes, com aderência rigorosa a todos os requisitos autorais, funcionais e não-funcionais (RA001–RA003, RF001–RF012, RNF001–RNF010).

---

## Estrutura de Pacotes

```
src/
├── main/java/br/com/pokesal/
│   ├── Main.java                               # Ponto de entrada do Torneio
│   ├── model/
│   │   ├── Antidote.java                       # Item de cura de envenenamento
│   │   ├── AsfaltoQuente.java                  # Terreno de fogo (+15%)
│   │   ├── CanteiroCentral.java                # Terreno de regeneração (+5 HP)
│   │   ├── Golpe.java                          # Golpes e movimentos de ataque
│   │   ├── HeldItem.java                       # Itens segurados passivos
│   │   ├── ItemDeBatalha.java                  # Classe base de itens consumíveis
│   │   ├── PocaDeChuva.java                    # Terreno aquático (+15%)
│   │   ├── Pokesal.java                        # Entidade das criaturas de batalha
│   │   ├── Potion.java                         # Poção básica (+15 HP)
│   │   ├── SuperPotion.java                    # Super poção (+25 HP)
│   │   ├── Terreno.java                        # Classe base de arenas
│   │   ├── TipoElemental.java                  # Enum elemental (PLANTA, FOGO, AGUA)
│   │   ├── TipoStatus.java                     # Enum de status (QUEIMADO, etc.)
│   │   └── Treinador.java                      # Entidade do competidor
│   └── service/
│       ├── Batalha.java                        # Máquina de estados do combate
│       ├── EfeitoDeStatus.java                 # Interface/classe abstrata de status
│       ├── EfeitoDeStatusGolpe.java            # Status decorrentes de ataques
│       ├── EfeitoDeStatusHeld.java             # Buffs passivos de itens segurados
│       ├── EfeitoDeStatusTerreno.java          # Buffs ambientais da arena
│       ├── LimiteItensExcedidosException.java  # Exceção de limite de itens
│       └── SimuladorDeBatalha.java             # Catálogo, sorteios e utilitários
└── test/java/br/com/pokesal/
    ├── GeradorPdf.java                         # Emissor de relatório técnico em PDF
    ├── TestRunner.java                         # Executor autônomo de testes (100% Java)
    └── service/
        └── BatalhaTest.java                    # Suíte oficial de testes unitários JUnit 5
```

---
