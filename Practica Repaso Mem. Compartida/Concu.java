"Semáforos
1) Resolver los problemas siguientes:
a) En una estación de trenes, asisten P personas que deben realizar una carga de su tarjeta SUBE
en la terminal disponible. La terminal es utilizada en forma exclusiva por cada persona de acuerdo
con el orden de llegada. Implemente una solución utilizando únicamente procesos Persona. Nota:
la función UsarTerminal() le permite cargar la SUBE en la terminal disponible."

sem accesoCola=1;
sem Perosna[P]=([P] 0);
boolean terminalLibre=true
colaFifo cola;
process Persona(id:0..p-1){
    P(accesoCola);
    if terminalLibre { // si esta libre indico que voy a usarla yo
        terminalLibre=false;
        V(accesoCola);
    }else{
        cola.push(id); 
        v(accesoCola); 
        p(Persona[id]) // si la terminal se esta usando espero en la fila
    }
    UsarTerminal();
    p(accesoCola)
    if (cola.isEmpty()){ // ya no hay nadie en la fila
        terminalLibre=true // entonces indico que cualquiera puede usar la terminal
    }else {
        v(Perosna[cola.pop()]);
    }
    p(accesoCola)
}


"b) Resuelva el mismo problema anterior pero ahora considerando que hay T terminales disponibles.
Las personas realizan una única fila y la carga la realizan en la primera terminal que se libera.
Recuerde que sólo debe emplear procesos Persona. Nota: la función UsarTerminal(t) le permite
cargar la SUBE en la terminal t."
sem AccesoCola=1;
sem AccesoTerminal=1;
sem Personas[P] = ([P] 0);
sem Terminales[T] = ([T] 0);
ColaFifo cola;
boolean terminalLibre [T] = [[T] true];
int terminalAsignada[n];   // esto lo agrego para uar la opcion 2 de enciar la terminal que se libero

Process Personas(id:0..P-1){
    int idTerminalLibre = -1;
    int sigPersona;
    P(accesoCola);
    // que pasa si no hay terminal libre
    for (int i=0; i<T ; i++){
        if (terminalLibre[i]){
            idTerminalLibre = i; // busca tntre todas las terminales y me quedo con el id de la 1era terminal libre que encuentro
            break;
        }
    }
    if (idTerminalLibre != 1) { // si esta libre indico que voy a usarla yo
        terminalLibre[i]e=false;
        V(accesoCola);
    }else{
        cola.push(id); 
        v(accesoCola); 
        p(Persona[id]) // si la terminal se esta usando espero en la fila
        // opcion 1 usar otro for (no realizada)
        // opcion 2, que me asignen la terminal que dejo de usar la persona que se fue
        idTerminalLibre = terminalAsignada[id]

    }
   // p(AccesoTerminal);  ==> Esto esta mal por dos motivos LA MAS IMPORTANTE ES QUE  SE VUELVE SECUENCIAL POR TENER T TERMINALES, el otro error pero menos grabe es que solo se livera 1 terminal a la vez
    UsarTerminal(idTerminalLibre);
   // v(AccesoTerminal);
    p(accesoCola)
    if (cola.isEmpty()){ // ya no hay nadie en la fila
        terminalLibre[idTerminalLibre]=true // entonces indico que cualquiera puede usar la terminal
    }else {
        sigPersona=cola.pop();        
         terminalAsignada[sigPersona] =  idTerminalLibre; // para la opcion 2 aca tengo que decirle a la terminal que va a usar
        v(Perosna[sigPersona]);
    }
    p(accesoCola);
}

"c) idem insiso a pero las personas mayores son prioridad"

// utilizare una cola FIFO con Prioridad

sem accesoCola =1;
sem Perosnas [p] = ([P] 0);
boolean terminalLibre= true;
ColaPrioridad cola;

Process Persona(id:0..p-1){
    int sigPersona, prioridad;
    boolean esMayor= ...;
    P(accesoCola);
    if(terminalLibre){
        terminalLibre=false;
        V(accesoCola);
    }else{
        if(esMayor) prioridad=0;
        else prioridad=1;
        cola.push(id,prioridad);
        v(accesoCola);
        P(Perosnas[id]);
    }
    UsarTerminal();
    p(accesoCola);
    if(cola.isEmpty()){
        terminalLibre=true;
    }else{
        sigPersona=cola.pop()
        v(personas(sigPersona))
    }
    v(accesoCola);
}


