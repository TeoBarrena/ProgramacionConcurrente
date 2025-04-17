/*
Resolver el siguiente problema con PMS. En la estación de trenes hay una terminal de SUBE que
debe ser usada por P personas de acuerdo con el orden de llegada. Cuando la persona accede a la
terminal, la usa y luego se retira para dejar al siguiente. Nota: cada Persona usa sólo una vez la
terminal. 
*/

Process Persona[id:0..99]{

    Terminal!solicitarUso(id);
    Terminal?pasar();
    usarTerminal();
    Terminal!liberarUso();
}

Process Terminal{
    int id;
    cola personas;
    bool libre = true;

    while(true){
        do Persona[*]?solicitarUso(id) -> {
            if(libre){
                libre = false;
                Persona[id]!pasar();
            }
            else{
                personas.push(id);
            }
        }
        [] Persona[*]?liberarUso() -> {
            if(not empty(personas)){
                Persona[personas.pop()]!pasar();
            }
            else{
                libre = true;
            }
        }
    }
}