package com.remoto.command;

import com.remoto.receiver.Televisao;

public class ComandoAumentarVolume implements Comando {
    private final Televisao tv;

    public ComandoAumentarVolume(Televisao tv) {
        this.tv = tv;
    }

    @Override
    public void executar() { tv.aumentarVolume(); }

    @Override
    public void desfazer() { tv.diminuirVolume(); }
}
