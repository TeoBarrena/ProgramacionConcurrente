--Se dispone de un sistema compuesto por 1 central y 2 procesos periféricos, que se
--comunican continuamente. Se requiere modelar su funcionamiento considerando las
--siguientes condiciones:
--- La central siempre comienza su ejecución tomando una señal del proceso 1; luego
--  toma aleatoriamente señales de cualquiera de los dos indefinidamente. Al recibir una
--  señal de proceso 2, recibe señales del mismo proceso durante 3 minutos.
---   Los procesos periféricos envían señales continuamente a la central. La señal del
    --proceso 1 será considerada vieja (se deshecha) si en 2 minutos no fue recibida. Si la
    --señal del proceso 2 no puede ser recibida inmediatamente, entonces espera 1 minuto y
    --vuelve a mandarla (no se deshecha).


Procedure Sistema is

Task Central is
    Entry RecibirSenalProceso1();
    Entry RecibirSenalProceso2();
End Central;

Task type Proceso is
    Entry Identificador(id:IN int);
End Proceso;

arrProcesos(1..2) of Proceso;

Task Body Central is
    ok:boolean := false;
Begin

    accept RecibirSenalProceso1(); --hasta no recibir una señal del proceso 1 no puede recibir señales del proceso 2

    loop
        SELECT --despues de recibir la señal del proceso 1, puede recibir señales de cualquiera de los dos procesos
            if (!ok)
                accept RecibirSenalProceso1();
        OR
            accept FinTimer() do
                ok = false;
            end FinTimer;
        OR
            when (FinTimer'count = 0) ->
                accept RecibirSenalProceso2() do
                    Timer.Start();
                    ok = true;
                    --proceso timer que una vez que recibe el proceso 2, le decis al timer qeu empiece, y una variable booleana para no recibir señales de proceso 1, y otra guarda para cuando termino el timer
                end RecibirSenalProceso2;   
    end loop;

End Central;

Task Body Proceso is
    id:int;
Begin
    accept Identificador(id:IN int) do --aca se le asigna su identificador
        id = id;
    end Identificador;  
    
    if id = 1 then
        loop
            SELECT 
                Central.RecibirSenalProceso1();
            OR DELAY 120.0
                null; --si en 2 minutos no fue recibida la señal, se desecha
            END SELECT;
        end loop;
    else if id = 2 then
        loop
            SELECT 
                Central.RecibirSenalProceso2();
            ELSE --si no puede ser recibida inmediatamente
                delay 60.0;
                Central.RecibirSenalProceso2();
            END SELECT;
        end loop;
    end if;
End Proceso;

Begin   
    --los procesos por si solo no saben su identificador, entonces el sistema les avisa que número son
    for i in 1..2 loop
        arrProcesos(i).Identificador(i);
    end loop;
End Sistema;