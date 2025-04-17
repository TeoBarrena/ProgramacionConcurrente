/*Se desea modelar el funcionamiento de un banco en el cual existen 5 cajas para realizar
pagos. Existen P clientes que desean hacer un pago. Para esto, cada una selecciona la caja
donde hay menos personas esperando; una vez seleccionada, espera a ser atendido. En cada
caja, los clientes son atendidos por orden de llegada por los cajeros. Luego del pago, se les
entrega un comprobante. Nota: maximizar la concurrencia.*/

chan colaCajero[5](int,text); //cada cajero tiene su orden de llegada

chan comprobante[N](text); //tiene que haber un canal de request y otro de response, este seria el de response

chan hayPedido(bool); //esta para el admin
chan buscarCaja(int);//aca mandan cuando llegan para avisar que estan esperando que se les asigne una caja
chan obtenerCaja[N](int); //aca se quedan esperando hasta que se les asigne una caja

chan liberarCaja(int);

Process Persona[id:0.N]{
    int idCaja;
    text pago;
    text comprobante;

    send buscarCaja(id);//tiene que ir antes del hayPedido, xq si se llega a dar el procesador a otro proceso, puede haber errores
    send hayPedido();
    receive obtenerCaja[id](idCaja); //aca se queda esperando q le asignen el id de la caja
    //aca ya sabe a que cajero ir
    send colaCajero[idCaja](id,pago); //se encola en la coja del cajero respectivo
    receive comprobante[id](comprobante); //se queda esperando a que le den el comprobante
    send liberarCaja(idCaja); //tiene que ir antes del hayPedido, xq si se llega a dar el procesador a otro proceso, puede haber errores
    send hayPedido(); //el admin puede estar esperando o que llegue alguien o que alguien quiera liberar un cajero, entonces le aviso que llegue

}

Process Cajero[id:0.4]{
    int idAux;
    text pago;
    text comprobante;

    while (true){
        receive colaCajero[id](idAux,pago); //espera a que llegue una persona
        generarComprobante(pago, comprobante);
        send comprobante[idAux](comprobante); //le envia el comprobante a la persona q esperaba
    }
}

Process Admin{
    int cantEspera[5] = ([5] 0); 
    int idPersona;
    int min;
    int idCajero;
    bool pedido;

    while(true){
        receive hayPedido();
        if (!empty (buscarCaja) && empty(liberarCaja)){//si alguien busca caja y nadie quiere liberar una caja entra
            receive buscarCaja(idPersona);
            min = cajeroMasVacio(cantEspera);
            cantEspera[min]++;
            send obtenerCaja[id](min); //le envia a la persona el id del cajero con menos gent
        } 
        else{
            if (!empty(liberarCaja)){ //si alguien quiere liberar una caja
                receive liberarCaja(idCajero);
                cantEspera[idCajero]--;
            }
        }
    }


}