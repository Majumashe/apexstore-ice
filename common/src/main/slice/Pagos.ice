module Pagos
{
    enum EstadoPago { PENDIENTE, APROBADO, RECHAZADO, ERROR }

    struct SolicitudPago
    {
        string idOrden;
        string idTransaccion;
        double monto;
        string moneda;
        string tipoMedio;
        string tokenMedioPago;
    }

    struct ResultadoPago
    {
        string idTransaccion;
        EstadoPago estado;
        string mensaje;
    }

    interface EstrategiaPago
    {
        ResultadoPago procesarPago(SolicitudPago solicitud);
    }

    interface ReceptorCallbacks
    {
        void notificarResultadoPago(string idTransaccion, EstadoPago estado);
    }

    interface Checkout
    {
        string gestionarCompra(string idOrden, double monto, string moneda, string tipoMedio, string tokenMedioPago);
        EstadoPago consultarEstadoOrden(string idOrden);
    }
}
