Resolver el siguiente problema. En una elección estudiantil, se utiliza una máquina para voto
electrónico. Existen N Personas que votan y una Autoridad de Mesa que les da acceso a la máquina
de acuerdo con el orden de llegada, aunque ancianos y embarazadas tienen prioridad sobre el resto.
La máquina de voto sólo puede ser usada por una persona a la vez. Nota: la función Votar() permite
usar la máquina.


Process Persona[id:1.N]{
    int prioridad;
    
    if(estaEmbarazada || esAnciano){
        prioridad = 0;
    }
    else{
        prioridad = 1;
    }
    Autoridad.llegada(id,prioridad);
    Votar();
    Autoridad.salir();
}

Monitor Autoridad{
    ColaPrioridad cola;
    cond persona[N]; //se utiliza un arreglo de cond, porque tenes que saber especificamente a que persona que esta dormida tenes que despertar, no siempre va a ser el primero
    boolean maquinaLibre = true;

    Procedure llegada(int id,prioridad:in){
        if(!maquinaLibre){
            cola.push(id,prioridad); //asumo que se ordena en la cola automaticamente por la prioridad
            wait(persona[id]);
        }
        else{
            maquinaLibre = false;
        }
    }

    Procedure salir(){
        int idSig;
        if (cola.isEmpty()){ //si nadie esta esperando
            maquinaLibre = true;
        }
        else{
            idSig = cola.pop().id();
            signal(persona[idSig]);
        }
    }
}

