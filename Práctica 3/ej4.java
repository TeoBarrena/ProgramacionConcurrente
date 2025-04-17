Existen N vehículos que deben pasar por un puente de acuerdo con el orden de llegada.
Considere que el puente no soporta más de 50000kg y que cada vehículo cuenta con su propio
peso (ningún vehículo supera el peso soportado por el puente). 

Process Vehiculo [id: 0..N-1]{
    int peso;

    Puente.pasar(peso);
    //cruzar puente
    Puente.dejar(peso);
}

Monitor Puente{
    bool libre = true;
    int esperando = 0;
    int acum = 0;
    cond auto;
    cond pesoAdecuado;

    Procedure pasar (peso: in int){
        if (!libre){
            esperando++;
            wait(auto);
        }
        else {
            libre = false;
        }
        while (acum + peso > 50000){
            wait(pesoAdecuado);
        }
        acump += peso;
        if (esperando > 0){
            esperando--;
            signal(auto);
        }
        else {
            libre = true;
        }
    }

    Procedure dejar (peso: in int){
        acum -= peso;
        signal(pesoAdecuado);
    }
}