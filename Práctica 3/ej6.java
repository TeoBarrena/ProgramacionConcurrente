Existe una comisión de 50 alumnos que deben realizar tareas de a pares, las cuales son
corregidas por un JTP. Cuando los alumnos llegan, forman una fila. Una vez que están todos
en fila, el JTP les asigna un número de grupo a cada uno. Para ello, suponga que existe una
función AsignarNroGrupo() que retorna un número “aleatorio” del 1 al 25. Cuando un alumno
ha recibido su número de grupo, comienza a realizar su tarea. Al terminarla, el alumno le avisa
al JTP y espera por su nota. Cuando los dos alumnos del grupo completaron la tarea, el JTP
les asigna un puntaje (el primer grupo en terminar tendrá como nota 25, el segundo 24, y así
sucesivamente hasta el último que tendrá nota 1). Nota: el JTP no guarda el número de grupo
que le asigna a cada alumno.


Process Alumno[id:1..50]{
    int nro,nota;
    Tarea.esperarFila(id);
    Tarea.recibirNro(id,nro);
    //hacer tarea
    Tarea.obtenerNota(nro,nota);
}

Monitor Tarea{

    cond alumno;
    cond profesor;
    cond esperarNro[50] = ([50] 0); //cada alumno va a esperar su numero en su respc. posición
    cond notaLista[25] = ([25] 0); //para esperar la nota asignada

    int notaTarea[25] = ([25] -1);
    int nroAsignado[50] = ([50] -1); 

    int cantAlumnos = 0;

    cola fila;
    cola finalizadas;

    Procedure esperarAlumnos(){
        if (cantAlumnos < 50){
            wait(profesor);
        }
    }

    Procedure esperarFila(int id:in){
        cantAlumnos++;
        fila.push(id);
        if (cantAlumnos == 50){
            signal(profesor);
        }   
    }

    Procedure recibirNro(int id:in, int nro:out){
        wait(esperarNro[id]); //el alumno se duerme en la posicion correspondiente a su ID esperando que se le asigne el numero
        nro = nroAsignado[id];
    }

    Procedure asignarGrupo(int nro:in){
        int idAlumno = fila.pop();//obtengo el id del alumno que estaba encolado 
        nroAsignado[idAlumno] = nro; //le guardo el numero que le toco en el array
        signal(esperarNro[idAlumno]); //lo despierto
    }

    Procedure obtenerNota(int nro:in, nota:out){
        finalizadas.push(nro); //pushea a la cola el nro de su grupo
        signal(profesor); //despierta al profesor
        wait(notaLista[nro]); //se duerme esperando que le asignen su nota
        nota = notaTarea[nro]; //retorna la nota asignada
    }

    Procedure recibirTarea(int nroAux:out){
        if (finalizadas.isEmpty()){
            wait(profesor);
        }
        nroAux = finalizadas.pop(); //si la cola no esta vacía recibo tarea
    }

    Procedure corregirTarea(int nroAux:in, puntaje:out){
        notaTarea[nroAux] = puntaje; //en el arreglo notaTarea le asigno el puntaje al grupo
        signal_all(notaLista[nroAux]); //despierto a los dos alumnos del grupo correspondiente
    }   

}

Process JTP{
    int nroAux,i;
    int puntaje = 25;
    int tareasContador[25] = ([25] 0);

    Tarea.esperarAlumnos(); //Para evitar busy waiting
    for i = 1 to 50{
        nroAux = AsignarNroGrupo();
        Tarea.asignarGrupo(nroAux); //le asignas a cada alumno su grupo
    }
    
    for i= 1 to 50{
        Tarea.recibirTarea(nroAux);
        tareasContador[nroAux]++;
        if (tareasContador[nroAux] == 2){
            Tarea.corregirTarea(nroAux,puntaje);
            puntaje--;
        }
    }
}

