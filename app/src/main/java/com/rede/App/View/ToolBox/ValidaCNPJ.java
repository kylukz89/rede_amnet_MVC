package com.rede.App.View.ToolBox;

import java.util.InputMismatchException;

/**
 * Valida um CNPJ de acordo com padrão da receita
 * RETORNO:
 * FALSE                   - CNPJ inválido
 * TRUE                   - CNPJ válido
 *
 * @author Igor Maximo
 * @criado 19/02/2019
 * @editado 02/03/2019
 */

public final class ValidaCNPJ {

    public static boolean valida(String CNPJ) {
        // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
        if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") || CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") || CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") || CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") || (CNPJ.length() != 14))
            return (false);

        char dig13, dig14;
        int sm, i, r, num, peso;

        // "try" - protege o código para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                // converte o i-ésimo caractere do CNPJ em um número:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posição de '0' na tabela ASCII)
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else
                dig13 = (char) ((11 - r) + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int) (CNPJ.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else
                dig14 = (char) ((11 - r) + 48);

            // Verifica se os dígitos calculados conferem com os dígitos informados.
            if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
                return (true);
            else
                return (false);
        } catch (InputMismatchException erro) {
            return (false);
        }
        /*
        if (cnpj.length() != 14) {
            return false;
        }

        String[] crescente = new String[cnpj.length() - 2]; // CNPJ
        String[] decrescente = new String[]{"5", "4", "3", "2", "9", "8", "7", "6", "5", "4", "3", "2"}; // Pesos padrão
        String[] decrescente2 = new String[]{"6", "5", "4", "3", "2", "9", "8", "7", "6", "5", "4", "3", "2"}; // Pesos padrão
        String[] crescente2;
        String[] crescSegDig = new String[cnpj.length() - 2];

        // 11.222.333/0001-8X
        for (int i = 0, j = cnpj.length(); i < cnpj.length() - 2; i++, j--) {
            crescente[i] = cnpj.substring(i, i + 1);
        }

        int[] multiplicaValoresColuna = new int[crescente.length];
        int[] multiplicaValoresSegCol = new int[crescSegDig.length + 1];

        int soma = 0, resto = 0;

        for (int i = 0; i < crescente.length; i++) {
            multiplicaValoresColuna[i] = Integer.parseInt(crescente[i]) * Integer.parseInt(decrescente[i]);
            soma += multiplicaValoresColuna[i];
        }

        resto = soma % 11; // padrão 11 para cálculo
        resto = 11 - resto;

        String digitoVerificador1 = "";

        if (resto > 9) {
            digitoVerificador1 = "0";
        } else {
            digitoVerificador1 = String.valueOf(resto);
        }
        crescente2 = new String[crescente.length + 2];

        int k = 0;
        for (k = 0; k < crescente.length; k++) {
            crescente2[k] = crescente[k];
        }

        crescente2[crescente2.length - 2] = digitoVerificador1;

        soma = 0;
        resto = 0;
        for (int i = 0; i < crescente2.length - 1; i++) {
            multiplicaValoresSegCol[i] = Integer.parseInt(crescente2[i]) * Integer.parseInt(decrescente2[i]);
            soma += multiplicaValoresSegCol[i];
        }
        resto = soma % 11; // padrão 11 devido tamanho de um CNPJ original

        soma = (resto - 11) * -1;

        crescente2[crescente2.length - 1] = soma + "";

        String montaCNPJ = "";

        for (int i = 0; i < crescente2.length; i++) {
            montaCNPJ += crescente2[i];
        }

        if (montaCNPJ.equals((cnpj))) { // comparara-se
            return true;
        } else {
            return false;
        }
        */

    }

}
