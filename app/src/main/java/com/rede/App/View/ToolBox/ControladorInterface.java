package com.rede.App.View.ToolBox;


import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

/**
 * Classe responsável por conter animações
 * e demais efeitos de interface
 *
 * @author Igor Maximo
 * @date 23/06/2021
 */
public class ControladorInterface {

    /**
     * Participa do evento de onClick de qualquer
     * botão da interface gráfica de qualquer tela.
     *
     * @author Igor Maximo
     * @date 23/06/2021
     */
    public static void setClickBotao(Context context) {
        try {
            // Realiza efeito de vibrar o celular
            setVibrar(context);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Faz o celular vibrar com base nos milisegundos
     *
     * @author Igor Maximo
     * @date 23/06/2021
     */
    private static void setVibrar(Context context) {
        try {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(VariaveisGlobais.VIBRAR_TOQUE_MILI, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(VariaveisGlobais.VIBRAR_TOQUE_MILI);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Faz o celular vibrar com base nos milisegundos
     *
     * @author Igor Maximo
     * @date 23/06/2021
     */
    private static void setVibrar(Context context, int milisegundos) {
        try {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(milisegundos, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(milisegundos);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}