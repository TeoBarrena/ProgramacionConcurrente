/*
    Suponga que existe un antivirus distribuido que se compone de R procesos robots
    Examinadores y 1 proceso Analizador. Los procesos Examinadores están buscando
    continuamente posibles sitios web infectados; cada vez que encuentran uno avisan la
    dirección y luego continúan buscando. El proceso Analizador se encarga de hacer todas las
    pruebas necesarias con cada uno de los sitios encontrados por los robots para determinar si
    están o no infectados.
        a) Analice el problema y defina qué procesos, recursos y comunicaciones serán
        necesarios/convenientes para resolverlo.
        b) Implemente una solución con PMS sin tener en cuenta el orden de los pedidos.
        c) Modifique el inciso (b) para que el Analizador resuelva los pedidos en el orden
        en que se hicieron.

 */

B
//sin importar el orden de los pedidos

Process Examinador[id:1.R]{
    text web;

    while (true){
        web = buscarWeb();
        Analizador!webInfectada(web);
    }

}

Process Analizador{
    text web;

    while (true){
        Examinador?webInfectada(web);
        examinarWeb(web);
    }
}

C
//donde el orden si importa
//en este caso no se le pasa el id del Examinador porque no interesa en este caso, ya que el Analizador no le tiene que devolver ningun mensaje a nadie
Process Examinador[id:1.R]{
    text web;

    while(true){
        web = buscarWeb();
        Admin!aviso(web);
    }
}

Process Admin{
    cola avisos, text web;
    
    do Examinador[*]?aviso(web) -> push(avisos,(web));
    [] not empty(avisos); Analizador?pedido() -> Analizador!aviso(avisos.pop());
    od
}

Process Analizador{
    text web;
    while (true){
        Admin!pedido(); //le avisa al admin que ya esta listo para recibir mensajes
        Admin?aviso(web);
        examinarWeb(web);
    }
}