--Se debe calcular el valor promedio de un vector de 1 millón de números enteros que se
--encuentra distribuido entre 10 procesos Worker (es decir, cada Worker tiene un vector de
--100 mil números). Para ello, existe un Coordinador que determina el momento en que se
--debe realizar el cálculo de este promedio y que, además, se queda con el resultado. Nota:
--maximizar la concurrencia; este cálculo se hace una sola vez.



Procedure Promedio is

Task Coordinador is
    Entry EnviarResultado(resultado:IN int);
End Coordinador;

Task Type Worker is
    Entry Identificador(id:IN int);
    Entry Empezar();
END Worker;

arrWorkers(1..100) of Worker;


Task Body Worker is
    vector: array(1..1000000) of int;
    total:int = 0;
    id:int;
Begin

    accept Identificador(id:IN int) do
        id = id; --cada worker se identifica con un id
    end Identificador;

    accept Empezar(); --el coordinador le indica a cada worker que empiece a realizar la suma de los valores de su vector

    for i in 1.100000 loop
        total += vector[i]; --los workers realizan la suma de los valores de su vector
    end loop;
    Coordinador.EnviarResultado(total/1000000); --dps le envían directamente el promedio de SU suma de valores de su vector al coordinador
End Worker;

Task Body Coordinador is
    resultado:int = 0;
Begin

    for in 1..10 loop
        Worker(i).Empezar(); --esto seria porque el coordinador determina el momento en que se debe realizar el calculo del promedio
    end loop;

    for i in 1..10 loop
        accept EnviarResultado(resultado:IN int) do
            resultado += resultado;
        end EnviarResultado;
    end loop;

    print(resultado/10;) --imprime el promedio de los promedios de los 10 workers
END Coordinador;


Begin
    for i in 1..10 loop
        Worker(i).Identificador(i);
    end loop;
END Promedio;