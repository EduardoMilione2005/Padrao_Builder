package com.remoto.model;

import com.remoto.command.Comando;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class ControleRemoto {

    private final String marca;
    private final String modelo;
    private final int numeroBotoes;
    private final boolean temDesfazer;
    private final boolean temMacro;

    private final Map<String, Comando> botoes;
    private final Deque<Comando> historico;

    private ControleRemoto(Builder builder) {
        this.marca        = builder.marca;
        this.modelo       = builder.modelo;
        this.numeroBotoes = builder.numeroBotoes;
        this.temDesfazer  = builder.temDesfazer;
        this.temMacro     = builder.temMacro;
        this.botoes       = new HashMap<>(builder.botoes);
        this.historico    = new ArrayDeque<>();
    }

    public void pressionarBotao(String nomeBotao) {
        Comando cmd = botoes.get(nomeBotao);
        if (cmd == null) {
            System.out.println("[CONTROLE] Botão '" + nomeBotao + "' não configurado.");
            return;
        }
        cmd.executar();
        if (temDesfazer) {
            historico.push(cmd);
        }
    }

    public void desfazer() {
        if (!temDesfazer) {
            System.out.println("[CONTROLE] Este controle não suporta desfazer.");
            return;
        }
        if (historico.isEmpty()) {
            System.out.println("[CONTROLE] Nenhum comando para desfazer.");
            return;
        }
        Comando ultimo = historico.pop();
        ultimo.desfazer();
        System.out.println("[CONTROLE] Último comando desfeito.");
    }

    public void limparHistorico() {
        historico.clear();
    }

    public String getMarca()        { return marca; }
    public String getModelo()       { return modelo; }
    public int getNumeroBotoes()    { return numeroBotoes; }
    public boolean isTemDesfazer()  { return temDesfazer; }
    public boolean isTemMacro()     { return temMacro; }
    public int getTamanhoHistorico(){ return historico.size(); }
    public boolean temBotao(String nome) { return botoes.containsKey(nome); }

    @Override
    public String toString() {
        return "ControleRemoto{marca='" + marca + "', modelo='" + modelo +
               "', botoes=" + botoes.keySet() +
               ", desfazer=" + temDesfazer + ", macro=" + temMacro + "}";
    }

    public static class Builder {

        private final String marca;
        private final String modelo;

        private int numeroBotoes  = 10;
        private boolean temDesfazer = false;
        private boolean temMacro    = false;
        private final Map<String, Comando> botoes = new HashMap<>();

        public Builder(String marca, String modelo) {
            if (marca == null || marca.isBlank())
                throw new IllegalArgumentException("Marca não pode ser vazia.");
            if (modelo == null || modelo.isBlank())
                throw new IllegalArgumentException("Modelo não pode ser vazio.");
            this.marca  = marca;
            this.modelo = modelo;
        }

        public Builder numeroBotoes(int n) {
            if (n <= 0) throw new IllegalArgumentException("Número de botões deve ser positivo.");
            this.numeroBotoes = n;
            return this;
        }

        public Builder comDesfazer() {
            this.temDesfazer = true;
            return this;
        }

        public Builder comMacro() {
            this.temMacro = true;
            return this;
        }

        public Builder adicionarBotao(String nome, Comando comando) {
            if (nome == null || nome.isBlank())
                throw new IllegalArgumentException("Nome do botão não pode ser vazio.");
            if (comando == null)
                throw new IllegalArgumentException("Comando não pode ser nulo.");
            botoes.put(nome, comando);
            return this;
        }

        public ControleRemoto build() {
            return new ControleRemoto(this);
        }
    }
}
