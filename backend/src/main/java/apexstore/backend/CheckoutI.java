package apexstore.backend;

import Pagos.*;


public class CheckoutI implements Checkout {
    private final ProcesadorPagosContexto contexto;
    private final RepositorioTransacciones repositorio;

    public CheckoutI(ProcesadorPagosContexto contexto, RepositorioTransacciones repositorio) {
        this.contexto = contexto; this.repositorio = repositorio;
    }

    @Override
    public String gestionarCompra(String idOrden, double monto, String moneda,
                                  String tipoMedio, String token,
                                  com.zeroc.Ice.Current current) {
        System.out.println("Checkout compra " + idOrden + " via " + tipoMedio);
        return contexto.iniciarPagoOrden(idOrden, monto, moneda, tipoMedio, token);
    }

    @Override
    public EstadoPago consultarEstadoOrden(String idOrden, com.zeroc.Ice.Current current) {
        return repositorio.consultarPorOrden(idOrden);   
    }
}