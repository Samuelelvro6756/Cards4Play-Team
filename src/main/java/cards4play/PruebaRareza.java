/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cards4play;
import cards4play.modelo.Rareza;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author acer
 */
public class PruebaRareza {

    public static void main(String[] args) {
        int total = 10000;
        Random random = new Random();
        Map<Rareza, Integer> conteo = new EnumMap<>(Rareza.class);
        for (Rareza r : Rareza.values()) {
            conteo.put(r, 0);
        }
        for (int i = 0; i < total; i++) {
            Rareza r = Rareza.sortear(random);
            conteo.put(r, conteo.get(r) + 1);
        }
        for (Rareza r : Rareza.values()) {
            double porcentaje = conteo.get(r) * 100.0 / total;
            System.out.printf("%-3s esperado %2d%%  obtenido %.2f%%%n",
                    r, r.getProbabilidad(), porcentaje);
        }
    }
}
