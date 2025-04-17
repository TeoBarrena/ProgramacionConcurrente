Simular la atención en una Terminal de Micros que posee 3 puestos para hisopar a 150
pasajeros. En cada puesto hay una Enfermera que atiende a los pasajeros de acuerdo
con el orden de llegada al mismo. Cuando llega un pasajero se dirige al Recepcionista,
quien le indica qué puesto es el que tiene menos gente esperando. Luego se dirige al puesto
y espera a que la enfermera correspondiente lo llame para hisoparlo. Finalmente, se retira.
a) Implemente una solución considerando los procesos Pasajeros, Enfermera y
Recepcionista.


sem mutexColasPuestos = 1; //para poder acceder a las colas
sem despiertoRecepcionista = 0; //para despertar al recepcionista
sem esperoPuesto[150] = ([150] 0); //para que se duerman los pasajeros hasta que se les asigne un puesto
int buffer bufferPuesto[150]; //a cada pasajero se le guarda en la posicion correspondiente a su ID el puesto al que debe ir a atenderse
cola colaRecepcionista; //encolar pasajeros en la cola del recepcionista
sem mutexColaRecepcionista = 1; //semaforo para excl. mutua
cola colasEspera[3]; //array de puestos
sem mutexColaEspera[3] = ([3] 1);
sem llena[3] = ([3] 0); //para avisar a cada enfermera que estoy esperando

sem espero_hisopado[150] = ([150] 0);

sem mutexHisopado = 1;
int hisopados = 0;

process Pasajeros[id:0..149]{
    int puesto;

    P(mutexColaRecepcionista); //para encolar en cola recepcionista
    colaRecepcionista.push(id); //pasajero encola su id
    V(despiertoRecepcionista); //despierta al recepcionista, se podria intercambiar por lal inea de abajo
    V(mutexColaRecepcionista); //libera por si otro se quiere encolar

    P(esperoPuesto[id]); //se duerme hasta que le asignen un puesto
    puesto = bufferPuesto[id]; //obtengo el puesto al que debo ir

    //P(colaEspera[puesto]); //bloqueo la cola del puesto correspondiente
    P(mutexColasPuestos); //esto es para que el proceso recepcionista no intente acceder al puestoMenosGente hasta que se actualice con el nuevo pasajero encolado
    colasEspera[puesto].push() //me encolo en la cola del puesto correspondiente
    V(mutexColasPuestos); //libero el uso 
    //V(colaEspera[puesto]); //libero por si otra persona tiene que usar esa cola

    V(llena[puesto]); //le aviso a la enfermera que me encole y estoy esperandola

    P(espero_hisopado[id]); //me duermo esperando que me hisopen 
}

process Enfermera[id: 0..2]{
    int aux;
    while (hisopados < 150){
        P(llena[id]); //espero a que alguien me despierte
        if (!colasEspera[id].isEmpty()){
            P(mutexColasPuestos); //asi no se encola otro pasajero en mi puesto
            aux = colaEspera[id].pop();//obtengo el id del pasajero
            V(mutexColasPuestos); //libero asi otro se puede encolar

            Hisopar(aux); //hisopo al pasajero aux
            V(espero_hisopado[aux]); //despierta al pasajero dps de haberlo hisopado

            P(mutexHisopado); //asi otra enfermera no modifica hisopados a la vez
            hisopados++;
            if (hisopados == 150){
                for j = 0..2{
                    V(llena[j]); //esto se da porque al finalizar de pasar los pasajeros podria darse que una enfermera quedo dormida en P(llena[id]),
                                 //entonces la ultima despierta a todas por las dudas
                }   
            }
            V(mutexHisopado);
        }   
    }
}

process Recepcionista{
    int puesto, i, aux; //aux sirve para identificar al pasajero
    for i:= 1 to 150 {
        P(despiertoRecepcionista); //se despierta el recepcionista
        P(mutexColaRecepcionista); //asi no se encola nadie en ese momento, es necesario
        aux = colaRecepcionista.pop();
        V(mutexColaRecepcionista); //libero uso de la cola
        P(mutexColasPuestos); //este semaforo es para que al momento de analizar el puesto con menos gente, no se modifique ese tamaño, CONSULTAR
        puesto = puestoMenosGente(colasEspera);
        V(mutexColasPuestos); //libero el uso del semaforo
        bufferPuesto[aux] = puesto; //le asigno al Pasajero en el buffer puesto en la posicion correspondiente a su ID el puesto al que debe ir
        V(esperoPuesto[aux]);  //despierto al Pasajero
    }
}


//poetry black .