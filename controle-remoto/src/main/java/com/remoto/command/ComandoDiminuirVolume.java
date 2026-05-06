package com.remoto.command;

import com.remoto.receiver.Televisao;

public class ComandoDiminuirVolume implements Comando {
    private final Televisao tv;

    public ComandoDiminuirVolume(Televisao tv) {
        this.tv = tv;
    }

    @Override
    public void executar() { tv.diminuirVolume(); }

    @Override
    public void desfazer() { tv.aumentarVolume(); }
}
