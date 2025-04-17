Implemente una solución para el siguiente problema. Un sistema debe validar un conjunto de 10000
transacciones que se encuentran disponibles en una estructura de datos. Para ello, el sistema dispone
de 7 workers, los cuales trabajan colaborativamente validando de a 1 transacción por vez cada uno.
Cada validación puede tomar un tiempo diferente y para realizarla los workers disponen de la
función Validar(t), la cual retorna como resultado un número entero entre 0 al 9. Al finalizar el
procesamiento, el último worker en terminar debe informar la cantidad de transacciones por cada
resultado de la función de validación. Nota: maximizar la concurrencia. 


//si cada item toma el mismo tiempo -.> si tiene sentido lo del buffer dividido 7

colaTransacciones cola;
int buffer[0..9] = ([9] 0);
int termine = 0;
sem mutexCola = 1;
int cantidad = 10000;
sem mutexCantidad = 1;
sem mutexContador = 1;
sem cantidadTermino = 1;


process Worker[id: 1..7]{
    int contador[10] = ([10] 0);

    Transaccion transaccion;
    int resultado;
    P(mutexCantidad);
    while(cantidad > 0){

        cantidad--;
        V(mutexCantidad);

        P(cola);
        transaccion = c.pop();
        V(cola);

        resultado = Validar(t);
        contador[resultado]++; 

        P(mutexCantidad);

    }
    V(mutexCantidad);

    P(mutexContador);
    for(int i=0.9){
        buffer[i] += contador[i]; //esto permte hacer muchas menos operaciones que si por cada repeticion en el while 
    }
    V(mutexContador);

    P(cantidadTermino);
    termine++;
    if(termine == 7){
        for int j:= 0 to 9 {
            print(buffer[j]);
        } 
    }
    V(cantidadTermino);
}