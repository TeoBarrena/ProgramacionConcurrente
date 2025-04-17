/*
En una oficina existen 100 empleados que envían documentos para imprimir en 5 impresoras
compartidas. Los pedidos de impresión son procesados por orden de llegada y se asignan a la primera
impresora que se encuentre libre:
a) Implemente un programa que permita resolver el problema anterior usando PMA.
b) Resuelva el mismo problema anterior pero ahora usando PMS.
 */

A) PMA

chan hayPedido(); //para avisarle al admin que hay alguien esperando por una impresora

chan buscarImpresora(int);
chan obtenerImpresora[100](int);

chan liberarImpresora(int);

chan colaImpresora[5](int,text);

chan recibirDocumentos[100](text);

Process Empleado[id:1..100]{
    int idImpresora;
    text documentos;

    while(true){
        documentos = generarDocumentos();
        send buscarImpresora(id); //le avisa al admin que necesita una impresora
        send hayPedido(true);
        receive obtenerImpresora[id](idImpresora); //se queda esperando a que el admin le asigne una impresora
        //ya sabe a que impresora ir
        send colaImpresora[idImpresora](id, documentos); //se encola en la cola de la impresora
        receive recibirDocumentos[id](documentos); //se queda esperando a que la impresora le devuelva los documentos
        send liberarImpresora(idImpresora); //le avisa al admin que ya termino de imprimir
        send hayPedido(true); //le avisa al admin que ya termino de imprimir
    }
}

Process Impresora[id:1..5]{
    int idPersona;
    text documentos;

    while(true){
        receive colaImpresora[id](idPersona, documentos); //espera a que le llegue un pedido
        documentos = imprimirDocumentos(documentos);
        send recibirDocumentos[idPersona](documentos); //le envia los documentos a la persona
    }
}

//alTERNATIVA
//ESTE QUEDO BIEN
Process Admin{

    boolean impLibres = [5] = ([5] true); //inicializo todas las impresoras como libres);
    bool pedido;
    idImpresora;

    while(true){
        receive hayPedido(); //hasta que alguien no le solicite una impresora o que alguien quiere liberar una impresora no arranca
        if(!empty(liberarImpresora)){ //si no esta vacio el canal para liberar impresoras
            receive liberarImpresora(idImpresora);
            impLibres[idImpresora] = true;
        }
        
        else{
            if (!empty(buscarImpresora)){
                receive buscarImpresora(idPersona);
                if (hayImpLibres(impLibres))
                    int idImpresora = buscarImpLibre(impLibres);
                    impLibres[idImpresora] = false; 
                else
                    receive liberarImpresora(idImpresora); //se bloquea hasta que alguein libere, y le pasa esa impresora liberada a la persona
                send obtenerImpresora[idPersona](idImpresora); //le envia a la persona la impresora que le toco
            }    
        }
    }
}


/*Process Admin{ ESTA FUE LA PRIMER SOLUCION QUE HICE, ESTA MAL

    boolean impLibres = [5] = ([5] true); //inicializo todas las impresoras como libres);
    bool pedido;
    idImpresora;

    while(true){
        receive hayPedido(pedido); //hasta que alguien no le solicite una impresora o que alguien quiere liberar una impresora no arranca
        if (!empty(buscarImpresora) && empty(liberarImpresora) && ){
            receive buscarImpresora(idPersona);
            if (hayImpLibres(impLibres))
                int idImpresora = buscarImpLibre(impLibres);
                impLibres[idImpresora] = false; 
            else
                receive liberarImpresora(idImpresora); //se bloquea hasta que alguein libere, y le pasa esa impresora liberada a la persona
            send obtenerImpresora[idPersona](idImpresora); //le envia a la persona la impresora que le toco
        }
        else{
            if(!empty(liberarImpresora)){ //si no esta vacio el canal para liberar impresoras
                receive liberarImpresora(idImpresora);
                impLibres[idImpresora] = true;
            }
        }
    }
}
*/





B) PMS


Process Impresora[id:0..4]{
    text documentos;
    while(true){
        Admin!pedido(id);//le avisas que estas listo para recibir un pedido
        Admin?imprimir(idPersona, documentos);
        documentos = imprimirDocumentos(documentos);
        Persona[idPersona]!recibirDocumentos(documentos); //le envia a la persona los documentos impresos
    }
}

Process Persona[id:0..99]{
    documentos: text;
    while(true){
        documentos = generarDocumentos();
        Admin!enviarDocumento(id, documentos);
        Impresora[*]?recibirDocumentos(documentos); //se queda esperando que la impresora le devuelva los documentos
    }
}

Process Admin{
    int idP;
    int idI;

    cola personas; //cola que guarda id y documentos de una persona

    do Persona[*]?enviarDocumento(idP, documentos) -> {
        personas.push(idP, documentos); //se encola en la cola personas hasta que una impresora le pida al admin una persona, es decir cuando la impresora este libre
    }
    [] not empty(personas) Impresora[*]?pedido(idI) -> {
        Impresora[idI]!imprimir(personas.pop()); //una impresora se encuentra libre, entonces solicita el pedido, y el admin le envia la persona
    }

}
