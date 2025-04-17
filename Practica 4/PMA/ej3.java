/*
     Se debe modelar el funcionamiento de una casa de comida rápida, en la cual trabajan 2
    cocineros y 3 vendedores, y que debe atender a C clientes. El modelado debe considerar
    que:
        - Cada cliente realiza un pedido y luego espera a que se lo entreguen.
        - Los pedidos que hacen los clientes son tomados por cualquiera de los vendedores y se
        lo pasan a los cocineros para que realicen el plato. Cuando no hay pedidos para atender,
        los vendedores aprovechan para reponer un pack de bebidas de la heladera (tardan entre
        1 y 3 minutos para hacer esto).
        - Repetidamente cada cocinero toma un pedido pendiente dejado por los vendedores, lo
        cocina y se lo entrega directamente al cliente correspondiente.
    Nota: maximizar la concurrencia.
 */

chan realizaPedido(text, int); //el cliente realiza el pedi
chan obtenerPlato[C](text); //se queda esperando a que le entreguen el plato

chan obtenerPedido(int); //este es para que el empleado avise al coordinador que quiere recibir un cliente o en caso de no haber clientes realiza otra tarea 
chan retornarPedido[3](text, int); //Siguiente de la explicacion practica

chan pedidoPendiente(text,int);

Process Cliente[id:0.C]{
    text plato;
    text pedido; 

    send realizaPedido(pedido,id);
    receive obtenerPlato[id](plato);
}

Process Coordinador{ //este esta porque los vendedores tienen que reponer stock si no hay nadie
    text plato;
    int idCliente;
    int idVendedor;
    while(true){
        receive obtenerPedido (idVendedor);
        if (empty(realizaPedido)){
            idCliente = -1;
            pedido = 'vacio';
        }
        else{
            receive realizaPedido(plato, idCliente);
        }
        send retornarPedido[idVendedor](pedido,idCliente); //Seria el Siguiente de la expl. práctica
    }
}

Process Vendedor[id:0.2]{
    text pedido;
    int idCliente;

    while(true){
        send obtenerPedido(id);
        receive retornarPedido[id](pedido, idCliente);
        if (idCliente <> -1){
            send pedidoPendiente(pedido, idCliente);
        }
        else{
            delay(60..180);
        }
    }
}

Process Cocinero[id:0.1]{
    int idCliente;
    text pedido;
    text plato;

    while(true){
        receive pedidoPendiente(pedido,idCliente);
        plato = cocinarPlato(pedido);
        send obtenerPlato[idCliente](plato);
    }
}