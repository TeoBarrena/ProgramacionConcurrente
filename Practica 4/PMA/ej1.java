/*
    Suponga que N clientes llegan a la cola de un banco y que serán atendidos por sus
    empleados. Analice el problema y defina qué procesos, recursos y canales/comunicaciones
    serán necesarios/convenientes para resolverlo. Luego, resuelva considerando las siguientes
    situaciones:
    a. Existe un único empleado, el cual atiende por orden de llegada.
    b. Ídem a) pero considerando que hay 2 empleados para atender, ¿qué debe
    modificarse en la solución anterior?
    c. Ídem b) pero considerando que, si no hay clientes para atender, los empleados
    realizan tareas administrativas durante 15 minutos. ¿Se puede resolver sin usar
    procesos adicionales? ¿Qué consecuencias implicaría?
 */

A

chan colaLlegada(int)

Process Cliente[id:0.N]{
    int id;
    send colaLlegada(id);
}

Process Empleado{
    int id;
    while (true){
        receive colaLlegada(id);
        atender(id)
    }
}

B
//Misma solucion que A, agregando un empleado más

Process Cliente[id:0.N]{
    int id;
    send colaLlegada(id);
}

Process Empleado[id:0.1]{
    int id;
    while (true){
        receive colaLlegada(id);
        atender(id)
    }
}

C
//Se puede resolver sin procesos adicionales pero genera BW

chan colaLlegada(int)
chan Pedido(int) //este es para manejar los Pedidos de los Empleados
chan Siguiente[2](int)

Process Cliente[id:0.N]{
    int id;
    send colaLlegada(id);
}

Process Empleado[id:0.1]{
    int idPersona;
    while (true){
        send Pedido(id); //envias para que te devuelvan el cliente o que no hay nada y podes hacer otra tarea, para evitar BW
        receive Siguiente[id](idPersona);
        if (idPersona <> -1){
            atender(idPersona);
        }
        else{
            delay(900); //si no hay nadie para atender realiza tareas administrativas
        }
    }
}

Process Admin{
    int idE;
    int idPersona;
    while(true){
        receive Pedido(idE); //se fija si hay un idE en el canal Pedido, si hay lo toma, sino espera a que haya uno, para evitar BW
        if (empty(colaLlegada)){
            idPersona = -1;
        }
        else{
            receive colaLlegada(idPersona);
        }

        send Siguiente[idE](idPersona); //le mando al Empleado el id de la persona en caso de que haya, sino le mando -1
    }
}
