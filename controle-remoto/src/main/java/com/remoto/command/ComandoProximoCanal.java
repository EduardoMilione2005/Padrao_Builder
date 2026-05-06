package com.remoto.command;

import com.remoto.receiver.Televisao;

public class ComandoProximoCanal implements Comando {
    private final Televisao tv;

    public ComandoProximoCanal(Televisao tv) {
        this.tv = tv;
    }

    @Override
    public void executar() { tv.proximoCanal(); }

    @Override
    public void desfazer() { tv.canalAnterior(); }
}
