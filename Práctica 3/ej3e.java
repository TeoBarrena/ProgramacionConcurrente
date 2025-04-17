
Existen N personas que deben fotocopiar un documento. La fotocopiadora sólo puede ser usada por una persona a la vez. Analice el problema 
y defina qué procesos, recursos y monitores serán necesarios/convenientes, además de las posibles sincronizaciones requeridas para resolver el problema. 
Luego, resuelva considerando las siguientes situaciones:

a) Implemente una solución suponiendo no importa el orden de uso. Existe una función Fotocopiar() que simula el uso de la fotocopiadora.

b) Modifique la solución de (a) para el caso en que se deba respetar el orden de llegada.

c) Modifique la solución de (b) para el caso en que se deba dar prioridad de acuerdo con la edad de cada persona (cuando la fotocopiadora está libre la debe usar la persona de mayor edad entre las que estén esperando para usarla).

d) Modifique la solución de (a) para el caso en que se deba respetar estrictamente el orden dado por el identificador del proceso (la persona X no puede usar la fotocopiadora hasta que no haya terminado de usarla la persona X-1).

e) Modifique la solución de (b) para el caso en que además haya un Empleado que le indica a cada persona cuando debe usar la fotocopiadora.

f) Modificar la solución (e) para el caso en que sean 10 fotocopiadoras. El empleado le indica a la persona cuál fotocopiadora usar y cuándo hacerlo.


e)
Monitor Fotocopiadora{
    int esperando = 0;
    cond empleado, persona, fin;

    Procedure usar(){
        signal(empleado); //despertas al empleado avisando que hay alguien que quiere usarlo, si el empleado no estaba durmiendo no tiene ningun efecto
        esperando++;
        wait(persona); //te dormis hasta que el empleado te avise que ya podes usar la impresora
    }

    Procedure dejar(){
        signal(fin); //despertas al Empleado que estaba en la cola fin, para permitir que otra persona use la impresora
    }

    Procedure asignar(){
        if (esperando == 0){
            wait(empleado); //si no hay nadie queriendo usar la impresora te dormis, evitando busy waiting
        }
        esperando--;
        signal(persona);//despertas a la persona que estaba dormida hasta que le dijeran que podia usar la impresora
        wait(fin); //se duerme al empleado asi nadie va a poder solicitar el uso de la impresora mientras otra persona ya la este usando
    }
}

Process Persona[id=1..N]{
    Fotocopiadora.usar();
    Fotocopiar();
    Fotocopiadora.dejar();
}

Process Empleado(){
    int i;
    for i= 1 to N{
        Fotocopiadora.asignar();
    }
}

f) Modificar la solución (e) para el caso en que sean 10 fotocopiadoras. El empleado le indica a la persona cuál fotocopiadora usar y cuándo hacerlo.

f)
Monitor Impresora(){
    cond empleado;
    ColaPersonas cola;
    ColaFotocopiadoras fotocopiadoras = {1,2,3,4,5,6,7,8,9,10};
    int asignada[N] = ([N] 0);
    cond fotocopiadora;

    Procedure usar(int fotocopiadoraAsignada: out, int idP: in){
        signal(empleado);
        cola.push(idP);
        wait(persona);
        fotocopiadoraAsignada = asignada[idP];
    }

    Procedure dejar(int fotocopiadoraAsignada: in){
        fotocopiadoras.push(fotocopiadoraAsignada);
        signal(fotocopiadora); //despierto al empleado si estaba esperando por una fotocopiadora, avisandole que ya hay una disponible
    }

    Procedure asignar(){
        int idAux;

        if (cola.isEmpty()){
            wait(empleado); //si no hay nadie queriendo usar la impresora te dormis, evitando busy waiting
        }

        idAux = cola.pop();

        if (fotocopiadoras.isEmpty()){
            wait(fotocopiadora); //espero hasta que una fotocopiadora se libere
        }
        
        asignada[idAux] = fotocopiadoras.pop(); //le asignas a la persona una fotocopiadora
        signal(persona[idAux]);//despertas a la persona que estaba dormida hasta que le dijeran que podia usar la impresora
    }


}

Process Persona[id:1..N]{
    int fotocopiadoraAsignada;
    Fotocopiadora.usar(fotocopiadoraAsignada, id);
    Fotocopiar();
    Fotocopiadora.dejar(fotocopiadoraAsignada);
}

Process Empleado{
    int i;
    for i = 1..N{
        Fotocopiadora.asignar();
    }
}