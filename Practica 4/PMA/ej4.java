/*
Simular la atención en un locutorio con 10 cabinas telefónicas, el cual tiene un empleado
que se encarga de atender a N clientes. Al llegar, cada cliente espera hasta que el empleado
le indique a qué cabina ir, la usa y luego se dirige al empleado para pagarle. El empleado
atiende a los clientes en el orden en que hacen los pedidos. A cada cliente se le entrega un
ticket factura por la operación.
    a) Implemente una solución para el problema descrito.
    b) Modifique la solución implementada para que el empleado dé prioridad a los que
    terminaron de usar la cabina sobre los que están esperando para usarla.
Nota: maximizar la concurrencia; suponga que hay una función Cobrar() llamada por el
empleado que simula que el empleado le cobra al cliente.
*/

A

chan solicitarCabina(int);
chan obtenerCabina[C](int);

chan pagarEmpleado(int, int);
chan obtenerTicket[C](int);


//if -> no deterministico, cualquiera de los dos se puede ejecutar, se ejecuta en un while do hasta que todas las condiciones sean falsas

Process Empleado{
    bool cabinasOcupadas[10] = ([10] false);
    int idCliente;
    int idCabinaLibre;
    text ticket;

    while (true){
        if (!empty(solicitarCabina) && (hayCabinasLibres(cabinasOcupadas))) -> { //if no deterministico
            receive solicitarCabina(idCliente);
            idCabinaLibre = obtenerCabinaLibre(cabinasOcupadas);
            cabinasOcupadas[idCabinaLibre] = true;
            send obtenerCabina[idCliente](idCabinaLibre);
        }
        if (!empty(pagarEmpleado)) -> { //if no deterministico
            receive pagarEmpleado(idCliente,idCabinaLibre);
            ticket = Cobrar(idCliente,idCabinaLibre);
            cabinasOcupadas[idCabinaLibre] = false;
            send obtenerTicket[idCliente](ticket);
        }
        }
    }
}

Process Cliente[id:0.N-1]{
    int idCabina;
    text ticket;

    send solicitarCabina(id);
    receive obtenerCabina[id](idCabina);
    usarCabina(idCabina);
    send pagarEmpleado(id, idCabina);
    receive obtenerTicket[id](ticket);
}

B
//Igual que el A, pero cambiando el orden del if y no haciendolo deterministico, primero se fija si alguien termino y dps recien si alguien esta solicitando alguna cabina

chan solicitarCabina(int);
chan obtenerCabina[C](int);

chan pagarEmpleado(int, int);
chan obtenerTicket[C](int);


Process Empleado{
    bool cabinasOcupadas[10] = ([10] false);
    int idCliente;
    int idCabinaLibre;
    text ticket;

    while (true){
        //para evitar generar B y que se quede iterando en el while, se puede hacer un receive(hayPedido()), y se queda esperando a que haya un pedido, entonces alguna de las dos condiciones sera True
        if (!empty(pagarEmpleado)){
            receive pagarEmpleado(idCliente,idCabinaLibre);
            ticket = Cobrar(idCliente,idCabinaLibre);
            cabinasOcupadas[idCabinaLibre] = false;
            send obtenerTicket[idCliente](ticket);
        }
        else{
            if (!empty(solicitarCabina) && (hayCabinasLibres(cabinasOcupadas))){
                receive solicitarCabina(idCliente);
                idCabinaLibre = obtenerCabinaLibre(cabinasOcupadas);
                cabinasOcupadas[idCabinaLibre] = true;
                send obtenerCabina[idCliente](idCabinaLibre);
            }
        }    
    }
}

Process Cliente[id:0.N-1]{
    int idCabina;
    text ticket;

    send solicitarCabina(id);
    receive obtenerCabina[id](idCabina);
    usarCabina(idCabina);
    send pagarEmpleado(id, idCabina);
    receive obtenerTicket[id](ticket);
}