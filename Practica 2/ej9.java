9. Resolver el funcionamiento en una fábrica de ventanas con 7 empleados (4 carpinteros, 1
vidriero y 2 armadores) que trabajan de la siguiente manera:
• Los carpinteros continuamente hacen marcos (cada marco es armando por un único
carpintero) y los deja en un depósito con capacidad de almacenar 30 marcos.
• El vidriero continuamente hace vidrios y los deja en otro depósito con capacidad para
50 vidrios.
• Los armadores continuamente toman un marco y un vidrio (en ese orden) de los
depósitos correspondientes y arman la ventana (cada ventana es armada por un único
armador).

colaMarcos cM;
colaVidrios cV;
colaVentanas cVentanas;
sem mutexMarco = 1;
sem mutexVidrio = 1;
sem capacidad_marcos=30;
sem capacidad_vidrios=50;
sem hayMarco=0;
sem hayVidrio=0;
sem mutexArmador = 1;


process Carpintero[id=0..3]{
    Marco marco;
    while(true){
        P(capacidad_marcos);
        marco = armarMarco();
        P(mutexMarco); //para verificar que no esta utilizando esa cola tambien un Armador
        cM.push(marco);
        V(mutexMarco);
        V(hayMarco);
    }
}

process Vidriero{
    Vidrio vidrio;
    while(true){
        P(capacidad_vidrios);
        vidrio = armarVidrio();
        P(mutexVidrio); //para verificar que no esta usando esta cola un Armador
        cV.push(vidrio);
        V(mutexVidrio);
        V(hayVidrio);
    }
}

process Armador[id=0..1]{
    Marco marco; Vidrio vidrio; Ventana ventana;
    while(true){
        P(hayMarco);
        P(mutexMarco);
        marco = cM.pop();
        V(mutexMarco);
        V(capacidad_marcos);
        P(hayVidrio);
        P(mutexVidrio);
        vidrio = cV.pop();
        V(mutexVidrio);
        V(capacidad_vidrios);
        ventana = armarVentana(marco,vidrio);
        P(mutexArmador);
        cVentanas.push(ventana);
        V(mutexArmador);
    }
}
