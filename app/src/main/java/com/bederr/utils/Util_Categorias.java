package com.bederr.utils;


import pe.bederr.com.R;

/**
 * Created by Gantz on 16/07/14.
 */
public class Util_Categorias {

    public static int getImageCateogry(int id_categoria) {

        int categorias[] = {
                R.drawable.categoria_viajes,
                R.drawable.categoria_markets,//
                R.drawable.categoria_salud,
                R.drawable.categoria_ropa,
                R.drawable.grifos,
                R.drawable.categoria_descuento,
                R.drawable.categoria_comida,
                R.drawable.categoria_beneficio,//
        };
        return categorias[id_categoria];
    }

    public static int getImageCategory(String name) {
        if (name.equals("Viajes")) {
            return R.drawable.categoria_viajes;
        } else if (name.equals("Tiendas y servicios")) {
            return R.drawable.categoria_markets;
        } else if (name.equals("Salud y belleza")) {
            return R.drawable.categoria_salud;
        } else if (name.equals("Ropa")) {
            return R.drawable.categoria_ropa;
        } else if (name.equals("Grifo")) {
            return R.drawable.grifos;
        } else if (name.equals("Descuentos")) {
            return R.drawable.categoria_descuento;
        } else if (name.equals("Comida")) {
            return R.drawable.categoria_comida;
        } else if (name.equals("Beneficios")) {
            return R.drawable.categoria_beneficio;
        }
        return R.drawable.otros;
    }
}
