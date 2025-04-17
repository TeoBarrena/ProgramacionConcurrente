--En un sistema para acreditar carreras universitarias, hay UN Servidor que atiende pedidos
--de U Usuarios de a uno a la vez y de acuerdo con el orden en que se hacen los pedidos.
--Cada usuario trabaja en el documento a presentar, y luego lo envía al servidor; espera la
--respuesta de este que le indica si está todo bien o hay algún error. Mientras haya algún error,
--vuelve a trabajar con el documento y a enviarlo al servidor. Cuando el servidor le responde
--que está todo bien, el usuario se retira. Cuando un usuario envía un pedido espera a lo sumo
--2 minutos a que sea recibido por el servidor, pasado ese tiempo espera un minuto y vuelve a
--intentarlo (usando el mismo documento).

Procedure Sistema is

Task Servidor is6
    Entry RecibirPedido(doc:IN texto; respuesta:OUT boolean);
End Servidor;

Task Type Usuario;

arrUsuarios(1..N) of Usuario;

Task Body Servidor is
    doc:texto;
    respuesta:boolean;
Begin
    
    loop
        accept RecibirPedido(doc:IN texto; respuesta:OUT boolean) do
            if todoBien(doc) then
                respuesta = true;
            else
                respuesta = false;
            end if; 
        end RecibirPedido;
    end loop;

End Servidor;

Task Body Usuario is
    doc:texto;
    respuesta:boolean = false;
Begin
    while !respuesta loop
        doc = trabajarDocumento();
        select
            --no puede haber codigo antes del Entry Call en un Select, dps de que se hizo el Entry Call si puede haber
            Servidor.RecibirPedido(doc,respuesta); --como el Servidor atiende de a uno a la vez se aprovecha la comunicación bidireccional y se envía la respuesta por parametro en el mismo canal
        or delay 120.0
            delay 60.0
            Servidor.RecibirPedido(doc,respuesta);
        end select;
    end loop;
End Usuario;


Begin
    null;
End Sistema;