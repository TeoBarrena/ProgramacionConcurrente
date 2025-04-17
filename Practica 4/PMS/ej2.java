/**
    En un laboratorio de genética veterinaria hay 3 empleados. El primero de ellos
    continuamente prepara las muestras de ADN; cada vez que termina, se la envía al segundo
    empleado y vuelve a su trabajo. El segundo empleado toma cada muestra de ADN
    preparada, arma el set de análisis que se deben realizar con ella y espera el resultado para
    archivarlo. Por último, el tercer empleado se encarga de realizar el análisis y devolverle el
    resultado al segundo empleado. 
 */


//se necesita una cola de muestras de ADN para que no se frene el empleado 1 por estar esperando que el Empleado 2 reciba las muestras
Process Empleado1{
    text muestra;

    while (true){
        muestra = prepararMuestra();
        Admin!muestra(muestra);
    }
}

//tiene que agarrar de la cola de muestras de Admin
Process Empleado2{
    text muestra;
    text set;
    text resultado;

    while(true){
        Admin!pedido(); //avisas que llegaste
        Admin?tomarMuestra(muestra);
        set = armarSet(muestra);
        Empleado3!enviarSet(set);
        Empleado3?esperarResultado(resultado);
        archivarAnalisis(resultado);
    }
}

Process Empleado3{
    text set;
    text = resultado;
    
    while(true){
        Empleado2?enviarSet(set);
        resultado = realizarAnalisis(set);
        Empleado2!esperarResultado(resultado);
    }
}

Process Admin{
    cola muestras;
    text muest;

    do Empleado1?muestra(muest) -> muestras.push(muest);
    [] not empty(muestras); Empleado2?pedido() -> Empleado2!tomarMuestra(muestras.pop());
    od
}