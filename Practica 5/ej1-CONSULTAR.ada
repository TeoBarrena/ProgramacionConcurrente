--Se requiere modelar un puente de un único sentido que soporta hasta 5 unidades de peso.
--El peso de los vehículos depende del tipo: cada auto pesa 1 unidad, cada camioneta pesa 2
--unidades y cada camión 3 unidades. Suponga que hay una cantidad innumerable de
--vehículos (A autos, B camionetas y C camiones). Analice el problema y defina qué tareas,
--recursos y sincronizaciones serán necesarios/convenientes para resolverlo.
    --a. Realice la solución suponiendo que todos los vehículos tienen la misma prioridad.
    --b. Modifique la solución para que tengan mayor prioridad los camiones que el resto de los
        --vehículos.

        --distintas tareas por tipo de vehiculo
Procedure Puente is

Task puente is
    Entry pasar(peso:IN int);
End puente;

Task type vehiculo

arrClientes: array(1..N) of vehiculo;

Task Body vehiculo is
miPeso = ...;
Begin
    puente.pasar(miPeso);
End vehiculo;

Task Body puente is
pesoActual: int = 0;
Begin
    loop
        accept pasar(peso:IN int);
        if 
    end loop;
End puente;

Begin
    null;
End Puente;