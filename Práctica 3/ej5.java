
En un corralón de materiales se deben atender a N clientes de acuerdo con el orden de llegada.
Cuando un cliente es llamado para ser atendido, entrega una lista con los productos que
comprará, y espera a que alguno de los empleados le entregue el comprobante de la compra
realizada.
a) Resuelva considerando que el corralón tiene un único empleado.
b) Resuelva considerando que el corralón tiene E empleados (E > 1). Los empleados no
deben terminar su ejecución.
c) Modifique la solución (b) considerando que los empleados deben terminar su ejecución
cuando se hayan atendido todos los clientes

a) Resuelva considerando que el corralón tiene un único empleado.

process Cliente[id:1..N]{
    Lista lista;
    Corralon.llegada(lista,comprobante);
}

process Empleado(){
    Comprobante comprobante;
    Lista lista;
    int i;
    for i= 1 to N{
        Corralon.obtenerLista(lista);
        comprobante = comprobarLista(lista);
        Corralon.dejarComprobante(comprobante);
    }
}

Monitor Corralon{

    cond empleado; //para evitar busy waiting
    cond cliente;
    cond datos;
    cond recibiComprobante;
    int esperando;
    text listaCliente;
    text comprobante;

    Procedure llegada(Lista lista in, Comprobante comprobante out){
        signal(empleado);
        esperando++;
        wait(cliente); //el cliente se duerme
        listaCliente = lista;
        signal(datos); //despertas al Empleado avisandole que ya esta la lista
        wait(atencion); //te dormis en la cola atencion, esperando que te entreguen el comprobante
        comprobante = comprobanteEmpleado; 
        esperando--;
        signal(recibiComprobante); 
    }

    Procedure obtenerLista(Lista lista:out){
        if (esperando == 0){
            wait(empleado); //si no hay nadie el empleado se duerme
        }
        signal(cliente); //despertas al cliente
        wait(datos); //te quedas esperando los datos
        lista = listaCliente;
    }

    Procedure dejarComprobante(Comprobante comprobante:in){
        comprobante = comprobante;
        signal(atencion); //despertas al cliente que espera por el comprobante.
        wait(recibiComprobante); //para que el empleado este con un cliente a la vez
    } 

}

b) Resuelva considerando que el corralón tiene E empleados (E > 1). Los empleados no
deben terminar su ejecución.

Process Cliente[id:0..N-1]{
    int idE;
    text comprobante;
    text lista;
    
    Corralon.llegada(idE);
    Escritorio[idE].atencion(lista,comprobante);
}

Process Empleado[id:0..E-1]{
    int j;
    while(true){
        Corralon.proximo(id);
        Escritorio[id].obtenerLista(lista);
        comprobante = comprobar(lista);
        Escritorio[id].dejarComprobante(comprobante);
    }
}

Monitor Corralon{
    int cantLibres = 0;
    int esperando = 0; 
    cola eLibres;
    cond esperaC;

    Procedure llegada (idE: out int){
        if (cantLibres == 0){ //cantLibres referencia cantidad de empleados libres
            esperando++;
            wait(esperaC); //se encola para respetar orden de llegada
        }
        else {
            cantLibres--;
        }
        idE = eLibres.pop(); //obtiene el id del Empleado que lo va a atender
    }

    Procedure proximo(idE: in int){
        eLibres.push(idE); //se encola cuando esta libre
        if (esperando > 0){ //si hay gente esperando despierta al cliente
            esperando--;
            signal(esperaC);
        }
        else {
            cantLibres++; //si no hay nadie esperando, incrementa cantLibres avisando que hay un empleado libre
        }
    }
}

Monitor Escritorio[id:0..E-1]{
    text listaC;
    text compE;
    bool hayDatos = false;
    cond datos;
    cond atencionE;

    Procedure atencion(list: in text; comp: out text){
        listaC = list;
        hayDatos = true; //cada Escritorio pertenece a un Empleado, por ende hayDatos es una variable que pertenece a cada Empleado
        signal(datos); //despierta al empleado avisando que ya entrego la lista
        wait(atencionE); //se duerme esperando la atencion del Empleado
        comp = compE; //guarda el comprobante del Empleado
        signal(datos); //despierta al empleado avisando se fue el cliente y puede atender a otra persona
    }

    Procedure obtenerLista(list: out text){
        if (!hayDatos){ //si no hay datos me duermo esperando esos datos
            wait(datos);
        }
        list = listaC; //guardo la lista
    }

    Procedure dejarComprobante(comp: in text){
        compE = comp; //guardo en conmpE el comprobante
        signal(atencionE); //despierto al cliente informando que ya esta el comprobante 
        wait(datos); //espero que el cliente termine
        hayDatos = false;
    }

}



