package com.remoto.receiver;

public class Televisao {

    private boolean ligada;
    private int volume;
    private int canal;
    private String entrada;

    public Televisao() {
        this.ligada = false;
        this.volume = 10;
        this.canal = 1;
        this.entrada = "HDMI1";
    }

    public void ligar() {
        this.ligada = true;
        System.out.println("[TV] Televisão LIGADA.");
    }

    public void desligar() {
        this.ligada = false;
        System.out.println("[TV] Televisão DESLIGADA.");
    }

    public void aumentarVolume() {
        if (ligada && volume < 100) {
            volume++;
            System.out.println("[TV] Volume: " + volume);
        }
    }

    public void diminuirVolume() {
        if (ligada && volume > 0) {
            volume--;
            System.out.println("[TV] Volume: " + volume);
        }
    }

    public void setVolume(int volume) {
        if (volume >= 0 && volume <= 100) {
            this.volume = volume;
            System.out.println("[TV] Volume definido para: " + volume);
        }
    }

    public void proximoCanal() {
        if (ligada) {
            canal++;
            System.out.println("[TV] Canal: " + canal);
        }
    }

    public void canalAnterior() {
        if (ligada && canal > 1) {
            canal--;
            System.out.println("[TV] Canal: " + canal);
        }
    }

    public void setCanal(int canal) {
        if (canal >= 1) {
            this.canal = canal;
            System.out.println("[TV] Canal definido para: " + canal);
        }
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
        System.out.println("[TV] Entrada: " + entrada);
    }

    public boolean isLigada() { return ligada; }
    public int getVolume()    { return volume; }
    public int getCanal()     { return canal; }
    public String getEntrada(){ return entrada; }
}
