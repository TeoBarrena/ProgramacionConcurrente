/*
Resolver la administración de 3 impresoras de una oficina. Las impresoras son usadas por N
administrativos, los cuales están continuamente trabajando y cada tanto envían documentos
a imprimir. Cada impresora, cuando está libre, toma un documento y lo imprime, de
acuerdo con el orden de llegada.
    a) Implemente una solución para el problema descrito.
    b) Modifique la solución implementada para que considere la presencia de un director de
    oficina que también usa las impresas, el cual tiene prioridad sobre los administrativos.
    c) Modifique la solución (a) considerando que cada administrativo imprime 10 trabajos y
    que todos los procesos deben terminar su ejecución.
    d) Modifique la solución (b) considerando que tanto el director como cada administrativo
    imprimen 10 trabajos y que todos los procesos deben terminar su ejecución.
    e) Si la solución al ítem d) implica realizar Busy Waiting, modifíquela para evitarlo.
Nota: ni los administrativos ni el director deben esperar a que se imprima el documento
*/



A

chan Pedido(int); //este recibe los pedidos de las impresoras

chan Documentos(text); //canal donde los admin envian los docs

chan Siguiente[3](text); //canal privado a cada impresora para recibir los documentos

Process Impresora[id:1..3]{

    while(true){
        send Pedido(id); //le avisa al coordinador que esta listo para recibir documentos
        receive Siguiente[id](documentos); //se queda esperando a recibir los documentos
        documentos = imprimirDocumentos(documentos);
    }
}

Process Coordinador{
    documentos: text;
    idI: int;

    while(true){
        receive Pedido(idI); //recibe el id de la impresora
        receive Documentos(documentos);//aca se queda esperando a que haya algun documento en el canal Documentos CONSULTAR
        send Siguiente[idI](documentos);
    }
}

Process Administrativo[id:1..N]{
    documentos: text;

    while(true){
        documentos = generarDocumentos();
        send Documentos(documentos);
    }
}

B C Y D HACER