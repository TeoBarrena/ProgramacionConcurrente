Resolver el siguiente problema. En una empresa trabajan 20 vendedores ambulantes que forman 5
equipos de 4 personas cada uno (cada vendedor conoce previamente a qué equipo pertenece). Cada
equipo se encarga de vender un producto diferente. Las personas de un equipo se deben juntar antes
de comenzar a trabajar. Luego cada integrante del equipo trabaja independientemente del resto
vendiendo ejemplares del producto correspondiente. Al terminar cada integrante del grupo debe
conocer la cantidad de ejemplares vendidos por el grupo. Nota: maximizar la concurrencia.


Process Vendedor[id:1.20]{
    int equipo = id MOD 4; //se conoce previamente
    int cantVentas;
    int cantTotalVentasEquipo; //como dice que cada integrante debe conocer

    Equipo[equipo].llegada(); //para coordinar que los 4 lleguen a la vez 

    //Realiza las ventas de forma independiente
    cantVentas = realizarVentas();

    Equipo[equipo].sumarVentas(cantVentas);

    Equipo[equipo].finalizar();

    Equipo[equipo].conocerVentas(cantTotalVentasEquipo);

}

Monitor Equipo[id:1.4]{
    cond empleados;
    cond finalizar;
    int cantEmpleados = 0;
    int terminaron = 0;
    int totalVentas = 0;

    Procedure llegada(){
        cantEmpleados++;
        if(cantEmpleados < 4){
            wait(empleados);
        }
        else{
            signal_all(empleados);
        }
    }

    Procedure sumarVentas(int cantVentas:in){
        totalVentas += cantVentas;
    }

    Procedure finalizar(){
        terminaron++;
        if(terminaron < 4){
            wait(finalizar);
        }
        else{
            signal_all(finalizar);
        }
    }

    Procedure conocerVentas(int ventas:out){
        ventas = totalVentas;
    }
}