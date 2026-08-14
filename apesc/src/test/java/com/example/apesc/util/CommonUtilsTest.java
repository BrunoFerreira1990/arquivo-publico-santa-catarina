package com.example.apesc.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommonUtilsTest {

    @Test
    void toTitleCase_deveCapitalizarCadaPalavra() {
        assertThat(CommonUtils.toTitleCase("maria da silva")).isEqualTo("Maria Da Silva");
    }

    @Test
    void toTitleCase_deveNormalizarTextoJaEmMaiusculo() {
        assertThat(CommonUtils.toTitleCase("MARIA DA SILVA")).isEqualTo("Maria Da Silva");
    }

    @Test
    void toTitleCase_deveRetornarNuloQuandoEntradaForNula() {
        assertThat(CommonUtils.toTitleCase(null)).isNull();
    }

    @Test
    void toTitleCase_deveRetornarTextoOriginalQuandoVazio() {
        assertThat(CommonUtils.toTitleCase("   ")).isEqualTo("   ");
    }

    @Test
    void toUpperCaseSafe_deveConverterParaMaiusculoERemoverEspacos() {
        assertThat(CommonUtils.toUpperCaseSafe("  santa catarina  ")).isEqualTo("SANTA CATARINA");
    }

    @Test
    void toUpperCaseSafe_deveRetornarNuloQuandoEntradaForNula() {
        assertThat(CommonUtils.toUpperCaseSafe(null)).isNull();
    }

    @Test
    void digitsOnly_deveRemoverPontuacaoDoCpf() {
        assertThat(CommonUtils.digitsOnly("123.456.789-30")).isEqualTo("12345678930");
    }

    @Test
    void digitsOnly_deveManterStringJaComSoDigitos() {
        assertThat(CommonUtils.digitsOnly("12345678930")).isEqualTo("12345678930");
    }

    @Test
    void digitsOnly_deveRemoverLetrasEEspacos() {
        assertThat(CommonUtils.digitsOnly("CEP 88 010-000")).isEqualTo("88010000");
    }

    @Test
    void digitsOnly_deveRetornarNuloQuandoEntradaForNula() {
        assertThat(CommonUtils.digitsOnly(null)).isNull();
    }

    @Test
    void digitsOnly_deveRetornarStringVaziaQuandoNaoHaDigitos() {
        assertThat(CommonUtils.digitsOnly("abc.-")).isEmpty();
    }
}
