En un entrenamiento de fútbol hay 20 jugadores que forman 4 equipos (cada jugador conoce
el equipo al cual pertenece llamando a la función DarEquipo()). Cuando un equipo está listo
(han llegado los 5 jugadores que lo componen), debe enfrentarse a otro equipo que también
esté listo (los dos primeros equipos en juntarse juegan en la cancha 1, y los otros dos equipos
juegan en la cancha 2). Una vez que el equipo conoce la cancha en la que juega, sus jugadores
se dirigen a ella. Cuando los 10 jugadores del partido llegaron a la cancha comienza el partido,
juegan durante 50 minutos, y al terminar todos los jugadores del partido se retiran (no es
necesario que se esperen para salir).

Process Jugador[id:1.20]{
    int nroEquipo;
    int canchaId;

    Jugar.elegirEquipo(nroEquipo); //se les asigna el equipo de a uno a la vez
    Equipo[nroEquipo].listo(canchaId);
    Cancha[canchaId].llegada();
}

Process Partido[id:0.1]{
    Cancha[id].iniciar();
    //delay(50 min);
    Cancha[id].terminar();
}

Monitor Jugar{
    Procedure elegirEquipo(int nro: out){
        nro = DarEquipo();
    }
}

Monitor Equipo[id:1.4]{
    int cant = 0;
    int cancha;
    cond esperarCompañeros;

    Procedure listo(int canchaId:out){
        cant++;
        if(cant < 5){
            wait(esperarCompañeros);
        }
        else{
            Administrador.calcularCancha(cancha); //el ultimo jugador del equipo en llegar le avisa al administrador que ya llego y le solicita cancha
            signal_all(esperarCompañeros);
        }
        canchaId = cancha; //esto se pone afuera del if y el else porque todos lo tienen que recibir
    }
}

Monitor Administrador{
    int totalFormados = 0;

    Procedure calcularCancha(int cancha:out){
        totalFormados++;
        if (totalFormados <= 2){
            cancha = 1;
        }
        else{
            cancha = 2;
        }
    }
}

Monitor Cancha[id:0.1]{
    cond espera, inicio;
    int cantJugadores = 0;

    Procedure llegada(){
        cantJugadores++;
        if (cantJugadores <10){
            wait(espera);
        }
        else{
            signal(inicio);
        }
    }

    Procedure iniciar(){
        if (cantJugadores < 10){
            wait(inicio);
        }
    }

    Procedure terminar(){
        signal_all(espera);
    }
}