/*
Resolver el siguiente problema con PMA. En un negocio de cobros digitales hay P personas que
deben pasar por la única caja de cobros para realizar el pago de sus boletas. Las personas son
atendidas de acuerdo con el orden de llegada, teniendo prioridad aquellos que deben pagar menos
de 5 boletas de los que pagan más. Adicionalmente, las personas embarazadas tienen prioridad sobre
los dos casos anteriores. Las personas entregan sus boletas al cajero y el dinero de pago; el cajero les
devuelve el vuelto y los recibos de pago.
*/

chan embarazadas;
chan menosDe5Boletas;
chan esperarRecibos[P](float, text);
chan masDe5Boletas; 

Process Persona[id:0..P-1]{
    embarazada: boolean;
    cantBoletas:int;
    boletas: text;
    dinero: float;

    if (embarazada){
        send embarazadas(id, boletas, dinero);
    }
    else if (cantBoletas < 5){
        send menosDe5Boletas(id, boletas, dinero);
    }
    else{
        send masDe5Boletas(id, boletas, dinero);
    }
    receive esperarRecibos[id](dinero, recibos); //se queda esperando que el cajero les devuelva el vuelto y los recibos de pago
}

Process CajaCobros{

    id:int;
    ok = boolean;
    boletas: text;
    dinero: float;

    while(true){
        ok = false;
        if (!empty(embarazada)) ->{ //si no esta vacio el embarazada, se usa el if porque con un do es aleatorio, no se puede manejar el tema de la prioridad
            receive embarazadas(id, boletas, dinero);
            ok = true;
        }
        else if (!empty(masDe5Boletas)) ->{
            receive masDe5Boletas(id, boletas, dinero);
            ok = true;
        }
        else if (!empty(menosDe5Boletas)) ->{
            receive menosDe5Boletas(id, boletas, dinero);
            ok = true;
        }
        if(ok){ //manejo de variable booleana para en el caso de que los 3 canales estuviesen vacio, no se ejecuta el bloque este
            generarRecibos(dinero, recibos); //genera los recibos de pago y en dinero se devuelve el valor del vuelto
            send esperarRecibos[id](dinero, recibos); //le envia los recibos de pago y el vuelto a la persona
        }
    }
}

------------------

embarazadas;
chan menosDe5Boletas;
chan esperarRecibos[P](float, text);
chan masDe5Boletas; 

Process Persona[id:0..P-1]{
    embarazada: boolean;
    cantBoletas:int;
    boletas: text;
    dinero: float;

    if (embarazada){
        send embarazadas(id, boletas, dinero);
    }
    else if (cantBoletas < 5){
        send menosDe5Boletas(id, boletas, dinero);
    }
    else{
        send masDe5Boletas(id, boletas, dinero);
    }
    send pedido()
    receive esperarRecibos[id](dinero, recibos); //se queda esperando que el cajero les devuelva el vuelto y los recibos de pago
}

Process CajaCobros{

    id:int;
    ok = boolean;
    boletas: text;
    dinero: float;
    recibos:text;

    while(true){
        receive pedido() //aca para no generar Busy waiting
        if (!empty(embarazada)) ->{ //si no esta vacio el embarazada, se usa el if porque con un do es aleatorio, no se puede manejar el tema de la prioridad
            receive embarazadas(id, boletas, dinero);
        }
        else if (!empty(masDe5Boletas)) ->{
            receive masDe5Boletas(id, boletas, dinero);
        }
        else if (!empty(menosDe5Boletas)) ->{
            receive menosDe5Boletas(id, boletas, dinero);
        }          
        recibos = generarRecibos(dinero, boletas); //genera los recibos de pago y en dinero se devuelve el valor del vuelto
        send esperarRecibos[id](dinero, recibos); //le envia los recibos de pago y el vuelto a la persona
        }
    }
}