a)En una estación de trenes, asisten P personas que deben realizar una carga de su tarjeta SUBE
en la terminal disponible. La terminal es utilizada en forma exclusiva por cada persona de acuerdo
con el orden de llegada. Implemente una solución utilizando únicamente procesos Persona. Nota:
la función UsarTerminal() le permite cargar la SUBE en la terminal disponible. 

colaFifo cola;
sem accesoCola = 1;
boolean terminalLibre = true;
sem turnos[P] = ([P] 0);

Process Persona[id:1.P]{
    P(accesoCola);
    if(terminalLibre){ //si la terminal esta libre indico que la voy a usar
        terminalLibre=false;
        V(accesoCola);
    }
    else{ //si la terminal no esta libre
        cola.push(id);
        V(accesoCola); //permito que otro acceda
        P(turnos[id]); //me duermo esperando a tener acceso
    }
    UsarTerminal(); //uso la terminal
    P(accesoCola);
    if(cola.isEmpty()){
        terminalLibre = true;
    }
    else{
        V(turnos[cola.pop()]); //despierto a la persona que estaba en la cola
    }
    V(accesoCola);
}

b)Resuelva el mismo problema anterior pero ahora considerando que hay T terminales disponibles.
Las personas realizan una única fila y la carga la realizan en la primera terminal que se libera.
Recuerde que sólo debe emplear procesos Persona. Nota: la función UsarTerminal(t) le permite
cargar la SUBE en la terminal t.

sem persona[T] = ([T] 0);
int terminalAsignada[T];
boolean [T] terminalLibre = ([T] true); 
sem accesoCola = 1;
colaFifo cola;

Process Persona[id:1.P]{
    int idTerminalLibre = -1;
    id sigPersona;

    P(accesoCola);
    for (i=1 to T){
        if (terminalLibre[i]){
            idTerminalLibre = i; //si encontre la terminal libre la asigno a mi var local
            break
        }
    }
    if (idTerminalLibre != -1){ //si es distinto de menos 1 es que encontre una terminal libre
        terminalLibre[idTerminalLibre] = false; //pongo esa terminal en false
        V(accesoCola); //libero el semaforo
    }
    else{ //si no encontre ninguna terminal libre
        cola.push(id); //me encolo
        V(accesoCola);
        P(persona[id]); //me duermo hasta que alla una terminal libre
        idTerminalLibre = terminalAsignada[id]; //si llegue aca es xq idTerminalLibre = -1, entonces necesito que alguien me asigne la terminal libre
    }
    UsarTerminal();
    P(accesoCola);
    if (cola.isEmpty()){ //nadie esta esperando por que se libere una terminal
        terminalLibre[idTerminalLibre] = true; //libero la terminal
        V(accesoCola);
    }
    else{
        sigPersona = cola.pop();
        V(accesoCola);//como ya tome el elemento de la cola puedo liberar
        terminalAsignada[sigPersona] = idTerminalLibre; //a la siguiente persona en la cola le asigno mi idTerminalLibre
        V(persona[sigPersona]); //despierto a la persona        
    }
}


