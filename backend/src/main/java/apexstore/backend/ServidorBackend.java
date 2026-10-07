package apexstore.backend;

import Pagos.EstrategiaPagoPrx;
import com.zeroc.Ice.*;
import java.util.HashMap;
import java.util.Map;

public class ServidorBackend {
    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args, "backend.cfg")) {
            ObjectAdapter adapter = communicator.createObjectAdapter("Backend");

            RepositorioTransacciones repo = new RepositorioTransacciones();

            // Proxies de las estrategias leídos del .cfg (patrón del iot-example)
            Map<String, EstrategiaPagoPrx> registro = new HashMap<>();
            registro.put("STRIPE", EstrategiaPagoPrx.uncheckedCast(communicator.propertyToProxy("stripe")));
            registro.put("PSE",    EstrategiaPagoPrx.uncheckedCast(communicator.propertyToProxy("pse")));
            registro.put("CRIPTO", EstrategiaPagoPrx.uncheckedCast(communicator.propertyToProxy("cripto")));

            ProcesadorPagosContexto contexto = new ProcesadorPagosContexto(registro, repo);

            adapter.add(new CheckoutI(contexto, repo), Util.stringToIdentity("checkout"));
            adapter.add(new ReceptorCallbacksI(repo), Util.stringToIdentity("receptor"));

            adapter.activate();
            System.out.println("ServidorBackend escuchando en 10000");
            communicator.waitForShutdown();
        }
    }
}