"2) Un sistema debe validar un conjunto de 10000 transacciones que se encuentran disponibles en una estructura de datos.
Para ello, el sistema disponede 7 workers, los cuales trabajan colaborativamente validando de a 1 transacción por vez cada uno.
Cada validación puede tomar un tiempo diferente y para realizarla los workers disponen de lafunción Validar(t),
la cual retorna como resultado un número entero entre 0 al 9. Al finalizar el procesamiento, 
el último worker en terminar debe informar la cantidad de transacciones por cada resultado de la función de validación. 
Nota: maximizar la concurrencia."

Sem mutexContador=1;
Sem mutexAux=1;
Transaccion transacciones[10000] = ....;
int cantidadTransacciones=10000;
int contador[10]=([10] 0);
int finalizoProcesamiento=0;

Process workers (id:0..6){
    int resultado=0;

    // Divido que cada proceso realice la misma cantidad de transacciones
    for (int i=id; i < cantidadTransacciones; i=+6){
        resultado=Validar(transacciones[i]);
        P(mutexContador);
        contador[resultado]++
        V(mutexContador);
    }

    //sumo cantidad de trabajo finalizado
    P(mutexAux)
    finalizoProcesamiento++
    V(mutexAux)

    // si es el ultimo en finalizar informa
    P(mutexContador)
    if(finalizoProcesamiento== 7){
        for (int c=0; i<10; i++){
            write("obtuvieron"+ c + ","+ contador[c] + "transacciones")
        }
        V(mutexContador)
    }else{
        V(mutexContador)
    }

}



"
3) Implemente una solución para el siguiente problema. Se debe simular el uso de una máquina
expendedora de gaseosas con capacidad para 100 latas por parte de U usuarios. Además, existe un
repositor encargado de reponer las latas de la máquina. Los usuarios usan la máquina según el orden
de llegada. Cuando les toca usarla, sacan una lata y luego se retiran. En el caso de que la máquina se
quede sin latas, entonces le debe avisar al repositor para que cargue nuevamente la máquina en forma
completa. Luego de la recarga, saca una botella y se retira. Nota: maximizar la concurrencia; mientras
se reponen las latas se debe permitir que otros usuarios puedan agregarse a la fila.
"
Sem accesoCola=1;
Sem EmpezaReponer=0;
Sem FinalizarReponer=0;
Sem UsuarioEsperando[u]=([u] 0);
Sem contadorLatas=1;

Colafifo cola;
int cantLatas=100;
boolean maquinaLibre=true;

Process usuarios(id: 0..u-1){
    int sigUsuario;
    P(accesoCola);
    if(maquinaLibre){
        maquinaLibre=false;
        V(accesoCola);
    }else{
        cola.push(id);
        v(accesoCola);
        P(UsuarioEsperando[id]) // aca se duermen todos
    }
    P(contadorLatas); // este semaforo no es necesario ya que los procesos despiertan de a 1 en  P(UsuarioEsperando[id])
    if (cantLatas=0){// si np quedan latas tengo que reporner todas
        V(EmpezaReponer)
        P(FinalizarReponer);
    }
    cantLatas--;
    V(contadorLatas);
    
    P(accesoCola)
    if(cola.isEmpty()){
        maquinaLibre=true;
        v(maquinaLibre);
    }else{
        sigUsuario=cola.pop();
        V(maquinaLibre);
        V(UsuarioEsperando[sigUsuario]);
    }
    V(accesoCola);
}
Process Reponedor(){
    while (true){
        P(EmpezaReponer)
        cantLatas=100;
        V(FinalizarReponer);
    }
}





"Monitores
1) Resolver el siguiente problema. En una elección estudiantil, se utiliza una máquina para voto
electrónico. Existen N Personas que votan y una Autoridad de Mesa que les da acceso a la máquina
de acuerdo con el orden de llegada, aunque ancianos y embarazadas tienen prioridad sobre el resto.
La máquina de voto sólo puede ser usada por una persona a la vez. Nota: la función Votar() permite
usar la máquina."

