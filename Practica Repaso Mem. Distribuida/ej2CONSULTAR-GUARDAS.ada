
--Resolver el siguiente problema. En un negocio de cobros digitales hay P personas que deben pasar
--por la única caja de cobros para realizar el pago de sus boletas. Las personas son atendidas de acuerdo
--con el orden de llegada, teniendo prioridad aquellos que deben pagar menos de 5 boletas de los que
--pagan más. Adicionalmente, las personas ancianas tienen prioridad sobre los dos casos anteriores.
--Las personas entregan sus boletas al cajero y el dinero de pago; el cajero les devuelve el vuelto y los
--recibos de pago.

Procedure CobrosDigitales;

Task Caja is
    Entry PagarBoletaAnciano(boletas: IN boletas; cantBoletas: IN int; monto: IN float, vuelto: OUT float, recibos: OUT text);
    Entry PagarBoletaMenos5Boletas(boletas: IN boletas; cantBoletas: IN int; monto: IN float, vuelto: OUT float, recibos: OUT text);
    Entry PagarBoletaSinPrioridad(boletas: IN boletas; cantBoletas: IN int; monto: IN float, vuelto: OUT float, recibos: OUT text);
End Caja;

Task Type Persona;

arrPersonas(1..P) of Persona;


--ESTA ESTA MAL PERO ES COMO LO HICE LA PRIMERA VEZ, GENERA BW
--Task Body Caja is
--Begin
--    loop
--        select
--
--            accept PagarBoletaAnciano(boletas: IN boletas; cantBoletas: IN int; monto: IN float; vuelto: OUT float; recibos: OUT text) do
--                vuelto = calcularVuelto(boletas, monto);
--                recibos = generarRecibos(boletas);
--            end PagarBoletaAnciano;
--
--        or
--            when (PagarBoletaAnciano'count = 0)->
--                accept PagarBoletasMenos5Boletas(boletas: IN boletas; cantBoletas: IN int; monto: IN float; vuelto: OUT float; recibos: OUT text) do
--                    vuelto = calcularVuelto(boletas, monto);
--                    recibos = generarRecibos(boletas);
--                end PagarBoletasMenos5Boletas;
--        else
--            select 
--                accept PagarBoletaSinPrioridad(boletas: IN boletas; cantBoletas: IN int; monto: IN float; vuelto: OUT float; recibos: OUT text) do
--                    vuelto = calcularVuelto(boletas, monto);
--                    recibos = generarRecibos(boletas);
--                end PagarBoletaSinPrioridad;
--            else
--                null;
--            end select;
--        end select;
--    end loop;
--End Caja;

--ESTA ESTA BIEN, no genera BW porque se queda esperando dentro del SELECT hasta que alguna de las condiciones se cumpla
Task Body Caja is
Begin
    loop
        select
            accept PagarBoletaAnciano(boletas: IN boletas; cantBoletas: IN int; monto: IN float; vuelto: OUT float; recibos: OUT text) do
                vuelto = calcularVuelto(boletas, monto);
                recibos = generarRecibos(boletas);
            end PagarBoletaAnciano;

        or
            when (PagarBoletaAnciano'count = 0)->
                accept PagarBoletasMenos5Boletas(boletas: IN boletas; cantBoletas: IN int; monto: IN float; vuelto: OUT float; recibos: OUT text) do
                    vuelto = calcularVuelto(boletas, monto);
                    recibos = generarRecibos(boletas);
                end PagarBoletasMenos5Boletas;
        or
            when (PagarBoletaAnciano'count = 0 and PagarBoletasMenos5Boletas'count = 0)->
                accept PagarBoletaSinPrioridad(boletas: IN boletas; cantBoletas: IN int; monto: IN float; vuelto: OUT float; recibos: OUT text) do
                    vuelto = calcularVuelto(boletas, monto);
                    recibos = generarRecibos(boletas);
                end PagarBoletaSinPrioridad;
            end select;
        end select;
    end loop;
End Caja;


Task Body Persona is
    boletas: boletas;
    cantBoletas: int;
    monto: float;
    edad: int;
    vuelto: float;
    recibosPago: text;
Begin 
    if (edad > 65) then
        Caja.PagarBoletaAnciano(boletas, cantBoletas, monto, vuelto, recibosPago);
    elsif (cantBoletas < 5) then
        Caja.PagarBoletaMenos5Boletas(boletas, cantBoletas, monto, vuelto, recibosPago);
    else
        Caja.PagarBoletaSinPrioridad(boletas, cantBoletas, monto, vuelto, recibosPago);
    end if;
End Persona;

Begin
    null;
End CobrosDigitales;

------------------------------------------------------------------------
ALT

--CONSULTAR SI ESTA BIEN MANEJADO EL TEMA DE LAS GUARDAS
Task Body Caja is
Begin
    loop
        select
            accept PagarBoletaAnciano(boletas: IN boletas; cantBoletas: IN int; monto: IN float; vuelto: OUT float; recibos: OUT text) do
                vuelto = calcularVuelto(boletas, monto);
                recibos = generarRecibos(boletas);
            end PagarBoletaAnciano;

        or
            when (PagarBoletaAnciano'count = 0)->
                accept PagarBoletasMenos5Boletas(boletas: IN boletas; cantBoletas: IN int; monto: IN float; vuelto: OUT float; recibos: OUT text) do
                    vuelto = calcularVuelto(boletas, monto);
                    recibos = generarRecibos(boletas);
                end PagarBoletasMenos5Boletas;
        or
            when (PagarBoletaAnciano'count = 0 and PagarBoletasMenos5Boletas'count = 0)->
                accept PagarBoletaSinPrioridad(boletas: IN boletas; cantBoletas: IN int; monto: IN float; vuelto: OUT float; recibos: OUT text) do
                    vuelto = calcularVuelto(boletas, monto);
                    recibos = generarRecibos(boletas);
                end PagarBoletaSinPrioridad;
            end select;
        end select;
    end loop;
End Caja;