# Diagrama de Casos de Uso - PokeSal

**Projeto:** PokeSal - Simulador de Batalha (UCSal Pituaçu)  
**Notação:** Mermaid (Flowchart)  

```mermaid
flowchart TD
    %% Atores
    Treinador["Treinador (Jogador)"]
    Sistema["Sistema PokeSal (Organizador)"]

    %% Modulo: Preparação do Torneio
    subgraph Preparacao ["1. Preparação e Escolha"]
        UC01(["UC01: Escolher Pokésal Inicial"])
        UC02(["UC02: Equipar Item Segurado (Held Item)"])
        UC03(["UC03: Receber Mochila de Itens"])
    end

    %% Modulo: Combate por Turnos
    subgraph Combate ["2. Batalha por Turnos"]
        UC04(["UC04: Disputar Batalha"])
        UC05(["UC05: Desferir Golpe Elemental"])
        UC06(["UC06: Usar Item no Turno"])
        UC07(["UC07: Aplicar Efeito de Terreno e Status"])
    end

    %% Modulo: Decisão e Torneio
    subgraph Torneio ["3. Finalização e Chaveamento"]
        UC08(["UC08: Julgar Vencedor (Desmaio ou HP)"])
        UC09(["UC09: Avançar Vencedor para a Final"])
    end

    %% Conexões do Treinador (Ações Diretas)
    Treinador --> UC01
    Treinador --> UC04
    Treinador --> UC06

    %% Relações de Inclusão e Extensão no Combate
    UC04 -.->|«include»| UC05
    UC04 -.->|«extend»| UC06
    UC04 -.->|«include»| UC07
    UC04 -.->|«include»| UC08

    %% Conexões do Sistema (Ações Automatizadas)
    Sistema --> UC02
    Sistema --> UC03
    Sistema --> UC07
    Sistema --> UC08
    Sistema --> UC09
```

---

## Descrição dos Casos de Uso

### 1. Preparação e Escolha
- **UC01 - Escolher Pokésal Inicial:** O treinador escolhe 1 entre as 6 criaturas permitidas (*BulbaSal, CharSal, SquirtSal, ChikoSal, CyndaSal, TotoSal*).
- **UC02 - Equipar Item Segurado (Held Item):** O sistema sorteia e equipa 1 entre os 6 Held Items no Pokésal do competidor.
- **UC03 - Receber Mochila de Itens:** O sistema abastece a mochila do treinador com o kit oficial de 3 itens (*Potion, Super Potion, Antidote*).

### 2. Batalha por Turnos
- **UC04 - Disputar Batalha:** Caso de uso central do combate. A ordem de atuação é definida pela velocidade (SPD).
- **UC05 - Desferir Golpe Elemental (`«include»`):** O Pokésal ataca usando multiplicadores elementais ($2.0\times, 0.5\times, 1.0\times$).
- **UC06 - Usar Item no Turno (`«extend»`):** O treinador opta por usar um item da mochila (consome o turno, limite de 2 por batalha).
- **UC07 - Aplicar Efeito de Terreno e Status (`«include»`):** No fim do turno, o sistema processa efeitos de terreno e status (*Queimado, Envenenado, Paralisado*).

### 3. Finalização e Chaveamento
- **UC08 - Julgar Vencedor (`«include»`):** O combate termina por desmaio (`HP <= 0`) ou pelo limite de 10 turnos (vitória por maior HP restante).
- **UC09 - Avançar Vencedor para a Final:** O vencedor da primeira batalha (semifinal) tem seus atributos restaurados e avança para enfrentar o Mestre Red na Grande Final.
