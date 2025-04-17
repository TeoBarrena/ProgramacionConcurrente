En un vacunatorio hay un empleado de salud para vacunar a 50 personas. El empleado
de salud atiende a las personas de acuerdo con el orden de llegada y de a 5 personas a la
vez. Es decir, que cuando está libre debe esperar a que haya al menos 5 personas
esperando, luego vacuna a las 5 primeras personas, y al terminar las deja ir para esperar
por otras 5. Cuando ha atendido a las 50 personas el empleado de salud se retira. Nota:
todos los procesos deben terminar su ejecución; suponga que el empleado tienen una
función VacunarPersona() que simula que el empleado está vacunando a UNA persona. 


colaLlegada c;
int cantidadPersonas = 0;
sem despierto_empleado = 0;
sem mutexCola = 1;
sem espera[50] = ([50] 0);

process Persona[id = 1..50]{
    P(mutexCola); //para poder encolarse
    cantidadPersonas++;
    c.push(id);
    if (cantidadPersonas == 5){
        V(despierto_empleado)
    }
    P(espera[id]);//espera que lo terminen de vacunar
    //
}

process Empleado{
    int i,j, aux; //aux toma el id de la Persona
    cola yaVacunados;
    for i:= 1 to 10 {
        P(despierto_empleado); //se queda esperando que lo despierten, cuando haya al menos 5 personas
        for j:= 1 to 5 {
            P(mutexCola);
            aux = c.pop();
            V(mutexCola);
            VacunarPersona(aux);
            yaVacunados.push(aux);
        }
        for j:= 1 to 5 {
            aux = yaVacunados.pop();
            V(espera[aux]);
        }
    }
}