package apexstore.cliente;

import Pagos.*;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;

public class ClientePrueba {
    public static void main(String[] args) throws Exception {
        try (Communicator communicator = Util.initialize(args, "cliente.cfg")) {
            CheckoutPrx checkout = CheckoutPrx.checkedCast(communicator.propertyToProxy("checkout"));
            if (checkout == null) throw new RuntimeException("No se encontró el checkout");

            String[][] compras = {
                {"ORD-001", "STRIPE", "120000"},
                {"ORD-002", "PSE",    "89000"},
                {"ORD-003", "CRIPTO", "450000"},
                {"ORD-004", "NEQUI",  "10000"},   // fallo controlado
            };

            for (String[] c : compras) {
                String idTx = checkout.gestionarCompra(c[0], Double.parseDouble(c[2]),
                        "COP", c[1], "tok-demo");
                System.out.println();
                System.out.println("Cliente " + c[0] + " " + c[1] + " iniciada, tx " + idTx
                        + ", estado inmediato " + checkout.consultarEstadoOrden(c[0]));

                // consulta hasta resolverse
                EstadoPago e = EstadoPago.PENDIENTE;
                for (int i = 0; i < 20 && e == EstadoPago.PENDIENTE; i++) {
                    Thread.sleep(500);
                    e = checkout.consultarEstadoOrden(c[0]);
                }
                System.out.println("Cliente " + c[0] + " resultado final: " + e);
            }
        }
    }
}