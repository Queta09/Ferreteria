package org.example.f.controles;

import org.example.f.modelos.Descuento;

public interface DescuentoAplicadoListener {

    void onDescuentoAplicado(Descuento descuento);

    void onDescuentoRemovido();

}