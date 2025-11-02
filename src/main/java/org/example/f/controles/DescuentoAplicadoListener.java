// Archivo: org.example.f.controles/DescuentoAplicadoListener.java

package org.example.f.controles;

import org.example.f.modelos.Descuento;

public interface DescuentoAplicadoListener {

    // 🛑 Ambos deben ser 'void'
    void onDescuentoAplicado(Descuento descuento);

    void onDescuentoRemovido();

}