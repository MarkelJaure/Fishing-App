package com.example.fishingapp.models

class Concurso{
    data class Concurso (
        var nombre: String,
        //var basesAndCondiciones: String,
        var basesAndCondiciones: List<BaseOrCondicion.BaseOrCondicion>,
        var premio: String,
        var ranking: List<Report.Reporte>?,
    )

    companion object {
        val data
            get() = listOf(
                Concurso(
                    "Nombre concurso 1",
                    BaseOrCondicion.data,
//                    "La participación en esta Promoción “FLORECE CON QIDA” implica el conocimiento y la " +
//                            "aceptación por parte del participante de las presentes bases y condiciones. " +
//                            "Cualquier violación a las mismas o a los procedimientos o sistemas aquí " +
//                            "establecidos para la realización del presente concurso implicará la inmediata " +
//                            "exclusión de las mismas y/o la revocación de los premios.-",
                   "nada de nada",
                    Report.data
                ),
                Concurso(
                    "Nombre concurso 2",
                    BaseOrCondicion.data,
//                    "Viaje a un viaje para 2 personas a la ciudad de Mar del Plata. Duración del " +
//                            "viaje: 6 días / 5 noches en la ciudad de Mar del Plata entre el 1/3/2018 y el " +
//                            "15/4/2018. Incluye alojamiento en hotel 3 estrellas o superior y pensión completa. " +
//                            "El hotel será elegido y designado por Empresur S.A. Traslado en avión para 2 " +
//                            "personas en clase económica; el horario y la empresa a realizar el servicio será " +
//                            "elegida y designada por Empresur S.A. El punto de partida del viaje será la " +
//                            "localidad de origen de la sucursal del cliente y el destino será la ciudad Mar del " +
//                            "Plata. Una vez entregado el premio, el EMPRESUR S.A. quedará liberado de toda " +
//                            "responsabilidad por el mismo. El viaje no podrá ser transferido; ni canjeado por " +
//                            "dinero ni ofertas. El PREMIO no incluye: cualquier concepto no especificado en las " +
//                            "presentes bases y condiciones como por ejemplo otros gastos de hotel tales como " +
//                            "el uso del teléfono, fax, servicio de habitación, bar de la habitación, películas " +
//                            "del hotel y otras facilidades del hotel; comidas y bebidas, u otros costos y gastos " +
//                            "de cualquier tipo para el ganador y acompañante del PREMIO ; dinero en efectivo, " +
//                            "impuestos gubernamentales, tasas aeroportuarias, y otros cargos personales que " +
//                            "tenga durante su estadía el ganador del PREMIO.",
                    "Un trofeito",
                    null

                ),
                Concurso(
                    "Nombre concurso 3",
                    BaseOrCondicion.data,
//                    "100 cajas navideñas que contendrán cada una: 1 Donuts Leche Bonafide x 78 " +
//                            "gramos, Rama Leche Bonafide 80 gramos, Postre Nugaton por 110 gramos, Nugaton " +
//                            "Blanco Bonafide DDL 3 x 27 gramos, Pan Dulce con Chips de Chocolate x 480 gramos, " +
//                            "Turrón Maní Bonafide con Miel Cremona x 100 gramos, Garrapiñada Bonafide x 100 " +
//                            "gramos, Budín de Limón Bonafide x 250 gramos, Vizzio Maní Bonafide x 80 gramos, " +
//                            "Vizzio Bonafide Cereal x 72 gramos, una sidra real y un maletín Bonafide diseño " +
//                            "fiestas. ",
                    "Una medallita",
                    null
                )
            )
    }}