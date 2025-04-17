Resolver el siguiente problema. En una montaña hay 30 escaladores que en una parte de la subida
deben utilizar un único paso de a uno a la vez y de acuerdo con el orden de llegada al mismo. Nota:
sólo se pueden utilizar procesos que representen a los escaladores; cada escalador usa sólo una vez
el paso.

Process Escalador[id:1.30]{
    Paso.llegada();
    //escala
    Paso.finalizado();
}

Monitor Paso{
    cond escaladores;
    boolean pasoLibre = true;
    int esperando = 0;

    Procedure llegada(){
        if(pasoLibre){
            pasoLibre = false;
        }
        else{
            esperando++; //como no se puede usar funcion empty
            wait(escaladores);
        }
    }

    Procedure finalizado(){
        if (esperando > 0){
            esperando--;
            signal(escaladores);
        }
        else{
            pasoLibre = true;
        }
    }
}