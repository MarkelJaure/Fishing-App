package com.example.fishingapp.models


data class BaseOrCondicion (
        var articulo: String,
        var descripcion: String
        )
{
    companion object {
        val data
            get() = listOf(
                BaseOrCondicion(
                    "Uso de cañas",
                    "Solo se permite el uso de cañas registradas en la asociacion de cañas madrynense. No se permitira copias ni otras cosas que nada que ver"
                ),
                BaseOrCondicion(
                    "Trampas",
                    "No se permiten trampas. Ni aprovechar el bug"
                ),
                BaseOrCondicion(
                    "Doracion del concurso",
                    "Viaje a un viaje para 2 personas a la ciudad de Mar del Plata. Duración del " +
                            "viaje: 6 días / 5 noches en la ciudad de Mar del Plata entre el 1/3/2018 y el " +
                            "15/4/2018. Incluye alojamiento en hotel 3 estrellas o superior y pensión completa. " +
                            "El hotel será elegido y designado por Empresur S.A. Traslado en avión para 2 " +
                            "personas en clase económica; el horario y la empresa a realizar el servicio será " +
                            "elegida y designada por Empresur S.A. El punto de partida del viaje será la " +
                            "localidad de origen de la sucursal del cliente y el destino será la ciudad Mar del " +
                            "Plata. Una vez entregado el premio, el EMPRESUR S.A. quedará liberado de toda " +
                            "responsabilidad por el mismo. El viaje no podrá ser transferido; ni canjeado por " +
                            "dinero ni ofertas. El PREMIO no incluye: cualquier concepto no especificado en las " +
                            "presentes bases y condiciones como por ejemplo otros gastos de hotel tales como " +
                            "el uso del teléfono, fax, servicio de habitación, bar de la habitación, películas " +
                            "del hotel y otras facilidades del hotel; comidas y bebidas, u otros costos y gastos " +
                            "de cualquier tipo para el ganador y acompañante del PREMIO ; dinero en efectivo, " +
                            "impuestos gubernamentales, tasas aeroportuarias, y otros cargos personales que " +
                            "tenga durante su estadía el ganador del PREMIO."
                ),
                BaseOrCondicion(
                    "¡Oíd, mortales, el grito sagrado libertad, libertad, libertad!",
                    "Oíd el ruido de rotas cadenas " +
                            "ved en trono a la noble igualdad. " +
                            "Ya su trono dignísimo abrieron " +
                            "las Provincias Unidas del Sur. " +
                            "Y los libres del mundo responden: " +
                            "Al gran Pueblo Argentino, salud... " +
                            "¡Al gran Pueblo Argentino, salud! " +
                            "Y los libres del mundo responden: " +
                            "Al gran Pueblo Argentino, salud... " +
                            "Y los libres del mundo responden: " +
                            "¡Al gran Pueblo Argentino, salud! " +
                            "Sean eternos los laureles " +
                            "que supimos conseguir, " +
                            "que supimos conseguir: " +
                            "coronados de gloria vivamos, " +
                            "o juremos con gloria morir, o juremos con gloria morir, o juremos con gloria morir."
                )
            )
    }
}
