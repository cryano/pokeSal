# PokéSal v2 - Sistema de Batalhas e Torneio Oficial UCSAL

Projeto acadêmico em Java que implementa o simulador de batalhas Pokémon customizado (**PokéSal**), modelado segundo diagramas UML de Casos de Uso e Classes, com aderência rigorosa a todos os requisitos autorais, funcionais e não-funcionais (RA001–RA003, RF001–RF012, RNF001–RNF010).

---
## Apresentação de 10min em vídeo não-listado: 

https://youtu.be/xS65EqgLDBQ
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
```

---
