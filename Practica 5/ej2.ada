--Se quiere modelar el funcionamiento de un banco, al cual llegan clientes que deben realizar
--un pago y retirar un comprobante. Existe un único empleado en el banco, el cual atiende de
--acuerdo con el orden de llegada.
    --a) Implemente una solución donde los clientes llegan y se retiran sólo después de haber sido
    --atendidos.
    --b) Implemente una solución donde los clientes se retiran si esperan más de 10 minutos para
    --realizar el pago.
    --c) Implemente una solución donde los clientes se retiran si no son atendidos
    --inmediatamente.
    --d) Implemente una solución donde los clientes esperan a lo sumo 10 minutos para ser
    --atendidos. Si pasado ese lapso no fueron atendidos, entonces solicitan atención una vez más
    --y se retiran si no son atendidos inmediatamente

--A)
Procedure Banco is
Task empleado is
    Entry Pedido (pago:IN int, comprobante: OUT texto);
End empleado;

Task Type cliente;

arrClientes(1..N) of Cliente;

Task Body cliente is
    comprobante:texto;
    pago:int;
Begin
    Empleado.Pedido(pago,comprobante); --cuando el empleado acepta la comunicacion el cliente le envía el pago y espera a que el empleado le devuelve el comprobante
End cliente;

Task Body Empleado is
Begin
    loop
        accept Pedido(p:IN int; comp: OUT texto) do
            comp = realizarComprobante(p);
        end Pedido; --aca termina el accept de pedido entonces termina la comunicacion sincronica con el cliente
    end loop;
End Empleado;

Begin
    null;
End Banco;

--B)
Procedure Banco is
Task empleado is
    Entry Pedido (pago:IN int, comprobante: OUT texto);
End empleado;

Task Type cliente;

arrClientes(1..N) of Cliente;

Task Body cliente is
    comprobante:texto;
    pago:int;
Begin
    SELECT
        Empleado.Pedido(pago,comprobante); --cuando el empleado acepta la comunicacion el cliente le envía el pago y espera a que el empleado le devuelve el comprobante
    OR DELAY 600.0 --en caso de no ser atendido a lo largo de los 10 minutos, se saca automaticamente de la cola de Pedido el pedido de este cliente en particular y se hace en este caso null
        null
    END SELECT;
End cliente;

Task Body Empleado is
Begin
    loop
        accept Pedido(p:IN int; comp: OUT texto) do
            comp = realizarComprobante(p);
        end Pedido; --aca termina el accept de pedido entonces termina la comunicacion sincronica con el cliente
    end loop;
End Empleado;

Begin
    null;
End Banco;


--C)
Procedure Banco is
Task empleado is
    Entry Pedido (pago:IN int, comprobante: OUT texto);
End empleado;

Task Type cliente;

arrClientes(1..N) of Cliente;

Task Body cliente is
    comprobante:texto;
    pago:int;
Begin
    SELECT
        Empleado.Pedido(pago,comprobante); --cuando el empleado acepta la comunicacion el cliente le envía el pago y espera a que el empleado le devuelve el comprobante
    else --en caso de no ser atendido a inmediatamente, se saca automaticamente de la cola de Pedido el pedido de este cliente en particular y se hace en este caso null
        null
    END SELECT;
End cliente;

Task Body Empleado is
Begin
    loop
        accept Pedido(p:IN int; comp: OUT texto) do
            comp = realizarComprobante(p);
        end Pedido; --aca termina el accept de pedido entonces termina la comunicacion sincronica con el cliente
    end loop;
End Empleado;

Begin
    null;
End Banco;

--D)
Procedure Banco is
Task empleado is
    Entry Pedido (pago:IN int, comprobante: OUT texto);
End empleado;

Task Type cliente;

arrClientes(1..N) of Cliente;

Task Body cliente is
    comprobante:texto;
    pago:int;
Begin
    SELECT
        Empleado.Pedido(pago,comprobante); --cuando el empleado acepta la comunicacion el cliente le envía el pago y espera a que el empleado le devuelve el comprobante
    OR DELAY 600.0 --en caso de no ser atendido a lo largo de los 10 minutos, se saca automaticamente de la cola de Pedido el pedido de este cliente en particular y solicita una vez más y si no es atendido inmediatamente se va
        SELECT 
            Empleado.Pedido(pago,comprobante); --vuelve a solicitar la conexión
        ELSE --si no es atendido automaticamente dps de 10 min se va 
            null;
        END SELECT;
    END SELECT;
End cliente;

Task Body Empleado is
Begin
    loop
        accept Pedido(p:IN int; comp: OUT texto) do
            comp = realizarComprobante(p);
        end Pedido; --aca termina el accept de pedido entonces termina la comunicacion sincronica con el cliente
    end loop;
End Empleado;

Begin
    null;
End Banco;