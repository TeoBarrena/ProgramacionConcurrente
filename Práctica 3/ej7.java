Se debe simular una maratón con C corredores donde en la llegada hay UNA máquina
expendedoras de agua con capacidad para 20 botellas. Además, existe un repositor encargado
de reponer las botellas de la máquina. Cuando los C corredores han llegado al inicio comienza
la carrera. Cuando un corredor termina la carrera se dirigen a la máquina expendedora, espera
su turno (respetando el orden de llegada), saca una botella y se retira. Si encuentra la máquina
sin botellas, le avisa al repositor para que cargue nuevamente la máquina con 20 botellas;
espera a que se haga la recarga; saca una botella y se retira. Nota: mientras se reponen las
botellas se debe permitir que otros corredores se encolen. //por esta nota es que se hace el encolamiento en el monitor carrera

Process Corredor[1..C]{

    Carrera.llegada();
    //corre carrera
    Carrera.accesoMaquina();
    Maquina.usar();
    Carrera.dejarMaquina();
}


Process Repositor(){
    while (true){
        Maquina.reponerAgua();
    }
}

Monitor Carrera{
    cond corredores;
    cond usarMaquina;
    
    boolean libre = true;

    int cant = 0;
    int espera = 0;

    Procedure llegada(){
        cant++;
        if (cant == C){
            signal_all(corredores);
        }
        else{
            wait(corredores);
        }
    }

    Procedure accesoMaquina(){
        if (!libre){ //esto para asegurar que no venga un proceso y se cole en la fila de los que estan esperando para usar la maquina
            espera++;
            wait(usarMaquina);
        }
        else{
            libre = false;
        }
    }

    Procedure dejarMaquina(){
        if (espera > 0){
            espera--;
            signal(usarMaquina); //despertas al proceso que esta en la cola para usar la maquina
        }
        else{
            libre = true; //si no lo pones en el else, se puede colar alguien en la fila    
        }
    }
}

Monitor Maquina{
    int botellas = 20;
    cond reponer;
    cond esperarBotella;

    Procedure usar(){
        if (botellas == 0){
            signal(reponer);
            wait(esperarBotella);
        }
        botellas--;
    }

    Procedure reponerAgua(){
        if (botellas > 0){
            wait(reponer);
        }
        botellas = 20;
        signal(esperarBotella); //despertas al primero de la fila
    }
}

