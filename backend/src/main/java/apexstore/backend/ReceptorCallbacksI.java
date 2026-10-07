package apexstore.backend;

import Pagos.*;

//  recibe TODAS las confirmaciones, incluida Cripto  
public class ReceptorCallbacksI implements ReceptorCallbacks {
    private final RepositorioTransacciones repositorio;

    public ReceptorCallbacksI(RepositorioTransacciones repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public void notificarResultadoPago(String idTransaccion, EstadoPago estado,
                                       com.zeroc.Ice.Current current) {
        System.out.println("Receptor callback " + idTransaccion + " = " + estado);
        repositorio.actualizarEstado(idTransaccion, estado);
        
    }
}