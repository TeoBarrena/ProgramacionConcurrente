--Resolver el siguiente problema. La página web del Banco Central exhibe las diferentes cotizaciones
--del dólar oficial de 20 bancos del país, tanto para la compra como para la venta. Existe una tarea
--programada que se ocupa de actualizar la página en forma periódica y para ello consulta la cotización
--de cada uno de los 20 bancos. Cada banco dispone de una API, cuya única función es procesar las
--solicitudes de aplicaciones externas. La tarea programada consulta de a una API por vez, esperando
--a lo sumo 5 segundos por su respuesta. Si pasado ese tiempo no respondió, entonces se mostrará
--vacía la información de ese banco.



Procedure BancoCentral;

Task TareaProgramada is
    Entry consultarCotizacion(idBanco: IN int; dolarCompra, dolarVenta: out float);
End TareaProgramada;

Task Type API is
    Entry Identificar(id: IN int);
End API;   

Task Type Banco is
    Entry Identificar(id: IN int);
    Entry consultarCotizacion(dolarCompra, dolarVenta: out float);
End Banco;

arrApis(1..20) of API;

arrBancos(1..20) of Banco;

Task Body TareaProgramada is
Begin
    loop
        for i in 1..20 loop
            select
                accept consultarCotizacion(idBanco:IN int; dolarCompra, dolarVenta: IN float) do
                    print("Banco: ", idBanco, "Dolar compra: ", dolarCompra, " - Dolar venta: ", dolarVenta);
                end consultarCotizacion;
                or delay 5.0;
                    print("Dolar compra: - Dolar venta: -");
            End select;
    end loop;
End TareaProgramada;

--ESTA ES LA PRIMER SOLUCION QUE HICE PERO ESTA MAL 
--Task Body API is
--    id:int;
--    dolarCompra, dolarVenta: float;
--Begin
--
--    accept Identificar(id: IN int) do
--        id = id;
--    end Identificar;
--
--    loop    
--        Banco(id).consultarCotizacion(dolarCompra,dolarVenta);
--        TareaProgramada.consultarCotizacion(id,dolarCompra, dolarVenta);
--        delay(1dia);
--    end loop;
--End API;

Task Body Banco is
    id: int;
    dolarCompra, dolarVenta: float;
Begin

    accept Identificar(id: IN int) do
        id = id;
    end Identificar;

    loop
        dolarCompra = cotizacionDolarCompra();
        dolarVenta = cotizacionDolarVenta();
        accept consultarCotizacion(dolarCompra, dolarVenta: out float) do
            dolarCompra = dolarCompra;
            dolarVenta = dolarVenta;
        end consultarCotizacion;
    end loop;
End Banco;

Begin
    for i in 1..20 loop
        arrApis(i).Identificar(i);
        arrBancos(i).Identificar(i);
    end loop;
End BancoBancoCentral;




----------------------------------------------
alt1


Task Body TareaProgramada is
Begin
    loop
        for i in 1..20 loop
            select
                API[i].consultarCotizacion(idBanco:IN int; dolarCompra, dolarVenta: IN float) do
            or delay 5.0;
                print("Dolar compra: - Dolar venta: -");
            End select;
    end loop;
End TareaProgramada;

Task Body API is
    id:int;
    dolarCompra, dolarVenta: float;
Begin

    accept Identificar(id: IN int) do
        id = id;
    end Identificar;

    Banco(id).consultarCotizacion(dolarCompra,dolarVenta);
    accept consultarCotizacion(idBanco:OUT int; dolarCompra, dolarVenta: OUT float) do
        dolarCompra = dolarCompra;
        dolarVenta = dolarVenta;
        idBanco = id;
    end consultarCotizacion;
    
End API;

Task Body Banco is
    id: int;
    dolarCompra, dolarVenta: float;
Begin

    accept Identificar(id: IN int) do
        id = id;
    end Identificar;

    loop
        dolarCompra = cotizacionDolarCompra();
        dolarVenta = cotizacionDolarVenta();
        accept consultarCotizacion(dolarCompra, dolarVenta: out float) do
            dolarCompra = dolarCompra;
            dolarVenta = dolarVenta;
        end consultarCotizacion;
    end loop;
End Banco;

Begin
    for i in 1..20 loop
        arrApis(i).Identificar(i);
        arrBancos(i).Identificar(i);
    end loop;
End BancoBancoCentral;