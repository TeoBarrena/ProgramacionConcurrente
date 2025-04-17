En un examen de la secundaria hay un preceptor y una profesora que deben tomar un examen
escrito a 45 alumnos. El preceptor se encarga de darle el enunciado del examen a los alumnos
cundo los 45 han llegado (es el mismo enunciado para todos). La profesora se encarga de ir
corrigiendo los exámenes de acuerdo con el orden en que los alumnos van entregando. Cada
alumno al llegar espera a que le den el enunciado, resuelve el examen, y al terminar lo deja
para que la profesora lo corrija y le envíe la nota. Nota: maximizar la concurrencia; todos los
procesos deben terminar su ejecución; suponga que la profesora tiene una función
corregirExamen que recibe un examen y devuelve un entero con la nota. 

Process Alumno[id:1.45]{
    Examen examen;
    int nota;

    Preceptor.llegada(examen);
    //resuelve examen
    Profesora.terminar(examen);
    Examen.esperarNota(nota);
}

Monitor Profesora{
    cond alumnos;
    int espera++;
    boolean libre = true;

    Procedure terminar(Examen examen:in){
        if(!libre){
            espera++;
            wait(alumnos);
        }
        else{
            libre = false;
        }
        Examen.corregir(examen); //Cuando un monitor llama a otro se queda esperando hasta que el otro monitor termine por completo su ejecucion 
    }

    Procedure siguiente(){
        espera--;
        if(espera > 0){
            signal(alumnos);
        }
        else{
            libre = true;
        }
    }
}

Monitor Examen{

    cond alumnosNota;
    cola notas;

    Procedure corregir(Examen examen: in){
        int nota = corregirExamen(examen);
        notas.push(nota);
        signal(alumnosNota);
        Profesora.siguiente();
    }

    Procedure esperarNota(int nota:out){
        if (notas.isEmpty()){
            wait(alumnosNota);
        }
        else{
            nota = notas.pop();
        }
    }
}

Monitor Preceptor{
    cond alumnosEsperando;
    int cant = 0;

    Procedure llegada(Examen examen:out){
        cant++;
        if(cant < 45){
            wait(alumnosEsperando);//sino soy el ultimo alumno me duermo esperando que lleguen todos los demas alumnos
        }
        else{
            signal_all(alumnosEsperando); //si soy el ultimo alumno despierto a todos
        }
        examen = EntregarExamen();
    }
}

//----------------------------------------------------------------------------------------------
Process Alumno[id:1.45]{
    Examen examen;
    int nota;

    Preceptor.llegada(examen);
    //resuelve examen
    Profesora.terminar(examen);
    Examen.esperarNota(nota);
}

Monitor Profesora{
    cond alumnos;
    int espera++;
    boolean libre = true;

    Procedure terminar(int nota:out){
        if(!libre){
            espera++;
            wait(alumnos);
        }
        else{
            libre = false;
        }
        nota = Examen.esperarNota(nota);//esto esta mal porque la profesora se queda bloqueada esperando la nota
    }

    Procedure siguiente(){
        espera--;
        if(espera > 0){
            signal(alumnos);
        }
        else{
            libre = true;
        }
    }
}

Monitor Examen{

    Procedure esperarNota(int nota:out){
        nota = corregirExamen();
        Profesora.siguiente();
    }
}

Monitor Preceptor{
    cond alumnosEsperando;
    int cant = 0;

    Procedure llegada(Examen examen:out){
        cant++;
        if(cant < 45){
            wait(alumnosEsperando);//sino soy el ultimo alumno me duermo esperando que lleguen todos los demas alumnos
        }
        else{
            signal_all(alumnosEsperando); //si soy el ultimo alumno despierto a todos
        }
        examen = EntregarExamen();
    }
}
//-------------------------------------------------------------------------- solucion sin monitor examen
Process Alumno[id:1.45]{
    Examen examen;
    int nota;

    Preceptor.llegada(examen);
    //resuelve examen
    Profesora.terminar(examen);
    Profesora.esperarNota(nota);
}

Monitor Profesora{
    cond alumnos;
    int espera++;
    boolean libre = true;

    Procedure terminar(Examen examen:in){
        if(!libre){
            espera++;
            wait(alumnos);
        }
        else{
            libre = false;
        }
    }

    Procedure esperarNota(int nota:out){
        espera--;
        nota = corregirExamen();
        if(espera > 0){
            signal(alumnos);
        }
        else{
            libre = true;
        }
    }
}


Monitor Preceptor{
    cond alumnosEsperando;
    int cant = 0;

    Procedure llegada(Examen examen:out){
        cant++;
        if(cant < 45){
            wait(alumnosEsperando);//sino soy el ultimo alumno me duermo esperando que lleguen todos los demas alumnos
        }
        else{
            signal_all(alumnosEsperando); //si soy el ultimo alumno despierto a todos
        }
        examen = EntregarExamen();
    }
}

//

Proceso alumno[id:0 45]
{
    Profesora.llegada()
    // hace el examen
    Profesora.termine(examen)    
    miNota = examen.tomarNota(id)
}

Proceso preceptor()
{
    Profesora.iniciar()

}

Proceso profesora()
{
    for i = 0 to 44
    {
        examen , id = Profesora.corrigiendo()
        // corrige el examen 
        nota = corregirExamen(examen)
        examen.dejarNota(nota, id)
        Profesora.yaEstaTuNota
    }

}


Monitor Profesora
{
    cond alumnos;
    cant = 0;
    cond preceptor;
    cond profesora;
    cola terminaronID; 

    Procedure iniciar()
    {
        if(cant < 45 ){
            wait(preceptor)
        }
        signalall(alumnos)  
    }

    Procedure llegada()
    {
        cant++;
        if(cant ==  45)
        {
            signal(preceptor)
        }
        wait(alumnos)
    }

    Procedure termine(in examen)
    {
        if(terminaronID.isvacia()){
            signal(profesora)
        }
        terminaronID.push(id, examen)
        wait(alumnos)
       
    }

    Procedure corrigiendo(out examen, id)
    {
        if(terminaronID.isvacia())
        {
            wait(profesora)
        }
        examen, id = terminaronID.pop(examen, id)
    }

    Procedure yaEstaTuNota()
    {
        signal(alumno)
    }


}

Monitor examen
{
    vector notas[N] = 0;
    int nota = 0;

    Procedure tomarNota(out nota, in id)
    {
        nota = notas[id]
    }

    Procedure dejarNota(in nota, in id)
    {
        notas[id] = nota;
    }

}