Process personas(id:0..n-1){
    int prioridad;
    boolean esMayor= ...;
    boolean estaEmbaradada= ...;
    if(EsMayor || estaEmbaradada)
        prioridad=0
    else
        prioridad=1
    autoridadMesa.pasar(id,prioridad);
    Votar();
    autoridadMesa.salir();
}

Monitor AutoridaMesa(){
    boolean maquinaLibre=true;
    Cond PersonaEspera[n];
    ColaPrioridad cola; // asumo que es una cola fifo con prioridad
    
    Persona sigPersona;
    //int personasEsperando=0;

    procedure pasar(int id, int prioridad){
        if(maquinaLibre){
            maquinaLibre=false
        }else{
            cola.push(id,Prioridad);
           // personasEsperando ++
            wait(PersonaEspera[id])
        }
    }
    procedure salir(){
        // if(esperando = 0)
        if (cola.isEmpty()){
            maquinaLibre=true
        }else{
            sigPersona=cola.pop()
            signal(personasEsperando[sigPersona.getId()])
        }
    }

}



"2) Resolver el siguiente problema. En una empresa trabajan 20 vendedores ambulantes que forman 5
equipos de 4 personas cada uno (cada vendedor conoce previamente a qué equipo pertenece). Cada
equipo se encarga de vender un producto diferente. Las personas de un equipo se deben juntar antes
de comenzar a trabajar. Luego cada integrante del equipo trabaja independientemente del resto
vendiendo ejemplares del producto correspondiente. Al terminar cada integrante del grupo debe
conocer la cantidad de ejemplares vendidos por el grupo. Nota: maximizar la concurrencia."

Process Vendedores(id: 0..19) {
    int idEquipo = id / 4;                      // Cada vendedor pertenece a un equipo basado en su id
    int cantidadVentasPropias, cantidadVentasEquipo;
    
    Empresa[idEquipo].llegada();                // Espera a que todo el equipo llegue
    
    // Realiza las ventas de forma independiente
    cantidadVentasPropias = realizarVentas();   // Simula las ventas que hace cada vendedor

    // Suma las ventas realizadas al total de ventas del equipo
    Empresa[idEquipo].sumarVentas(cantidadVentasPropias);

    // Espera a que todos los vendedores del equipo terminen de vender
    Empresa[idEquipo].TerminarVentas();

    // Obtiene el total de ventas del equipo
    cantidadVentasEquipo = Empresa[idEquipo].RetornarVentasEquipo();
}

Monitor Empresa(id: 0..4) {
    int cantLlegaron = 0;                       // Contador de vendedores que han llegado
    int cantFinalizaron = 0;                    // Contador de vendedores que han terminado de vender
    Cond espera, finalizar;                     
    int ventasTotales = 0;                      // Ventas totales por equipo

    // Procedimiento para controlar la llegada de los vendedores
    Procedure llegada() {
        cantLlegaron++;
        if (cantLlegaron == 4) {                // Si los 4 vendedores del equipo llegaron
            signalAll(espera);                  // Despierta a todos los vendedores del equipo
        } else {
            wait(espera);                       // Espera a que todos los vendedores lleguen
        }  
    } 

    // Procedimiento para sumar las ventas de cada vendedor al total del equipo
    Procedure sumarVentas(int cantidadVentas) {
        ventasTotales += cantidadVentas;
    }

    // Procedimiento para esperar a que todos los vendedores terminen de vender
    Procedure TerminarVentas() {
        cantFinalizaron++;
        if (cantFinalizaron == 4) {             // Si los 4 vendedores terminaron de vender
            signalAll(finalizar);               // Despierta a todos los vendedores
        } else {
            wait(finalizar);                    // Espera a que todos terminen
        }
    }    

    // Procedimiento para obtener las ventas totales del equipo
    Procedure RetornarVentasEquipo() {
        return ventasTotales;
    }
}



3) Resolver el siguiente problema. En una montaña hay 30 escaladores que en una parte de la subida
deben utilizar un único paso de a uno a la vez y de acuerdo con el orden de llegada al mismo. Nota:
sólo se pueden utilizar procesos que representen a los escaladores; cada escalador usa sólo una vez
el paso.




