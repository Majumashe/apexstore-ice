package apexstore.pasarelas;

import Pagos.ReceptorCallbacksPrx;
import com.zeroc.Ice.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ServidorPasarelas {
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args, "pasarelas.cfg")) {
            ObjectAdapter adapter = communicator.createObjectAdapter("Pasarelas");

            // Canal único de confirmación del nodo 2, leído del .cfg (hallazgo 2.2)
            ReceptorCallbacksPrx receptor = ReceptorCallbacksPrx.uncheckedCast(
                    communicator.propertyToProxy("receptor"));

            ScheduledExecutorService agenda = Executors.newScheduledThreadPool(4);

            adapter.add(new EstrategiaSimulada("Stripe", receptor, agenda, 1000, 3000, 0.15),
                    Util.stringToIdentity("stripe"));
            adapter.add(new EstrategiaSimulada("PSE",    receptor, agenda, 1000, 3000, 0.25),
                    Util.stringToIdentity("pse"));
            adapter.add(new EstrategiaSimulada("Cripto", receptor, agenda, 2000, 5000, 0.15),
                    Util.stringToIdentity("cripto"));

            adapter.activate();
            System.out.println("== ServidorPasarelas (nodo 3) escuchando en :10001 ==");
            communicator.waitForShutdown();
        }
    }
}