package apexstore.pasarelas;

import Pagos.*;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** responde PENDIENTE de inmediato
 *  y confirma después por el canal común ReceptorCallbacks */
public class EstrategiaSimulada implements EstrategiaPago {

    private final String nombre;
    private final ReceptorCallbacksPrx receptor;
    private final ScheduledExecutorService agenda;
    private final int demoraMinMs, demoraMaxMs;
    private final double tasaFallo;
    private final Random rnd = new Random();

    public EstrategiaSimulada(String nombre, ReceptorCallbacksPrx receptor,
                              ScheduledExecutorService agenda,
                              int demoraMinMs, int demoraMaxMs, double tasaFallo) {
        this.nombre = nombre; this.receptor = receptor; this.agenda = agenda;
        this.demoraMinMs = demoraMinMs; this.demoraMaxMs = demoraMaxMs; this.tasaFallo = tasaFallo;
    }

    @Override
    public ResultadoPago procesarPago(SolicitudPago s, com.zeroc.Ice.Current current) {
        int demora = demoraMinMs + rnd.nextInt(demoraMaxMs - demoraMinMs);
        System.out.println(nombre + " cobro " + s.idTransaccion + " " + s.monto + " " + s.moneda
                + " confirma en " + demora + " ms");

        agenda.schedule(new Runnable() {
            @Override
            public void run() {
                EstadoPago fin;
                if (rnd.nextDouble() < tasaFallo) {
                    fin = EstadoPago.RECHAZADO;
                } else {
                    fin = EstadoPago.APROBADO;
                }
                System.out.println(nombre + " " + s.idTransaccion + " -> " + fin + " callback al receptor");
                receptor.notificarResultadoPagoAsync(s.idTransaccion, fin);
            }
        }, demora, TimeUnit.MILLISECONDS);

        // Retorno inmediato: el hilo del backend no espera al "banco"
        return new ResultadoPago(s.idTransaccion, EstadoPago.PENDIENTE, nombre + " procesando");
    }
}