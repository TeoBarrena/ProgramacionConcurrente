10. A una cerealera van T camiones a descargarse trigo y M camiones a descargar maíz. Sólo
hay lugar para que 7 camiones a la vez descarguen, pero no pueden ser más de 5 del mismo
tipo de cereal.
a) Implemente una solución que use un proceso extra que actúe como coordinador
entre los camiones. El coordinador debe atender a los camiones según el orden de
llegada. Además, debe retirarse cuando todos los camiones han descargado.
b) Implemente una solución que no use procesos adicionales (sólo camiones). No
importa el orden de llegada para descargar. Nota: maximice la concurrencia.




b)
sem mutexCamiones = 7;
sem mutexMaiz = 5;
sem mutexTrigo = 5;

Process Trigo[id=1..T]{
    P(mutexTrigo);
    P(mutexCamiones);
    //descarga trigo
    P(mutexCamiones);
    P(mutexTrigo);
}

Process Maiz[id=1..M]{
    P(mutexMaiz);
    P(mutexCamiones);
    //descarga maiz
    P(mutexCamiones);
    P(mutexMaiz);
}