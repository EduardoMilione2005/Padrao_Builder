package com.remoto.command;

import com.remoto.receiver.Televisao;

public class ComandoDesligar implements Comando {
    private final Televisao tv;

    public ComandoDesligar(Televisao tv) {
        this.tv = tv;
    }

    @Override
    public void executar() { tv.desligar(); }

    @Override
    public void desfazer() { tv.ligar(); }
}
