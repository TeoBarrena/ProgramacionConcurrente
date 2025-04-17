Implemente una solución para el siguiente problema. Se debe simular el uso de una máquina
expendedora de gaseosas con capacidad para 100 latas por parte de U accesoCola. Además, existe un
repositor encargado de reponer las latas de la máquina. Los accesoCola usan la máquina según el orden
de llegada. Cuando les toca usarla, sacan una lata y luego se retiran. En el caso de que la máquina se
quede sin latas, entonces le debe avisar al repositor para que cargue nuevamente la máquina en forma
completa. Luego de la recarga, saca una botella y se retira. Nota: maximizar la concurrencia; mientras
se reponen las latas se debe permitir que otros accesoCola puedan agregarse a la fila.


int botellas = 100;
boolean libre = true;
ColaFifo cola;
sem accesoCola = 1;
sem espera[id] = ([id] 0);
sem empezaReponer = 0;
sem finalizoReponer = 0;

process Usuario[id:1..U]{
    int idSig;
    P(accesoCola);
    if (!libre){
        cola.push(id);
        V(accesoCola);
        P(espera[id]);
    }
    else{
        libre = false;
        V(accesoCola);
    }

    P(contadorBotellas);
    if(botellas == 0){
        V(empezaReponer); //le digo al repositor que reponga
        P(finalizoReponer);
    }
    //tomar lata
    botellas--;
    V(contadorBotellas);

    P(accesoCola);
    if(cola.isEmpty()){
        libre = true;
    }
    else{
        idSig = cola.pop();
        V(espera[idSig]);
    }
    V(accesoCola);
}

process Repositor{
    while(true){
        P(empezaReponer);
        botellas = 100;
        V(finalizoReponer);
    }
}