# PokéSal v2 - Sistema de Batalhas e Torneio Oficial UCSAL

Projeto acadêmico em Java que implementa o simulador de batalhas Pokémon customizado (**PokéSal**), modelado segundo diagramas UML de Casos de Uso e Classes, com aderência rigorosa a todos os requisitos autorais, funcionais e não-funcionais (RA001–RA003, RF001–RF012, RNF001–RNF010).

---
## Apresentação de 10min em vídeo não-listado: 

https://youtu.be/xS65EqgLDBQ
#### Vale salientar que não foi possível demonstrar o feedback do Google Checkstyle em detalhes devido ao limite de tempo da apresentação. É necessária a rodagem do código sob o checkstyle para verificar os feedbacks da ferramenta. A presença de Javadocs conforme o previsto pelas normas da Google e a configuração da formatação padrão em todas as classes funciona como uma prévia da conformidade com as normas do checkstyle.
---

## Estrutura de Pacotes

```
src/
├── main/java/br/com/pokesal/
│   ├── Main.java                               # Ponto de entrada do Torneio
│   ├── model/
│   │   ├── Antidote.java                       
│   │   ├── AsfaltoQuente.java                  
│   │   ├── CanteiroCentral.java                
│   │   ├── Golpe.java                          
│   │   ├── HeldItem.java                       
│   │   ├── ItemDeBatalha.java                  
│   │   ├── PocaDeChuva.java                    
│   │   ├── Pokesal.java                        
│   │   ├── Potion.java                         
│   │   ├── SuperPotion.java                    
│   │   ├── Terreno.java                        
│   │   ├── TipoElemental.java                  
│   │   ├── TipoStatus.java                     
│   │   └── Treinador.java                      
│   └── service/
│       ├── Batalha.java                        
│       ├── EfeitoDeStatus.java                 
│       ├── EfeitoDeStatusGolpe.java            
│       ├── EfeitoDeStatusHeld.java             
│       ├── EfeitoDeStatusTerreno.java          
│       ├── LimiteItensExcedidosException.java  
│       └── SimuladorDeBatalha.java             
└── test/java/br/com/pokesal/
    └── PokeSalTest.java                        # Suite oficial de testes unitarios (JUnit 5)
```

---

## Execução dos Testes e Qualidade (Fase 02)

Para executar a suite completa de testes automatizados com o Maven Surefire:
```bash
mvn test
```

Para executar a auditoria de conformidade com as normas do Google Checkstyle:
```bash
mvn checkstyle:check
```
