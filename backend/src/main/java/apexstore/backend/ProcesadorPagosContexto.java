package apexstore.backend;

import Pagos.*;
import java.util.Map;
import java.util.UUID;


public class ProcesadorPagosContexto {
    private final Map<String, EstrategiaPagoPrx> registro;
    private final RepositorioTransacciones repositorio;

    public ProcesadorPagosContexto(Map<String, EstrategiaPagoPrx> registro,
                                   RepositorioTransacciones repositorio) {
        this.registro = registro; this.repositorio = repositorio;
    }

    
    public String iniciarPagoOrden(String idOrden, double monto, String moneda,
                                   String tipoMedio, String token) {
        String idTx = "TX-" + UUID.randomUUID().toString().substring(0, 8);
        repositorio.registrar(idOrden, idTx);

        EstrategiaPagoPrx estrategia = registro.get(tipoMedio);
        if (estrategia == null) {
            System.out.println("Contexto medio " + tipoMedio + " no registrado");
            repositorio.actualizarEstado(idTx, EstadoPago.ERROR);
            return idTx;
        }

        SolicitudPago sol = new SolicitudPago(idOrden, idTx, monto, moneda, tipoMedio, token);
        estrategia.procesarPagoAsync(sol).whenComplete(new java.util.function.BiConsumer<ResultadoPago, Throwable>() {
            @Override
            public void accept(ResultadoPago r, Throwable ex) {
                if (ex != null) {                   // pasarela caída: fallo controlado
                    System.out.println("Contexto " + idTx + " fallo al despachar: " + ex.getMessage());
                    repositorio.actualizarEstado(idTx, EstadoPago.ERROR);
                } else {
                    System.out.println("Contexto " + idTx + " despachado: " + r.estado);
                }
            }
        });
        return idTx;    // el hilo queda libre, la aprobación llegará por callback
    }
}