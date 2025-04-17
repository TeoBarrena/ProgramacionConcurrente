--En una clínica existe un médico de guardia que recibe continuamente peticiones de
--atención de las E enfermeras que trabajan en su piso y de las P personas que llegan a la
--clínica ser atendidos.
--Cuando una persona necesita que la atiendan espera a lo sumo 5 minutos a que el médico lo
--haga, si pasado ese tiempo no lo hace, espera 10 minutos y vuelve a requerir la atención del
--médico. Si no es atendida tres veces, se enoja y se retira de la clínica.
--Cuando una enfermera requiere la atención del médico, si este no lo atiende inmediatamente
--le hace una nota y se la deja en el consultorio para que esta resuelva su pedido en el
--momento que pueda (el pedido puede ser que el médico le firme algún papel). Cuando la
--petición ha sido recibida por el médico o la nota ha sido dejada en el escritorio, continúa
--trabajando y haciendo más peticiones.
--El médico atiende los pedidos dándole prioridad a los enfermos que llegan para ser atendidos.
--Cuando atiende un pedido, recibe la solicitud y la procesa durante un cierto tiempo. Cuando
--está libre aprovecha a procesar las notas dejadas por las enfermeras.

Procedure Clinica is

Task Medico is
    Entry AtenderPersona(id:IN int);
    Entry AtenderEnfermera(id:IN int);
END Medico;

Task Type Persona is
    Entry Identificador(id:IN int); 
End Persona;

Task Escritorio is
    Entry dejarNota(nota:IN texto);
    Entry tomarNota(nota:OUT texto);
End Escritorio;

Task Type Enfermera is
    Entry Identificador(id:IN int);
End Enfermera;


Task Body Persona is
    intentos = 0, id:int;
    atendido = false;
Begin
    accept Identificador(id:IN int) do
        id = id;
    end Identificador;
    while(intentos < 3) && (!atendido) loop
        select
            Medico.AtenderPersona(id);
            atendido = true;
        or delay 300.0 --espera 5 min 
            delay 600.0 --si no lo atendio espera 10 minutos para volver a solicitar
            intentos = intentos + 1;
        end select;
    end loop;
End Persona;

Task Body Medico is
Begin
    loop
        select
            accept AtenderPersona(idP:IN int) do
                atenderPersona(idP);
            end AtenderPersona;
        or
            when(AtenderPersona'count = 0) ->
                accept AtenderNota(nota:IN OUT texto) do
                    nota = procesarNota();
                end AtenderNota;
        ELSE   --si no hay nadie para atender y ninguna enfermera en ese momento manda nota, se fija si en el escritorio hay notas
            select
                Escritorio.tomarNota(nota);
                procesarNota(nota);
            else --si el escritorio no tiene nota termina el select y vuelve a empezar
                null;
        end select;
    end loop;

End Medico;

Task Body Escritorio is
    colaNotas;
Begin

    loop
        select
            accept DejarNota(nota:IN texto) do
                nota = nota;
                colaNotas.push(nota);
            end DejarNota;
        or  
            when(colaNotas.count > 0) ->
                accept TomarNota(nota:OUT texto) do
                    nota = colaNotas.pop();
                end TomarNota;
        end select;
    end loop;

End Escritorio;

Task Body Enfermera is
    id:int;
    nota:texto;
Begin

    accept Identificador(id:IN int) do
        id = id;
    end Identificador;

    loop
        select
            Medico.AtenderEnfermera(id);    
        else
            nota = hacerNota();
            --aca tiene que haber un proceso Escritorio? para ir dejando la nota y que la enfermera pueda seguir trabajando? RESPUESTA ->si, asi no generas BW
            Escritorio.dejarNota(nota);
        end select;
    end loop;
    
End Enfermera;

Begin
    for i in 1..P loop
        Persona(i).Identificador(i);
    end loop;
    for i in 1..E loop
        Enfermera(i).Identificador(i);
    end loop;
End Clinica;