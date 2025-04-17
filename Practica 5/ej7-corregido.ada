--Hay un sistema de reconocimiento de huellas dactilares de la policía que tiene 8 Servidores
--para realizar el reconocimiento, cada uno de ellos trabajando con una Base de Datos propia;
--a su vez hay un Especialista que utiliza indefinidamente. El sistema funciona de la siguiente
--manera: el Especialista toma una imagen de una huella (TEST) y se la envía a los servidores
--para que cada uno de ellos le devuelva el código y el valor de similitud de la huella que más
--se asemeja a TEST en su BD; al final del procesamiento, el especialista debe conocer el
--código de la huella con mayor valor de similitud entre las devueltas por los 8 servidores.
--Cuando ha terminado de procesar una huella comienza nuevamente todo el ciclo. 
--Nota:
    --suponga que existe una función Buscar(test, código, valor) que utiliza cada Servidor donde
    --recibe como parámetro de entrada la huella test, y devuelve como parámetros de salida el
    --código y el valor de similitud de la huella más parecida a test en la BD correspondiente.
    --Maximizar la concurrencia y no generar demora innecesaria.

Procedure Sistema is   

Task Especialista is 
    Entry RecibirResultado(codigo:IN int; valor:IN float);
    Entry tomarHuella(huella:OUT huella); --una vez que el servidor esta listo para recibir, solicita al Especialista, asi no se genera demora innecesaria
End Especialista;

Task Type Servidor is 
    --Entry Buscar(test:IN huella) --aca no se pasa por parametro de salida el codigo y el valor de similitud para no bloquear la concurrencia y no generar demora innecesaria, una vez que se tiene el resultado se lo envia recien ahi al Especialista.
    Entry Identificador(id:IN int);--el id es para identificar en un arreglo de valores que va a tener el servidor donde guardara la nota que cada servidor le devuelve
End Servidor;

arrServidores(1..8) of Servidor;

Task Body Especialista is
    codigo:int;
    valor:float;
    huella:huella;
    arrValores = array(1..8) of float;
Begin

    loop
        huella = tomarHuella();
        for i in 1..8*2 loop --se hace dos veces osea 16 en este caso porque el servidor hara dos llamados, uno para pedir la huella y otro para devolver el resultado
            SELECT
                accept tomarHuella(huella:OUT huella) do
                    huella = huella; --aca un servidor le avisa que esta listo para recibir la huella, y el especialista le envía la huella al servidor, asi no se genera demora innecesaria
                end tomarHuella;
            OR
                accept RecibirResultado(codigo:IN int; valor:IN float, idServidor:IN int) do
                    arrValores(idServidor) = valor; --recibe el valor de similitud y lo guarda en la posición correspondiente del arreglo
                end RecibirResultado
            end select;
        end loop;
        print(max(arrValores)); --imprime el valor de similitud mas alto y termina el procesamiento.
        for i in 1..8 loop
            Servidor(i).barrera(); --le avisa a los servidores que termino de procesar la huella y asi pueden solicitar una nueva huella
        end loop;
    end loop;
End Especialista;

Task Body Servidor is
    id:int;
    test:huella;
    codigo:int;
    valor:float;
Begin 
    accept Identificador(id:IN int) do
        id = id;
    end Identificador;

    loop
        Especialista.tomarHuella(test); --le avisa al especialista que esta listo para recibir la huella
        Buscar(test, codigo, valor);
        Especialista.RecibirResultado(codigo, valor, id); --le envia los datos del resultado al Especialista
        --CONSULTAR: aca se tendria que implementar una barrera? xq sino capaz un Servidor que termino antes que otro, le vuelve a pedir una huella, y no se termino de procesar la otra huella
        --RESPUESTA: esta bien asi, pero tmb se podria implementar un for de 1..8 de tomarHuella y dps otro for de 1..8 de RecibirResultado y te ahorras la barrera.
        accept barrera() do
            null;
        end barrera;
    end loop;

End Servidor;

Begin 
    for i in 1..8 loop
        Servidor(i).Identificador(i);
    end loop;
End Sistema;