/*
    En un examen final hay N alumnos y P profesores. Cada alumno resuelve su examen, lo
    entrega y espera a que alguno de los profesores lo corrija y le indique la nota. Los
    profesores corrigen los exámenes respetando el orden en que los alumnos van entregando.
        a) Considerando que P=1.
        b) Considerando que P>1.
        c) Ídem b) pero considerando que los alumnos no comienzan a realizar su examen hasta
        que todos hayan llegado al aula.
    Nota: maximizar la concurrencia; no generar demora innecesaria; todos los procesos deben
    terminar su ejecución
 */



A
//considerando P=1
Process Alumno[id:0.N-1]{
    text examen;
    int nota;

    examen = resolverExamen();
    Admin!entregarExamen(id,examen);
    Profesor?recibirNota(nota);
}

Process Profesor{
    int idA;
    text examen;
    int nota;

    for (int i = 0; i<N; i++){
        Admin!pedido();
        Admin?corregirExamen(idA,examen);
        nota = evaluarExamen(examen);
        Alumno[idA]!recibirNota(nota); //le envia al alumno que esta esperando en la posicion idA la nota 
    }
}

//para maximizar la concurrencia se utiliza una cola para ir dejando los examenes, esta cola la tiene que administrar el proceso Admin, para no generar demoras innecesarias, y ademas para poder atender x orden de llegada
Process Admin{
    int idA;
    text examen;
    cola examenes;

    do Alumno[*]?entregarExamen(idA, examen) -> examenes.push(idA,examen); //espera a recibir de un alumno el id y el examen
    [] not empty(examenes); Profesor?pedido() -> Profesor!corregirExamen(examenes.pop());
    od;
}

B
//considerando que P>1, ahora el admin va a tener que saber el id del profesor que le esta pidiendo acceso a la cola de examenes, para saber a quien enviarselo

Process Alumno[id:0.N-1]{
    text examen;
    int nota;

    examen = resolverExamen();
    Admin!entregarExamen(id,examen);
    Profesor[*]?recibirNota(nota);//ahora puede recibir de cualquier profesor
}

Process Profesor[id:0.P]{
    int idA;
    text examen;
    int nota;

    for (int i = 0; i<N; i++){
        Admin!pedido(id);
        Admin?corregirExamen(idA,examen);
        nota = evaluarExamen(examen);
        Alumno[idA]!recibirNota(nota); //le envia al alumno que esta esperando en la posicion idA la nota 
    }
}

Process Admin{
    int idA;
    text examen;
    cola examenes;
    int idP;

    do Alumno[*]?entregarExamen(idA, examen) -> examenes.push(idA,examen); //espera a recibir de un alumno el id y el examen
    [] not empty(examenes); Profesor[*]?pedido(idP) -> Profesor[idP]!corregirExamen(examenes.pop()); //aca se agrega el idP de cada profesor que hizo el pedido y a qué profesor se le tiene que hacer el envío
    od;
}
