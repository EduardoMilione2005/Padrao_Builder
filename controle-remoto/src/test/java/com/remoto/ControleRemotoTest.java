package com.remoto;

import com.remoto.command.*;
import com.remoto.model.ControleRemoto;
import com.remoto.receiver.Televisao;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
class ControleRemotoTest {

    private Televisao tv;

    @BeforeEach
    void setUp() {
        tv = new Televisao();
    }

    @Test
    @DisplayName("1 - Builder: deve criar controle com marca e modelo obrigatórios")
    void deveCriarControleComMarcaEModelo() {
        ControleRemoto controle = new ControleRemoto.Builder("Samsung", "RM-L1088")
                .build();

        assertEquals("Samsung", controle.getMarca());
        assertEquals("RM-L1088", controle.getModelo());
    }

    @Test
    @DisplayName("2 - Builder: deve lançar exceção quando marca for vazia")
    void deveLancarExcecaoMarcaVazia() {
        assertThrows(IllegalArgumentException.class, () ->
                new ControleRemoto.Builder("", "RM-001").build()
        );
    }

    @Test
    @DisplayName("3 - Builder: deve lançar exceção quando modelo for nulo")
    void deveLancarExcecaoModeloNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new ControleRemoto.Builder("LG", null).build()
        );
    }

    @Test
    @DisplayName("4 - Builder: deve configurar número de botões")
    void deveConfigurarNumeroDeBotoes() {
        ControleRemoto controle = new ControleRemoto.Builder("Sony", "RMT-TX100B")
                .numeroBotoes(20)
                .build();

        assertEquals(20, controle.getNumeroBotoes());
    }

    @Test
    @DisplayName("5 - Builder: deve lançar exceção para número de botões inválido")
    void deveLancarExcecaoNumeroBotoesInvalido() {
        assertThrows(IllegalArgumentException.class, () ->
                new ControleRemoto.Builder("Sony", "RMT-TX100B")
                        .numeroBotoes(0)
                        .build()
        );
    }

    @Test
    @DisplayName("6 - Builder: deve habilitar funcionalidade de desfazer")
    void deveHabilitarDesfazer() {
        ControleRemoto controle = new ControleRemoto.Builder("Philips", "PH-001")
                .comDesfazer()
                .build();

        assertTrue(controle.isTemDesfazer());
    }

    @Test
    @DisplayName("7 - Builder: deve habilitar funcionalidade de macro")
    void deveHabilitarMacro() {
        ControleRemoto controle = new ControleRemoto.Builder("Philips", "PH-002")
                .comMacro()
                .build();

        assertTrue(controle.isTemMacro());
    }

    @Test
    @DisplayName("8 - Builder: deve adicionar botão com comando")
    void deveAdicionarBotaoComComando() {
        ControleRemoto controle = new ControleRemoto.Builder("LG", "AKB-001")
                .adicionarBotao("LIGAR", new ComandoLigar(tv))
                .build();

        assertTrue(controle.temBotao("LIGAR"));
    }

    @Test
    @DisplayName("9 - Builder: deve lançar exceção ao adicionar botão com nome vazio")
    void deveLancarExcecaoBotaoNomeVazio() {
        assertThrows(IllegalArgumentException.class, () ->
                new ControleRemoto.Builder("LG", "AKB-001")
                        .adicionarBotao("", new ComandoLigar(tv))
                        .build()
        );
    }

    @Test
    @DisplayName("10 - Builder: deve lançar exceção ao adicionar botão com comando nulo")
    void deveLancarExcecaoBotaoComandoNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new ControleRemoto.Builder("LG", "AKB-001")
                        .adicionarBotao("LIGAR", null)
                        .build()
        );
    }

    @Test
    @DisplayName("11 - Builder: deve criar controle completo com todos os recursos (fluent)")
    void deveCriarControleCompleto() {
        ControleRemoto controle = new ControleRemoto.Builder("Universal", "UNI-MAX")
                .numeroBotoes(30)
                .comDesfazer()
                .comMacro()
                .adicionarBotao("POWER", new ComandoLigar(tv))
                .adicionarBotao("VOL+",  new ComandoAumentarVolume(tv))
                .adicionarBotao("VOL-",  new ComandoDiminuirVolume(tv))
                .adicionarBotao("CH+",   new ComandoProximoCanal(tv))
                .build();

        assertAll("Controle completo",
                () -> assertEquals("Universal", controle.getMarca()),
                () -> assertEquals(30, controle.getNumeroBotoes()),
                () -> assertTrue(controle.isTemDesfazer()),
                () -> assertTrue(controle.isTemMacro()),
                () -> assertTrue(controle.temBotao("POWER")),
                () -> assertTrue(controle.temBotao("VOL+")),
                () -> assertTrue(controle.temBotao("VOL-")),
                () -> assertTrue(controle.temBotao("CH+"))
        );
    }

    @Test
    @DisplayName("12 - Comando: pressionar LIGAR deve ligar a TV")
    void develigarTVAoPressionarBotao() {
        ControleRemoto controle = new ControleRemoto.Builder("Samsung", "S-01")
                .adicionarBotao("LIGAR", new ComandoLigar(tv))
                .build();

        assertFalse(tv.isLigada());
        controle.pressionarBotao("LIGAR");
        assertTrue(tv.isLigada());
    }

    @Test
    @DisplayName("13 - Comando: pressionar DESLIGAR deve desligar a TV")
    void deveDesligarTVAoPressionarBotao() {
        tv.ligar();
        ControleRemoto controle = new ControleRemoto.Builder("Samsung", "S-01")
                .adicionarBotao("DESLIGAR", new ComandoDesligar(tv))
                .build();

        controle.pressionarBotao("DESLIGAR");
        assertFalse(tv.isLigada());
    }

    @Test
    @DisplayName("14 - Comando: VOL+ deve aumentar volume em 1")
    void deveAumentarVolume() {
        tv.ligar();
        int volumeInicial = tv.getVolume();
        ControleRemoto controle = new ControleRemoto.Builder("LG", "L-01")
                .adicionarBotao("VOL+", new ComandoAumentarVolume(tv))
                .build();

        controle.pressionarBotao("VOL+");
        assertEquals(volumeInicial + 1, tv.getVolume());
    }

    @Test
    @DisplayName("15 - Comando: VOL- deve diminuir volume em 1")
    void deveDiminuirVolume() {
        tv.ligar();
        tv.setVolume(50);
        ControleRemoto controle = new ControleRemoto.Builder("LG", "L-02")
                .adicionarBotao("VOL-", new ComandoDiminuirVolume(tv))
                .build();

        controle.pressionarBotao("VOL-");
        assertEquals(49, tv.getVolume());
    }

    @Test
    @DisplayName("16 - Comando: SetVolume deve definir volume específico")
    void deveDefinirVolumeEspecifico() {
        tv.ligar();
        ControleRemoto controle = new ControleRemoto.Builder("Sony", "S-02")
                .adicionarBotao("VOL30", new ComandoSetVolume(tv, 30))
                .build();

        controle.pressionarBotao("VOL30");
        assertEquals(30, tv.getVolume());
    }

    @Test
    @DisplayName("17 - Comando: volume não deve ultrapassar 100")
    void volumeNaoDeveUltrapassarMaximo() {
        tv.ligar();
        tv.setVolume(100);
        ControleRemoto controle = new ControleRemoto.Builder("Sony", "S-03")
                .adicionarBotao("VOL+", new ComandoAumentarVolume(tv))
                .build();

        controle.pressionarBotao("VOL+");
        assertEquals(100, tv.getVolume());
    }

    @Test
    @DisplayName("18 - Comando: volume não deve ser menor que 0")
    void volumeNaoDeveSerNegativo() {
        tv.ligar();
        tv.setVolume(0);
        ControleRemoto controle = new ControleRemoto.Builder("Sony", "S-04")
                .adicionarBotao("VOL-", new ComandoDiminuirVolume(tv))
                .build();

        controle.pressionarBotao("VOL-");
        assertEquals(0, tv.getVolume());
    }

    @Test
    @DisplayName("19 - Comando: CH+ deve avançar para o próximo canal")
    void deveAvancarCanal() {
        tv.ligar();
        tv.setCanal(5);
        ControleRemoto controle = new ControleRemoto.Builder("Philips", "P-01")
                .adicionarBotao("CH+", new ComandoProximoCanal(tv))
                .build();

        controle.pressionarBotao("CH+");
        assertEquals(6, tv.getCanal());
    }

    @Test
    @DisplayName("20 - Comando: SetEntrada deve trocar a entrada da TV")
    void deveTrocarEntrada() {
        tv.ligar();
        ControleRemoto controle = new ControleRemoto.Builder("Philips", "P-02")
                .adicionarBotao("HDMI2", new ComandoSetEntrada(tv, "HDMI2"))
                .build();

        controle.pressionarBotao("HDMI2");
        assertEquals("HDMI2", tv.getEntrada());
    }

    @Test
    @DisplayName("21 - Desfazer: deve reverter comando de ligar")
    void deveDesfazerComandoLigar() {
        ControleRemoto controle = new ControleRemoto.Builder("Samsung", "S-05")
                .comDesfazer()
                .adicionarBotao("LIGAR", new ComandoLigar(tv))
                .build();

        controle.pressionarBotao("LIGAR");
        assertTrue(tv.isLigada());

        controle.desfazer();
        assertFalse(tv.isLigada());
    }

    @Test
    @DisplayName("22 - Desfazer: deve reverter aumento de volume")
    void deveDesfazerAumentoDeVolume() {
        tv.ligar();
        tv.setVolume(20);
        ControleRemoto controle = new ControleRemoto.Builder("LG", "L-03")
                .comDesfazer()
                .adicionarBotao("VOL+", new ComandoAumentarVolume(tv))
                .build();

        controle.pressionarBotao("VOL+");
        assertEquals(21, tv.getVolume());

        controle.desfazer();
        assertEquals(20, tv.getVolume());
    }

    @Test
    @DisplayName("23 - Desfazer: deve reverter SetVolume para volume anterior")
    void deveDesfazerSetVolume() {
        tv.ligar();
        tv.setVolume(15);
        ControleRemoto controle = new ControleRemoto.Builder("Sony", "S-06")
                .comDesfazer()
                .adicionarBotao("VOL50", new ComandoSetVolume(tv, 50))
                .build();

        controle.pressionarBotao("VOL50");
        assertEquals(50, tv.getVolume());

        controle.desfazer();
        assertEquals(15, tv.getVolume());
    }

    @Test
    @DisplayName("24 - Desfazer: deve reverter troca de entrada")
    void deveDesfazerTrocaDeEntrada() {
        tv.ligar();
        ControleRemoto controle = new ControleRemoto.Builder("LG", "L-04")
                .comDesfazer()
                .adicionarBotao("HDMI2", new ComandoSetEntrada(tv, "HDMI2"))
                .build();

        controle.pressionarBotao("HDMI2");
        assertEquals("HDMI2", tv.getEntrada());

        controle.desfazer();
        assertEquals("HDMI1", tv.getEntrada());
    }

    @Test
    @DisplayName("25 - Desfazer: controle sem suporte não deve reverter comandos")
    void naoDeveDesfazerSemSuporteHabilitado() {
        tv.ligar();
        tv.setVolume(10);
        ControleRemoto controle = new ControleRemoto.Builder("Genérico", "G-01")
                .adicionarBotao("VOL+", new ComandoAumentarVolume(tv))
                .build();

        controle.pressionarBotao("VOL+");
        controle.desfazer(); // não deve fazer nada
        assertEquals(11, tv.getVolume());
    }

    @Test
    @DisplayName("26 - Desfazer: histórico vazio não deve lançar exceção")
    void desfazerComHistoricoVazioNaoDeveLancarExcecao() {
        ControleRemoto controle = new ControleRemoto.Builder("Sony", "S-07")
                .comDesfazer()
                .build();

        assertDoesNotThrow(controle::desfazer);
    }

    @Test
    @DisplayName("27 - Desfazer: limpar histórico deve zerar contagem")
    void deveLimparHistorico() {
        tv.ligar();
        ControleRemoto controle = new ControleRemoto.Builder("Samsung", "S-08")
                .comDesfazer()
                .adicionarBotao("VOL+", new ComandoAumentarVolume(tv))
                .build();

        controle.pressionarBotao("VOL+");
        controle.pressionarBotao("VOL+");
        assertEquals(2, controle.getTamanhoHistorico());

        controle.limparHistorico();
        assertEquals(0, controle.getTamanhoHistorico());
    }

    @Test
    @DisplayName("28 - Integração: botão não configurado não deve lançar exceção")
    void botaoNaoConfiguradoNaoDeveLancarExcecao() {
        ControleRemoto controle = new ControleRemoto.Builder("Genérico", "G-02").build();
        assertDoesNotThrow(() -> controle.pressionarBotao("BOTAO_INEXISTENTE"));
    }

    @Test
    @DisplayName("29 - Integração: TV desligada não deve mudar volume")
    void tvDesligadaNaoDeveMudarVolume() {
        assertFalse(tv.isLigada());
        int volumeInicial = tv.getVolume();
        ControleRemoto controle = new ControleRemoto.Builder("Sony", "S-09")
                .adicionarBotao("VOL+", new ComandoAumentarVolume(tv))
                .build();

        controle.pressionarBotao("VOL+");
        assertEquals(volumeInicial, tv.getVolume());
    }

    @Test
    @DisplayName("30 - Integração: sequência liga → sobe volume → desfaz → desfaz")
    void deveDesfazerSequenciaDeComandos() {
        ControleRemoto controle = new ControleRemoto.Builder("LG", "L-05")
                .comDesfazer()
                .adicionarBotao("LIGAR", new ComandoLigar(tv))
                .adicionarBotao("VOL+",  new ComandoAumentarVolume(tv))
                .build();

        controle.pressionarBotao("LIGAR");
        controle.pressionarBotao("VOL+");
        int volumeAposAumento = tv.getVolume();

        controle.desfazer();  // desfaz VOL+ → volume 10
        assertEquals(volumeAposAumento - 1, tv.getVolume());

        controle.desfazer();
        assertFalse(tv.isLigada());
    }
}
