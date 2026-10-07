package apexstore.backend;

import Pagos.EstadoPago;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class RepositorioTransacciones {
    private final Map<String, EstadoPago> porTransaccion = new ConcurrentHashMap<>();
    private final Map<String, String> ordenATransaccion = new ConcurrentHashMap<>();

    public void registrar(String idOrden, String idTransaccion) {
        porTransaccion.put(idTransaccion, EstadoPago.PENDIENTE);
        ordenATransaccion.put(idOrden, idTransaccion);
        System.out.println("Repositorio " + idTransaccion + " orden " + idOrden + " = PENDIENTE");
    }

    public void actualizarEstado(String idTransaccion, EstadoPago estado) {
        porTransaccion.put(idTransaccion, estado);
        System.out.println("Repositorio " + idTransaccion + " = " + estado);
    }

    public EstadoPago consultarPorOrden(String idOrden) {
        String tx = ordenATransaccion.get(idOrden);
        if (tx == null) {
            return EstadoPago.ERROR;
        }
        return porTransaccion.get(tx);
    }
}