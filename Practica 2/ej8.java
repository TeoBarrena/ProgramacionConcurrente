Una fábrica de piezas metálicas debe producir T piezas por día. Para eso, cuenta con E
empleados que se ocupan de producir las piezas de a una por vez. La fábrica empieza a
producir una vez que todos los empleados llegaron. Mientras haya piezas por fabricar, los
empleados tomarán una y la realizarán. Cada empleado puede tardar distinto tiempo en
fabricar una pieza. Al finalizar el día, se debe conocer cual es el empleado que más piezas
fabricó.
a) Implemente una solución asumiendo que T > E

int contador = 0;
int contadorEmpleado[E] = ([E] 0 )
sem mutex;
sem barrera;
int piezas = 0;
sem mutexFinalizo = 1;
int finalizo = 0;
sem despiertoEmpresa = 0;
sem detPremio = 0;
int idMejorEmpleado = -1;

process Empleado[id:0..E]{
    //llega empleado
    P(mutex)
    contador++;
    if (contador == E){
        for i:= 1 to E{
            V(barrera); //asi todos los empleados empiezan a la vez
        }
    }
    V(mutex);
    P(barrera); //asi todos los empleados empiezan a la vez
    P(mutex);
    while (piezas < T){
        //toma pieza
        piezas++;
        V(mutex);
        //fabrica pieza
        contadorEmpleado[id]++;
        P(mutex); //esto para verificar si piezas es < T
    }
    V(mutex);
    P(mutexFinalizo);
    finalizo++;
    if (finalizo == E){
        V(despiertoEmpresa);
    }
    V(mutexFinalizo);
    P(detPremio);
    if (id == idMejorEmpleado){
        print("Soy el mejor empleado");
    }
}

process Empresa{
    P(despiertoEmpresa);
    idMejorEmpleado = contadorEmpleado.indexOf(contadorEmpleado.max());
    for i = 1..E{
        V(detPremio);
    }
}

