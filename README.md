# 📺 Controle Remoto — Padrão Builder + Command

Projeto Java que demonstra a combinação dos padrões de projeto **Builder** e **Command** aplicados a um sistema de controle remoto de televisão.

---

## 🏗️ Estrutura do Projeto

```
controle-remoto/
├── pom.xml
├── diagrama-classes.mermaid
├── src/
│   ├── main/java/com/remoto/
│   │   ├── model/
│   │   │   └── ControleRemoto.java       ← Invoker + Builder (inner class)
│   │   ├── command/
│   │   │   ├── Comando.java              ← Interface Command
│   │   │   ├── ComandoLigar.java
│   │   │   ├── ComandoDesligar.java
│   │   │   ├── ComandoAumentarVolume.java
│   │   │   ├── ComandoDiminuirVolume.java
│   │   │   ├── ComandoSetVolume.java
│   │   │   ├── ComandoProximoCanal.java
│   │   │   └── ComandoSetEntrada.java
│   │   └── receiver/
│   │       └── Televisao.java            ← Receiver
│   └── test/java/com/remoto/
│       └── ControleRemotoTest.java       ← 30 casos de teste
```

---

## 🎯 Padrões Utilizados

### Builder
O `ControleRemoto` só pode ser instanciado por meio do seu `Builder` interno.  
Parâmetros **obrigatórios** (marca e modelo) são passados no construtor do Builder.  
Parâmetros **opcionais** (botões, desfazer, macro, número de botões) são encadeados via fluent API:

```java
ControleRemoto controle = new ControleRemoto.Builder("Samsung", "RM-L1088")
        .numeroBotoes(20)
        .comDesfazer()
        .comMacro()
        .adicionarBotao("LIGAR",  new ComandoLigar(tv))
        .adicionarBotao("VOL+",   new ComandoAumentarVolume(tv))
        .adicionarBotao("HDMI2",  new ComandoSetEntrada(tv, "HDMI2"))
        .build();
```

### Command
Cada ação do controle é encapsulada em uma classe de comando concreta que implementa `Comando`.  
O `ControleRemoto` mantém um histórico de comandos executados, permitindo **desfazer** (undo) a última ação:

```java
controle.pressionarBotao("VOL+"); // executa
controle.desfazer();              // reverte
```

---

## ▶️ Como executar

**Pré-requisito:** Java 17+ e Maven 3.8+

```bash
# Compilar e rodar todos os testes
mvn test

# Ver relatório de testes
mvn surefire-report:report
```

---

## ✅ Casos de Teste (30 no total)

| Categoria          | Qtd |
|--------------------|-----|
| Builder            | 11  |
| Comandos (TV)      | 9   |
| Desfazer (Undo)    | 7   |
| Integração / Borda | 3   |
