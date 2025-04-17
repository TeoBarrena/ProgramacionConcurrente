/*
    En un estadio de fútbol hay una máquina expendedora de gaseosas que debe ser usada por
    E Espectadores de acuerdo con el orden de llegada. Cuando el espectador accede a la
    máquina en su turno usa la máquina y luego se retira para dejar al siguiente. Nota: cada
    Espectador una sólo una vez la máquina
*/


//CONSULTAR: aca no pide maximizar la concurrecia, pero si fuera asi se tendria que hacer un proceso Admin que administre la cola de los espectadores que quieren usar la maquina?
Process Espectador[id:0.E]{
    text gaseosa;

    Maquina!solicitarUso(id); //solicita el uso
    Maquina?dejarUsar(); //espera a que la maquina le conceda el uso
    gaseosa = usarMaquina();
    Maquina!liberarUso(); //deja libre la maquina
}

Process Maquina{
    cola Fila;
    int idE;
    boolean libre = true;

    do Espectador[*]?solicitarUso(idE) ->
                                        if (libre){
                                            libre = false;
                                            Espectador[idE]!dejarUsar();
                                        }
                                        else{
                                            Fila.push(idE);
                                        }
    [] Espectador[*]?liberarUso() ->
                                   if (empty(fila)){ //si la fila esta vacía
                                        libre = true;
                                   }
                                   else{
                                        idE = Fila.pop();
                                        Espectador[idE]!dejarUsar(); //despierto al Espectador que quiere usar la máquina
                                   }
}