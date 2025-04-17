/*
En una exposición aeronáutica hay un simulador de vuelo (que debe ser usado con
exclusión mutua) y un empleado encargado de administrar su uso. Hay P personas que
esperan a que el empleado lo deje acceder al simulador, lo usa por un rato y se retira.
    a) Implemente una solución donde el empleado sólo se ocupa de garantizar la exclusión
    mutua.
    b) Modifique la solución anterior para que el empleado considere el orden de llegada para
    dar acceso al simulador.
    Nota: cada persona usa sólo una vez el simulador.
*/



A
//en B ya el empleado se encarga de garantizar la exclusión mutua


B

Process Persona[id:1..P]{

    Empleado!solicitarUso(id);
    Empleado?pasar(id);
    usarSimulador();
    Empleado!liberarUso(id);
}

Process Empleado{
    cola personas;
    libre: boolean = libre;

    while(true){
        do Persona[*]?solicitarUso(id) -> 
            if(libre){
                libre = false;
                Persona[id]!pasar();
            }
            else{
                personas.push(id);
            }
        [] Persona[*]?liberarUso(id) ->
            if (empty(personas)){
                libre = true;
            }
            else{
                Persona[personas.pop()]!pasar();
            }
        od
    }

}

