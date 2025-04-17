--Resolver el siguiente problema. La oficina central de una empresa de venta de indumentaria debe
--calcular cuántas veces fue vendido cada uno de los artículos de su catálogo. La empresa se compone
--de 100 sucursales y cada una de ellas maneja su propia base de datos de ventas. La oficina central
--cuenta con una herramienta que funciona de la siguiente manera: ante la consulta realizada para un
--artículo determinado, la herramienta envía el identificador del artículo a las sucursales, para que cada
--una calcule cuántas veces fue vendido en ella. Al final del procesamiento, la herramienta debe
--conocer cuántas veces fue vendido en total, considerando todas las sucursales. Cuando ha terminado
--de procesar un artículo comienza con el siguiente (suponga que la herramienta tiene una función
--generarArtículo() que retorna el siguiente ID a consultar). Nota: maximizar la concurrencia. Existe
--una función ObtenerVentas(ID) que retorna la cantidad de veces que fue vendido el artículo con
--identificador ID en la base de la sucursal que la llama.

Procedure Empresa;

--cuando dice maximizar la concurrencia, es porque la sucursal es la que le informa a la herramienta que esta listo para recibir un articulo, porque si fuera secuencial se puede generar demora innecesaria

Task Herramienta is
    Entry pedirArticulo(id: OUT int);
    Entry calcularVentas(ventas: IN int);
End Herramienta;

Task Type Sucursal is
    Entry barrera;
    Entry Identificar(id:IN int);
End Sucursal;

arrSucursales(1..100) of Sucursal;

Task Body Herramienta is
    idArticulo: int;
    ventasTotales: int;
Begin
    loop
        ventasTotales = 0;
        idArticulo = generarArticulo();
        for i in 1..100*2 loop --lo hace 200 veces, xq cada sucursal hara 2 llamados, uno para decir que esta listo para recibir un articulo y otro para enviar la cantidad de ventas
            select  
                accept pedirArticulo(id: OUT int) do
                    id = idArticulo;
                end pedirArticulo;
            or --el uso de este formato, que este dentro de un mismo loop, es para maximizar la concurrencia, porque si se hace un loop para pedir y otro para calcular, se puede generar demora innecesaria, puede pasar que el proceso 1 termine de calcular sus ventas y el proceso 2 ni siquiera alla pedido  el articulo.
                accept calcularVentas(ventas:IN int) do
                    ventasTotales += ventas;
                end calcularVentas;
            end select;
        end loop
        print("Ventas totales del producto con id: ", idArticulo, " son: ", ventasTotales);
        --Consultar si esta bien esta barrera, lo hago mas que nada para maximizar la concurrencia, porque si hago un for separado arriba (uno para pedir y otro para calcular, puede haber demora innecesaria)
        for i in 1..100 loop
            Sucursal(i).barrera(); --sincronizo esta barrera para que no se de el caso de que una sucursal termine de calcular las ventas con un ID determinado y vuelva a pedir por el mismo ID
        end loop;
        --aca creo que tambien se podria generar demora pero ya es medio inevitable me parece
    end loop;
End Herramienta;

Task Body Sucursal is
    idArticulo: int;
    ventas: int;
    id: int;
Begin

    accept Identificar(id:IN int) do
        id = id;
    end Identificar;

    loop --aca es necesario el uso de la barrera porque se esta ejecutando en un loop, si se ejecutara una sola vez, no es necesaria la barrera.
        Herramienta.pedirArticulo(idArticulo);
        ventas = ObtenerVentas(idArticulo);
        Herramienta.calcularVentas(ventas);
        accept barrera();
    end loop;

End;

Begin
    for i in 1..100 loop
        Sucursal(i).Identificar(i);
    end loop;
End Empresa;