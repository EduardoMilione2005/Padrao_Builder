package com.remoto.command;

import com.remoto.receiver.Televisao;

public class ComandoSetEntrada implements Comando {
    private final Televisao tv;
    private final String novaEntrada;
    private String entradaAnterior;

    public ComandoSetEntrada(Televisao tv, String novaEntrada) {
        this.tv = tv;
        this.novaEntrada = novaEntrada;
    }

    @Override
    public void executar() {
        this.entradaAnterior = tv.getEntrada();
        tv.setEntrada(novaEntrada);
    }

    @Override
    public void desfazer() {
        tv.setEntrada(entradaAnterior);
    }
}
