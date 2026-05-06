package com.remoto.command;

import com.remoto.receiver.Televisao;

public class ComandoLigar implements Comando {
    private final Televisao tv;

    public ComandoLigar(Televisao tv) {
        this.tv = tv;
    }

    @Override
    public void executar() { tv.ligar(); }

    @Override
    public void desfazer() { tv.desligar(); }
}
