package com.remoto.command;

import com.remoto.receiver.Televisao;

public class ComandoSetVolume implements Comando {
    private final Televisao tv;
    private final int novoVolume;
    private int volumeAnterior;

    public ComandoSetVolume(Televisao tv, int novoVolume) {
        this.tv = tv;
        this.novoVolume = novoVolume;
    }

    @Override
    public void executar() {
        this.volumeAnterior = tv.getVolume();
        tv.setVolume(novoVolume);
    }

    @Override
    public void desfazer() {
        tv.setVolume(volumeAnterior);
    }
}
