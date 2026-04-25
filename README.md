# Analisador Léxico

Um analisador léxico em Java que realiza tokenização de código-fonte, identificando palavras-chave, identificadores, números, strings e operadores, gerando relatórios de erros quando necessário.

## Sobre o Projeto

Este é um projeto acadêmico para a disciplina de **Linguagens Formais e Compiladores (LFAC)** que implementa a primeira fase de um compilador: a **análise léxica**.

### O que faz?

1. **Pré-processa** o arquivo de entrada (remove comentários e normaliza espaços)
2. **Tokeniza** o código, identificando:
   - Palavras reservadas (`var`, `read`, `write`, `if`, `else`, etc.)
   - Identificadores (nomes de variáveis)
   - Números inteiros e decimais
   - Strings entre aspas
   - Operadores e símbolos especiais
3. **Gera saída** em 3 arquivos:
   - `arquivo.ppr` - código pré-processado
   - `arquivo.tks` - tokens encontrados
   - `arquivo.err` - erros de tokenização (se houver)

## Estrutura do Projeto

```
analisador-lexico/
├── src/main/java/com/projeto/
│   ├── Main.java                          # Ponto de entrada
│   └── core/
│       ├── analyzer/                      # Análise léxica
│       │   ├── AnalisadorLexico.java      # Orquestrador principal
│       │   ├── Tokenizacao.java           # Motor de tokenização
│       │   └── GerenciadorErros.java      # Gerenciamento de erros
│       ├── processor/                     # Processamento de arquivos
│       │   ├── PreProcessador.java        # Remove comentários
│       │   └── GerenciadorArquivos.java   # I/O de arquivos
│       ├── lexema/                        # Dados lexicais
│       │   ├── PalavraReservada.java      # Mapa de palavras reservadas
│       │   └── TabelaSimbolos.java        # Tabela de símbolos
│       └── model/                         # Modelos de dados
│           ├── Token.java                 # Representação de token
│           └── ResultadoAnalise.java      # Resultado da análise
├── src/test/java/com/projeto/             # Testes unitários (em desenvolvimento)
├── resources/
│   ├── arquivo.ent                        # Arquivo de entrada (código-fonte)
│   ├── arquivo.ppr                        # Saída pré-processada
│   ├── arquivo.tks                        # Saída de tokens
│   └── arquivo.err                        # Saída de erros
├── bin/                                   # Arquivos compilados
└── README.md                              # Este arquivo
```

## Como Usar

### Pré-requisitos

- Java 8 ou superior
- Um arquivo de entrada nomeado `arquivo.ent` em `resources/`

### Compilação

```bash
# Navegar até a raiz do projeto
cd analisador-lexico

# Compilar todos os arquivos Java
javac -d bin src/main/java/com/projeto/**/*.java
```

Alternativamente, use sua IDE favorita (IntelliJ IDEA, Eclipse, VS Code).

### Execução

```bash
# Executar o analisador
java -cp bin main.java.com.projeto.Main
```

A saída será exibida no console, e os arquivos de resultado serão gerados em `resources/`:

- `arquivo.ppr` - código processado (sem comentários)
- `arquivo.tks` - lista de tokens identificados
- `arquivo.err` - erros de tokenização (se houver)

### Exemplo de Entrada (`arquivo.ent`)

```
var x;
var y;

read entrada;

if (x > y)
  write(x);
else
  write(y);

// Isto é um comentário de linha
/* Isto é um comentário em bloco */
```

### Exemplo de Saída (`arquivo.tks`)

```
pt var id:0 num:0 col:0
pt x id:1 num:1 col:4
pt ; id:2 num:2 col:5
pt var id:0 num:3 col:7
pt y id:3 num:4 col:11
...
```

## Tokens Reconhecidos

| Classe     | Descrição                 | Exemplos                                    |
| ---------- | ------------------------- | ------------------------------------------- |
| **pr**     | Palavra reservada         | `var`, `if`, `read`, `write`, `else`        |
| **id**     | Identificador             | `x`, `contador`, `nome_var`                 |
| **num**    | Número inteiro ou decimal | `123`, `45.67`, `0`                         |
| **string** | String entre aspas        | `"Hello"`, `"teste"`                        |
| **op**     | Operador/Símbolo          | `+`, `-`, `>`, `<`, `=`, `;`, `,`, `(`, `)` |

## Palavras Reservadas Suportadas

- `var` - declaração de variável
- `read` - leitura de entrada
- `write` - escrita de saída
- `if` - condicional
- `else` - condicional alternativo

## Gerenciamento de Erros

O analisador detecta e registra:

- **Strings não fechadas** - `"texto sem fechar`
- **Caracteres inválidos** - `@`, `#`, `$` (não esperados)
- **Tokens não reconhecidos** - sequências desconhecidas

Todos os erros são salvos em `arquivo.err` com:

- Número da linha
- Número da coluna
- Descrição do erro

Exemplo de `arquivo.err`:

```
ERRO na linha 5, coluna 12: String não fechada
ERRO na linha 8, coluna 3: Caractere inválido: @
```

## Como Estender o Projeto

### Adicionar Novas Palavras Reservadas

Edite [src/main/java/com/projeto/core/lexema/PalavraReservada.java](src/main/java/com/projeto/core/lexema/PalavraReservada.java):

```java
private static final Map<String, String> palavras = new HashMap<>();
static {
    palavras.put("var", "var");
    palavras.put("read", "read");
    palavras.put("write", "write");
    palavras.put("if", "if");
    palavras.put("else", "else");
    // Adicione aqui:
    palavras.put("while", "while");
    palavras.put("for", "for");
}
```

### Adicionar Novos Operadores

Modifique o método `identificarOperador()` em [src/main/java/com/projeto/core/analyzer/Tokenizacao.java](src/main/java/com/projeto/core/analyzer/Tokenizacao.java).

### Modificar o Arquivo de Entrada

Edite o arquivo `resources/arquivo.ent` com seu próprio código-fonte para análise.

## Referências

- **Análise Léxica**: Primeira fase da compilação que transforma código-fonte em tokens
- **Token**: Unidade mínima de código reconhecida pelo compilador
- **Palavras Reservadas**: Identificadores especiais com significado na linguagem

## Autor

Desenvolvimento para disciplina de **Linguagens Formais e Compiladores (LFAC)**

Projeto acadêmico - Uso educacional

---

**Última atualização**: Abril 2026
